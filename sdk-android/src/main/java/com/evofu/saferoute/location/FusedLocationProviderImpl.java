package com.evofu.saferoute.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import com.evofu.saferoute.utils.AuditLogger;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

/**
 * Modern wrapper for Fused Location Provider.
 * Features adaptive accuracy: High during SOS, Low during standby.
 */
public class FusedLocationProviderImpl {
    private final FusedLocationProviderClient client;
    private final LocationUpdateListener listener;

    public interface LocationUpdateListener {
        void onLocationUpdate(Location location);
    }

    public FusedLocationProviderImpl(Context context, LocationUpdateListener listener) {
        this.client = LocationServices.getFusedLocationProviderClient(context);
        this.listener = listener;
    }

    private final LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null)
                return;
            for (Location location : locationResult.getLocations()) {
                if (location.isFromMockProvider()) {
                    AuditLogger.logEvent("LocationUpdate", "TAMPER_DETECTED", "Mock location ignored");
                    continue;
                }
                listener.onLocationUpdate(location);
            }
        }
    };

    /**
     * Starts location updates with high priority for SOS.
     */
    @SuppressLint("MissingPermission")
    public void startSosTracking() {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .setWaitForAccurateLocation(true)
                .build();

        client.requestLocationUpdates(request, callback, Looper.getMainLooper());
        AuditLogger.logEvent("LocationTracking", "STARTED", "High Accuracy Mode");
    }

    /**
     * Standard tracking for low-battery standby.
     */
    @SuppressLint("MissingPermission")
    public void startStandbyTracking() {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 60000)
                .setMinUpdateIntervalMillis(30000)
                .build();

        client.requestLocationUpdates(request, callback, Looper.getMainLooper());
        AuditLogger.logEvent("LocationTracking", "STARTED", "Balanced Power Mode");
    }

    public void stopTracking() {
        client.removeLocationUpdates(callback);
        AuditLogger.logEvent("LocationTracking", "STOPPED");
    }
}
