'use client'
import React from "react";
import signIn from "@/services/firebase/auth/signin";
import { useRouter } from 'next/navigation'
import { useAuthContext } from "@/context/authContext";
import { useTranslation } from "@/app/i18n/client";

const EMAIL_REGEX = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[A-Za-z]+$/

function Page({lng}: {lng: string}) {
  const router = useRouter()
  const { user } = useAuthContext();

  if (!!user) {
    router.push("/")
  }

  const [email, setEmail] = React.useState('')
  const [password, setPassword] = React.useState('')
  const [error, setError] = React.useState('')

  const { t } = useTranslation(lng, 'signin')

  const handleForm = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (!email.match(EMAIL_REGEX)) {
      setError(t('incorrectEmail'))
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
        <h1 className="text-center mb-4">{t('signIn')}</h1>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleForm} className="form">
          <div className="form-group">
            <label htmlFor="email">{t('email')}</label>
            <input
              onChange={(e) => setEmail(e.target.value)}
              required
              type="text"
              name="email"
              id="email"
              className="form-control"
              placeholder="test@example.com"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">{t('password')}</label>
            <input
              onChange={(e) => {setPassword(e.target.value)}}
              required
              type="password"
              name="password"
              id="password"
              className="form-control"
              placeholder={t('password')}
            />
          </div>
          <button type="submit" className="btn btn-primary w-100 mt-3">
            {t('signIn')}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Page;
