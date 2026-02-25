import React, { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  listJams,
  getJam,
  getJamThread,
  getLineage,
  type JamResponse,
  type ThreadItemResponse,
  type LineageAudioLayerResponse,
} from "../api";
import ThreadTree from "../components/ThreadTree";
import { buildThreadTree } from "../components/buildThreadTree";
import type { ThreadNode } from "../components/threadTypes";
import vinylImage from "../assets/beatlayervinyl.png";

export default function WorkspacePage() {
  const navigate = useNavigate();
  const params = useParams();
  const routeJamId = params.jamId;

  const [jams, setJams] = useState<JamResponse[]>([]);
  const [activeJamId, setActiveJamId] = useState<string | null>(routeJamId ?? null);

  const [jam, setJam] = useState<JamResponse | null>(null);
  const [thread, setThread] = useState<ThreadItemResponse[]>([]);
  const roots = useMemo(() => buildThreadTree(thread), [thread]);

  const [selected, setSelected] = useState<ThreadItemResponse | null>(null);

  const [lineage, setLineage] = useState<LineageAudioLayerResponse[] | null>(null);
  const [activeAudioUrl, setActiveAudioUrl] = useState<string | null>(null);

  const [isPlaying, setIsPlaying] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Load jam list once
  useEffect(() => {
    listJams()
      .then(setJams)
      .catch((e) => setError(String(e?.message ?? e)));
  }, []);

  // Keep active jam in sync with URL if user navigates directly
  useEffect(() => {
    if (routeJamId && routeJamId !== activeJamId) setActiveJamId(routeJamId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [routeJamId]);

  // Load selected jam + thread
  useEffect(() => {
    if (!activeJamId) {
      setJam(null);
      setThread([]);
      setSelected(null);
      setLineage(null);
      setActiveAudioUrl(null);
      setIsPlaying(false);
      return;
    }

    setError(null);
    setSelected(null);
    setLineage(null);
    setActiveAudioUrl(null);
    setIsPlaying(false);

    Promise.all([getJam(activeJamId), getJamThread(activeJamId)])
      .then(([j, t]) => {
        setJam(j);
        setThread(t);
      })
      .catch((e) => setError(String(e?.message ?? e)));
  }, [activeJamId]);

  async function onClickAudio(node: ThreadNode) {
    // keep Studio + Thread selection in sync
    setSelected(node);

    // reset playback + lineage each time
    setLineage(null);
    setActiveAudioUrl(null);
    setIsPlaying(false);

    if (node.itemType !== "AUDIO") return;

    try {
      const layers = await getLineage(node.id);
      setLineage(layers);

      // best-effort “selected audio layer” url for future real playback
      const last = layers[layers.length - 1];
      setActiveAudioUrl(last?.storageLocator ?? null);
    } catch (e: any) {
      setError(String(e?.message ?? e));
    }
  }

  function openJam(id: string) {
    setActiveJamId(id);
    navigate(`/app/jams/${id}`);
  }

  const canPlay = !!selected && selected.itemType === "AUDIO";

  return (
    <div className="bl-app">
      <header className="bl-nav">
        <div className="bl-nav-left">
          <Link className="bl-brand" to="/">
            Beat Layer
          </Link>
          <div className="bl-nav-muted">workspace</div>
        </div>

        {jam && (
          <div className="bl-jam-meta">
              {jam.bpm ? `BPM ${jam.bpm}` : "No BPM"}
              <span className="bl-dot">•</span>
              {jam.musicalKey ? `Key ${jam.musicalKey}` : "No key"}
              <span className="bl-dot">•</span>
              {`Loop ${(jam.loopLengthMs / 1000).toFixed(1)}s`}
          </div>
        )}

        <div className="bl-nav-right">
          <button className="bl-button bl-button-ghost" disabled title="Coming soon">
            New Jam
          </button>
          <button className="bl-button bl-button-ghost" disabled title="Coming soon">
            Profile
          </button>
        </div>
      </header>

      {error && <div className="bl-alert bl-alert-error">{error}</div>}

      <div className="bl-workspace">
        {/* Left: Jam crate */}
        <aside className="bl-pane bl-pane-left">
          <div className="bl-pane-title">Crate</div>
          <div className="bl-pane-sub">Your jams</div>

          <div className="bl-crate">
            {jams.map((j) => {
              const active = String(j.id) === String(activeJamId);
              return (
                <button
                  key={j.id}
                  className={`bl-crate-item ${active ? "bl-crate-item-active" : ""}`}
                  onClick={() => openJam(String(j.id))}
                >
                  <div className="bl-crate-name">{j.title}</div>
                  <div className="bl-crate-meta">
                    {j.bpm ? `BPM ${j.bpm}` : "—"}
                    <span className="bl-dot">•</span>
                    {j.musicalKey ? `Key ${j.musicalKey}` : "No key"}
                  </div>
                </button>
              );
            })}
          </div>
        </aside>

        {/* Center: Thread (fills bottom) */}
        <main className="bl-pane bl-pane-center">
          <div className="bl-thread-header">
            <div>
              <div className="bl-pane-title">{jam?.title ?? "Select a jam"}</div>
              <div className="bl-pane-sub">
                {jam ? "Thread (tree) — audio + comments" : "Pick a jam from the crate"}
              </div>
            </div>

            <div className="bl-thread-actions">
              <button className="bl-button bl-button-ghost" disabled>
                Reply
              </button>
            </div>
          </div>

          <div className="bl-thread-scroll">
            <ThreadTree
              roots={roots}
              selectedId={selected?.id ? String(selected.id) : undefined}
              onSelectNode={(n) => setSelected(n)}
              onClickAudio={onClickAudio}
            />
          </div>
        </main>

        {/* Right: Studio (info + transport only) */}
        <aside className="bl-pane bl-pane-right">
          <div className="bl-pane-title">Studio</div>
          <div className="bl-pane-sub">Inspect + play selected layer</div>

          <div className="bl-studio-card">
            <div className="bl-studio-top">
              <div className="bl-studio-selected">
                {!selected ? (
                  <div className="bl-empty">Select an item in the thread.</div>
                ) : (
                  <>
                    <div className="bl-studio-row">
                      <span className="bl-badge">{selected.itemType}</span>
                      <span className="bl-mono">{String(selected.id).slice(0, 8)}…</span>
                    </div>
                    <div className="bl-studio-row bl-muted">
                      parent:{" "}
                      <span className="bl-mono">
                        {selected.parentItemId
                          ? String(selected.parentItemId).slice(0, 8) + "…"
                          : "root"}
                      </span>
                    </div>
                  </>
                )}
              </div>

              <div className="bl-transport">
                <button
                  className="bl-button"
                  disabled={!canPlay}
                  onClick={() => setIsPlaying(true)}
                >
                  Play
                </button>
                <button
                  className="bl-button bl-button-ghost"
                  disabled={!canPlay}
                  onClick={() => setIsPlaying(false)}
                >
                  Stop
                </button>
              </div>
            </div>

            <div className="bl-lineage">
              <div className="bl-section-title">Lineage</div>

              {!lineage && (
                <div className="bl-empty">Click “Lineage” on an AUDIO node.</div>
              )}

              {lineage && (
                <div className="bl-lineage-list">
                  {lineage.map((layer, idx) => {
                    const label =
                      idx === 0
                        ? "Root"
                        : idx === lineage.length - 1
                        ? "Selected"
                        : `Layer ${idx}`;

                    return (
                      <div key={layer.threadItemId} className="bl-lineage-item">
                        <div className="bl-lineage-left">
                          <div className="bl-lineage-label">{label}</div>

                          <div className="bl-mono">
                            {layer.instrument ?? "—"} • {(layer.durationMs / 1000).toFixed(2)}s
                          </div>

                          <div className="bl-lineage-meta">
                            gain {layer.gainDb.toFixed(1)}dB • pan {layer.pan.toFixed(2)}
                            {layer.muted ? " • muted" : ""}
                          </div>
                        </div>

                        <button className="bl-button bl-button-ghost" disabled>
                          Play
                        </button>
                      </div>
                    );
                  })}
                </div>
              )}
            </div>

            {activeAudioUrl ? (
              <div style={{ marginTop: 10, color: "rgba(255,255,255,0.45)", fontSize: 12 }}>
                source: <span className="bl-mono">{activeAudioUrl}</span>
              </div>
            ) : null}
          </div>

          <button className="bl-button bl-button-ghost" disabled title="Coming soon">
            Open Recording Page
          </button>
        </aside>
      </div>

      {/* Vinyl lives on the “desk” (bottom-right of the whole page), not in Studio */}
      <div className="bl-page-vinyl" aria-hidden="true">
        <img
          src={vinylImage}
          className={`bl-vinyl-image ${isPlaying ? "bl-vinyl-spinning" : ""}`}
          alt=""
        />
      </div>
    </div>
  );
}