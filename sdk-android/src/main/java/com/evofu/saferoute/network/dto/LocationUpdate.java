package com.evofu.saferoute.network.dto;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for continuous location updates during an active SOS.
 */
public class LocationUpdate {
    @SerializedName("sos_id")
    private final String sosId;

    @SerializedName("latitude")
    private final double latitude;

    @SerializedName("longitude")
    private final double longitude;

    @SerializedName("altitude")
    private final double altitude;

    @SerializedName("accuracy")
    private final float accuracy;

    @SerializedName("timestamp")
    private final long timestamp;

    public LocationUpdate(String sosId, double latitude, double longitude, double altitude, float accuracy) {
        this.sosId = sosId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.timestamp = System.currentTimeMillis();
    }
}
