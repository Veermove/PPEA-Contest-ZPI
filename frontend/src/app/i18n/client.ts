'use client'

import { useEffect, useState } from 'react'
import i18next from 'i18next'
import {
  UseTranslationOptions,
  initReactI18next,
  useTranslation as useTranslationOrg
} from 'react-i18next'
import resourcesToBackend from 'i18next-resources-to-backend'
import LanguageDetector from 'i18next-browser-languagedetector'
import { getOptions, locales, cookieName } from './settings'

const runsOnServerSide = typeof window === 'undefined'

i18next
  .use(initReactI18next)
  .use(LanguageDetector)
  .use(resourcesToBackend((locale: string, namespace: string) =>
    import(`./locales/${locale}/${namespace}.json`)))
  .init({
    ...getOptions(),
    lng: undefined, // let detect the language on client side
    detection: {
      order: ['path', 'htmlTag', 'cookie', 'navigator'],
    },
    preload: runsOnServerSide ? locales : []
  })

export function useTranslation(ns: string, options: UseTranslationOptions<undefined> = {}) {
  const ret = useTranslationOrg(ns, options)
  const { i18n } = ret
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [activeLng, setActiveLng] = useState(i18n.resolvedLanguage)
  // eslint-disable-next-line react-hooks/rules-of-hooks
  useEffect(() => {
    if (activeLng === i18n.resolvedLanguage) return
    setActiveLng(i18n.resolvedLanguage)
  }, [activeLng, i18n.resolvedLanguage])
  return ret
}

export function changeLanguage(lng: string) {
  i18next.changeLanguage(lng)
  if (!runsOnServerSide) {
    document.cookie = `${cookieName}=${lng};path=/`
  }
}
