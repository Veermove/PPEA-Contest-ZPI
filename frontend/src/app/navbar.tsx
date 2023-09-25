import React from "react";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/context/authContext";
import { Navbar, Nav } from "react-bootstrap";
import { logout } from "@/firebase/auth/logout";

function AppNavbar() {
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
                <Nav.Link href="/signup" className="text-dark">
                  Sign up
                </Nav.Link><Nav.Link href="/signin" className="text-dark">
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