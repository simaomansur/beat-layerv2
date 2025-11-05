// src/App.tsx
import React from "react";
import { Routes, Route, Link } from "react-router-dom";
import HomePage from "./pages/HomePage";
import JamsPage from "./pages/JamsPage";
import ProfilePage from "./pages/ProfilePage";
import AboutPage from "./pages/AboutPage";
import "./App.css";

const App = () => {
  return (
    <div className="app">
      <header className="app-header">
        <div className="app-logo">Beat Layer</div>
        <nav className="app-nav">
          <Link className="app-nav-link" to="/">
            Home
          </Link>
          <Link className="app-nav-link" to="/jams">
            Jams
          </Link>
          <Link className="app-nav-link" to="/profile/me">
            My Profile
          </Link>
          <Link className="app-nav-link" to="/about">
            About
          </Link>
        </nav>
      </header>

      <main className="app-main">
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

export default App;
