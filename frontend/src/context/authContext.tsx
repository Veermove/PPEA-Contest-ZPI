'use client'
import Spinner from '@/components/spinner';
import firebase_app from '@/services/firebase/config';
import {
  User,
  getAuth,
  onAuthStateChanged,
} from 'firebase/auth';
import React from 'react';

export const auth = getAuth(firebase_app);

export interface Context {
  user: User | undefined;
  loading: boolean;
}

export const AuthContext = React.createContext<Context>({ user: undefined, loading: true });

export const useAuthContext = () => React.useContext(AuthContext);

export const AuthContextProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = React.useState<User>();
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setUser(!!user ? user : undefined)
      setLoading(false)
    });

    return () => unsubscribe();
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading }}>
      {loading ? <Spinner /> : children}
    </AuthContext.Provider>
  )
};

