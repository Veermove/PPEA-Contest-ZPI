import { AuthContextProvider } from '@/context/authContext';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import AppNavbar from '../components/navbar';
import './globals.scss';

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
