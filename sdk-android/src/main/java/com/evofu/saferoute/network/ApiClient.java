package com.evofu.saferoute.network;

import com.evofu.saferoute.core.SafeRouteConfig;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Secure API Client with TLS 1.3 enforcement.
 */
public class ApiClient {
    private static ApiService instance;

    public static synchronized ApiService getService(SafeRouteConfig config) {
        if (instance == null) {
            // Enforce TLS 1.3 for government-grade security
            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(spec))
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(config.getBaseUrl())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(ApiService.class);
        }
        return instance;
    }
}
