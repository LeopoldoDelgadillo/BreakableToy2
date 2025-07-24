
import React, { type ReactNode } from 'react'
import { BrowserRouter } from 'react-router-dom'
import { IdentifierProvider } from '../components/IdentifierContext'
import { Top10Provider } from '../components/Top10Context'
import { ProfileProvider } from '../components/ProfileContext'
import { SongSearchProvider } from '../components/songSearchContext'
import { MiddleProvider } from '../components/MiddleContext'
import { render } from '@testing-library/react'
//ChatGPT helped me wrap all the providers
export function Providers({ children }: { children: ReactNode }) {
  
  return (
    <IdentifierProvider>
      <Top10Provider>
        <ProfileProvider>
          <SongSearchProvider>
            <MiddleProvider>
              <BrowserRouter>{children}</BrowserRouter>
            </MiddleProvider>
          </SongSearchProvider>
        </ProfileProvider>
      </Top10Provider>
    </IdentifierProvider>
  )
}

export function renderWithProviders(ui: React.ReactElement) {
  return render(<Providers>{ui}</Providers>)
}