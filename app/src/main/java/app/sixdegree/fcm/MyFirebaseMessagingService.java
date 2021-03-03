package app.sixdegree.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.FacebookSdk;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import app.sixdegree.R;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.chatchat.ChatDetails;
import app.sixdegree.view.activity.home_module.HomeActivity;
import app.sixdegree.view.activity.home_module.fragments.NotificationFragment;


import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Snow-Dell-05 on 05-Feb-18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String chat_id = "";
    String name = "";
    String type = "";
    String body = "";

    int notificationcount = 0;
    int msgcount = 0;
    AppSession sessionManager;
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param // messageBody FCM message body received.
     */
    Intent intent;
    private NotificationUtils notificationUtils;
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
   /* private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }*/

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "DATA: " + remoteMessage);
        //  StaticValues.mNotificationBlinking = true;
        Map<String, String> mMap = remoteMessage.getData();

        //  Check if message contains a notification payload.
     /*   if (remoteMessage.getNotification() != null) {
            Log.wtf(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification("Hello working");
        }*/

        //   Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData().toString());
            //    handleNow(remoteMessage.getData());


            type = remoteMessage.getData().get("notification_type");
            name = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            Log.e("type---", "--" + type);
            Log.e("title---", "--" + name);
            //   try {


            handleNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            handleNow(mMap);


            // testNiti("test");
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */


    private void handleNow(Map<String, String> mMapp) {
        Log.e(TAG, "Short lived task is done.");
      //  sendNotification(mMapp);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;

      Random  randnum = new Random();
        String channelId = "channel-011";
        String channelName = "Six Degree";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon( R.mipmap.ic_launcher)
                .setContentTitle(name)
                 .setAutoCancel(true)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(randnum.nextInt(), mBuilder.build());


    }

    private void sendNotification(Map<String, String> mMapp) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String mTitle = "", mUuid = "", mBody = "", mBody_img_url = "", mType = "Type", title_img_url = "img",
                mNotification = "";


        notificationUtils = new NotificationUtils(getApplicationContext());

        notificationUtils.showNotificationMessage(mTitle, mBody, intent, mBody_img_url);


        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", mBody);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

/*

`        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)

                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("Title")
                        .setContentText("Body")
                        .setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 */
        /* ID of notification*//*
       ,notificationBuilder.build());
       */

        // play notification sound
        notificationUtils.playNotificationSound();
    }

    private void handleNotification(String mTitle, String message) {


        if (type.toLowerCase().equals("chat")) {
//            intent = new Intent(this, NotificationsAct.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent = new Intent(this, HomeActivity.class);
            intent.putExtra("type", "chat");
//            intent.putExtra("chat_group_id", chat_id);
//            intent.putExtra("name", name);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        } else {
            intent = new Intent(this, HomeActivity.class);
         intent.putExtra("type", "");
//            intent.putExtra("name", name);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        }


        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            //     is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            // If the app is in background, firebase itself handles the notification

        }
        notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.showNotificationMessage(mTitle, message, intent, "");
//        notificationUtils.
        // play sound when notification is come
        // notificationUtils.playNotificationSound();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.wtf(TAG, "Refreshed token: " + refreshedToken);
        Log.wtf("refresh", "refreshed");
        //  Saving reg id to shared preferences

        //    If you want to send messages to this application instance or
        //    manage this apps subscriptions on the server side, send the
        //    Instance ID token to your app server.


        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void testNiti(String messageBody) {
        Intent intent = new Intent(this, ChatDetails.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Touchrooms Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
