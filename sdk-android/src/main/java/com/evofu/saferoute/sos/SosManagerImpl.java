package com.evofu.saferoute.sos;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import com.evofu.saferoute.core.SafeRouteConfig;
import com.evofu.saferoute.location.FusedLocationProviderImpl;
import com.evofu.saferoute.network.ApiClient;
import com.evofu.saferoute.network.ApiService;
import com.evofu.saferoute.network.dto.SosRequest;
import com.evofu.saferoute.offline.EncryptedOfflineQueue;
import com.evofu.saferoute.security.SecurityGuard;
import com.evofu.saferoute.utils.AuditLogger;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of SOS Manager.
 * Orchestrates the trigger, network calls, and service lifecycle.
 */
public class SosManagerImpl {
    private final Context context;
    private final SafeRouteConfig config;
    private final EncryptedOfflineQueue offlineQueue;

    public SosManagerImpl(Context context, SafeRouteConfig config) {
        this.context = context;
        this.config = config;
        this.offlineQueue = new EncryptedOfflineQueue(context);
    }

    /**
     * Entry point for triggering an SOS.
     */
    public void triggerEmergency() {
        AuditLogger.logEvent("EmergencyTrigger", "INITIATED");

        // 1. Capture quick location and start service for continuous tracking
        startForegroundTracking();

        // 2. Prepare SOS request
        String deviceId = SecurityGuard.getDeviceBindingId(context);
        SosRequest request = new SosRequest(deviceId, 0.0, 0.0, "UNKNOWN"); // Initial placeholder, service will update

        // 3. Attempt network call
        ApiService api = ApiClient.getService(config);
        api.startSos(config.getApiKey(), config.getDeviceToken(), request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    AuditLogger.logEvent("SosStart", "SUCCESS");
                } else {
                    handleFailure(request);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleFailure(request);
            }
        });
    }

    private void startForegroundTracking() {
        Intent intent = new Intent(context, SosForegroundService.class);
        ContextCompat.startForegroundService(context, intent);
    }

    public void stopEmergency() {
        AuditLogger.logEvent("EmergencyStop", "INITIATED");
        context.stopService(new Intent(context, SosForegroundService.class));
    }

    private void handleFailure(SosRequest request) {
        AuditLogger.logEvent("SosStart", "FAILED", "Moving to offline queue");
        String json = new Gson().toJson(request);
        offlineQueue.enqueue(json);
        // WorkManager (RetryWorker) will handle the sync later
    }
}
