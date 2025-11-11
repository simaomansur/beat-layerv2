// src/pages/LayerStudioPage.tsx
import React, {
  useState,
  useEffect,
  useRef,
  FormEvent,
} from "react";
import { useNavigate } from "react-router-dom";
import { createJamWithBaseLayer } from "../api";
import "../components/LayerStudioPage.css";

const MIN_BPM = 40;
const MAX_BPM = 260;
const DEFAULT_LOOP_BARS = 4;

const LayerStudioPage: React.FC = () => {
  const navigate = useNavigate();

  // Jam metadata (only editable here, before publish)
  const [title, setTitle] = useState("");
  const [key, setKey] = useState("");
  const [bpm, setBpm] = useState<number | "">("");
  const [genre, setGenre] = useState("");
  const [loopBars, setLoopBars] = useState<number>(DEFAULT_LOOP_BARS);

  // Audio device + recording state
  const [inputDevices, setInputDevices] = useState<MediaDeviceInfo[]>([]);
  const [selectedDeviceId, setSelectedDeviceId] = useState<string>("default");
  const [isRecording, setIsRecording] = useState(false);
  const [hasRecording, setHasRecording] = useState(false);
  const [audioBlob, setAudioBlob] = useState<Blob | null>(null);
  const [audioUrl, setAudioUrl] = useState<string | null>(null);
  const [audioError, setAudioError] = useState<string | null>(null);

  const mediaStreamRef = useRef<MediaStream | null>(null);
  const recorderRef = useRef<MediaRecorder | null>(null);
  const chunksRef = useRef<BlobPart[]>([]);

  // Publishing state
  const [isPublishing, setIsPublishing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Ask for audio permission + enumerate input devices on mount
  useEffect(() => {
    const setupDevices = async () => {
      try {
        setAudioError(null);

        // Request permission so enumerateDevices returns full info
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        mediaStreamRef.current = stream;

        const devices = await navigator.mediaDevices.enumerateDevices();
        const audioInputs = devices.filter(
          (d) => d.kind === "audioinput"
        ) as MediaDeviceInfo[];

        setInputDevices(audioInputs);

        // If there is a default device, keep "default" selected;
        // otherwise fall back to first device id.
        if (audioInputs.length > 0 && selectedDeviceId === "default") {
          // no-op is fine; user can pick manually
        }
      } catch (err) {
        console.error(err);
        setAudioError(
          "Could not access your microphone. Check browser permissions and try again."
        );
      }
    };

    setupDevices();

    // Cleanup on unmount: stop any open stream
    return () => {
      if (mediaStreamRef.current) {
        mediaStreamRef.current.getTracks().forEach((t) => t.stop());
      }
      if (audioUrl) {
        URL.revokeObjectURL(audioUrl);
      }
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleStartRecording = async () => {
    setError(null);
    setAudioError(null);

    try {
      // Stop previous stream if any
      if (mediaStreamRef.current) {
        mediaStreamRef.current.getTracks().forEach((t) => t.stop());
        mediaStreamRef.current = null;
      }

      const constraints: MediaStreamConstraints = {
        audio:
          selectedDeviceId === "default"
            ? true
            : {
                deviceId: { exact: selectedDeviceId },
              },
      };

      const stream = await navigator.mediaDevices.getUserMedia(constraints);
      mediaStreamRef.current = stream;

      const recorder = new MediaRecorder(stream);
      recorderRef.current = recorder;
      chunksRef.current = [];
      setIsRecording(true);
      setHasRecording(false);
      setAudioBlob(null);

      recorder.ondataavailable = (e: BlobEvent) => {
        if (e.data && e.data.size > 0) {
          chunksRef.current.push(e.data);
        }
      };

      recorder.onstop = () => {
        const blob = new Blob(chunksRef.current, { type: "audio/webm" });
        setAudioBlob(blob);
        setHasRecording(true);

        // Revoke previous URL if exists
        if (audioUrl) {
          URL.revokeObjectURL(audioUrl);
        }

        const url = URL.createObjectURL(blob);
        setAudioUrl(url);

        // Stop tracks when done
        if (mediaStreamRef.current) {
          mediaStreamRef.current.getTracks().forEach((t) => t.stop());
          mediaStreamRef.current = null;
        }
      };

      recorder.start();
    } catch (err) {
      console.error(err);
      setAudioError(
        "Failed to start recording. Check your input device and permissions."
      );
      setIsRecording(false);
    }
  };

  const handleStopRecording = () => {
    if (recorderRef.current && recorderRef.current.state === "recording") {
      recorderRef.current.stop();
    }
    setIsRecording(false);
  };

  const handleDiscardTake = () => {
    setHasRecording(false);
    setAudioBlob(null);
    if (audioUrl) {
      URL.revokeObjectURL(audioUrl);
      setAudioUrl(null);
    }
  };

  const handleCancel = () => {
    navigate("/jams");
  };

  const handlePublish = async (e: FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!canPublish || !audioBlob) return;

    try {
        setIsPublishing(true);

        const numericBpm = typeof bpm === "number" ? bpm : Number(bpm);

        await createJamWithBaseLayer({
        title: title.trim(),
        key: key.trim() || null,
        bpm: numericBpm,
        genre: genre.trim() || null,
        loopBars,
        audioBlob,
        });

        // Once jam + base layer are created, go back to jams list
        navigate("/jams");
    } catch (err) {
        console.error(err);
        setError("Something went wrong while publishing the jam.");
    } finally {
        setIsPublishing(false);
    }
  };

  const numericBpm = typeof bpm === "number" ? bpm : Number(bpm);
  const bpmIsValid =
    numericBpm >= MIN_BPM && numericBpm <= MAX_BPM && !Number.isNaN(numericBpm);

  const canPublish =
    title.trim().length > 0 && bpmIsValid && !!audioBlob && !isPublishing;

  return (
    <div className="studio-container">
      <header className="studio-header">
        <h1>Create New Jam</h1>
        <p className="studio-subtitle">
          Set up your jam and record the first layer here. Once you publish,
          the jam cannot be edited.
        </p>
      </header>

      <form className="studio-layout" onSubmit={handlePublish}>
        {/* Left side: jam settings */}
        <section className="studio-panel studio-settings">
          <h2>Jam Settings</h2>

          <label className="studio-field">
            <span>Title</span>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Groovy Loop in A minor"
              required
            />
          </label>

          <label className="studio-field">
            <span>Key</span>
            <input
              type="text"
              value={key}
              onChange={(e) => setKey(e.target.value)}
              placeholder="A minor"
            />
          </label>

          <label className="studio-field">
            <span>BPM</span>
            <input
              type="number"
              value={bpm}
              onChange={(e) =>
                setBpm(e.target.value === "" ? "" : Number(e.target.value))
              }
              placeholder="120"
              min={MIN_BPM}
              max={MAX_BPM}
            />
            {!bpmIsValid && bpm !== "" && (
              <small className="studio-error-text">
                BPM should be between {MIN_BPM} and {MAX_BPM}.
              </small>
            )}
          </label>

          <label className="studio-field">
            <span>Genre (optional)</span>
            <input
              type="text"
              value={genre}
              onChange={(e) => setGenre(e.target.value)}
              placeholder="Funk, lo-fi, rock..."
            />
          </label>

          <label className="studio-field">
            <span>Loop length (bars)</span>
            <select
              value={loopBars}
              onChange={(e) => setLoopBars(Number(e.target.value))}
            >
              <option value={2}>2 bars</option>
              <option value={4}>4 bars</option>
              <option value={8}>8 bars</option>
              <option value={12}>12 bars</option>
              <option value={16}>16 bars</option>
            </select>
          </label>

          <div className="studio-note">
            These settings are only editable here. After you publish the jam,
            they are locked in.
          </div>
        </section>

        {/* Right side: recording area */}
        <section className="studio-panel studio-recording">
          <h2>Base Layer Recording</h2>

          {/* Input device selector */}
          <div className="studio-field">
            <span>Input device</span>
            <select
              value={selectedDeviceId}
              onChange={(e) => setSelectedDeviceId(e.target.value)}
              disabled={isRecording}
            >
              <option value="default">System default</option>
              {inputDevices.map((d) => (
                <option key={d.deviceId} value={d.deviceId}>
                  {d.label || `Input ${d.deviceId}`}
                </option>
              ))}
            </select>
            <small className="studio-help-text">
              Choose your mic, guitar interface, or keyboard input.
            </small>
          </div>

          {audioError && (
            <div className="studio-error-banner">{audioError}</div>
          )}

          {/* Waveform / status placeholder */}
          <div className="studio-waveform-placeholder">
            {audioBlob ? (
              <p>Recorded take ready. You&apos;ll see the waveform here later.</p>
            ) : isRecording ? (
              <p>Recording in progress... play your part.</p>
            ) : (
              <p>
                No recording yet. Click Record to capture your base layer.
              </p>
            )}
          </div>

          {/* Simple audio preview */}
          {audioUrl && (
            <div className="studio-audio-preview">
              <span>Preview</span>
              <audio controls src={audioUrl} />
            </div>
          )}

          <div className="studio-transport">
            {!isRecording ? (
              <button
                type="button"
                onClick={handleStartRecording}
                className="studio-button primary"
              >
                Record
              </button>
            ) : (
              <button
                type="button"
                onClick={handleStopRecording}
                className="studio-button danger"
              >
                Stop
              </button>
            )}

            <button
              type="button"
              onClick={handleDiscardTake}
              className="studio-button"
              disabled={!audioBlob || isRecording}
            >
              Discard Take
            </button>
          </div>

          <div className="studio-publish-row">
            <button
              type="button"
              onClick={handleCancel}
              className="studio-button secondary"
              disabled={isPublishing}
            >
              Cancel
            </button>

            <button
              type="submit"
              className="studio-button primary"
              disabled={!canPublish}
            >
              {isPublishing ? "Publishing..." : "Publish Jam"}
            </button>
          </div>

          {!audioBlob && (
            <small className="studio-help-text">
              You need at least one recorded take to publish this jam.
            </small>
          )}

          {error && <div className="studio-error-banner">{error}</div>}
        </section>
      </form>
    </div>
  );
};

export default LayerStudioPage;
