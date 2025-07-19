import { createContext, useContext, useState, type ReactNode } from "react";
//chatGPT helped with this one, not gonna lie
type ProfileContextType = {
  profileContent: JSON;
  setProfile: (val: JSON) => void;
};
const ProfileContext = createContext<ProfileContextType | undefined>(undefined);

export function ProfileProvider({ children }: { children: ReactNode }) {
  const [profileContent,setProfileContent] = useState(JSON);

  const setProfile = (val: JSON) => setProfileContent(val);

  return (
    <ProfileContext.Provider value={{ profileContent, setProfile }}>
      {children}
    </ProfileContext.Provider>
  );
}

export function useProfile() {
  const ctx = useContext(ProfileContext);
  if (!ctx) throw new Error("useProfile must be used inside <ProfileProvider>");
  return ctx;
}