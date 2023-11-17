'use client'

import { useTranslation } from "@/app/i18n/client";
import Spinner from "@/components/spinner";
import { useAuthContext } from "@/context/authContext";
import signIn from "@/services/firebase/auth/signin";
import Image from "next/image";
import { redirect, useRouter } from 'next/navigation';
import React from "react";
import isEmail from 'validator/es/lib/isEmail';

function Page() {
  const router = useRouter()
  const { user } = useAuthContext();

  const [email, setEmail] = React.useState('')
  const [password, setPassword] = React.useState('')
  const [error, setError] = React.useState('')
  const [loading, setLoading] = React.useState(false)

  const { t } = useTranslation('signin')

  const translateSigninError = (error: Error) => {
    if (error.message.includes('invalid-login-credentials')) {
      return t('invalidCredentialsError')
    }
    else if (error.message.includes('too-many-requests')) {
      return t('tooManyRequestsError')
    } else {
      return t('defaultError')
    }
  }

  const handleForm = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (!isEmail(email)) {
      setError(t('incorrectEmail'))
      return
    }

    setLoading(true)
    setError('')
    signIn(email, password)
      .then(() => {
        redirect('/dashboard')
      })
      .catch((error) => {
        console.error('Error signing in');
        const err = error as Error
        setError(translateSigninError(err))
        return console.error(error)
      })
      .finally(() => setLoading(false))
  }

  if (!!user) {
    return redirect("/")
  }

  return (
    <div className="d-flex justify-content-center align-items-center h-100 mt-3">
      <div className="form-wrapper p-5">
        <div className="d-flex justify-content-center">
          <Image
            src="/img/ppea-logo.png"
            height="75"
            width="150"
            className="d-inline-block align-top mb-5"
            alt="PPEA logo"
          />
        </div>

        <h1 className="text-center mb-4 text-purple">{t('signIn')}</h1>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={(e) => handleForm(e)} className="form">
          <div className="form-group">
            <label htmlFor="email" className="text-purple">{t('email')}</label>
            <input
              onChange={(e) => setEmail(e.target.value)}
              value={email}
              required
              type="text"
              name="email"
              id="email"
              className="form-control"
              placeholder="test@example.com"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password" className="text-purple">{t('password')}</label>
            <input
              onChange={(e) => { setPassword(e.target.value) }}
              value={password}
              required
              type="password"
              name="password"
              id="password"
              className="form-control"
              placeholder={t('password')}
            />
          </div>
          {loading ? (
            <div className="d-flex justify-content-center mt-3">
              <Spinner />
            </div>
          ) : (
            <button type="submit" className="btn btn-primary w-100 mt-3 text-white">
              {t('signIn')}
            </button>
          )}
        </form>
      </div>
    </div>
  );
}

export default Page;
