package com.evofu.saferoute.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Manages the Android Keystore for hardware-backed security.
 */
public class KeyStoreManager {
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String SOS_KEY_ALIAS = "SafeRouteSosKey";

    public static SecretKey getOrCreateSosKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (keyStore.containsAlias(SOS_KEY_ALIAS)) {
            return (SecretKey) keyStore.getKey(SOS_KEY_ALIAS, null);
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(
                SOS_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build());

        return keyGenerator.generateKey();
    }
}
