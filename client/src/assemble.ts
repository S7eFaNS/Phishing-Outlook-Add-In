import { ReceivePayload } from "./model/email";

export async function computeSha256(input: string): Promise<string> {
  const data = new TextEncoder().encode(input);
  const digest = await crypto.subtle.digest("SHA-256", data);
  return Array.from(new Uint8Array(digest))
    .map((b) => (b < 16 ? "0" : "") + b.toString(16))
    .join("");
}

export async function buildPayload(rawEmail: string): Promise<ReceivePayload> {
  const hash = await computeSha256(rawEmail);
  return { hash, rawEmail };
}
