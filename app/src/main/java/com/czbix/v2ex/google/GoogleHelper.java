package com.czbix.v2ex.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.czbix.v2ex.service.RegistrationIntentService;

public class GoogleHelper {
    private static final String TAG = GoogleHelper.class.getSimpleName();

    public static boolean isPlayServicesAvailable(Context context) {
//        final int errCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
//
//        return errCode == ConnectionResult.SUCCESS;
        return false;
    }

    /**
     * @return null if success, otherwise it's the error message to show
     */
    public static String checkPlayServices(Activity activity) {
//        final int errCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
//
//        if (errCode == ConnectionResult.SUCCESS) {
//            return null;
//        }
//        LogUtils.i(TAG, "check play services failed: %s",
//                GoogleApiAvailability.getInstance().getErrorString(errCode));
//
//        return GoogleApiAvailability.getInstance().getErrorString(errCode);
        return "";
    }

    public static Intent getRegistrationIntentToStartService(Context context, boolean isRegister) {
        final Intent intent = new Intent(context, RegistrationIntentService.class);
        if (!isRegister) {
            intent.putExtra(RegistrationIntentService.KEY_UNREGISTER, true);
        }

        return intent;
    }
}
