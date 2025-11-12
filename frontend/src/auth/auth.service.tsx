// src/auth/auth.service.ts
import { BASE_URL } from "../api";

type RegisterRequest = {
  handle: string;
  email: string;
  password: string;
};

type LoginRequest = {
  email: string;
  password: string;
};

type UserResponse = {
  id: string;
  handle: string;
  email: string;
  createdAt: string;
};

export default {
  register: async (data: RegisterRequest): Promise<UserResponse> => {
    const res = await fetch(`${BASE_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Registration failed");
    }
    return res.json();
  },

  login: async (data: LoginRequest): Promise<UserResponse> => {
    const res = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    if (!res.ok) {
      const text = await res.text();
      throw new Error(text || "Login failed");
    }
    return res.json();
  },
};
