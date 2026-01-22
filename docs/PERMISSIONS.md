# Permission Checklist

| Permission | Purpose | Required For |
|------------|---------|--------------|
| `ACCESS_FINE_LOCATION` | Precise tracking during emergency | High Accuracy SOS |
| `ACCESS_BACKGROUND_LOCATION` | Tracking while app is in background | SOS Continuity |
| `INTERNET` | Sending alerts to backend gateway | SOS Alerts |
| `FOREGROUND_SERVICE` | Running SOS tracking service | Service Lifetime |
| `POST_NOTIFICATIONS` | High-priority SOS notification | User Visibility |
| `WAKE_LOCK` | Keeping the CPU awake during SOS | Tracking Reliability |

### Consent Requirements
- User must grant "Always Allow" for Location for background tracking.
- Host app must display a clear rationale before requesting permissions.
