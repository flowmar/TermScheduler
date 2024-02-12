package com.ebookfrenzy.termscheduler.courses;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-23 4:35 AM
 * Created By: flowmar
 *
 *
 */
@Dao
public interface CourseDao
    {
        /**
         * Retrieves all courses from the database.
         *
         * @return A LiveData object containing a list of Course objects.
         */
        @Query("SELECT * FROM courses")
        LiveData<List<Course>> getAllCourses();

        @Query("SELECT * FROM courses")
        List<Course> getCoursesList();


        /**
         * Retrieves a single course from the database.
         *
         * @param courseName the course name
         *
         * @return the course
         */
        @Query("SELECT * FROM courses WHERE course_name = :courseName")
        LiveData<Course> getCourse(String courseName);


        /**
         * Inserts a new course into the database.
         *
         * @param course The course to be inserted.
         */
        @Insert
        void insertCourse(Course course);

        /**
         * Updates a course in the database.
         *
         * @param course the course
         */
        @Update
        void updateCourse(Course course);

        /**
         * Delete course.
         *
         * @param course the course
         */
        @Delete
        void deleteCourse(Course course) throws SQLiteConstraintException;

        /**
         * Delete all courses.
         */
        @Query("DELETE FROM courses")
        void deleteAllCourses();


        /**
         * Deletes a course from the database.
         *
         * @param courseName Name of the course to delete.
         */
        @Query("DELETE FROM courses WHERE course_name = :courseName")
        void deleteCourseByName(String courseName);


    }
