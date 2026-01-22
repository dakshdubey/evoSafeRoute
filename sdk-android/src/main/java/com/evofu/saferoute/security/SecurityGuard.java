package com.evofu.saferoute.security;

import android.content.Context;
import android.provider.Settings;
import java.io.File;

/**
 * Handles device binding and basic environment integrity checks.
 */
public class SecurityGuard {

    /**
     * Generates a unique device bind token using hardware identifier.
     * Note: In a real govt-grade deployment, this should be combined with
     * SafetyNet/Play Integrity.
     */
    public static String getDeviceBindingId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Basic root detection check.
     * Checks for common binaries used in rooted devices.
     */
    public static boolean isDeviceRooted() {
        String[] paths = {
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
        };
        for (String path : paths) {
            if (new File(path).exists())
                return true;
        }
        return false;
    }
}
