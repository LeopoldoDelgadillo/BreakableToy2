import { createContext, useContext, useState, type ReactNode } from "react";

type songSearchContextType = {
  searchedSong: string;
  changeSearch: (val: string) => void;
};
const SongSearchContext = createContext<songSearchContextType | undefined>(undefined);

export function SongSearchProvider({ children }: { children: ReactNode }) {
  const [searchedSong, setSearchedSong] = useState("");

  const changeSearch = (val: string) => {
    setSearchedSong(val)
    console.log("Search changed")
  }

  return (
    <SongSearchContext.Provider value={{ searchedSong, changeSearch }}>
      {children}
    </SongSearchContext.Provider>
  );
}

export function useSongSearch() {
  const ctx = useContext(SongSearchContext);
  if (!ctx) throw new Error("useSongSearch must be used inside <SongSearchProvider>");
  return ctx;
}