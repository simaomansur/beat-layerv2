import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import JamListPage from "./pages/JamListPage";
import JamDetailPage from "./pages/JamDetailPage";
import "./App.css";

const App: React.FC = () => {
  return (
    <div className="app">
      <Routes>
        <Route path="/" element={<JamListPage />} />
        <Route path="/jams/:jamId" element={<JamDetailPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </div>
  );
};

export default App;
