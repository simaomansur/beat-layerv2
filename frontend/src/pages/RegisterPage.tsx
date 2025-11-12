import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/auth.context";
import "../components/FormPages.css";

const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const { register } = useAuth();

  const [handle, setHandle] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!handle.trim() || !email.trim() || !password) {
      setError("Please fill in all fields");
      return;
    }

    try {
      await register(handle.trim(), email.trim(), password);
      navigate("/jams");  // or wherever you want after register
    } catch (err: any) {
      setError(err.message || "Registration failed");
    }
  };

  return (
    <div className="form-page">
      <h2>Register</h2>
      {error && <div className="error">{error}</div>}

      <form onSubmit={handleSubmit}>
        <label>
          Handle
          <input
            type="text"
            value={handle}
            onChange={(e) => setHandle(e.target.value)}
            placeholder="Your handle"
            required
          />
        </label>

        <label>
          Email
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="you@example.com"
            required
          />
        </label>

        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            required
          />
        </label>

        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default RegisterPage;
