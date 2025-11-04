import React from "react";
import { useParams } from "react-router-dom";

const ProfilePage = () => {
  const { username } = useParams();

  return (
    <div style={{ maxWidth: "800px", margin: "0 auto" }}>
      <h1 style={{ fontSize: "1.6rem", marginBottom: "0.75rem" }}>
        {username === "me" ? "My Profile" : `Profile: ${username}`}
      </h1>
      <p style={{ opacity: 0.8 }}>
        This will eventually show the user's jams, bio, and links.
      </p>
    </div>
  );
};

export default ProfilePage;
