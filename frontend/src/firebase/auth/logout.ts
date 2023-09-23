import { signOut } from "firebase/auth";
import { auth } from "@/context/authContext";

export const logout = async () => {
  return await signOut(auth);
};
