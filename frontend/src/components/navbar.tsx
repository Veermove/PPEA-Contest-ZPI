'use client'
import React from "react";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/context/authContext";
import { Navbar, Nav } from "react-bootstrap";
import { logout } from "@/services/firebase/auth/logout";
import { Trans } from "react-i18next";
import { fallbackLocale, locales } from "@/app/i18n/settings";
import Link from "next/link";
import { useTranslation } from "@/app/i18n/client";

function AppNavbar({lng}: {lng: string})  {
  const { user } = useAuthContext()
  const router = useRouter()

  const handleLogout = async () => {
    try {
      await logout()
    } catch (error) {
      const err = error as Error
      console.error('Unable to logout:' + err.message)
    }

    return router.push("/signin")
  }

  const { t } = useTranslation(lng, 'navbar')

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
        <Trans i18nKey="languageSwitcher" t={t}>
        Switch from <strong>{{lng}}</strong> to:{' '}
      </Trans>
      {locales.filter((l) => lng !== l).map((l, index) => {
        return (
          <span key={l}>
            {index > 0 && (' or ')}
            <Link href={`/${l}`}>
              {l}
            </Link>
          </span>
        )
      })}
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
                <Nav.Link href={`/${lng || fallbackLocale}/signin`} className="text-dark">
                  Sign in
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