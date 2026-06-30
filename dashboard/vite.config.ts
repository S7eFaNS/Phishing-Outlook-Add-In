import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
// The dashboard calls the backend directly using its CORS-allowed origin
// (VITE_API_BASE_URL), so no dev proxy is configured.
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    host: true, // listen on 0.0.0.0 so the container's port is reachable
    port: 5173,
    strictPort: true,
    // Polling makes file watching reliable across Windows hosts and Docker bind mounts.
    watch: { usePolling: true },
  },
})
