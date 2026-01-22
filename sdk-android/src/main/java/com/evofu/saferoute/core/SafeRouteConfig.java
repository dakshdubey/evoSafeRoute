package com.evofu.saferoute.core;

import androidx.annotation.NonNull;

/**
 * Configuration object for SafeRoute SDK.
 * Use the Builder to instantiate.
 */
public class SafeRouteConfig {
    private final String apiKey;
    private final String baseUrl;
    private final String deviceToken;
    private final boolean debugMode;

    private SafeRouteConfig(Builder builder) {
        this.apiKey = builder.apiKey;
        this.baseUrl = builder.baseUrl;
        this.deviceToken = builder.deviceToken;
        this.debugMode = builder.debugMode;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public static class Builder {
        private String apiKey;
        private String baseUrl = "https://api.saferoute.evofu.com/v1/";
        private String deviceToken;
        private boolean debugMode = false;

        public Builder setApiKey(@NonNull String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder setBaseUrl(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setDeviceToken(@NonNull String deviceToken) {
            this.deviceToken = deviceToken;
            return this;
        }

        public Builder setDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }

        public SafeRouteConfig build() {
            // API Key is optional for Standalone Collection Mode
            return new SafeRouteConfig(this);
        }
    }
}
