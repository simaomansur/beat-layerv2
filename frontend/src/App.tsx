import React from "react";

export default function App() {
  const [status, setStatus] = React.useState<"checking" | "up" | "down">("checking");
  const [msg, setMsg] = React.useState<string>("");

  React.useEffect(() => {
    // thanks to Vite proxy, this hits http://localhost:8080/health
    fetch("/api/health")
      .then(async (r) => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        const json = await r.json();
        setStatus(json.status === "UP" ? "up" : "down");
        setMsg(JSON.stringify(json));
      })
      .catch((e) => {
        setStatus("down");
        setMsg(String(e));
      });
  }, []);

  return (
    <div style={{ fontFamily: "system-ui, sans-serif", padding: 24 }}>
      <h1>Beat Layer</h1>
      <p>API health: {status === "checking" ? "checking..." : status === "up" ? "✅ UP" : "❌ DOWN"}</p>
      <pre style={{ background: "#111", color: "#eee", padding: 12, borderRadius: 8 }}>{msg}</pre>
    </div>
  );
}
