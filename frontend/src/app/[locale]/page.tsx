'use client'
import React from "react";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/context/authContext";

function Page() {
  const { user } = useAuthContext()
  const router = useRouter()

  console.log(user);

  React.useEffect(() => {
    if (user == null) router.push("/signin")
  }, [user])

  return (
    <>
      <h1>xyz</h1>
    </>
  );
}

export default Page;
