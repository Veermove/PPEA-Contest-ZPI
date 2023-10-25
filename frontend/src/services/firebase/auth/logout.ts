import { auth } from "@/context/authContext";
import { signOut } from "firebase/auth";

export const logout = async () => {
  return await signOut(auth);
};
