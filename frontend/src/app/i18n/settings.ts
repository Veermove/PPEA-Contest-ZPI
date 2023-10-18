export const fallbackLocale = 'en';
export const locales = [fallbackLocale, 'pl']
export const cookieName = 'i18next'
export const defaultNS = 'translation'

export function getOptions (language = fallbackLocale, ns = defaultNS) {
  return {
    supportedLngs: locales,
    fallbackLng: fallbackLocale,
    lng: language,
    fallbackNS: defaultNS,
    defaultNS,
    ns
  }
}
