package com.evofu.saferoute.security;

import android.util.Base64;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Utility class for AES-256-GCM encryption and decryption.
 * Production-ready implementation with IV management.
 */
public class CryptoUtils {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    public static String encrypt(String data) throws Exception {
        if (data == null)
            return null;
        SecretKey key = KeyStoreManager.getOrCreateSosKey();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        // Prefix IV to encrypted data for retrieval during decryption
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encryptedData.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);

        return Base64.encodeToString(byteBuffer.array(), Base64.NO_WRAP);
    }

    public static String decrypt(String encryptedDataWithIv) throws Exception {
        if (encryptedDataWithIv == null)
            return null;
        SecretKey key = KeyStoreManager.getOrCreateSosKey();
        byte[] decoded = Base64.decode(encryptedDataWithIv, Base64.NO_WRAP);

        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);

        byte[] encryptedData = new byte[byteBuffer.remaining()];
        byteBuffer.get(encryptedData);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        return new String(cipher.doFinal(encryptedData));
    }
}
