# SafeRoute SDK

**SafeRoute SDK** is a production-ready, government-grade Android SDK designed for women's safety and emergency response. It provides high-reliability SOS triggers, adaptive location tracking, and hardware-backed security.

## Features
- **One-line SOS Trigger**: `SafeRouteSDK.triggerEmergency()`
- **Hardware-backed Security**: AES-256 encryption via Android Keystore.
- **Standalone Mode**: Collect data locally without an initial API key.
- **Reliable Sync**: WorkManager integration with exponential backoff.
- **Adaptive Accuracy**: High-accuracy GPS during SOS, power-save mode otherwise.
- **Tamper Detection**: Root detection and mock location prevention.

## Installation
Add the following dependency to your `build.gradle`:

```gradle
implementation 'com.evofu:saferoute:1.0.0'
```

## Quick Start
Initialize the SDK in your `Application` class:

```java
SafeRouteConfig config = new SafeRouteConfig.Builder()
    .setApiKey("YOUR_API_KEY")
    .setDeviceToken("DEVICE_TOKEN")
    .build();

SafeRouteSDK.init(this, config);
```

To trigger an emergency:
```java
SafeRouteSDK.triggerEmergency();
```

## Documentation
- [API Reference](docs/API_GUIDE.md)
- [Security Model](docs/SECURITY_MODEL.md)
- [Permissions Guide](docs/PERMISSIONS.md)

## License
Apache License 2.0
