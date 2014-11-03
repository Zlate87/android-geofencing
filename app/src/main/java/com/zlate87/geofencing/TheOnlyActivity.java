package com.zlate87.geofencing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.Collections;
import java.util.List;


public class TheOnlyActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener {

  private static final String LOG_TAG = TheOnlyActivity.class.toString();

  public static final double LATITUDE = 42.0056099;
  public static final double LONGITUDE = 21.4170419;
  public static final int RADIUS = 100;

  public static final String REQUEST_ID = "42"; // random number :)

  LocationClient locationClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.the_only_activity);

    int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    Log.d(LOG_TAG, String.format("googlePlayServicesAvailable is [%s]", googlePlayServicesAvailable));
    if (ConnectionResult.SUCCESS == googlePlayServicesAvailable) {
      locationClient = new LocationClient(this, this, this);
      locationClient.connect();
    } else {
      String message = "GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) returned [%s]";
      throw new RuntimeException(String.format(message, googlePlayServicesAvailable));
    }
  }

  private PendingIntent getTransitionPendingIntent() {
    Intent intent = new Intent(this, GeofencesIntentService.class);
    return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public Geofence prepareGeofence() {
    return new Geofence.Builder()
            .setRequestId(REQUEST_ID)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
            .setCircularRegion(LATITUDE, LONGITUDE, RADIUS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build();
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.d(LOG_TAG, "onDisconnected");
    List<Geofence> listOfGeofences = Collections.singletonList(prepareGeofence());
    locationClient.addGeofences(listOfGeofences, getTransitionPendingIntent(), this);
  }

  @Override
  public void onDisconnected() {
    Log.d(LOG_TAG, "onDisconnected");
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.d(LOG_TAG, String.format("onConnectionFailed with error code [%s]", connectionResult.getErrorCode()));
  }

  @Override
  public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
    Log.d(LOG_TAG, String.format("onAddGeofencesResult with status code [%s]", statusCode));
    for (String id : geofenceRequestIds) {
      Log.d(LOG_TAG, String.format("and ID [%s]", id));
    }

  }
}
