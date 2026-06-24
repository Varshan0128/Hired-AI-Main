const API_BASE = (import.meta.env.VITE_HEIREDAI_API_URL || "http://localhost:8080").replace(/\/$/, "");

/**
 * Generates a UUID-like identifier.
 *
 * @returns A pseudo-random UUID v4-style string.
 */
function generateUUID(): string {
  // Simple UUID v4 generator in vanilla JS/TS
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0;
    const v = c === "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

/**
 * Gets the persistent anonymous analytics identifier for the browser.
 *
 * @returns The stored anonymous ID, or a newly generated ID if none exists.
 */
function getAnonymousId(): string {
  let anonymousId = localStorage.getItem("hiredai_analytics_anonymous_id");
  if (!anonymousId) {
    anonymousId = generateUUID();
    localStorage.setItem("hiredai_analytics_anonymous_id", anonymousId);
  }
  return anonymousId;
}

/**
 * Retrieves the current analytics session identifier.
 *
 * @returns The stored session ID, or a newly generated one after it is saved.
 */
function getSessionId(): string {
  let sessionId = sessionStorage.getItem("hiredai_analytics_session_id");
  if (!sessionId) {
    sessionId = generateUUID();
    sessionStorage.setItem("hiredai_analytics_session_id", sessionId);
  }
  return sessionId;
}

let cachedDeviceType = "desktop";

if (typeof window !== "undefined") {
  const width = window.innerWidth;
  if (width >= 1024) {
    cachedDeviceType = "desktop";
  } else if (width >= 768) {
    cachedDeviceType = "tablet";
  } else {
    cachedDeviceType = "mobile";
  }
}

/**
 * Gets the cached device type.
 *
 * @returns The precomputed device type.
 */
function getDeviceType(): string {
  return cachedDeviceType;
}

/**
 * Tracks an analytics event and sends it to the analytics API.
 *
 * For `signup_succeeded`, it also derives `email_domain` and `acquisition_source` from the current page, form field, or URL query parameters when available.
 *
 * @param eventName - The analytics event name.
 * @param props - Additional event properties.
 */
export function track(eventName: string, props: Record<string, any> = {}) {
  try {
    const eventId = generateUUID();
    const occurredAt = new Date().toISOString();
    const anonymousId = getAnonymousId();
    const sessionId = getSessionId();
    const deviceType = getDeviceType();
    
    // Resolve user ID from global window variable safely
    const userId = typeof window !== "undefined" ? (window as any).hiredai_user_id || null : null;
    const pagePath = window.location.pathname;
    const pageName = document.title;

    // Handle properties serialization / extraction
    let propertiesObj: Record<string, any> = {};
    if (props) {
      if (typeof props === "string") {
        try {
          propertiesObj = JSON.parse(props);
        } catch {
          propertiesObj = { value: props };
        }
      } else {
        propertiesObj = JSON.parse(JSON.stringify(props));
      }
    }

    // Intercept signup_succeeded dynamic extraction of properties if called empty
    if (eventName === "signup_succeeded") {
      let email = propertiesObj.email || "";
      if (!email) {
        const emailInput = document.querySelector('input[name="email"]') as HTMLInputElement;
        if (emailInput) {
          email = emailInput.value;
        }
      }
      if (!email) {
        const urlParams = new URLSearchParams(window.location.search);
        email = urlParams.get("email") || "";
      }
      
      const email_domain = email.includes("@") ? email.split("@")[1] : "";
      const urlParams = new URLSearchParams(window.location.search);
      const acquisition_source = urlParams.get("utm_source") || "direct";

      propertiesObj = {
        ...propertiesObj,
        email_domain,
        acquisition_source
      };
    }

    const payload = {
      event_id: eventId,
      occurred_at: occurredAt,
      user_id: userId ? String(userId) : null,
      anonymous_id: anonymousId,
      session_id: sessionId,
      event_name: eventName,
      page_path: pagePath,
      page_name: pageName,
      properties: propertiesObj,
      context: {
        device_type: deviceType,
        user_agent: navigator.userAgent,
        screen_width: window.screen.width,
        screen_height: window.screen.height,
      },
    };

    fetch(`${API_BASE}/api/analytics/events`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    }).catch((err) => {
      // Fail silently, log to console for debugging
      console.warn("Analytics tracking background send failed:", err);
    });
  } catch (err) {
    // Fail silently, never throw or block UI
    console.error("Analytics track error:", err);
  }
}

// Expose track globally so components can access it without importing
if (typeof window !== "undefined") {
  (window as any).hiredai_track = track;
}
