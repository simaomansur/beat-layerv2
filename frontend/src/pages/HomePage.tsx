import React from "react";
import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Welcome to Beat Layer</h1>
      <p style={styles.subtitle}>
        Browse community jams, stack your own layers, and share your sound.
      </p>

      <div style={styles.actions}>
        <Link to="/jams" style={styles.primaryButton}>
          Explore Jams
        </Link>
        <Link to="/profile/me" style={styles.secondaryButton}>
          Go to My Profile
        </Link>
      </div>
    </div>
  );
};

const styles: any = {
  container: {
    maxWidth: "800px",
    margin: "0 auto",
    paddingTop: "2rem",
  },
  title: {
    fontSize: "2rem",
    marginBottom: "0.75rem",
  },
  subtitle: {
    fontSize: "1rem",
    opacity: 0.8,
    marginBottom: "1.5rem",
  },
  actions: {
    display: "flex",
    gap: "1rem",
  },
  primaryButton: {
    padding: "0.6rem 1rem",
    borderRadius: "999px",
    textDecoration: "none",
    border: "none",
    fontWeight: 600,
    fontSize: "0.9rem",
    background:
      "radial-gradient(circle at 0% 0%, #22c55e 0%, #22d3ee 30%, #6366f1 70%, #ec4899 100%)",
    color: "#0b1120",
  },
  secondaryButton: {
    padding: "0.6rem 1rem",
    borderRadius: "999px",
    textDecoration: "none",
    border: "1px solid rgba(148,163,184,0.6)",
    fontWeight: 500,
    fontSize: "0.9rem",
    color: "#e5e7eb",
  },
};

export default HomePage;
