import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import "./index.css";
import { AuthProvider } from "./auth/AuthContext";

// Setup global analytics queue to prevent events from being dropped during cold load
if (typeof window !== "undefined") {
  (window as any).hiredai_analytics_queue = [];
  (window as any).hiredai_track = (eventName: string, props: any) => {
    (window as any).hiredai_analytics_queue.push({ eventName, props });
  };
}

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
