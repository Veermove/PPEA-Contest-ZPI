'use client'
import React from "react";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/context/authContext";
import AppNavbar from "../navbar";

function Page() {
  const { user } = useAuthContext()
  const router = useRouter()

  console.log(user);

  React.useEffect(() => {
    if (user == null) router.push("/signin")
  }, [user])

  return (
    <>
      <h1>Only logged in users can view this page</h1>
    </>
  );
}

export default Page;
