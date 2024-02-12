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

public class AssessmentAlertReceiver extends BroadcastReceiver
    {

        public static final String ASSESSMENT_CHANNEL_ID = "com.ebookfrenzy.termscheduler.assessment_notification_channel";

        @Override
        public void onReceive(Context context, Intent intent)
            {
                // Get info from the Intent that the AlarmManager sent to this receiver
                String assessmentId    = intent.getStringExtra("ASSESSMENT_ID");
                String assessmentTitle = intent.getStringExtra("ASSESSMENT_TITLE");
                createNotificationChannel(context);

                Notification notification = new NotificationCompat.Builder(context, ASSESSMENT_CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(intent.getStringExtra("ALERT_TITLE"))
                        .setContentText(assessmentTitle)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build();

                if (assessmentId != null)
                    {
                        try
                            {
                                NotificationManagerCompat.from(context).notify(assessmentId.hashCode(), notification);
                            }
                        catch (SecurityException e)
                            {
                                Log.e("CourseAlertReceiver onReceive Error", e.getMessage());
                            }
                    }

            }

        private void createNotificationChannel(Context context)
            {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        CharSequence        name        = context.getString(R.string.assessment_channel_name);
                        String              description = context.getString(R.string.assessment_channel_description);
                        int                 importance  = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel     = new NotificationChannel(ASSESSMENT_CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                        if (notificationManager != null)
                            {
                                notificationManager.createNotificationChannel(channel);
                            }
                    }
            }
    }

