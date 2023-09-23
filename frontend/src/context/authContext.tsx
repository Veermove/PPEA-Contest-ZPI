import React from 'react';
import {
  onAuthStateChanged,
  getAuth,
  User,
} from 'firebase/auth';
import firebase_app from '@/firebase/config';

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
      if (user) {
        setUser(user);
      } else {
        setUser(undefined);
      }
      setLoading(false);
    });

    return () => unsubscribe();
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading }}>
      {loading ? <div>Loading...</div> : children}
    </AuthContext.Provider>
  )
};

