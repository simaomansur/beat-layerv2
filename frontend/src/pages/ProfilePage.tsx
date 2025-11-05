// src/pages/ProfilePage.tsx
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchCurrentUser } from "../api";
import "../components/ProfilePage.css";

type CurrentUser = {
  id: string;
  handle: string;
  email: string;
  createdAt?: string | null;
};

const ProfilePage: React.FC = () => {
  const { username } = useParams(); // /profile/:username
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const isMe = !username || username === "me";

  useEffect(() => {
    if (!isMe) {
      // For now, only /profile/me uses the backend.
      setLoading(false);
      return;
    }

    const load = async () => {
      try {
        const data = await fetchCurrentUser();
        setUser(data);
      } catch (err) {
        console.error(err);
        setError("Failed to load your profile.");
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [isMe]);

  return (
    <div className="profile-page">
      <section className="profile-header">
        <h1>{isMe ? "My profile" : `Profile: ${username}`}</h1>
        <p>
          Manage your Beat Layer identity and keep track of ideas tied to your account.
        </p>
      </section>

      <section className="profile-layout">
        {/* LEFT: main profile card */}
        <div className="profile-card">
          <div className="profile-avatar">
            <span>
              {isMe
                ? user?.handle?.charAt(0)?.toUpperCase() ?? "U"
                : username?.charAt(0)?.toUpperCase() ?? "U"}
            </span>
          </div>

          <div className="profile-info">
            {loading && <p className="profile-meta">Loading profile…</p>}

            {error && <p className="profile-meta profile-meta--error">{error}</p>}

            {!loading && !error && (
              <>
                {isMe && user ? (
                  <>
                    <h2>@{user.handle}</h2>
                    <p className="profile-email">{user.email}</p>
                    {user.createdAt && (
                      <p className="profile-meta">
                        Joined Beat Layer on{" "}
                        {new Date(user.createdAt).toLocaleDateString()}
                      </p>
                    )}
                  </>
                ) : (
                  <>
                    <h2>@{username}</h2>
                    <p className="profile-meta">
                      Public profile view. Detailed data for other users will come later.
                    </p>
                  </>
                )}
              </>
            )}
          </div>

          <div className="profile-stats">
            <div className="profile-stat">
              <span className="profile-stat-label">Jams</span>
              <span className="profile-stat-value">—</span>
            </div>
            <div className="profile-stat">
              <span className="profile-stat-label">Layers</span>
              <span className="profile-stat-value">—</span>
            </div>
            <div className="profile-stat">
              <span className="profile-stat-label">Favorites</span>
              <span className="profile-stat-value">—</span>
            </div>
          </div>
        </div>

        {/* RIGHT: activity / coming soon */}
        <div className="profile-side">
          <div className="profile-panel">
            <h3>My jams</h3>
            <p className="profile-meta">
              This is where jams linked to your account will show up once the user–jam
              connection is wired in on the backend.
            </p>
            <ul className="profile-list">
              <li>Filter jams by BPM, genre, or mood.</li>
              <li>Pin your favorite ideas to revisit later.</li>
              <li>See which ideas you&apos;ve layered into full tracks.</li>
            </ul>
          </div>

          <div className="profile-panel profile-panel--subtle">
            <h3>Next steps for this page</h3>
            <p className="profile-meta">
              Later you can:
            </p>
            <ul className="profile-list">
              <li>Show a grid of the user&apos;s jams.</li>
              <li>Add profile customization (bio, avatar, links).</li>
              <li>Expose stats like total jams, average BPM, and favorite keys.</li>
            </ul>
          </div>
        </div>
      </section>
    </div>
  );
};

export default ProfilePage;
