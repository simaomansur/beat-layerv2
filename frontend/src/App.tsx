import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import WorkspacePage from "./pages/WorkspacePage";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/app" element={<WorkspacePage />} />
      <Route path="/app/jams/:jamId" element={<WorkspacePage />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}