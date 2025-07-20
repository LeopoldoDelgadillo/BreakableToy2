import { createContext, useContext, useState, type ReactNode } from "react";

type IdentifierContextType = {
  identifierValue: string[];
  changeIdentifier: (val: string[]) => void;
};
const IdentifierContext = createContext<IdentifierContextType | undefined>(undefined);

export function IdentifierProvider({ children }: { children: ReactNode }) {
  const [identifierValue, setIdentifier] = useState<string[]>([]);

  const changeIdentifier = (val: string[]) => setIdentifier(val);

  return (
    <IdentifierContext.Provider value={{ identifierValue, changeIdentifier }}>
      {children}
    </IdentifierContext.Provider>
  );
}

export function useIdentifier() {
  const ctx = useContext(IdentifierContext);
  if (!ctx) throw new Error("useMiddle must be used inside <MiddleProvider>");
  return ctx;
}