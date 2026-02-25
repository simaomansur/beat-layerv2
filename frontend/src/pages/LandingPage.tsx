import React from "react";
import { useNavigate } from "react-router-dom";
import vinylImage from "../assets/beatlayervinyl.png";

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <div className="bl-landing">
      <div className="bl-landing-hero">
        <div className="bl-landing-title">Beat Layer</div>
        <div className="bl-landing-sub">
          A collaborative music thread — build sound layer by layer.
        </div>

        <div className="bl-landing-actions">
          <button className="bl-button" onClick={() => navigate("/app")}>
            Enter Studio
          </button>
          <button className="bl-button bl-button-ghost" disabled>
            Learn more
          </button>
        </div>

        <div className="bl-landing-foot">Matte • warm • collaborative</div>
      </div>

      <div className="bl-landing-lower">
        <div className="bl-landing-steps">
          <div className="bl-step">
            <div className="bl-step-title">Start a jam</div>
            <div className="bl-step-sub">Create a root layer and set the loop.</div>
          </div>
          <div className="bl-step">
            <div className="bl-step-title">Reply with sound</div>
            <div className="bl-step-sub">Layer audio or drop comments in the thread.</div>
          </div>
          <div className="bl-step">
            <div className="bl-step-title">Inspect the lineage</div>
            <div className="bl-step-sub">See exactly what layers built the moment.</div>
          </div>
        </div>

        <div className="bl-landing-preview">
          <div className="bl-preview-title">Preview</div>
          <div className="bl-preview-card">
            <div className="bl-preview-row">
              <span className="bl-badge">AUDIO</span>
              <span className="bl-preview-muted">Root</span>
              <span className="bl-preview-right">Lineage</span>
            </div>
            <div className="bl-waveform-placeholder" />
            <div className="bl-preview-row" style={{ marginTop: 10 }}>
              <span className="bl-badge">COMMENT</span>
              <span className="bl-preview-muted">“adding drums on top”</span>
            </div>
          </div>
        </div>
      </div>

      {/* decorative vinyl */}
      <div className="bl-landing-vinyl" aria-hidden="true">
        <img
          src={vinylImage}
          className={`bl-landing-vinyl-image`}
          alt=""
        />
      </div>
    </div>
  );
}