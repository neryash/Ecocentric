package com.teamducky.ecocentric;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityRecognizedService extends IntentService{

    public static final String RECOGNITION_RESULT = "result";
    public static final String BROADCAST_UPDATE = "new_update";

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        if(ActivityRecognitionResult.hasResult(intent)){
            Log.d("intentService", "got new update!");

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            // notify main activity
            Intent i = new Intent(BROADCAST_UPDATE);
            i.putExtra(RECOGNITION_RESULT, result);
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
            manager.sendBroadcast(i);
        }
    }
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if(ActivityRecognitionResult.hasResult(intent)) {
//            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
//            handleDetectedActivities( result.getProbableActivities() );
//        }
//    }
//    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
//        for( DetectedActivity activity : probableActivities ) {
//            switch( activity.getType() ) {
//                case DetectedActivity.IN_VEHICLE: {
//                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"In Vehicle: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.ON_BICYCLE: {
//                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"In Bicycle: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.ON_FOOT: {
//                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"In Foot: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.RUNNING: {
//                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"In Running: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.STILL: {
//                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"In Still: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.TILTING: {
//                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"Tilting: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.WALKING: {
//                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"In Vehicle: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                case DetectedActivity.UNKNOWN: {
//                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
//                    Toast.makeText(ActivityRecognizedService.this,"Unknown: " + activity.getConfidence(),Toast.LENGTH_SHORT).show();
//                    break;
//                }
//            }
//        }
//    }
}