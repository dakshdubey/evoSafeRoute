package com.evofu.saferoute.network;

import com.evofu.saferoute.network.dto.LocationUpdate;
import com.evofu.saferoute.network.dto.SosRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Retrofit interface for SafeRoute Backend.
 */
public interface ApiService {

    @POST("sos/start")
    Call<ResponseBody> startSos(
            @Header("X-Api-Key") String apiKey,
            @Header("X-Device-Token") String deviceToken,
            @Body SosRequest request);

    @POST("sos/update-location")
    Call<ResponseBody> updateLocation(
            @Header("X-Api-Key") String apiKey,
            @Body LocationUpdate update);

    @POST("sos/stop")
    Call<ResponseBody> stopSos(
            @Header("X-Api-Key") String apiKey,
            @Header("X-SOS-ID") String sosId);
}
