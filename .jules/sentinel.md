## 2026-01-31 - Hardcoded Secrets in Docker and Config files
**Vulnerability:** Sensitive credentials (DB_USERNAME, DB_PASSWORD, ENCRYPTION_KEY) were hardcoded as default values in Dockerfile, docker-compose.yml, and application properties.
**Learning:** Hardcoded secrets in a Dockerfile's ENV instruction are baked into the image layers and can be extracted using `docker history` or `docker inspect`, even if overwritten at runtime.
**Prevention:** Always use runtime environment variables for secrets. Remove all default values for sensitive parameters from configuration files and force them to be provided via a secure mechanism like an `.env` file (not committed) or a secret manager.
