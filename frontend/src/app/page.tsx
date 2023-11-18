'use client'

import { useAuthContext } from "@/context/authContext";
import { redirect } from "next/navigation";
import { useEffect } from "react";

function Page() {
  const { user } = useAuthContext()

  useEffect(() => {
    if (user) {
      redirect(`/dashboard`)
    } else {
      redirect(`/signin`)
    }
  }, [user])

  return null;
}

export default Page;
