import '../globals.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import AppNavbar from '../../components/navbar';
import { AuthContextProvider } from '@/context/authContext';

export default function RootLayout({ children, params: { lng } }: { children: React.ReactNode, params: { lng: string } }) {
  return (
    <html>
      <head />
      <body>
        <AuthContextProvider>
          <AppNavbar lng={lng} />
          {children}
        </AuthContextProvider>
      </body>
    </html>
  )
}
