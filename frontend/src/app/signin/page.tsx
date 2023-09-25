'use client'
import React from "react";
import signIn from "@/firebase/auth/signin";
import { useRouter } from 'next/navigation'
import { useAuthContext } from "@/context/authContext";

function Page() {
  const router = useRouter()
  const { user } = useAuthContext();

  if (!!user) {
    router.push("/")
  }

  const [email, setEmail] = React.useState('')
  const [password, setPassword] = React.useState('')
  const [error, setError] = React.useState('')

  const handleForm = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (!email.match(/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[A-Za-z]+$/)) {
      setError('Niepoprawny email')
      return
    }

    try {
      await signIn(email, password);
      return router.push("/")
    } catch (error) {
      const err = error as Error
      setError(err.message)
      return console.log(error)
    }

  }
  return (
    <div className="d-flex justify-content-center align-items-center h-100 mt-5">
      <div className="form-wrapper p-5">
        <h1 className="text-center mb-4">Zaloguj</h1>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleForm} className="form">
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              onChange={(e) => setEmail(e.target.value)}
              required
              type="email"
              name="email"
              id="email"
              className="form-control"
              placeholder="test@mail.com"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Hasło</label>
            <input
              onChange={(e) => setPassword(e.target.value)}
              required
              type="password"
              name="password"
              id="password"
              className="form-control"
              placeholder="hasło"
            />
          </div>
          <button type="submit" className="btn btn-primary w-100 mt-3">
            Zaloguj
          </button>
        </form>
      </div>
    </div>
  );
}

export default Page;
