// src/api.ts

export type Jam = {
  id: string;
  title: string;
  key: string | null;
  bpm: number | null;
  genre: string | null;
  instrumentHint: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
};

const BASE_URL = "http://localhost:8080"; // Spring Boot backend

export async function fetchJams(): Promise<Jam[]> {
  const res = await fetch(`${BASE_URL}/jams`);
  if (!res.ok) {
    throw new Error(`Failed to fetch jams: ${res.status}`);
  }
  return res.json();
}

export type CreateJamRequest = {
  title: string;
  key?: string | null;
  bpm?: number | null;
  genre?: string | null;
  instrumentHint?: string | null;
};

export async function createJam(payload: CreateJamRequest): Promise<Jam> {
  const res = await fetch(`${BASE_URL}/jams`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    throw new Error(`Failed to create jam: ${res.status}`);
  }

  return res.json();
}
