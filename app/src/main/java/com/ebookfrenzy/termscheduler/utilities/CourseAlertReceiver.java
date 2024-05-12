package com.ebookfrenzy.termscheduler.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.ebookfrenzy.termscheduler.R;

/** The {@link CourseAlertReceiver} class */
public class CourseAlertReceiver extends BroadcastReceiver {
  /** The {@code COURSE_CHANNEL_ID} */
  public static final String COURSE_CHANNEL_ID =
      "com.ebookfrenzy.termscheduler.course_notification_channel";

  /** The {@code TAG_COURSE_A} */
  private final String TAG_COURSE_ALERT_RECEIVER = CourseAlertReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {

    // Get info from the Intent that the AlarmManager sent to this receiver
    String courseId = intent.getStringExtra("COURSE_ID");
    String courseName = intent.getStringExtra("COURSE_NAME");
    String alertTitle = intent.getStringExtra("ALERT_TITLE");
    String alertMessage = intent.getStringExtra("ALERT_MESSAGE");

    Log.i(
        TAG_COURSE_ALERT_RECEIVER,
        "onReceive: CourseAlertReceiver about to create a notification channel");
    // Create the notification channel
    createNotificationChannel(context);

    Log.i(TAG_COURSE_ALERT_RECEIVER, "onReceive: about to create a notification");
    Notification notification =
        new NotificationCompat.Builder(context, COURSE_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(alertTitle)
            .setContentText(alertMessage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build();

    Log.i(TAG_COURSE_ALERT_RECEIVER, "onReceive: about to show a notification");
    if (courseId != null) {
      try {
        NotificationManagerCompat.from(context).notify(courseId.hashCode(), notification);
      } catch (SecurityException e) {
        Log.e("CourseAlertReceiver onReceive Error", e.getMessage());
      }
    }
  }

  private void createNotificationChannel(Context context) {

    // Check if the device is running Android Oreo or higher
    if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {

      // Create the NotificationChannel name, description, and importance
      CharSequence name = context.getString(R.string.course_channel_name);
      String description = context.getString(R.string.course_channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;

      // Create the NotificationChannel
      NotificationChannel channel = new NotificationChannel(COURSE_CHANNEL_ID, name, importance);

      // Set the NotificationChannel description
      channel.setDescription(description);

      // Create a NotificationManager
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

      // Register the NotificationChannel with the system
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(channel);
        Log.i(TAG_COURSE_ALERT_RECEIVER, "createNotificationChannel: Notification channel created");
      }
    }
  }

  /*
   private void showNotification(Context context, String title, String message, int notificationId)
       {

           // Create a notification
           NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

           // Create a NotificationBuilder
           NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                   .setSmallIcon(R.drawable.notification_icon)
                   .setContentText(message)
                   .setContentTitle(title)
                   .setPriority(NotificationCompat.PRIORITY_HIGH)
                   .setAutoCancel(true); // Dismiss notification after tap

           // Add an intent to launch an activity on notification tap
           Intent tapIntent = new Intent(context, MainActivity.class);
           PendingIntent pendingIntent =
                   PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
           notificationBuilder.setContentIntent(pendingIntent);


           // Request code for the notification
           int requestCode = 3000;

           if (notificationManager != null)
               {
                   notificationManager.notify(requestCode, notificationBuilder.build());
               }

       }
  */

}
