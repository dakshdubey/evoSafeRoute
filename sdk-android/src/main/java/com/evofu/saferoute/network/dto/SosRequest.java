package com.evofu.saferoute.network.dto;

import com.google.gson.annotations.SerializedName;

/**
 * SOS Request DTO for initiating an emergency.
 */
public class SosRequest {
    @SerializedName("device_id")
    private final String deviceId;

    @SerializedName("timestamp")
    private final long timestamp;

    @SerializedName("latitude")
    private final double latitude;

    @SerializedName("longitude")
    private final double longitude;

    @SerializedName("emergency_type")
    private final String emergencyType;

    public SosRequest(String deviceId, double latitude, double longitude, String emergencyType) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emergencyType = emergencyType;
        this.timestamp = System.currentTimeMillis();
    }
}
