'use client'

import { useAuthContext } from "@/context/authContext";
import { redirect } from "next/navigation";

function Page() {
  const { user } = useAuthContext()
  if (user) {
    return redirect(`/dashboard`)
  } else {
    return redirect(`/signin`)
  }
}

export default Page;
