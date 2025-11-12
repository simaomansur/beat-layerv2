import React, { JSX } from "react";
import { useAuth } from "../auth/auth.context";
import { Navigate, useLocation } from "react-router-dom";

type Props = {
  children: JSX.Element;
};

const ProtectedRoute: React.FC<Props> = ({ children }) => {
  const { user } = useAuth();
  const location = useLocation();

  if (!user) {
    // redirect to login, but save where they were trying to go
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return children;
};

export default ProtectedRoute;
