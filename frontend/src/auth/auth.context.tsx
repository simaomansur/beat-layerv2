import React, { createContext, useContext, useState, ReactNode } from "react";
import authService from "./auth.service";
import type { UserResponse } from "../api";

type AuthContextType = {
  user: UserResponse | null;
  login: (email: string, password: string) => Promise<void>;
  register: (handle: string, email: string, password: string) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserResponse | null>(null);

  const login = async (email: string, password: string) => {
    const loggedUser = await authService.login({ email, password });
    setUser(loggedUser);
    // Optionally store token/localStorage later
  };

  const register = async (handle: string, email: string, password: string) => {
    const newUser = await authService.register({ handle, email, password });
    setUser(newUser);
  };

  const logout = () => {
    setUser(null);
    // clear token/localStorage if used
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
};
