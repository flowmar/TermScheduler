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
public class CourseRepository {

  // ------------------------------ FIELDS ------------------------------

  /** The constant for amount of time to put the thread to sleep while it fetches all courses. */
  private static final int THREAD_SLEEP_TIME = 500;

  /** The CourseDao */
  private final CourseDao mCourseDao;

  /** LiveData list of all courses */
  private final LiveData<List<Course>> allCourses;

  private List<Course> coursesList;

  // --------------------------- CONSTRUCTORS ---------------------------

  /**
   * Constructor
   *
   * @param application The application context
   */
  public CourseRepository(Application application) throws InterruptedException {
    SchedulerRoomDatabase database = SchedulerRoomDatabase.getInstance(application);
    mCourseDao = database.courseDao();
    allCourses = mCourseDao.getAllCourses();
    coursesList = getCoursesList();

    // Delay to ensure that the async tasks have time to be executed
    Thread.sleep(THREAD_SLEEP_TIME);
  }

  /**
   * Gets courses list.
   *
   * @return the courses list
   */
  List<Course> getCoursesList() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> coursesList = mCourseDao.getCoursesList());
    List<Course> safeCoursesList = coursesList;
    executor.shutdown();
    return safeCoursesList;
  }

  /**
   * Inserts a course into the database.
   *
   * @param course the course object to be inserted
   */
  public void insert(Course course) {
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
  public void update(Course course) {

    // Create a new single thread executor to update the course in the database
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> mCourseDao.updateCourse(course));
    executor.shutdown();
  }

  /**
   * Delete.
   *
   * @param course the course
   */
  public void delete(Context context, Course course) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(
        () -> {
          boolean isDeletionSuccessful = false;
          try {
            mCourseDao.deleteCourse(course);
            isDeletionSuccessful = true;
          } catch (SQLiteConstraintException e) {
            String message =
                "Cannot delete course: "
                    + course.getCourseName()
                    + ", since it has assessments associated with it.";
            new Handler(Looper.getMainLooper())
                .post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
            isDeletionSuccessful = false;
          }

          if (isDeletionSuccessful) {
            String message = "Course deleted: " + course.getCourseName();
            new Handler(Looper.getMainLooper())
                .post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
          }
        });
    executor.shutdown();
  }

  /**
   * Gets all courses.
   *
   * @return the list of courses
   */
  public LiveData<List<Course>> getAllCourses() {
    return allCourses;
  }
}
