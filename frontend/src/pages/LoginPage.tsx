import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/auth.context";
import "../components/FormPages.css";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as any)?.from?.pathname || "/jams";  // fallback

  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!email.trim() || !password) {
      setError("Please enter your email and password");
      return;
    }

    try {
      await login(email.trim(), password);
      navigate(from, { replace: true });
    } catch (err: any) {
      setError(err.message || "Login failed");
    }
  };

  return (
    <div className="form-page">
      <h2>Login</h2>
      {error && <div className="error">{error}</div>}

      <form onSubmit={handleSubmit}>
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

        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default LoginPage;
