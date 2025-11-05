// src/pages/HomePage.tsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchJams } from "../api";
import type { Jam } from "../api";
import "../components/HomePage.css";

const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [jams, setJams] = useState<Jam[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await fetchJams();

        const getTime = (j: Jam) =>
          j.createdAt ? new Date(j.createdAt).getTime() : 0;

        const sorted = [...data].sort((a, b) => getTime(b) - getTime(a));

        setJams(sorted.slice(0, 3)); // show top 3 recent
      } catch {
        // keep the homepage from exploding if jams fail
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  return (
    <div className="home-page">
      {/* HERO */}
      <section className="home-hero">
        <div className="home-hero-text">
          <p className="home-pill">Welcome to Beat Layer</p>
          <h1>
            Save your ideas.
            <br />
            Stack your <span className="home-gradient-text">layers</span>.
          </h1>
          <p className="home-hero-subtitle">
            Capture quick jam ideas, organize them by vibe, and come back later
            to build full tracks – without losing that first spark.
          </p>

          <div className="home-cta-row">
            <button
              className="home-btn home-btn-primary"
              onClick={() => navigate("/jams")}
            >
              Browse jams
            </button>
            <button
              className="home-btn home-btn-ghost"
              onClick={() => navigate("/profile/me")}
            >
              Go to my profile
            </button>
          </div>

          <p className="home-meta-text">
            Tip: Start by creating a few jams with just a title, BPM, and a
            short instrument hint.
          </p>
        </div>

        <div className="home-hero-card">
          <h2>Today&apos;s session mood</h2>
          <ul>
            <li>🔊 90–110 BPM · laid-back, lo-fi, head-nod grooves</li>
            <li>🎸 Warm keys, mellow guitar, soft sidechain</li>
            <li>🥁 Simple drums now, add layers later</li>
          </ul>
        </div>
      </section>

      {/* FEATURES */}
      <section className="home-section">
        <h2 className="home-section-title">Why use Beat Layer?</h2>
        <div className="home-feature-grid">
          <article className="home-feature-card">
            <h3>Capture the moment</h3>
            <p>
              Jot down key, BPM, genre, and instrument ideas in seconds so you
              never lose a riff again.
            </p>
          </article>
          <article className="home-feature-card">
            <h3>Organize your ideas</h3>
            <p>
              Filter and sort jams by tempo, genre, or mood once we hook up
              more controls on the Jams page.
            </p>
          </article>
          <article className="home-feature-card">
            <h3>Grow your sound</h3>
            <p>
              Come back later, add layers, and turn quick sketches into
              full-blown tracks.
            </p>
          </article>
        </div>
      </section>

      {/* RECENT JAMS PREVIEW */}
      <section className="home-section">
        <div className="home-section-header">
          <h2 className="home-section-title">Recently added jams</h2>
          <button
            className="home-link-button"
            onClick={() => navigate("/jams")}
          >
            View all →
          </button>
        </div>

        {loading ? (
          <p className="home-meta-text">Loading a few recent jams…</p>
        ) : jams.length === 0 ? (
          <p className="home-meta-text">
            No jams yet. Head over to <strong>Jams</strong> to create your first
            idea.
          </p>
        ) : (
          <div className="home-jams-grid">
            {jams.map((jam) => (
              <article key={jam.id} className="home-jam-card">
                <header className="home-jam-card-header">
                  <h3>{jam.title}</h3>
                  {jam.bpm !== null && (
                    <span className="home-badge">{jam.bpm} bpm</span>
                  )}
                </header>

                <p className="home-jam-meta">
                  {jam.key && <span>{jam.key}</span>}
                  {jam.genre && <span> · {jam.genre}</span>}
                </p>

                {jam.instrumentHint && (
                  <p className="home-jam-hint">{jam.instrumentHint}</p>
                )}

                {jam.createdAt && (
                  <p className="home-jam-date">
                    {new Date(jam.createdAt).toLocaleString()}
                  </p>
                )}
              </article>
            ))}
          </div>
        )}
      </section>
    </div>
  );
};

export default HomePage;
