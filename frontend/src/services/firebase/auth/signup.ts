import firebase_app from "../config";
import { UserCredential, createUserWithEmailAndPassword, getAuth } from "firebase/auth";

const auth = getAuth(firebase_app);

export default async function signUp(email: string, password: string) {
  return await createUserWithEmailAndPassword(auth, email, password);
}
