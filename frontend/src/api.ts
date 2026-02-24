export const BASE_URL = "http://localhost:8080";

export type JamResponse = {
  id: string;
  createdByUserId: string;
  title: string;
  description: string | null;
  loopLengthMs: number;
  bpm: number | null;
  musicalKey: string | null;
  genre: string | null;
  instrumentHint: string | null;
  visibility: string | null;
  rootItemId: string | null;
  createdAt: string;
  updatedAt: string;
};

export type ThreadItemResponse = {
  id: string;
  jamId: string;
  parentItemId: string | null;
  createdByUserId: string;
  itemType: "AUDIO" | "COMMENT";
  body: string | null;
  createdAt: string;
};

export type LineageAudioLayerResponse = {
  threadItemId: string;
  jamId: string;
  parentItemId: string | null;
  createdByUserId: string;
  createdAt: string;

  audioAssetId: string;
  storageLocator: string;
  mimeType: string;
  durationMs: number;

  startOffsetMs: number;
  trimStartMs: number;
  trimEndMs: number | null;
  gainDb: number; // JSON number
  pan: number;    // JSON number
  muted: boolean;

  instrument: string | null;
  notes: string | null;
};

async function http<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, init);
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`${res.status} ${res.statusText} - ${text}`);
  }
  return res.json() as Promise<T>;
}

export function listJams() {
  return http<JamResponse[]>("/jams");
}

export function getJam(jamId: string) {
  return http<JamResponse>(`/jams/${jamId}`);
}

export function getJamThread(jamId: string) {
  return http<ThreadItemResponse[]>(`/thread/jams/${jamId}`);
}

export function getLineage(threadItemId: string) {
  return http<LineageAudioLayerResponse[]>(`/thread/${threadItemId}/lineage`);
}

export function createComment(parentId: string, createdByUserId: string, body: string) {
  return http<ThreadItemResponse>(`/thread/${parentId}/comment`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ createdByUserId, body }),
  });
}
