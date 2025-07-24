import { expect, test } from 'vitest'
import {render, screen} from '@testing-library/react'
import Home from '../pages/home.tsx'
import { renderWithProviders } from './test-utils.tsx'

test('Home',() => {
    renderWithProviders(<Home/>)

    const TopBar = screen.getByTitle("TopBar")
    const Middle = screen.getByTitle("Middle")
    const NowPlaying = screen.getByTitle("NowPlaying")

    expect(TopBar).toBeInTheDocument()
    expect(Middle).toBeInTheDocument()
    expect(NowPlaying).toBeInTheDocument()
})