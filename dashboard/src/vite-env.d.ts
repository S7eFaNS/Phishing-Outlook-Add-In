/// <reference types="vite/client" />

// Custom env vars exposed to the client (see .env / .env.example).
interface ImportMetaEnv {
  // Backend origin prefix for the API client. The dashboard calls the backend
  // directly (cross-origin), relying on the backend's CORS allow-list.
  readonly VITE_API_BASE_URL?: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
