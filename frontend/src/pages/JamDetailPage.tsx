import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getJam, getJamThread, getLineage, type JamResponse, type ThreadItemResponse } from "../api";

export default function JamDetailPage() {
  const { jamId } = useParams();
  const [jam, setJam] = useState<JamResponse | null>(null);
  const [thread, setThread] = useState<ThreadItemResponse[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [lineageText, setLineageText] = useState<string>("");

  useEffect(() => {
    if (!jamId) return;

    setError(null);
    setLineageText("");

    Promise.all([getJam(jamId), getJamThread(jamId)])
      .then(([j, t]) => {
        setJam(j);
        setThread(t);
      })
      .catch((e) => setError(String(e?.message ?? e)));
  }, [jamId]);

  async function onClickAudio(item: ThreadItemResponse) {
    try {
      const layers = await getLineage(item.id);
      setLineageText(JSON.stringify(layers, null, 2));
    } catch (e: any) {
      setLineageText(String(e?.message ?? e));
    }
  }

  return (
    <div style={{ padding: 24, maxWidth: 900, margin: "0 auto" }}>
      <Link to="/">← Back</Link>

      {error && (
        <div style={{ padding: 12, border: "1px solid #f00", marginTop: 12 }}>
          {error}
        </div>
      )}

      {jam && (
        <>
          <h1 style={{ marginTop: 12 }}>{jam.title}</h1>
          <div style={{ opacity: 0.8 }}>
            {jam.bpm ? `bpm: ${jam.bpm}` : ""} {jam.musicalKey ? `• key: ${jam.musicalKey}` : ""}
          </div>
          <div style={{ opacity: 0.7, fontSize: 12 }}>jamId: {jam.id}</div>
        </>
      )}

      <h2 style={{ marginTop: 24 }}>Thread</h2>

      <ul style={{ listStyle: "none", padding: 0, display: "grid", gap: 10 }}>
        {thread.map((t) => (
          <li key={t.id} style={{ border: "1px solid #ddd", borderRadius: 8, padding: 10 }}>
            <div style={{ display: "flex", justifyContent: "space-between", gap: 12 }}>
              <div>
                <div style={{ fontWeight: 700 }}>
                  {t.itemType} {t.parentItemId ? "↳ reply" : "(root)"}
                </div>
                <div style={{ fontSize: 12, opacity: 0.7 }}>
                  id: {t.id}
                </div>
                {t.itemType === "COMMENT" && (
                  <div style={{ marginTop: 8 }}>{t.body}</div>
                )}
              </div>

              {t.itemType === "AUDIO" && (
                <button onClick={() => onClickAudio(t)}>Lineage</button>
              )}
            </div>
          </li>
        ))}
      </ul>

      <h2 style={{ marginTop: 24 }}>Lineage output</h2>
      <pre style={{ background: "#111", color: "#eee", padding: 12, borderRadius: 8, overflow: "auto" }}>
        {lineageText || "Click an AUDIO item to fetch lineage..."}
      </pre>
    </div>
  );
}
