import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import firebase_app from "../config";

const auth = getAuth(firebase_app);

export default async function signIn(email: string, password: string) {
  return await signInWithEmailAndPassword(auth, email, password);
}
