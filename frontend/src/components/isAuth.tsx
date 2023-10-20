'use client';
import { useEffect } from "react";
import { redirect } from "next/navigation";
import { useAuthContext } from "@/context/authContext";

function isAuth(Component: any) {
  return function IsAuth(props: any) {
    const {user} = useAuthContext();
    useEffect(() => {
      if (!user) {
        return redirect("/");
      }
    }, []);


    if (!user) {
      return null;
    }

    return <Component {...props} />;
  };
}

export default isAuth;