'use client'
import React from "react";
import { useRouter, usePathname, useParams } from "next/navigation";
import { useAuthContext } from "@/context/authContext";
import { Navbar, Nav, Dropdown } from "react-bootstrap";
import { logout } from "@/services/firebase/auth/logout";
import { fallbackLocale, locales } from "@/app/i18n/settings";
import { useTranslation } from "@/app/i18n/client";

function AppNavbar()  {
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
        <h4><span className="font-bold">PPEA</span><span className="d-none d-lg-inline"> - Polish Project Excellence Award</span></h4>
      </Navbar.Brand>
      <Navbar.Text>
        <h5>Edition XXX</h5>
      </Navbar.Text>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto d-flex align-items-center">
        </Nav>
        <Dropdown>
        <Dropdown.Toggle variant="success" id="dropdown-secondary">
          {t('selectLanguage')}
        </Dropdown.Toggle>
      <Dropdown.Menu>
        {locales.map(locale => (
          <Dropdown.Item key={locale} onClick={() => handleLocaleSwitch(locale)}>{t(locale)}</Dropdown.Item>
        ))}
      </Dropdown.Menu>
    </Dropdown>
        <Nav className="d-flex align-items-center">
          {
            user ? (
              <>
                <Nav.Link href="/" className="text-dark">
                  {user?.providerData[0].email}
                </Nav.Link><Nav.Link className="text-dark" onClick={handleLogout}>
                  Logout
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link href={`/${locale || fallbackLocale}/signin`} className="text-dark">
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