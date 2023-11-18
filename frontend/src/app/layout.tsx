import { AuthContextProvider } from '@/context/authContext';
import { ClapAPIProvider } from '@/context/clapApiContext';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Metadata } from 'next';
import React from 'react';
import { ToastContainer } from 'react-bootstrap';
import AppNavbar from '../components/navbar/navbar';
import './globals.scss';

export const metadata: Metadata = {
  title: 'IPMA PEM'
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html>
      <body>
        <AuthContextProvider>
          <ToastContainer />
          <ClapAPIProvider>
            <AppNavbar />
            {children}
          </ClapAPIProvider>
        </AuthContextProvider>
      </body>
    </html>
  )
}
