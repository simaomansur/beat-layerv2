// src/pages/JamsPage.tsx
import React, { useEffect, useState } from "react";
import { fetchJams, deleteJam, BASE_URL } from "../api"; // 👈 add BASE_URL here
import type { Jam } from "../api";
import { Link } from "react-router-dom";
import "../components/JamsPage.css";

const JamsPage: React.FC = () => {
  const [jams, setJams] = useState<Jam[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Load jams on mount
  useEffect(() => {
    const load = async () => {
      try {
        const data = await fetchJams();

        const getTime = (j: Jam) =>
          j.createdAt ? new Date(j.createdAt).getTime() : 0;

        const sorted = [...data].sort((a, b) => getTime(b) - getTime(a));
        setJams(sorted);
      } catch (err) {
        console.error(err);
        setError("Failed to load jams. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  const handleDelete = async (id: string) => {
    if (!window.confirm("Delete this jam?")) return;

    try {
      await deleteJam(id);
      setJams((prev) => prev.filter((j) => j.id !== id));
    } catch (err) {
      console.error(err);
      alert("Failed to delete jam. Please try again.");
    }
  };

  return (
    <div className="jams-page">
      <div className="jams-page__inner">
        <header className="jams-header">
          <div>
            <h1>Beat Layer – Jams</h1>
            <p>Browse base grooves and find something to build on.</p>
          </div>

          {/* New flow: creation happens in the Studio */}
          <Link to="/studio/new" className="create-jam-button">
            Create Jam
          </Link>
        </header>

        <section className="jams-layout jams-layout--list-only">
          <div className="jams-list-card">
            <div className="jams-list-header">
              <h2>All jams</h2>

              {loading && <span className="pill pill--loading">Loading…</span>}
              {!loading && jams.length === 0 && (
                <span className="pill">No jams yet</span>
              )}
              {error && <span className="pill pill--error">{error}</span>}
            </div>

            {!loading && (
              <div className="jams-grid">
                {jams.map((jam) => (
                  <article key={jam.id} className="jam-card">
                    <div className="jam-card__header">
                      <h3>{jam.title}</h3>
                      <button
                        type="button"
                        className="jam-card__delete"
                        onClick={() => handleDelete(jam.id)}
                      >
                        ✕
                      </button>
                    </div>

                    <div className="jam-card__meta">
                      {jam.key && <span>{jam.key}</span>}
                      {jam.bpm !== null && <span>{jam.bpm} bpm</span>}
                      {jam.genre && <span>{jam.genre}</span>}
                    </div>

                    {jam.instrumentHint && (
                      <p className="jam-card__hint">{jam.instrumentHint}</p>
                    )}

                    {/* 👇 NEW: audio player if a base layer exists */}
                    {jam.baseAudioUrl && (
                      <div className="jam-card__audio">
                        <audio
                          controls
                          src={`${BASE_URL}${jam.baseAudioUrl}`}
                        />
                      </div>
                    )}

                    <div className="jam-card__footer">
                      {jam.createdAt && (
                        <span className="jam-card__date">
                          {new Date(jam.createdAt).toLocaleString()}
                        </span>
                      )}
                    </div>
                  </article>
                ))}
              </div>
            )}
          </div>
        </section>
      </div>
    </div>
  );
};

export default JamsPage;
