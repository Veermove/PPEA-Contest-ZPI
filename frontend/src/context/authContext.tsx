'use client'
import firebase_app from '@/services/firebase/config';
import {
  User,
  getAuth,
  onAuthStateChanged,
} from 'firebase/auth';
import React from 'react';

export const auth = getAuth(firebase_app);

interface UserData {
  firstName: string;
  lastName: string;
  email: string;
  personId: number;
  assessorId?: number;
}

export interface Context {
  user: User | undefined;
  loading: boolean;
  userData: UserData | undefined
}

export const AuthContext = React.createContext<Context>({ user: undefined, loading: true, userData: undefined });

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
    <AuthContext.Provider value={{ user, loading, userData: {
      firstName: 'Test',
      lastName: 'Assessor',
      email: 'test@example.com',
      personId: 1,
      assessorId: 1,
    } }}>
      {loading ? <div>Loading...</div> : children}
    </AuthContext.Provider>
  )
};

