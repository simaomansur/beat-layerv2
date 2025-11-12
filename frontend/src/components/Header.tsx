import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../auth/auth.context";

const Header: React.FC = () => {
  const { user, logout } = useAuth();

  return (
    <header className="app-header">
      <div className="app-logo">Beat Layer</div>
      <nav className="app-nav">
        <Link className="app-nav-link" to="/">
          Home
        </Link>
        <Link className="app-nav-link" to="/jams">
          Jams
        </Link>

        {user ? (
          <>
            <Link className="app-nav-link" to={`/profile/${user.handle}`}>
              {user.handle}
            </Link>
            <button
              className="app-nav-link"
              onClick={() => {
                logout();
              }}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <Link className="app-nav-link" to="/login">
              Login
            </Link>
            <Link className="app-nav-link" to="/register">
              Register
            </Link>
          </>
        )}

        <Link className="app-nav-link" to="/about">
          About
        </Link>
      </nav>
    </header>
  );
};

export default Header;
