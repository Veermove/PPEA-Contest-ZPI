import '../globals.scss'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import AppNavbar from '../../components/navbar';
import { AuthContextProvider } from '@/context/authContext';

export default function RootLayout({ children }: { children: React.ReactNode}) {
  return (
    <html>
      <head />
      <body>
        <AuthContextProvider>
          <AppNavbar/>
          {children}
        </AuthContextProvider>
      </body>
    </html>
  )
}
