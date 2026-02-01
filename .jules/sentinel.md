## 2026-02-01 - Recurring Stored XSS in Frontend
**Vulnerability:** Widespread Stored XSS across multiple dashboard pages (Devices, Users, RMCs) due to direct use of `.innerHTML` with unsanitized data from the server.
**Learning:** The application follows a pattern of building table rows as large HTML strings and injecting them into the DOM. This is particularly dangerous for a C2 server where data often originates from untrusted "infected" hosts.
**Prevention:** Always escape dynamic content before inserting it into HTML. The most robust approach in this codebase is to use jQuery's safe DOM construction methods (e.g., `.text()`) and event handlers instead of string-based `innerHTML` and `onclick` attributes.
