/* global document, Office */

import { extractPhishingEmail } from "../extract/phishingEmail";
import { buildPayload } from "../assemble";

const PREVIEW_LIMIT = 1000;

Office.onReady((info) => {
  if (info.host === Office.HostType.Outlook) {
    el("sideload-msg").style.display = "none";
    el("app-body").style.display = "flex";
    el("analyze").onclick = analyze;
  }
});

function el(id: string): HTMLElement {
  const node = document.getElementById(id);
  if (!node) {
    throw new Error(`PhishAid: missing UI element #${id}.`);
  }
  return node;
}

function setStatus(text: string): void {
  el("status").textContent = text;
}

function showError(message: string): void {
  const area = el("error");
  area.textContent = message;
  area.style.display = "block";
}

function resetOutput(): void {
  const area = el("error");
  area.textContent = "";
  area.style.display = "none";
  el("result").style.display = "none";
}

function grabbedAttachmentName(): string | undefined {
  const item = Office.context.mailbox.item as Office.MessageRead | undefined;
  if (!item) {
    return undefined;
  }
  const attachments = item.attachments;
  const forwardedMessage = attachments.find(
    (a) => a.attachmentType === Office.MailboxEnums.AttachmentType.Item
  );
  if (forwardedMessage) {
    return forwardedMessage.name;
  }
  const emlFile = attachments.find(
    (a) =>
      a.attachmentType === Office.MailboxEnums.AttachmentType.File &&
      /\.(eml|msg)$/i.test(a.name)
  );
  return emlFile?.name;
}

async function analyze(): Promise<void> {
  resetOutput();
  setStatus("Analyzing the forwarded email…");
  try {
    const rawEmail = await extractPhishingEmail();
    const payload = await buildPayload(rawEmail);

    el("attachment-name").textContent = grabbedAttachmentName() ?? "(name unavailable)";
    el("hash").textContent = payload.hash;

    const truncated = payload.rawEmail.length > PREVIEW_LIMIT;
    el("raw-preview").textContent = truncated
      ? payload.rawEmail.slice(0, PREVIEW_LIMIT) +
        `\n\n… [truncated — ${payload.rawEmail.length} chars total]`
      : payload.rawEmail;

    el("result").style.display = "block";
    setStatus("");
  } catch (err) {
    setStatus("");
    showError(err instanceof Error ? err.message : String(err));
  }
}
