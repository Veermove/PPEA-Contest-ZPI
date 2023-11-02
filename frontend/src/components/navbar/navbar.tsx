'use client'

import { changeLanguage, useTranslation } from "@/app/i18n/client";
import { locales } from "@/app/i18n/settings";
import { useAuthContext } from "@/context/authContext";
import { logout } from "@/services/firebase/auth/logout";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { Dropdown, Nav, Navbar } from "react-bootstrap";
import './navbar.css';

function AppNavbar() {
  const { user } = useAuthContext()
  const router = useRouter()

  const handleLocaleSwitch = (newLocale: string): void => {
    changeLanguage(newLocale);
  }

  const { t } = useTranslation('navbar')

  const handleLogout = async () => {
    try {
      await logout()
      router.push("/signin")
      return null;
    } catch (error) {
      const err = error as Error
      console.error('Unable to logout:' + err.message)
    }
  }

  return (
    <Navbar bg="light" expand="lg" className="px-4 py-3">
      <Navbar.Brand href="/" className="d-flex align-items-center">
        <Image
          src="/img/ppea-logo.png"
          height="60"
          width="120"
          className="d-inline-block align-top"
          alt="PPEA logo"
        />
      </Navbar.Brand>
      <Navbar.Text className="mx-3">
        <h5 className="text-purple">{t('edition')} 2023</h5>
      </Navbar.Text>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="d-flex align-items-center ml-auto mr-3">
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
                  <Nav.Link href="/dashboard" className="text-purple mx-3">
                    {user?.providerData[0].email}
                  </Nav.Link>
                ) : <></>}
                <Nav.Link className="text-purple" onClick={async() => await handleLogout()}>
                  {t('logout')}
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link href="/signin" className="text-purple">
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
