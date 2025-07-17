import { createContext, useContext, useState, type ReactNode } from "react";
//chatGPT helped with this one, not gonna lie
type MiddleContextType = {
  middleValue: number;
  changeValue: (val: number) => void;
};
const MiddleContext = createContext<MiddleContextType | undefined>(undefined);

export function MiddleProvider({ children }: { children: ReactNode }) {
  const [middleValue, setMiddleValue] = useState(0);

  const changeValue = (val: number) => setMiddleValue(val);

  return (
    <MiddleContext.Provider value={{ middleValue, changeValue }}>
      {children}
    </MiddleContext.Provider>
  );
}

export function useMiddle() {
  const ctx = useContext(MiddleContext);
  if (!ctx) throw new Error("useMiddle must be used inside <MiddleProvider>");
  return ctx;
}