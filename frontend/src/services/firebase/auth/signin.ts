import firebase_app from "../config";
import { signInWithEmailAndPassword, getAuth, UserCredential } from "firebase/auth";

const auth = getAuth(firebase_app);

export default async function signIn(email: string, password: string) {
  return await signInWithEmailAndPassword(auth, email, password);
}
