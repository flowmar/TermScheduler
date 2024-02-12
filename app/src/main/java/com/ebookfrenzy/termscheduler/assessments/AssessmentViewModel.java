package com.ebookfrenzy.termscheduler.assessments;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-12-05 8:40PM
 * Created By: flowmar
 *
 *
 */
public class AssessmentViewModel extends AndroidViewModel
    {

//**********************************************Fields**************************************************/
        /**
         * An instance of the {@link AssessmentRepository}
         */
        private final AssessmentRepository       mAssessmentRepository;
        /**
         * A LiveData List of all assessments.
         */
        private final LiveData<List<Assessment>> allAssessments;


//**********************************************Constructors**************************************************

        /**
         * Instantiates a new Assessment view model.
         *
         * @param application the application
         */
        public AssessmentViewModel(@NonNull Application application)
            {
                super(application);
                mAssessmentRepository = new AssessmentRepository(application);

                allAssessments = mAssessmentRepository.getAllAssessments();
            }

        /********************************************** Methods **************************************************/
        /**
         * Inserts an assessment into the database
         *
         * @param assessment the assessment
         */
        public void insert(Context context, Assessment assessment, int courseId)
            {
                mAssessmentRepository.insert(context, assessment, courseId);
            }


        public void update(Assessment assessment)
            {
                mAssessmentRepository.update(assessment);
            }

        /**
         * Delete an assessment
         *
         * @param assessment the assessment
         */
        public void delete(Assessment assessment)
            {
                mAssessmentRepository.delete(assessment);
            }

        /**
         * Gets a LiveData List of all assessments.
         *
         * @return a List of all assessments
         */
        LiveData<List<Assessment>> getAllAssessments()
            {
                return allAssessments;
            }
    }
