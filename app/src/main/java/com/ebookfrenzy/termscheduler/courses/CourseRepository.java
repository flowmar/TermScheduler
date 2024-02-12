package com.ebookfrenzy.termscheduler.courses;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.ebookfrenzy.termscheduler.SchedulerRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/******************
 * Project: TermScheduler
 * Created At: 2023-12-03 3:53PM
 * Created By: flowmar
 */
public class CourseRepository
    {

        // ------------------------------ FIELDS ------------------------------

        /**
         * The constant for amount of time to put the thread to sleep while it fetches all courses.
         */
        private static final int                    THREAD_SLEEP_TIME = 500;
        /**
         * The CourseDao
         */
        private final        CourseDao              mCourseDao;
        /**
         * LiveData list of all courses
         */
        private final        LiveData<List<Course>> allCourses;

        private List<Course> coursesList;


        // --------------------------- CONSTRUCTORS ---------------------------

        /**
         * Constructor
         *
         * @param application The application context
         */
        public CourseRepository(Application application) throws InterruptedException
            {
                SchedulerRoomDatabase database = SchedulerRoomDatabase.getInstance(application);
                mCourseDao  = database.courseDao();
                allCourses  = mCourseDao.getAllCourses();
                coursesList = getCoursesList();


                // Delay to ensure that the async tasks have time to be executed
                Thread.sleep(THREAD_SLEEP_TIME);
            }

        /**
         * Gets courses list.
         *
         * @return the courses list
         */
        List<Course> getCoursesList()
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> coursesList = mCourseDao.getCoursesList());
                List<Course> safeCoursesList = coursesList;
                executor.shutdown();
                return safeCoursesList;
            }


        // -------------------------- OTHER METHODS --------------------------

        /**
         * Sets an alarm for the course's start date.'
         *
         * @param course the course object to be updated
         */
        // private static void setAlarmForCourseStart(Context context, Course course)
        //     {
        //         String message = "Course " + course.getCourseName() + " starts today!";
        //         try
        //             {
        //                 // Schedule alarm for the course start
        //                 scheduleAlarm(context, course, Course.getStartDateAsLong(course.getStartDate()), message);
        //
        //                 // Create a new intent to fire
        //                 Intent intent = new Intent(context, CourseAlertReceiver.class);
        //                 intent.putExtra("title", "Course Start");
        //                 intent.putExtra("alert_message", message);
        //
        //                 // Get the notification_id and place it into the Intent as an extra
        //                 NotificationManager notificationManager = (NotificationManager) context.getSystemService(
        //                         Context.NOTIFICATION_SERVICE);
        //                 List<NotificationChannel> notificationChannels         = notificationManager.getNotificationChannels();
        //                 int                       notificationChannelCount     = notificationChannels.size();
        //                 NotificationChannel       defaultNotificationChannel   = notificationChannels.get(notificationChannelCount - 1);
        //                 int                       defaultNotificationChannelId = Integer.parseInt(defaultNotificationChannel.getId());
        //                 intent.putExtra("notification_id", defaultNotificationChannelId + 1);
        //
        //                 // Set up a PendingIntent to fire when the alarm goes off
        //                 PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 300, intent,
        //                                                                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        //                                                                       );
        //
        //             }
        //         catch (ParseException e)
        //             {
        //                 if (e.getMessage() != null)
        //                     {
        //                         Log.e("CourseRepository SetAlarmForCourseStart", e.getMessage());
        //                         Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        //                     }
        //             }
        //     }

        /**
         * Schedules an alarm
         *
         * @param course the course to be updated
         */

        // private static void scheduleAlarm(Context context, Course course, long triggerAtMillis, String message)
        //     {
        //         // Set an alarm at midnight the day of the course starting
        //         Calendar calendar = Calendar.getInstance();
        //         calendar.setTimeInMillis(triggerAtMillis);
        //
        //
        //         // Create an instance of AlarmManager
        //         AlarmManager alarmManager = getSystemService(context, AlarmManager.class);
        //
        //         // Create an intent for the CourseAlertReceiver class
        //         Intent intent = new Intent(context, CourseAlertReceiver.class);
        //         intent.putExtra("alert_message", message);
        //
        //         // Create a unique request code
        //         int requestCode = course.getId() + 100000;
        //
        //         // Create a pending intent using the broadcast receiver
        //         PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,
        //                                                                  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        //                                                                 );
        //         Log.i("CourseRepository ScheduleAlarm 1: ", "Created Pending Intent");
        //
        //         // If alarmManager exists, and the API version is high enough and the app has the required permissions...
        //         if (alarmManager != null)
        //             {
        //                 // Then set an exact alarm at the specified time
        //                 alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        //                 Toast.makeText(context, "Alarm set for course: " + course.getCourseName() + " on " + course.getStartDate(), Toast
        //                 .LENGTH_LONG)
        //                      .show();
        //                 Log.i("CourseRepository ScheduleAlarm", "Alarm set for course: " + course.getCourseName() + " on " + course.getStartDate
        //                 ());
        //             }
        //     }

        /**
         * Sets an alarm for the course's end date.
         *
         * @param context the context
         * @param course  the course
         */
        // private static void setAlarmForCourseEnd(Context context, Course course)
        //     {
        //         try
        //             {
        //                 // Schedule alarms for the course
        //                 scheduleAlarm(context, course, Course.getEndDateAsLong(course.getEndDate()),
        //                               "Course " + course.getCourseName() + " ends today!"
        //                              );
        //             }
        //         catch (ParseException e)
        //             {
        //                 if (e.getMessage() != null)
        //                     {
        //                         Log.e("CourseRepository SetAlarmForCourseEnd", e.getMessage());
        //                         Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        //                     }
        //             }
        //
        //     }

        // private static void cancelCourseStartAlert(Context context, Course course)
        //     {
        //         Intent intent = new Intent(context, CourseAlertReceiver.class);
        //
        //         // Convert the courseId to a String
        //         String courseId = String.valueOf(course.getId());
        //
        //         String combinedIdAndName = courseId + " " + course.getCourseName();
        //
        //         PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, combinedIdAndName.hashCode(), intent,
        //                                                                       PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        //                                                                      );
        //
        //         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //
        //         if (startPendingIntent != null && alarmManager != null)
        //             {
        //                 alarmManager.cancel(startPendingIntent);
        //                 startPendingIntent.cancel();
        //             }
        //     }

        // private static void cancelCourseEndAlert(Context context, Course course)
        //     {
        //         Intent intent = new Intent(context, CourseAlertReceiver.class);
        //
        //         // Convert the courseId to a String
        //         String courseId = String.valueOf(course.getId());
        //
        //         String combinedIdAndName = courseId + " " + course.getCourseName();
        //
        //         PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, combinedIdAndName.hashCode(), intent,
        //                                                                     PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        //                                                                    );
        //
        //         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //
        //         if (endPendingIntent != null && alarmManager != null)
        //             {
        //                 alarmManager.cancel(endPendingIntent);
        //                 endPendingIntent.cancel();
        //             }
        //     }

        /**
         * Inserts a course into the database.
         *
         * @param course the course object to be inserted
         */
        public void insert(Course course)
            {
                // Check if the toggles are on, and set an alarm for the course start and end if they are
                // if (isStartAlarmOn) setAlarmForCourseStart(context, course);
                // if (isEndAlarmOn) setAlarmForCourseEnd(context, course);

                // if (isStartAlarmOn)
                //     {
                //         scheduleCourseStartAlert(context, course);
                //     }
                // else
                //     {
                //         cancelCourseStartAlert(context, course);
                //     }
                //
                //
                // if (isEndAlarmOn)
                //     {
                //         scheduleCourseEndAlert(context, course);
                //     }
                // else
                //     {
                //         cancelCourseEndAlert(context, course);
                //     }


                // Create a new single thread executor to insert the course in the database
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> mCourseDao.insertCourse(course));
                executor.shutdown();
            }

        /**
         * Updates the specified course in the database.
         *
         * @param course the course to be updated
         */
        public void update(Course course)

            {
                // // If the start alarm toggle is on, set an alarm for the course's start date
                // if (isStartAlarmOn)
                //     {
                //         scheduleCourseStartAlert(context, course);
                //     }
                // // If the end alarm toggle is on, set an alarm for the course's end date
                // if (isEndAlarmOn)
                //     {
                //         scheduleCourseEndAlert(context, course);
                //     }

                // Create a new single thread executor to update the course in the database
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> mCourseDao.updateCourse(course));
                executor.shutdown();
            }

        // private static void scheduleCourseEndAlert(Context context, Course course)
        //     {
        //         // Create an alarm manager
        //         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //
        //         // Create an Intent to launch the CourseAlertReceiver
        //         Intent endIntent = new Intent(context, CourseAlertReceiver.class);
        //         // Add extras to the Intent
        //         endIntent.putExtra("COURSE_ID", course.getId());
        //         endIntent.putExtra("COURSE_NAME", course.getCourseName());
        //         endIntent.putExtra("ALERT_TITLE", "Course Ending Today!");
        //         // Convert the courseId to a String
        //         String courseId = String.valueOf(course.getId());
        //
        //         String combinedIdAndName = courseId + " " + course.getCourseName();
        //
        //         // Create a PendingIntent to first the broadcast receiver
        //         PendingIntent endPendingIntent = PendingIntent.getBroadcast(context, combinedIdAndName.hashCode(), endIntent,
        //                                                                     PendingIntent.FLAG_UPDATE_CURRENT
        //                                                                    );
        //
        //         // Convert the selected alarm time to epoch time
        //         String dateString = course.getEndDate();
        //         dateString = dateString + " 06:00:00";
        //         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        //         long             epochEndDateInMillis;
        //         try
        //             {
        //                 Date date = sdf.parse(dateString);
        //                 // Convert the date to epoch time in milliseconds
        //                 epochEndDateInMillis = date.getTime();
        //             }
        //         catch (ParseException e)
        //             {
        //                 throw new RuntimeException(e);
        //             }
        //
        //
        //         if (alarmManager != null)
        //             {
        //                 alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, epochEndDateInMillis, endPendingIntent);
        //             }
        //     }
        //
        // private static void scheduleCourseStartAlert(Context context, Course course)
        //     {
        //         // Create an alarm manager
        //         AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //
        //         // Create an Intent to launch the CourseAlertReceiver
        //         Intent startIntent = new Intent(context, CourseAlertReceiver.class);
        //         // Add extras to the Intent
        //         startIntent.putExtra("COURSE_ID", course.getId());
        //         startIntent.putExtra("COURSE_NAME", course.getCourseName());
        //         startIntent.putExtra("ALERT_TITLE", "Course Starting Today!");
        //         // Convert the courseId to a String
        //         String courseId = String.valueOf(course.getId());
        //
        //         // Create a PendingIntent to first the broadcast receiver
        //         PendingIntent startPendingIntent = PendingIntent.getBroadcast(context, courseId.hashCode(), startIntent,
        //                                                                       PendingIntent.FLAG_UPDATE_CURRENT
        //                                                                      );
        //
        //         // Convert the selected alarm time to epoch time
        //         String dateString = course.getStartDate();
        //         dateString = dateString + " 06:00:00";
        //         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        //         long             epochStartDateInMillis;
        //         try
        //             {
        //                 Date date = sdf.parse(dateString);
        //                 // Convert the date to epoch time in milliseconds
        //                 epochStartDateInMillis = date.getTime();
        //             }
        //         catch (ParseException e)
        //             {
        //                 throw new RuntimeException(e);
        //             }
        //
        //
        //         if (alarmManager != null)
        //             {
        //                 alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, epochStartDateInMillis, startPendingIntent);
        //             }
        //     }

        /**
         * Delete.
         *
         * @param course the course
         */
        public void delete(Context context, Course course)
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() ->
                                     {
                                         boolean isDeletionSuccessful = false;
                                         try
                                             {
                                                 mCourseDao.deleteCourse(course);
                                                 isDeletionSuccessful = true;
                                             }
                                         catch (SQLiteConstraintException e)
                                             {
                                                 String message = "Cannot delete course: " + course.getCourseName() +
                                                         ", since it has assessments associated with it.";
                                                 new Handler(Looper.getMainLooper()).post(
                                                         () -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                                                 isDeletionSuccessful = false;
                                             }

                                         if (isDeletionSuccessful)
                                             {
                                                 String message = "Course deleted: " + course.getCourseName();
                                                 new Handler(Looper.getMainLooper()).post(
                                                         () -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
                                             }
                                     });
                executor.shutdown();
            }

        /**
         * Gets all courses.
         *
         * @return the list of courses
         */
        public LiveData<List<Course>> getAllCourses()
            {
                return allCourses;
            }


    }
