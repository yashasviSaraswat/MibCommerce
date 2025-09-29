import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': 'http://localhost:8085',
    },
  },
  plugins: [react()],
})
