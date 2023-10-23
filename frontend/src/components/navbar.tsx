'use client'
import React from "react";
import { useRouter, usePathname, useParams } from "next/navigation";
import { useAuthContext } from "@/context/authContext";
import { Navbar, Nav, Dropdown } from "react-bootstrap";
import { logout } from "@/services/firebase/auth/logout";
import { fallbackLocale, locales } from "@/app/i18n/settings";
import { useTranslation } from "@/app/i18n/client";
import './navbar.css'

function AppNavbar() {
  const { user } = useAuthContext()
  const router = useRouter()
  const params = useParams()
  const locale = (typeof params.locale === 'object' ? params.locale[0] : params.locale)
    || fallbackLocale
  const pathname = usePathname()

  const handleLocaleSwitch = (newLocale: string): void => {
    const newPathname = pathname.replace(/^\/[^\/]+/, `/${newLocale}`);
    console.log('newPathname', newPathname)
    return router.push(newPathname);
  }

  const handleLogout = async () => {
    try {
      await logout()
    } catch (error) {
      const err = error as Error
      console.error('Unable to logout:' + err.message)
    }

    return router.push("/")
  }

  const { t } = useTranslation(locale, 'navbar')

  return (
    <Navbar bg="light" expand="lg" className="px-4 py-3">
      <Navbar.Brand href="/" className="d-flex align-items-center">
        <img
          src="/img/ppea-logo.png"
          height="60"
          width="120"
          className="d-inline-block align-top"
          alt="PPEA logo"
        />
      </Navbar.Brand>
      <Navbar.Text className="mx-3">
        <h5 className="text-purple">Edition XXX</h5>
      </Navbar.Text>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="d-flex align-items-center ml-auto">
          <Dropdown className="mx-sm-auto">
            <Dropdown.Toggle id="dropdown" className="btn-language">
              {t('selectLanguage')}
            </Dropdown.Toggle>
            <Dropdown.Menu>
              {locales.map(locale => (
                <Dropdown.Item className="text-purple" key={locale} onClick={() => handleLocaleSwitch(locale)}>{t(locale)}</Dropdown.Item>
              ))}
            </Dropdown.Menu>
          </Dropdown>
        </Nav>
        <Nav className="d-flex align-items-center">
          {
            user ? (
              <>
                {user?.providerData[0].email ? (
                  <Navbar.Text className="text-purple mx-3">
                    {user?.providerData[0].email}
                  </Navbar.Text>
                ) : <></>}
                <Nav.Link className="text-purple" onClick={handleLogout}>
                  {t('logout')}
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link href={`/${locale || fallbackLocale}/signin`} className="text-purple">
                  {t('signIn')}
                </Nav.Link>
              </>
            )
          }
        </Nav>
      </Navbar.Collapse>
    </Navbar >
  );
}

export default AppNavbar;