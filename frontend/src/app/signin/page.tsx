'use client'

import { useTranslation } from "@/app/i18n/client";
import { useAuthContext } from "@/context/authContext";
import signIn from "@/services/firebase/auth/signin";
import { useRouter } from 'next/navigation';
import React from "react";

const EMAIL_REGEX = /^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\.[A-Za-z]+$/

function Page() {
  const router = useRouter()
  const { user } = useAuthContext();

  if (user) {
    router.push("/")
  }

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

  const handleForm = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    if (!email.match(EMAIL_REGEX)) {
      setError(t('incorrectEmail'))
      return
    }

    try {
      setLoading(true)
      setError('')
      await signIn(email, password);
      return router.push('/dashboard')
    } catch (error) {
      const err = error as Error
      setError(translateSigninError(err))
      return console.log(error)
    } finally {
      setLoading(false)
    }

  }
  return (
    <div className="d-flex justify-content-center align-items-center h-100 mt-3">
      <div className="form-wrapper p-5">
        <div className="d-flex justify-content-center">
          <img
            src="/img/ppea-logo.png"
            height="75"
            width="150"
            className="d-inline-block align-top mb-5"
            alt="PPEA logo"
          />
        </div>

        <h1 className="text-center mb-4 text-purple">{t('signIn')}</h1>
        {error && <div className="alert alert-danger">{error}</div>}
        <form onSubmit={handleForm} className="form">
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
              <div className="spinner-border text-purple" role="status">
              </div>
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
