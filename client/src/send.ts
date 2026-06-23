import { ReceivePayload } from "./model/email";

const RECEIVE_URL = "http://localhost:8080/api/email/receive";

export async function sendToBackend(payload: ReceivePayload): Promise<void> {
  let response: Response;
  try {
    response = await fetch(RECEIVE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
  } catch (err) {
    // Network-level failure: backend not working, CORS failure, etc.
    throw new Error(
      `Could not reach the backend at ${RECEIVE_URL}. Check it is running and that CORS allows ` +
        `this add-in's origin. (${err instanceof Error ? err.message : String(err)})`
    );
  }

  if (!response.ok) {
    const detail = await readDetail(response);
    throw new Error(`Backend rejected the report (HTTP ${response.status})${detail ? `: ${detail}` : ""}.`);
  }
}

async function readDetail(response: Response): Promise<string | undefined> {
  try {
    const text = await response.text();
    if (!text) {
      return undefined;
    }
    try {
      return (JSON.parse(text) as { detail?: string }).detail ?? text;
    } catch {
      return text;
    }
  } catch {
    return undefined;
  }
}
