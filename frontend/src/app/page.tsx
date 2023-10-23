'use client'
import { redirect } from "next/navigation";
import { useAuthContext } from "@/context/authContext";

function Page() {
  const { user } = useAuthContext()
  if (user) {
    return redirect(`/dashboard`)
  } else {
    return redirect(`/signin`)
  }
}

export default Page;
