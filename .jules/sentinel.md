## 2024-07-25 - Hardcoded Secrets in Configuration

**Vulnerability:** The application used hardcoded default credentials (e.g., `DB_PASSWORD=changeme`) in `Dockerfile`, `docker-compose.yml`, and `application-docker.properties`.

**Learning:** Default credentials, even for development, create a significant security risk. Developers may forget to change them in production, or they may be used in staging environments that are exposed to the internet. The `SECURITY.md` file explicitly forbade this practice, but it was not being followed.

**Prevention:** All secrets, including passwords and encryption keys, must be loaded from environment variables. Configuration files should not contain fallback values. The application should be designed to fail fast if a required secret is not provided. The `.env.example` file should be updated to remove default values and provide clear instructions for setting strong secrets.
