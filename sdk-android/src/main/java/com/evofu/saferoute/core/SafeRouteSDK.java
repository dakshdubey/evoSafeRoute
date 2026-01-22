package com.evofu.saferoute.core;

import android.content.Context;
import androidx.annotation.NonNull;
import com.evofu.saferoute.sos.SosManagerImpl;
import com.evofu.saferoute.utils.AuditLogger;

/**
 * Main entry point for the SafeRoute SDK.
 * Production-ready singleton implementation.
 */
public class SafeRouteSDK {
    private static SafeRouteSDK instance;
    private final Context context;
    private final SafeRouteConfig config;
    private final SosManagerImpl sosManager;
    private final StandaloneCollector standaloneCollector;

    private SafeRouteSDK(Context context, SafeRouteConfig config) {
        this.context = context.getApplicationContext();
        this.config = config;
        this.sosManager = new SosManagerImpl(this.context, config);
        this.standaloneCollector = new StandaloneCollector(this.context);
        AuditLogger.logEvent("SDK_INIT", "SUCCESS");
    }

    /**
     * Initializes the SDK with a configuration.
     */
    public static synchronized void init(@NonNull Context context, @NonNull SafeRouteConfig config) {
        if (instance == null) {
            instance = new SafeRouteSDK(context, config);
        }
    }

    public static SafeRouteSDK getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SafeRouteSDK must be initialized first. call SafeRouteSDK.init()");
        }
        return instance;
    }

    public static SafeRouteConfig getConfig() {
        return getInstance().config;
    }

    /**
     * Triggers a high-priority emergency SOS.
     * One-line API as requested.
     */
    public static void triggerEmergency() {
        getInstance().sosManager.triggerEmergency();
    }

    /**
     * Stops the active emergency session.
     */
    public static void stopEmergency() {
        getInstance().sosManager.stopEmergency();
    }

    /**
     * STANDALONE MODE: Collects emergency data locally without API key.
     */
    public static void collectEmergencyLocally(String emergencyType) {
        getInstance().standaloneCollector.collectSosEvent(emergencyType);
    }

    /**
     * STANDALONE MODE: Pushes all locally collected data to the Government REST
     * API.
     * Use this when network is available or app is ready to sync.
     */
    public static void flushToGovApi() {
        AuditLogger.logEvent("StandaloneFlush", "INITIATED");
        // Reuse the RetryWorker logic via WorkManager to handle the background sync
        com.evofu.saferoute.offline.RetryWorker.enqueueOneTimeWork(getInstance().context);
    }
}
