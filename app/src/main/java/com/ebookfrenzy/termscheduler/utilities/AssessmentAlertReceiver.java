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

/** The {@link AssessmentAlertReceiver} class */
public class AssessmentAlertReceiver extends BroadcastReceiver {

  /** The {@code ASSESSMENT_CHANNEL_ID} */
  public static final String ASSESSMENT_CHANNEL_ID =
      "com.ebookfrenzy.termscheduler.assessment_notification_channel";

  /** The {@code TAG_ASSESSMENT_ALERT_RECEIVER} */
  private final String TAG_ASSESSMENT_ALERT_RECEIVER = "AssessmentAlertReceiver.java";

  @Override
  public void onReceive(Context context, Intent intent) {
    // Get info from the Intent that the AlarmManager sent to this receiver
    int assessmentId = intent.getIntExtra("ASSESSMENT_ID", -1);
    String alertTitle = intent.getStringExtra("ALERT_TITLE");
    String alertMessage = intent.getStringExtra("ALERT_MESSAGE");

    Log.i(
        TAG_ASSESSMENT_ALERT_RECEIVER,
        "onReceive: AssessmentAlertReceiver about to create notification channel");
    createNotificationChannel(context);

    Log.i(
        TAG_ASSESSMENT_ALERT_RECEIVER,
        "onReceive: AssessmentAlertReceiver about to create notification");
    Notification notification =
        new NotificationCompat.Builder(context, ASSESSMENT_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(alertTitle)
            .setContentText(alertMessage)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build();

    Log.i(TAG_ASSESSMENT_ALERT_RECEIVER, "onReceive: About to show notification");
    if (assessmentId != -1) {
      try {
        NotificationManagerCompat.from(context)
            .notify(("" + assessmentId).hashCode(), notification);
      } catch (SecurityException e) {
        Log.e("CourseAlertReceiver onReceive Error", e.getMessage());
      }
    }
  }

  private void createNotificationChannel(Context context) {
    if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Create the NotificationChannel name, description, and importance
      CharSequence name = context.getString(R.string.assessment_channel_name);
      String description = context.getString(R.string.assessment_channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;

      // Create the NotificationChannel
      NotificationChannel channel =
          new NotificationChannel(ASSESSMENT_CHANNEL_ID, name, importance);
      // Set the NotificationChannel description
      channel.setDescription(description);

      // Create a NotificationManager
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

      // Register the NotificationChannel with the system
      if (notificationManager != null) {
        notificationManager.createNotificationChannel(channel);
        Log.i(
            TAG_ASSESSMENT_ALERT_RECEIVER,
            "createNotificationChannel: Notification channel created");
      }
    }
  }
}
