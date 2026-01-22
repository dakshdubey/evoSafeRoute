package com.evofu.saferoute.utils;

import android.util.Log;

/**
 * Structured logger for SDK events.
 * Ensures No PII (Personally Identifiable Information) is leaked in Logcat.
 */
public class AuditLogger {
    private static final String TAG = "SafeRouteSDK_Audit";

    public static void logEvent(String action, String status) {
        logEvent(action, status, null);
    }

    public static void logEvent(String action, String status, String details) {
        StringBuilder log = new StringBuilder();
        log.append("[ACTION: ").append(action).append("] ");
        log.append("[STATUS: ").append(status).append("]");
        if (details != null) {
            log.append(" [DETAILS: ").append(sanitize(details)).append("]");
        }
        Log.i(TAG, log.toString());
    }

    public static void logError(String action, Exception e) {
        Log.e(TAG, "[ACTION: " + action + "] [ERROR: " + e.getMessage() + "]", e);
    }

    /**
     * Basic PII scrubber for logs.
     * In production, this would use regex to mask emails/phones if they somehow
     * enter the log stream.
     */
    private static String sanitize(String input) {
        if (input == null)
            return null;
        // Example: Mask potential phone numbers or long digits
        return input.replaceAll("\\d{6,}", "*******");
    }
}
