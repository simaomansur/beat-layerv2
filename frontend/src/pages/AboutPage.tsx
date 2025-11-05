// src/pages/AboutPage.tsx
import React from "react";
import "../components/AboutPage.css";

const AboutPage: React.FC = () => {
  return (
    <div className="about-page">
      <section className="about-header">
        <h1>About Beat Layer</h1>
        <p>
          Beat Layer is a small tool for capturing jam ideas before they disappear —
          a place to store tempos, keys, vibes, and rough plans so you can come back
          later and build them into full tracks.
        </p>
      </section>

      <section className="about-layout">
        {/* LEFT: story */}
        <div className="about-card">
          <h2>The idea</h2>
          <p>
            Most of the time, musical ideas arrive when you&apos;re not in a full DAW
            session. You might be away from your main setup, experimenting with a
            groove, or just noodling on guitar.
          </p>
          <p>
            Beat Layer gives you a quick way to log the essentials — title, BPM, key,
            genre, and a short instrument hint — so that spark doesn&apos;t get lost
            in a random note or voice memo.
          </p>

          <h3>What it focuses on</h3>
          <ul>
            <li>Fast capture of jam ideas</li>
            <li>Simple organization by tempo and vibe</li>
            <li>A friendly, low-friction place to come back to</li>
          </ul>
        </div>

        {/* RIGHT: tech + you */}
        <div className="about-side">
          <div className="about-card">
            <h2>Under the hood</h2>
            <p className="about-meta">
              Beat Layer is built as a full-stack project to explore modern web patterns.
            </p>
            <div className="about-tech-grid">
              <span className="about-pill">React</span>
              <span className="about-pill">TypeScript</span>
              <span className="about-pill">Spring Boot</span>
              <span className="about-pill">PostgreSQL</span>
              <span className="about-pill">Flyway</span>
            </div>
          </div>

          <div className="about-card about-card--subtle">
            <h2>The person behind it</h2>
            <p className="about-meta">
              This project is a way to combine coding and music — a spot where CS
              skills and jam sessions meet. Over time, the plan is to extend it with:
            </p>
            <ul className="about-list">
              <li>User-linked jams and profiles</li>
              <li>Layered ideas for each jam</li>
              <li>Playback hooks into audio or DAW tools</li>
            </ul>
          </div>
        </div>
      </section>
    </div>
  );
};

export default AboutPage;
