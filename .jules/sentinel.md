## 2024-05-22 - Stored XSS in Dashboard Tables
**Vulnerability:** User-controlled data from infected devices (name, IP, etc.) was directly concatenated into HTML strings and inserted into the DOM using `.innerHTML` in the dashboard pages (`devices.js`, `users.js`, `rmcs.js`).
**Learning:** This is a classic Stored XSS vulnerability. In a C2 server, this is particularly dangerous as it allows a compromised or malicious "infected" device to execute scripts in the admin's browser when they view the dashboard.
**Prevention:** Use jQuery's safe DOM construction methods like `.text()` for all variable content. For complex elements like popovers, escape variable content by setting it as text in a temporary element before extracting the HTML.
