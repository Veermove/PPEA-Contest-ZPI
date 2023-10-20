'use client'
import isAuth from "@/components/isAuth";

function Page() {
  return(
    <div className="m-3">
        <h3>Dashboard for authenticated user</h3>
    </div>
  )
}

export default isAuth(Page);