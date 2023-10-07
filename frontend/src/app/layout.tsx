'use client'
import { AuthContextProvider } from '@/context/authContext'
import './globals.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap.css';
import React from 'react'
import AppNavbar from '../components/navbar';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      {/*
        <head /> will contain the components returned by the nearest parent
        head.js. Find out more at https://beta.nextjs.org/docs/api-reference/file-conventions/head
      */}
      <head />
      <body>
        <AuthContextProvider>
          <AppNavbar />
          {children}
        </AuthContextProvider>
      </body>
    </html>
  )
}

