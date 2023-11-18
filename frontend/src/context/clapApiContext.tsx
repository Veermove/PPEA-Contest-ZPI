'use client'

import { useAuthContext } from '@/context/authContext';
import { ClapApi } from '@/services/clap/api';
import React, { createContext, useContext, useEffect, useState } from 'react';

const ClapAPIContext = createContext<ClapApi | undefined>(undefined);

export function ClapAPIProvider({ children }: { children: React.ReactNode }) {
  const { user } = useAuthContext();
  const [clapAPI, setClapAPI] = useState<ClapApi>();

  useEffect(() => {
    if (!user) {
      return;
    }
    const initializeClapAPI = async () => {
      let token = await user.getIdTokenResult();
      if (token.claims.refresh as boolean) {
        token = await user.getIdTokenResult(true);
      }

      const clapAPIInstance = new ClapApi(token.token);
      setClapAPI(clapAPIInstance);
    };

    if (user) {
      initializeClapAPI();
    }
  }, [user]);

  return (
    <ClapAPIContext.Provider value={clapAPI}>
      {children}
    </ClapAPIContext.Provider>
  );
}

export function useClapAPI() {
  return useContext(ClapAPIContext);
}
