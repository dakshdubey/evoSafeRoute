package com.evofu.saferoute.sos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.evofu.saferoute.core.SafeRouteSDK;
import com.evofu.saferoute.location.FusedLocationProviderImpl;
import com.evofu.saferoute.network.ApiClient;
import com.evofu.saferoute.network.ApiService;
import com.evofu.saferoute.network.dto.LocationUpdate;
import com.evofu.saferoute.utils.AuditLogger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Foreground Service that handles the active SOS lifecycle.
 * Ensures tracking persists even if the app is closed or process is killed.
 */
public class SosForegroundService extends Service implements FusedLocationProviderImpl.LocationUpdateListener {
    private static final String CHANNEL_ID = "sos_channel";
    private static final int NOTIFICATION_ID = 911;
    private FusedLocationProviderImpl locationProvider;
    private ApiService api;
    private String currentSosId = "EMERGENCY_ACTIVE";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification());

        locationProvider = new FusedLocationProviderImpl(this, this);
        api = ApiClient.getService(SafeRouteSDK.getConfig());

        locationProvider.startSosTracking();
        AuditLogger.logEvent("SosForegroundService", "CREATED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Restart if killed
    }

    @Override
    public void onLocationUpdate(Location location) {
        LocationUpdate update = new LocationUpdate(
                currentSosId,
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracy());

        api.updateLocation(SafeRouteSDK.getConfig().getApiKey(), update).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    AuditLogger.logEvent("LocationUpdate", "SENT",
                            location.getLatitude() + "," + location.getLongitude());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AuditLogger.logError("LocationUpdate", new Exception(t));
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Emergency SOS",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Critical alerts for manual safety tracking");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null)
                manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SafeRoute Emergency Active")
                .setContentText("Your location is being shared with trusted contacts.")
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
    }

    @Override
    public void onDestroy() {
        if (locationProvider != null)
            locationProvider.stopTracking();
        AuditLogger.logEvent("SosForegroundService", "DESTROYED");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
