import '@testing-library/jest-dom'
import { vi } from 'vitest'

global.fetch = vi.fn(() =>
  Promise.resolve(
    new Response(JSON.stringify({}), {
      status: 200,
      headers: { 'Content-Type': 'application/json' },
    })
  )
) as typeof fetch