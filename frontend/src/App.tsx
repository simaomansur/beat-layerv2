// src/App.tsx
import React from "react";
import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import JamsPage from "./pages/JamsPage";
import ProfilePage from "./pages/ProfilePage";
import AboutPage from "./pages/AboutPage";
import LayerStudioPage from "./pages/LayerStudioPage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import Header from "./components/Header";
import ProtectedRoute from "./components/ProtectedRoute";
import "./App.css";

const App: React.FC = () => {
  return (
    <div className="app">
      <Header />

      <main className="app-main">
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<HomePage />} />
          <Route path="/jams" element={<JamsPage />} />
          <Route path="/about" element={<AboutPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Protected routes */}
          <Route path="/profile/:username" element={
            <ProtectedRoute>
              <ProfilePage />
            </ProtectedRoute>
          } />
          <Route path="/studio/new" element={
            <ProtectedRoute>
              <LayerStudioPage />
            </ProtectedRoute>
          } />
        </Routes>
      </main>
    </div>
  );
};

export default App;
