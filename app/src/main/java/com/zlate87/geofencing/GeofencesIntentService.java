package com.zlate87.geofencing;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;

public class GeofencesIntentService extends IntentService {
  private static final String LOG_TAG = GeofencesIntentService.class.toString();

  public GeofencesIntentService() {
    super(GeofencesIntentService.class.toString());
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Log.d(LOG_TAG, "onHandleIntent");
    if (LocationClient.hasError(intent)) {
      int errorCode = LocationClient.getErrorCode(intent);
      throw new RuntimeException(String.format("Intent has location client error code [%s]", errorCode));
    }

    int transitionType = LocationClient.getGeofenceTransition(intent);
    if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) || (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
      List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
      for (int i = 0; i < triggerList.size(); i++) {
        String requestId = triggerList.get(i).getRequestId();
        Log.d(LOG_TAG, String.format("Geofences with ID [%s] triggered with transitionType %s", requestId, transitionType));
      }
    }
  }

}