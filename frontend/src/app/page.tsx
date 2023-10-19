'use client'
import { useRouter } from "next/navigation";
import { fallbackLocale } from "./i18n/settings";

function Page() {
  const router = useRouter()
  router.push(`/${fallbackLocale}`)
}

export default Page;
