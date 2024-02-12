package com.ebookfrenzy.termscheduler.assessments;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-23 8:27 AM
 * Created By: flowmar
 *
 *
 *******************/
@Dao
public interface AssessmentDao
    {
        /**
         * Retrieves all assessments from a specified course name.
         */
        @Query("SELECT * FROM assessments WHERE courseName = :courseName")
        LiveData<List<Assessment>> getAllAssessmentsByCourseName(String courseName);

        /**
         * Retrieves all assessments from the database.
         *
         * @return
         */
        @Query("SELECT * FROM assessments")
        LiveData<List<Assessment>> getAllAssessments();

        /**
         * Inserts a new assessment into the database.
         */
        @Insert()
        void insertAssessment(Assessment assessment);

        /**
         * Updates an assessment in the database.
         */
        @Update
        void updateAssessment(Assessment assessment);

        /**
         * Deletes an assessment from the database.
         *
         * @param assessment
         */
        @Delete
        void deleteAssessment(Assessment assessment);

        /**
         * Deletes all assessments from the database.
         */
        @Query("DELETE FROM assessments")
        void deleteAllAssessments();

        /**
         * Gets the count of assessments for a given courseId
         */
        @Query("SELECT COUNT(*) FROM assessments WHERE courseId = :courseId")
        int getAssessmentCount(int courseId);
    }
