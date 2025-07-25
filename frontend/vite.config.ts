import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/__tests__/setupTests.tsx'],
  },
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 8080,},
    
    
})
