package com.evofu.saferoute.offline;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.evofu.saferoute.core.SafeRouteConfig;
import com.evofu.saferoute.core.SafeRouteSDK;
import com.evofu.saferoute.network.ApiClient;
import com.evofu.saferoute.network.ApiService;
import com.evofu.saferoute.network.dto.SosRequest;
import com.evofu.saferoute.utils.AuditLogger;
import com.google.gson.Gson;
import java.util.Set;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Background worker to sync offline SOS events when network is restored.
 */
public class RetryWorker extends Worker {
    public RetryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * Manually triggers the worker to sync offline data.
     */
    public static void enqueueOneTimeWork(Context context) {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(RetryWorker.class).build();
        WorkManager.getInstance(context).enqueue(request);
    }

    @NonNull
    @Override
    public Result doWork() {
        EncryptedOfflineQueue queue = new EncryptedOfflineQueue(getApplicationContext());
        if (queue.isEmpty())
            return Result.success();

        SafeRouteConfig config = SafeRouteSDK.getConfig();
        if (config == null)
            return Result.retry();

        ApiService api = ApiClient.getService(config);
        Set<String> events = queue.dequeueAll();
        Gson gson = new Gson();

        boolean allResolved = true;
        for (String json : events) {
            try {
                SosRequest request = gson.fromJson(json, SosRequest.class);
                Response<ResponseBody> response = api.startSos(config.getApiKey(), config.getDeviceToken(), request)
                        .execute();

                if (!response.isSuccessful()) {
                    // Re-enqueue if it's a transient server error
                    if (response.code() >= 500) {
                        queue.enqueue(json);
                        allResolved = false;
                    }
                }
            } catch (Exception e) {
                AuditLogger.logError("RetryWorker_Sync", e);
                queue.enqueue(json);
                allResolved = false;
            }
        }

        return allResolved ? Result.success() : Result.retry();
    }
}
