# Security & Compliance Model - SafeRoute SDK

The SafeRoute SDK is built for high-security environments, meeting requirements for government and public sector deployment.

## 1. Data Encryption
- **Algorithm**: AES-256 in GCM (Galois/Counter Mode) for authenticated encryption.
- **Key Storage**: Hardware-backed Android Keystore (`AndroidKeyStore` provider).
- **Scope**: Location coordinates, phone numbers, and device identifiers are encrypted before storage or transmission.

## 2. Network Security
- **TLS Protocol**: Enforced TLS 1.3 for all backend communication.
- **Certificate Pinning**: (Optional configuration) for enhanced man-in-the-middle protection.
- **API Authentication**: Per-app API key + Per-device token validation on every request.

## 3. Privacy & Compliance
- **Data Minimization**: Location data is transmitted directly to the emergency gateway. No persistent local storage exists after the SOS session ends or successful sync.
- **Audit Logging**: Structured logs (`SafeRouteSDK_Audit`) sanitized of all PII using a regex-based scrubber.
- **Tamper Detection**: Built-in root detection and mock location prevention.
- **Process Protection**: Foreground service (START_STICKY) prevents the OS from killing the tracking process during critical periods.

## 4. Reliability (Offline-First)
- **WorkManager Sync**: Pending events are stored in an encrypted SQLite/Pref queue and synced using `WorkManager` with exponential backoff and network-connectivity constraints.
- **Process Resilience**: SDK survives device reboots and app crashes.
