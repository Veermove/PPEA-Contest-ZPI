'use client'

import { useAuthContext } from "@/context/authContext";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

function Page() {
  const { user } = useAuthContext()
  const router = useRouter()

  useEffect(() => {
    if (user) {
      router.push(`/dashboard`)
    } else {
      router.push(`/signin`)
    }
  }, [user, router])

  return null;
}

export default Page;
