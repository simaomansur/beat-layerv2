// src/api.ts

export type Jam = {
  id: string;
  title: string;
  key: string | null;
  bpm: number;
  genre: string | null;
  instrumentHint: string | null;
  createdAt: string;
  loopBars?: number;
  baseAudioUrl?: string | null; // 👈 new
};

export const BASE_URL = "http://localhost:8080"; // Spring Boot backend

export async function fetchJams(): Promise<Jam[]> {
  const res = await fetch(`${BASE_URL}/jams?page=0&size=50`);
  if (!res.ok) {
    throw new Error("Failed to fetch jams");
  }

  const page = await res.json(); // PageResponse<Jam>
  return page.content;           // 👈 just the array
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

export async function deleteJam(id: string) {
  const res = await fetch(`${BASE_URL}/jams/${id}`, {
    method: "DELETE",
  });

  if (!res.ok && res.status !== 404) {
    // 404 is "already gone" which we can ignore
    throw new Error(`Failed to delete jam: ${res.status}`);
  }
}

export async function fetchCurrentUser() {
  const res = await fetch(`${BASE_URL}/users/me`);
  if (!res.ok) {
    throw new Error(`Failed to load current user: ${res.status}`);
  }
  return res.json(); // { id, handle, email, createdAt }
}

/**
 * Create a new jam and upload its base layer audio.
 * This is used by the Studio when you hit "Publish Jam".
 */
export async function createJamWithBaseLayer(input: {
  title: string;
  key?: string | null;
  bpm: number;
  genre?: string | null;
  instrumentHint?: string | null;
  loopBars: number;
  audioBlob: Blob;
}): Promise<Jam> {
  const {
    title,
    key,
    bpm,
    genre,
    instrumentHint,
    loopBars,
    audioBlob,
  } = input;

  // 1) Create the jam (metadata only)
  const jamRes = await fetch(`${BASE_URL}/jams`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      title,
      key: key ?? null,
      bpm,
      genre: genre ?? null,
      instrumentHint: instrumentHint ?? null,
      // If/when you add loopBars to the backend Jam entity,
      // you can include it here as well.
      // loopBars,
    }),
  });

  if (!jamRes.ok) {
    throw new Error(`Failed to create jam: ${jamRes.status}`);
  }

  const jam: Jam = await jamRes.json();

  // 2) Upload the base layer audio as multipart/form-data
  const formData = new FormData();
  formData.append("audio", audioBlob, "base-layer.webm");
  formData.append("isBase", "true");
  formData.append("loopBars", String(loopBars));

  const layerRes = await fetch(`${BASE_URL}/jams/${jam.id}/layers/base`, {
    method: "POST",
    body: formData,
  });

  if (!layerRes.ok) {
    throw new Error(`Failed to upload base layer: ${layerRes.status}`);
  }

  // You could parse a Layer DTO here if your backend returns one,
  // but for now we just return the jam so the caller can redirect.
  return jam;
}
