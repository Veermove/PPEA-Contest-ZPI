import { getDownloadURL, getStorage, ref } from "firebase/storage";
import firebaseApp from "../config";
const storage = getStorage(firebaseApp);

export async function getFileLink(submissionId: number, filename: string): Promise<string> {
  return await getDownloadURL(ref(storage, `${submissionId}/${filename}`))
}
