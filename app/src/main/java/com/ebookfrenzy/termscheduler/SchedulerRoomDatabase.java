package com.ebookfrenzy.termscheduler;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ebookfrenzy.termscheduler.assessments.Assessment;
import com.ebookfrenzy.termscheduler.assessments.AssessmentDao;
import com.ebookfrenzy.termscheduler.courses.Course;
import com.ebookfrenzy.termscheduler.courses.CourseDao;
import com.ebookfrenzy.termscheduler.terms.Term;
import com.ebookfrenzy.termscheduler.terms.TermDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-23 7:53AM
 * Created By: flowmar
 *
 *******************/
@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1)
public abstract class SchedulerRoomDatabase extends RoomDatabase
    {

        // ------------------------------ FIELDS ------------------------------
        // Number of threads used to write to the database
        private static final int NUMBER_OF_THREADS = 4;

        public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        private static SchedulerRoomDatabase sINSTANCE;

        // -------------------------- STATIC METHODS --------------------------
        private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
            {
                /**
                 * Overrides the `onCreate` method of the `SQLiteOpenHelper` class.
                 *
                 * @param db the `SupportSQLiteDatabase` instance representing the database
                 */
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db)
                    {
                        super.onCreate(db);

                        // Create an ExecutorService to handle database operations
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.execute(() ->
                                             {

                                                 // Populate the database with terms
                                                 TermDao termDao = sINSTANCE.termDao();

                                                 termDao.insertTerm(new Term("Fall 2022", "2022-09-01", "2022-12-20"));

                                                 termDao.insertTerm(
                                                         new Term("Spring 2022", "2022-01-01", "2022-05-31"));

                                                 // Populate the database with courses
                                                 CourseDao courseDao = sINSTANCE.courseDao();

                                                 courseDao.insertCourse(
                                                         new Course("CS 101", "2022-09-01", "2022-12-20", "Completed",
                                                                    "Vincent" +
                                                                            " Jones", "(555)" +
                                                                            "341-2222", "vcjones" +
                                                                            "@school.edu", "Instructor was great.",
                                                                    "Fall " +
                                                                            "2022", 1, null, null
                                                         ));

                                                 courseDao.insertCourse(
                                                         new Course("CS 201", "2022-01-01", "2022-05-31", "In Progress",
                                                                    "Emily Planners", "(653)352-5234",
                                                                    "eplanners@school" +
                                                                            ".edu", "Assessments were very difficult"
                                                                 , "Spring 2022", 2, null, null
                                                         ));

                                                 // Populate the database with assessments
                                                 AssessmentDao assessmentDao = sINSTANCE.assessmentDao();
                                                 assessmentDao.insertAssessment(
                                                         new Assessment("Objective", "Midterm Exam", "2022-10-15", "2022-10-22", 1,
                                                                        "CS 101", 1, "Fall 2022", null, null
                                                         ));
                                                 assessmentDao.insertAssessment(
                                                         new Assessment("Performance", "Final Exam", "2022-12-20", "2022-12-27",
                                                                        1, "CS 101", 1, "Fall 2022", null, null
                                                         ));
                                                 assessmentDao.insertAssessment(
                                                         new Assessment("Objective", "First Exam", "2022-08-15", "2022-08-22", 1,
                                                                        "CS 101", 1, "Fall 2022", null, null
                                                         ));
                                                 assessmentDao.insertAssessment(
                                                         new Assessment("Performance", "Second Exam", "2022-09-15", "2022-09-22", 1,
                                                                        "CS 101", 1, "Fall 2022", null, null
                                                         ));
                                                 assessmentDao.insertAssessment(
                                                         new Assessment("Objective", "Makeup Exam", "2022-12-20", "2022-12-30", 1,
                                                                        "CS 101", 1, "Fall 2022", null, null
                                                         ));
                                             });

                        // Shutdown the executor service
                        executor.shutdown();
                    }
            };

        /**
         * Returns the instance of the SchedulerRoomDatabase class.
         *
         * @param context the context used to create the database instance
         *
         * @return the SchedulerRoomDatabase instance
         */
        public static synchronized SchedulerRoomDatabase getInstance(Context context)
            {
                if (sINSTANCE == null)
                    {
                        synchronized (SchedulerRoomDatabase.class)
                            {
                                sINSTANCE = Room.databaseBuilder(
                                                        context.getApplicationContext(),
                                                        SchedulerRoomDatabase.class,
                                                        "scheduler_database.db"
                                                                )
                                                .addCallback(roomCallback)
                                                .build();
                            }
                    }
                return sINSTANCE;
            }


        // -------------------------- OTHER METHODS --------------------------
        public abstract AssessmentDao assessmentDao();

        public abstract com.ebookfrenzy.termscheduler.courses.CourseDao courseDao();

        public abstract TermDao termDao();


    }
