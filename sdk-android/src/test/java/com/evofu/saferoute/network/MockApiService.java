package com.evofu.saferoute.network;

import com.evofu.saferoute.network.dto.LocationUpdate;
import com.evofu.saferoute.network.dto.SosRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.mock.Calls;

/**
 * Mock implementation of ApiService for testing and rapid prototyping.
 */
public class MockApiService implements ApiService {
    @Override
    public Call<ResponseBody> startSos(String apiKey, String deviceToken, SosRequest request) {
        return Calls.response(ResponseBody.create(null, "{\"status\":\"success\", \"sos_id\":\"MOCK_123\"}"));
    }

    @Override
    public Call<ResponseBody> updateLocation(String apiKey, LocationUpdate update) {
        return Calls.response(ResponseBody.create(null, "{\"status\":\"received\"}"));
    }

    @Override
    public Call<ResponseBody> stopSos(String apiKey, String sosId) {
        return Calls.response(ResponseBody.create(null, "{\"status\":\"stopped\"}"));
    }
}
