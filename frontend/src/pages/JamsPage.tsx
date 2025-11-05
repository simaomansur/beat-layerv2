// src/pages/JamsPage.tsx
import React, { useEffect, useState, FormEvent } from "react";
import { fetchJams, createJam, deleteJam } from "../api";
import "../components/JamsPage.css";

type Jam = {
  id: string;
  title: string;
  key: string;
  bpm: number;
  genre: string;
  instrumentHint: string;
  createdAt: string; // adjust if your backend uses a different field name
};

const MIN_BPM = 40;
const MAX_BPM = 260;

const JamsPage: React.FC = () => {
  const [jams, setJams] = useState<Jam[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // form state
  const [title, setTitle] = useState("");
  const [keyVal, setKeyVal] = useState("");
  const [bpm, setBpm] = useState<string>("");
  const [genre, setGenre] = useState("");
  const [instrumentHint, setInstrumentHint] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);

  // Load jams on mount
  useEffect(() => {
    const load = async () => {
      try {
        const data = await fetchJams();
        // Optional: sort newest first if backend doesn’t already
        const sorted = [...data].sort(
          (a: Jam, b: Jam) =>
            new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
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

  const resetForm = () => {
    setTitle("");
    setKeyVal("");
    setBpm("");
    setGenre("");
    setInstrumentHint("");
    setFormError(null);
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setFormError(null);

    const bpmNumber = Number(bpm);
    if (
      Number.isNaN(bpmNumber) ||
      bpmNumber < MIN_BPM ||
      bpmNumber > MAX_BPM
    ) {
      setFormError(`BPM must be between ${MIN_BPM} and ${MAX_BPM}.`);
      return;
    }

    if (!title.trim()) {
      setFormError("Title is required.");
      return;
    }

    setSubmitting(true);

    try {
      const newJam = await createJam({
        title: title.trim(),
        key: keyVal.trim(),
        bpm: bpmNumber,
        genre: genre.trim(),
        instrumentHint: instrumentHint.trim(),
      });

      // Prepend the new jam to the list
      setJams((prev) => [newJam, ...prev]);
      resetForm();
    } catch (err) {
      console.error(err);
      setFormError("Failed to create jam. Check your inputs and try again.");
    } finally {
      setSubmitting(false);
    }
  };

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
          <h1>Beat Layer – Jams</h1>
          <p>Create and explore groove ideas to build on later.</p>
        </header>

        <section className="jams-layout">
          {/* LEFT: form */}
          <div className="jams-form-card">
            <h2>Create a new jam</h2>
            <form onSubmit={handleSubmit} className="jams-form">
              {formError && (
                <div className="jams-form__error">{formError}</div>
              )}

              <div className="jams-form__row">
                <label>
                  Title<span className="required">*</span>
                  <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="Lo-fi midnight groove"
                    required
                  />
                </label>
              </div>

              <div className="jams-form__row jams-form__row--split">
                <label>
                  Key
                  <input
                    type="text"
                    value={keyVal}
                    onChange={(e) => setKeyVal(e.target.value)}
                    placeholder="e.g. C#m"
                  />
                </label>

                <label>
                  BPM
                  <input
                    type="number"
                    value={bpm}
                    onChange={(e) => setBpm(e.target.value)}
                    placeholder="120"
                    min={MIN_BPM}
                    max={MAX_BPM}
                  />
                  <small className="field-hint">
                    {MIN_BPM}–{MAX_BPM} bpm
                  </small>
                </label>
              </div>

              <div className="jams-form__row">
                <label>
                  Genre
                  <input
                    type="text"
                    value={genre}
                    onChange={(e) => setGenre(e.target.value)}
                    placeholder="Lo-fi / hip-hop / trap"
                  />
                </label>
              </div>

              <div className="jams-form__row">
                <label>
                  Instrument hint
                  <textarea
                    value={instrumentHint}
                    onChange={(e) => setInstrumentHint(e.target.value)}
                    placeholder="Start with a mellow Rhodes loop and soft sidechained kick..."
                    rows={3}
                  />
                </label>
              </div>

              <button
                type="submit"
                className="jams-form__submit"
                disabled={submitting}
              >
                {submitting ? "Creating..." : "Create Jam"}
              </button>
            </form>
          </div>

          {/* RIGHT: list */}
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
                      {jam.bpm && <span>{jam.bpm} bpm</span>}
                      {jam.genre && <span>{jam.genre}</span>}
                    </div>

                    {jam.instrumentHint && (
                      <p className="jam-card__hint">{jam.instrumentHint}</p>
                    )}

                    <div className="jam-card__footer">
                      <span className="jam-card__date">
                        {new Date(jam.createdAt).toLocaleString()}
                      </span>
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
