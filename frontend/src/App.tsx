import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import JamsPage from "./pages/JamsPage";
import ProfilePage from "./pages/ProfilePage";
import AboutPage from "./pages/AboutPage";

const App = () => {
  return (
    <div style={styles.app}>
      <header style={styles.header}>
        <div style={styles.logo}>Beat Layer</div>
        <nav style={styles.nav}>
          <Link style={styles.navLink} to="/">
            Home
          </Link>
          <Link style={styles.navLink} to="/jams">
            Jams
          </Link>
          <Link style={styles.navLink} to="/profile/me">
            My Profile
          </Link>
          <Link style={styles.navLink} to="/about">
            About
          </Link>
        </nav>
      </header>

      <main style={styles.main}>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/jams" element={<JamsPage />} />
          <Route path="/profile/:username" element={<ProfilePage />} />
          <Route path="/about" element={<AboutPage />} />
        </Routes>
      </main>
    </div>
  );
};

const styles: any = {
  app: {
    minHeight: "100vh",
    background:
      "linear-gradient(135deg, #050816 0%, #111827 50%, #020617 100%)",
    color: "#e5e7eb",
    fontFamily: "system-ui, -apple-system, BlinkMacSystemFont, sans-serif",
  },
  header: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "0.75rem 1.5rem",
    borderBottom: "1px solid rgba(148, 163, 184, 0.3)",
    background: "rgba(15, 23, 42, 0.95)",
    position: "sticky",
    top: 0,
    zIndex: 10,
  },
  logo: {
    fontWeight: 700,
    letterSpacing: "0.08em",
    textTransform: "uppercase",
    fontSize: "0.9rem",
  },
  nav: {
    display: "flex",
    gap: "1rem",
    fontSize: "0.9rem",
  },
  navLink: {
    textDecoration: "none",
    color: "#e5e7eb",
    opacity: 0.85,
  },
  main: {
    padding: "1.5rem",
  },
};

export default App;
