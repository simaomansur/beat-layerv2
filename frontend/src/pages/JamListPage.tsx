import React, { useEffect, useState } from "react";
import { listJams, type JamResponse } from "../api";
import { Link } from "react-router-dom";

export default function JamListPage() {
  const [jams, setJams] = useState<JamResponse[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    listJams()
      .then(setJams)
      .catch((e) => setError(String(e?.message ?? e)));
  }, []);

  return (
    <div style={{ padding: 24, maxWidth: 900, margin: "0 auto" }}>
      <h1>Beat Layer</h1>
      <p>Jams</p>

      {error && (
        <div style={{ padding: 12, border: "1px solid #f00", marginBottom: 12 }}>
          {error}
        </div>
      )}

      <ul style={{ listStyle: "none", padding: 0, display: "grid", gap: 12 }}>
        {jams.map((j) => (
          <li key={j.id} style={{ border: "1px solid #ddd", borderRadius: 8, padding: 12 }}>
            <div style={{ fontWeight: 700 }}>
              <Link to={`/jams/${j.id}`}>{j.title}</Link>
            </div>
            <div style={{ opacity: 0.8, fontSize: 14 }}>
              loopLengthMs: {j.loopLengthMs} {j.bpm ? `• bpm: ${j.bpm}` : ""}{" "}
              {j.musicalKey ? `• key: ${j.musicalKey}` : ""}
            </div>
            <div style={{ opacity: 0.7, fontSize: 12 }}>
              rootItemId: {j.rootItemId ?? "(none yet)"}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
