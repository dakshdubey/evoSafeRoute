package com.evofu.saferoute.offline;

import android.content.Context;
import android.content.SharedPreferences;
import com.evofu.saferoute.security.CryptoUtils;
import java.util.HashSet;
import java.util.Set;

/**
 * Persists pending SOS events in an encrypted local queue.
 * Reliable storage for offline-first design.
 */
public class EncryptedOfflineQueue {
    private static final String PREF_NAME = "SafeRoute_OfflineQueue";
    private static final String KEY_PENDING_EVENTS = "pending_events";
    private final SharedPreferences prefs;

    public EncryptedOfflineQueue(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Encrypts and adds a serialized JSON string to the queue.
     */
    public synchronized void enqueue(String jsonData) {
        try {
            String encrypted = CryptoUtils.encrypt(jsonData);
            Set<String> events = prefs.getStringSet(KEY_PENDING_EVENTS, new HashSet<>());
            events.add(encrypted);
            prefs.edit().putStringSet(KEY_PENDING_EVENTS, events).apply();
        } catch (Exception e) {
            // Log error
        }
    }

    /**
     * Retrieves all events, decrypts them, and clears the queue.
     */
    public synchronized Set<String> dequeueAll() {
        Set<String> encryptedEvents = prefs.getStringSet(KEY_PENDING_EVENTS, new HashSet<>());
        Set<String> decryptedEvents = new HashSet<>();

        for (String encrypted : encryptedEvents) {
            try {
                decryptedEvents.add(CryptoUtils.decrypt(encrypted));
            } catch (Exception e) {
                // Skip corrupted items
            }
        }

        prefs.edit().remove(KEY_PENDING_EVENTS).apply();
        return decryptedEvents;
    }

    public boolean isEmpty() {
        return prefs.getStringSet(KEY_PENDING_EVENTS, new HashSet<>()).isEmpty();
    }
}
