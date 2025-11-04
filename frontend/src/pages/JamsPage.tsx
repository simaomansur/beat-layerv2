import React, { useEffect, useState, FormEvent } from "react";
import { fetchJams, createJam, deleteJam } from "../api";

const JamsPage = () => {
  const [jams, setJams] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // form state
  const [title, setTitle] = useState("");
  const [key, setKey] = useState("");
  const [bpm, setBpm] = useState<string>("");
  const [genre, setGenre] = useState("");
  const [instrumentHint, setInstrumentHint] = useState("");
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await fetchJams();
        setJams(data);
      } catch (err: any) {
        console.error(err);
        setError(err.message ?? "Failed to load jams");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    if (!title.trim()) {
      setError("Title is required");
      return;
    }

    setError(null);
    setSubmitting(true);

    try {
      const newJam = await createJam({
        title: title.trim(),
        key: key.trim() || null,
        bpm: bpm ? Number(bpm) : null,
        genre: genre.trim() || null,
        instrumentHint: instrumentHint.trim() || null,
      });

      setJams((prev) => [newJam, ...prev]);

      setTitle("");
      setKey("");
      setBpm("");
      setGenre("");
      setInstrumentHint("");
    } catch (err: any) {
      console.error(err);
      setError(err.message ?? "Failed to create jam");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: string) => {
    try {
      await deleteJam(id);
      setJams((prev) => prev.filter((jam) => jam.id !== id));
    } catch (err: any) {
      console.error(err);
      setError(err.message ?? "Failed to delete jam");
    }
  };

  if (loading) {
    return (
      <div style={styles.page}>
        <h1 style={styles.title}>Jams</h1>
        <p>Loading jams...</p>
      </div>
    );
  }

  return (
    <div style={styles.page}>
      <h1 style={styles.title}>Jams</h1>

      <div style={styles.layout}>
        {/* Left: form */}
        <div style={styles.formCard}>
          <h2 style={styles.sectionTitle}>New Jam</h2>
          <form onSubmit={handleSubmit} style={styles.form}>
            <label style={styles.label}>
              Title *
              <input
                style={styles.input}
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Lo-fi Groove"
              />
            </label>

            <label style={styles.label}>
              Key
              <input
                style={styles.input}
                value={key}
                onChange={(e) => setKey(e.target.value)}
                placeholder="Am"
              />
            </label>

            <label style={styles.label}>
              BPM
              <input
                style={styles.input}
                type="number"
                value={bpm}
                onChange={(e) => setBpm(e.target.value)}
                placeholder="90"
              />
            </label>

            <label style={styles.label}>
              Genre
              <input
                style={styles.input}
                value={genre}
                onChange={(e) => setGenre(e.target.value)}
                placeholder="lofi"
              />
            </label>

            <label style={styles.label}>
              Instrument hint
              <input
                style={styles.input}
                value={instrumentHint}
                onChange={(e) => setInstrumentHint(e.target.value)}
                placeholder="guitar"
              />
            </label>

            {error && <p style={styles.error}>{error}</p>}

            <button style={styles.button} type="submit" disabled={submitting}>
              {submitting ? "Saving..." : "Save Jam"}
            </button>
          </form>
        </div>

        {/* Right: list */}
        <div style={styles.listCard}>
          <h2 style={styles.sectionTitle}>Your Jams</h2>
          {jams.length === 0 ? (
            <p>No jams yet. Add one on the left.</p>
          ) : (
            <ul style={styles.list}>
              {jams.map((jam) => (
                <li key={jam.id} style={styles.card}>
                  <div style={styles.cardHeader}>
                    <div>
                      <span style={styles.cardTitle}>{jam.title}</span>
                      {jam.bpm != null && (
                        <span style={styles.badge}>{jam.bpm} bpm</span>
                      )}
                    </div>
                    <button
                      style={styles.deleteButton}
                      type="button"
                      onClick={() => handleDelete(jam.id)}
                    >
                      ✕
                    </button>
                  </div>
                  <div style={styles.cardMeta}>
                    {jam.key && <span>{jam.key}</span>}
                    {jam.genre && <span> · {jam.genre}</span>}
                  </div>
                  {jam.instrumentHint && (
                    <p style={styles.hint}>🎸 {jam.instrumentHint}</p>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
};

const styles: any = {
  page: {
    maxWidth: "1100px",
    margin: "0 auto",
  },
  title: {
    fontSize: "1.6rem",
    marginBottom: "1rem",
  },
  layout: {
    display: "grid",
    gridTemplateColumns: "minmax(0, 1.2fr) minmax(0, 2fr)",
    gap: "1.5rem",
    alignItems: "flex-start",
  },
  sectionTitle: {
    fontSize: "1.1rem",
    marginBottom: "0.75rem",
  },
  formCard: {
    background: "rgba(15, 23, 42, 0.95)",
    borderRadius: "0.75rem",
    padding: "1.25rem",
    boxShadow: "0 10px 25px rgba(0, 0, 0, 0.5)",
    border: "1px solid rgba(148, 163, 184, 0.35)",
  },
  listCard: {
    background: "rgba(15, 23, 42, 0.85)",
    borderRadius: "0.75rem",
    padding: "1.25rem",
    boxShadow: "0 10px 25px rgba(0, 0, 0, 0.45)",
    border: "1px solid rgba(148, 163, 184, 0.3)",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "0.75rem",
  },
  label: {
    fontSize: "0.85rem",
    display: "flex",
    flexDirection: "column",
    gap: "0.35rem",
  },
  input: {
    padding: "0.45rem 0.6rem",
    borderRadius: "0.5rem",
    border: "1px solid rgba(148, 163, 184, 0.5)",
    background: "rgba(15, 23, 42, 0.9)",
    color: "#e5e7eb",
    outline: "none",
  },
  button: {
    marginTop: "0.5rem",
    padding: "0.6rem 0.9rem",
    borderRadius: "999px",
    border: "none",
    fontWeight: 600,
    fontSize: "0.9rem",
    cursor: "pointer",
    background:
      "radial-gradient(circle at 0% 0%, #22c55e 0%, #22d3ee 30%, #6366f1 70%, #ec4899 100%)",
  },
  list: {
    listStyle: "none",
    padding: 0,
    margin: 0,
    display: "grid",
    gap: "0.75rem",
  },
  card: {
    background: "rgba(15, 23, 42, 0.95)",
    borderRadius: "0.75rem",
    padding: "0.9rem 1rem",
    border: "1px solid rgba(148, 163, 184, 0.25)",
  },
  cardHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-start",
    marginBottom: "0.25rem",
    gap: "0.75rem",
  },
  cardTitle: {
    fontWeight: 600,
    fontSize: "1rem",
    marginRight: "0.5rem",
  },
  badge: {
    marginLeft: "0.3rem",
    padding: "0.15rem 0.5rem",
    borderRadius: "999px",
    fontSize: "0.75rem",
    border: "1px solid rgba(248, 250, 252, 0.3)",
  },
  deleteButton: {
    border: "none",
    background: "transparent",
    color: "#fca5a5",
    cursor: "pointer",
    fontSize: "0.9rem",
  },
  cardMeta: {
    fontSize: "0.85rem",
    color: "#9ca3af",
    marginBottom: "0.25rem",
  },
  hint: {
    fontSize: "0.8rem",
    color: "#d1d5db",
    marginTop: "0.1rem",
  },
  error: {
    fontSize: "0.8rem",
    color: "#fecaca",
  },
};

export default JamsPage;
