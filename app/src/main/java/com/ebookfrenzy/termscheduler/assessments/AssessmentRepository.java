package com.ebookfrenzy.termscheduler.assessments;

import android.app.Application;
import android.content.Context;
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
 * Created At: 2023-12-04 7:58AM
 * Created By: flowmar
 *
 *
 *******************/
public class AssessmentRepository
    {
        // ------------------------------ FIELDS ------------------------------

        /**
         * The constant for amount of time to put the thread to sleep while it fetches all assessments.
         */
        private static final int                        THREAD_SLEEP_TIME = 500;
        /**
         * The AssessmentsDao
         */
        private final        AssessmentDao              mAssessmentDao;
        /**
         * LiveData list of all assessments
         */
        private final        LiveData<List<Assessment>> allAssessments;


        // --------------------------- CONSTRUCTORS ---------------------------

        /**
         * Constructor
         *
         * @param application The application context
         */
        AssessmentRepository(Application application)
            {
                SchedulerRoomDatabase database = SchedulerRoomDatabase.getInstance(application);
                mAssessmentDao = database.assessmentDao();
                allAssessments = mAssessmentDao.getAllAssessments();

                // Delay to ensure that the async tasks have time to be executed
                try
                    {
                        Thread.sleep(THREAD_SLEEP_TIME);
                    }
                catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
            }

        // -------------------------- OTHER METHODS --------------------------

        /**
         * Inserts a assessment into the database.
         *
         * @param assessment the assessment object to be inserted
         */
        public void insert(Context context, Assessment assessment, int courseId)
            {
                // if (isStartAlarmOn)
                //     {
                //
                //     }
                // else
                //     {
                //
                //     }
                //
                //
                // if (isEndAlarmOn)
                //     {
                //
                //     }
                // else
                //     {
                //
                //     }


                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() ->
                                     {
                                         // Check the number of assessments in the database for the given courseId
                                         int count = mAssessmentDao.getAssessmentCount(courseId);
                                         // If it is less than 5, insert the assessment
                                         if (count < 5)
                                             {
                                                 mAssessmentDao.insertAssessment(assessment);
                                             }
                                         else
                                             {
                                                 // Create a new Handler to post to the main thread
                                                 new Handler(Looper.getMainLooper()).post(
                                                         // Display a toast error message
                                                         () -> Toast.makeText(context, "Error: You have reached the " +
                                                                 "maximum number of 5 assessments for this" +
                                                                 " course.", Toast.LENGTH_LONG).show());
                                             }
                                         executor.shutdown();
                                     });


            }


        /**
         * Delete.
         *
         * @param assessment the assessment
         */
        public void delete(Assessment assessment)
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() ->
                                     {
                                         mAssessmentDao.deleteAssessment(assessment);

                                         executor.shutdown();
                                     });
            }

        /**
         * Gets all assessments.
         *
         * @return the list of assessments
         */
        LiveData<List<Assessment>> getAllAssessments()
            {
                return allAssessments;
            }

        public void update(Assessment assessment)
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() ->
                                     {
                                         mAssessmentDao.updateAssessment(assessment);

                                         executor.shutdown();
                                     });
            }
    }
