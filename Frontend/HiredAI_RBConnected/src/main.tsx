import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./index.css";
import { AuthProvider } from "./auth/AuthContext";

// Dynamically import analytics to prevent build errors if the file is missing
import("./utils/analytics").catch((err) => {
  console.warn("Analytics utility not loaded:", err);
});

createRoot(document.getElementById("root")!).render(
  <BrowserRouter>
    <AuthProvider>
      <App />
    </AuthProvider>
  </BrowserRouter>
);
