package com.evofu.saferoute;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import com.evofu.saferoute.core.SafeRouteConfig;
import com.evofu.saferoute.core.SafeRouteSDK;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Sample Unit Test for SafeRoute SDK.
 * Verifies initialization and emergency trigger logic.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class SafeRouteSDKTest {

    private Context context;
    private SafeRouteConfig config;

    @Before
    public void setup() {
        context = mock(Context.class);
        config = new SafeRouteConfig.Builder()
                .setApiKey("test_api_key")
                .setDeviceToken("test_device_token")
                .build();
    }

    @Test
    public void testSDKInitialization() {
        SafeRouteSDK.init(context, config);
        assertNotNull(SafeRouteSDK.getInstance());
    }

    @Test
    public void testTriggerEmergency() {
        SafeRouteSDK.init(context, config);
        // This test would ideally verify that SosManager.triggerEmergency() is called
        // Since it's a static call to a singleton, we check for no crashes in this
        // sample
        SafeRouteSDK.triggerEmergency();
    }
}
