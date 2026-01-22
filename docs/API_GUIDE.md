# SafeRoute SDK API Guide

## 1. Initialization
The SDK must be initialized in your `Application` class.

```java
SafeRouteConfig config = new SafeRouteConfig.Builder()
    .setApiKey("YOUR_GOVERNMENT_ISSUED_KEY")
    .setDeviceToken("USER_SPECIFIC_TOKEN")
    .setDebugMode(false)
    .build();

SafeRouteSDK.init(context, config);
```

## 2. Triggering Emergency
One simple call to start the SOS flow. This will:
- Start the `SosForegroundService`
- Trigger a high-accuracy location tracking session
- Notify the backend servers
- Start an encrypted offline queue for data sync if the network fails

```java
SafeRouteSDK.triggerEmergency();
```

## 3. Stopping Emergency
Call this once the user is safe or after verification.

```java
SafeRouteSDK.stopEmergency();
```

## 4. Permissions Required
Ensure the following permissions are declared and requested:
- `ACCESS_FINE_LOCATION`
- `ACCESS_COARSE_LOCATION`
- `ACCESS_BACKGROUND_LOCATION` (Required for tracking while app is closed)
- `POST_NOTIFICATIONS` (For Android 13+)
- `INTERNET`
- `FOREGROUND_SERVICE`
- `FOREGROUND_SERVICE_LOCATION`

## Standalone Mode (Data Collection without API Key)

SafeRoute SDK supports a standalone mode where it collects emergency data locally without requiring a network or an initial API key.

### 1. Collect Data Locally
```java
// Collect SOS event
SafeRouteSDK.collectEmergencyLocally("THEFT");

// Location tracking in standalone mode should be handled by your activity 
// using the StandaloneCollector helper or by initializing tracking normally.
```

### 2. Flush to Government REST API
When the app is ready to sync, or when network returns, push the buffered data.

```java
SafeRouteSDK.flushToGovApi();
```
