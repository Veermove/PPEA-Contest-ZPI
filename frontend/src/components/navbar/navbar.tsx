'use client'

import { changeLanguage, useTranslation } from "@/app/i18n/client";
import { locales } from "@/app/i18n/settings";
import { useAuthContext } from "@/context/authContext";
import { logout } from "@/services/firebase/auth/logout";
import { useRouter } from "next/navigation";
import { Dropdown, Nav, Navbar } from "react-bootstrap";
import './navbar.css';

function AppNavbar() {
  const { user } = useAuthContext()
  const router = useRouter()

  const handleLocaleSwitch = (newLocale: string): void => {
    changeLanguage(newLocale);
  }

  const handleLogout = async () => {
    try {
      await logout()
    } catch (error) {
      const err = error as Error
      console.error('Unable to logout:' + err.message)
    }

    return router.push("/signin")
  }

  const { t } = useTranslation('navbar')

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
        <h5 className="text-purple">Edition 2023</h5>
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
                <Nav.Link href="/assessor/submissions">
                  {t('submissions')}
                </Nav.Link>
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
