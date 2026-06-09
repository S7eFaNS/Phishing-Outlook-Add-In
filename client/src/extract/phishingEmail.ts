function getAttachmentContent(
  item: Office.MessageRead,
  attachmentId: string
): Promise<Office.AttachmentContent> {
  return new Promise((resolve, reject) => {
    try {
      item.getAttachmentContentAsync(attachmentId, (result) => {
        if (result.status === Office.AsyncResultStatus.Succeeded) {
          resolve(result.value);
        } else {
          reject(result.error);
        }
      });
    } catch (err) {
      reject(err);
    }
  });
}


function decodeBase64ToUtf8(base64: string): string {
  const binary = atob(base64);
  const bytes = new Uint8Array(binary.length);
  for (let i = 0; i < binary.length; i++) {
    bytes[i] = binary.charCodeAt(i);
  }
  return new TextDecoder("utf-8").decode(bytes);
}

const NO_CANDIDATE_MESSAGE =
  "PhishAid found no forwarded email on this message. Forward the suspicious email " +
  'to the CSOC mailbox as an attachment (Forward as Attachment / "Attach Item"), open ' +
  "that message, and run PhishAid again.";

const DRAG_DROP_MESSAGE =
  "PhishAid could not read the attached email. If it was added by drag-and-drop, " +
  'remove it and re-attach using Forward as Attachment / "Attach Item" instead, then rerun.';

export async function extractPhishingEmail(): Promise<string> {
  await Office.onReady();

  const item = Office.context.mailbox.item as Office.MessageRead | undefined;
  if (!item) {
    throw new Error(NO_CANDIDATE_MESSAGE);
  }

  const attachments = item.attachments;

  const itemAttachments = attachments.filter(
    (a) => a.attachmentType === Office.MailboxEnums.AttachmentType.Item
  );
  const emlFileAttachments = attachments.filter(
    (a) =>
      a.attachmentType === Office.MailboxEnums.AttachmentType.File &&
      /\.(eml|msg)$/i.test(a.name)
  );
  const candidates =
    itemAttachments.length > 0 ? itemAttachments : emlFileAttachments;

  const [chosen, ...rest] = candidates;
  if (!chosen) {
    throw new Error(NO_CANDIDATE_MESSAGE);
  }
  if (rest.length > 0) {
    console.warn(
      `PhishAid: ${candidates.length} candidate attachments found; using "${chosen.name}", ignoring:`,
      rest.map((a) => a.name)
    );
  }

  let content: Office.AttachmentContent;
  try {
    content = await getAttachmentContent(item, chosen.id);
  } catch {
    throw new Error(DRAG_DROP_MESSAGE);
  }

  switch (content.format) {
    case Office.MailboxEnums.AttachmentContentFormat.Eml:
      return content.content;
    case Office.MailboxEnums.AttachmentContentFormat.Base64:
      return decodeBase64ToUtf8(content.content);
    default:
      throw new Error(
        `PhishAid received an unexpected attachment format "${content.format}" for "${chosen.name}".`
      );
  }
}
