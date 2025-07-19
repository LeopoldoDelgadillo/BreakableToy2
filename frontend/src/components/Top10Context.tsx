import { createContext, useContext, useState, type ReactNode } from "react";

type Top10ContextType = {
  top10: JSON;
  top10set: (val: JSON) => void;
};
const Top10Context = createContext<Top10ContextType | undefined>(undefined);

export function Top10Provider({ children }: { children: ReactNode }) {
  const [top10, setTop10] = useState(JSON);

  const top10set = (val: JSON) => {
    setTop10(val)
  }

  return (
    <Top10Context.Provider value={{ top10, top10set }}>
      {children}
    </Top10Context.Provider>
  );
}

export function useTop10() {
  const ctx = useContext(Top10Context);
  if (!ctx) throw new Error("useSongSearch must be used inside <SongSearchProvider>");
  return ctx;
}