package com.evofu.saferoute.core;

import android.content.Context;
import android.location.Location;
import com.evofu.saferoute.network.dto.LocationUpdate;
import com.evofu.saferoute.network.dto.SosRequest;
import com.evofu.saferoute.offline.EncryptedOfflineQueue;
import com.evofu.saferoute.security.SecurityGuard;
import com.evofu.saferoute.utils.AuditLogger;
import com.google.gson.Gson;

/**
 * Handles standalone data collection without requiring an active network or API
 * key.
 * Data is buffered in the EncryptedOfflineQueue.
 */
public class StandaloneCollector {
    private final EncryptedOfflineQueue offlineQueue;
    private final Context context;
    private final Gson gson;

    public StandaloneCollector(Context context) {
        this.context = context.getApplicationContext();
        this.offlineQueue = new EncryptedOfflineQueue(this.context);
        this.gson = new Gson();
    }

    /**
     * Records an SOS event locally.
     */
    public void collectSosEvent(String emergencyType) {
        String deviceId = SecurityGuard.getDeviceBindingId(context);
        SosRequest request = new SosRequest(deviceId, 0.0, 0.0, emergencyType);
        String json = gson.toJson(request);
        offlineQueue.enqueue(json);
        AuditLogger.logEvent("StandaloneCollect", "SOS_QUEUED");
    }

    /**
     * Records a location update locally.
     */
    public void collectLocation(Location location) {
        LocationUpdate update = new LocationUpdate(
                "STANDALONE_SESSION",
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracy());
        String json = gson.toJson(update);
        offlineQueue.enqueue(json);
        AuditLogger.logEvent("StandaloneCollect", "LOCATION_QUEUED",
                location.getLatitude() + "," + location.getLongitude());
    }
}
