// src/auth/auth.service.ts
import { BASE_URL } from "../api";
import { saveToken } from "./token";

type RegisterRequest = {
  handle: string;
  email: string;
  password: string;
};

type LoginRequest = {
  email: string;
  password: string;
};

export type UserResponse = {
  id: string;
  handle: string;
  email: string;
  createdAt: string;
};

type AuthResponse = {
  token: string;
  user: UserResponse;
};

const authService = {
  register: async (data: RegisterRequest): Promise<UserResponse> => {
    const res = await fetch(`${BASE_URL}/users/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Registration failed");
    }

    // backend returns just UserResponse for register
    return res.json();
  },

  login: async (data: LoginRequest): Promise<UserResponse> => {
    const res = await fetch(`${BASE_URL}/users/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Login failed");
    }

    const auth: AuthResponse = await res.json();

    // save JWT to localStorage
    saveToken(auth.token);

    // return user so AuthContext can store it
    return auth.user;
  },
};

export default authService;
