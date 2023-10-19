'use client'
import { useRouter, useParams } from "next/navigation";
import { useAuthContext } from "@/context/authContext";

function Page() {
  const { user } = useAuthContext()
  const router = useRouter()
  const params = useParams()
  const locale = params.locale
  if (user) {
    router.push(`${locale}/dashboard`)
  } else {
    router.push(`${locale}/signin`)
  }
}

export default Page;
