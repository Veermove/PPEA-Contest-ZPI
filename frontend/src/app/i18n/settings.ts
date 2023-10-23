export const fallbackLocale = 'en';
export const locales = [fallbackLocale, 'pl']
export const cookieName = 'i18next'

export function getOptions (language = fallbackLocale) {
  return {
    supportedLngs: locales,
    fallbackLng: fallbackLocale,
    lng: language,
  }
}
