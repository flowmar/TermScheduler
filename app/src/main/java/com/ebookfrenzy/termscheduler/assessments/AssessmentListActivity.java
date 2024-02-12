package com.ebookfrenzy.termscheduler.assessments;

import static com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity.EXTRA_ASSESSMENT_END_ALARM_DATE_TIME;
import static com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity.EXTRA_ASSESSMENT_START_ALARM_DATE_TIME;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.databinding.ActivityAssessmentListBinding;

public class AssessmentListActivity extends AppCompatActivity
    {
        private ActivityAssessmentListBinding binding;

        private AssessmentViewModel mAssessmentViewModel;


        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                binding = ActivityAssessmentListBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);


                // Create a listener for the AddNewAssessment Button
                binding.buttonAddAssessment.setOnClickListener(v ->
                                                                   {
                                                                       Intent intent = new Intent(
                                                                               AssessmentListActivity.this,
                                                                               AddEditAssessmentActivity.class
                                                                       );
                                                                       startActivity(intent);
                                                                   });

                // Create a reference to the RecyclerView
                RecyclerView assessmentRecyclerView = binding.assessmentListRecyclerview;

                // Set a LinearLayoutManager on the RecyclerView
                assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                // Create an AssessmentAdapter and set it to manage the RecyclerView
                AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
                assessmentRecyclerView.setAdapter(assessmentAdapter);

                // Get a reference to the AssessmentViewModel by creating a new ViewModelProvider instance
                mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

                // Create an ItemTouchHelper to implement the swipe to delete functionality
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                    {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView,
                                              @NonNull RecyclerView.ViewHolder viewHolder,
                                              @NonNull RecyclerView.ViewHolder target)
                            {
                                return false;
                            }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
                            {
                                int        position   = viewHolder.getAbsoluteAdapterPosition();
                                Assessment assessment = assessmentAdapter.getAssessmentAt(position);


                                mAssessmentViewModel.delete(
                                        assessment);
                                assessmentAdapter.notifyItemChanged(position);
                                Toast.makeText(AssessmentListActivity.this, "Assessment Deleted", Toast.LENGTH_SHORT)
                                     .show();
                            }
                    }).attachToRecyclerView(assessmentRecyclerView);

                // Set an OnItemClickListener on the individual items in the RecyclerView
                assessmentAdapter.setOnItemClickListener(assessment ->
                                                             {
                                                                 Intent intent = new Intent(AssessmentListActivity.this,
                                                                                            AddEditAssessmentActivity.class
                                                                 );

                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_ID,
                                                                         assessment.getId()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE,
                                                                         assessment.getAssessmentTitle()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE,
                                                                         assessment.getAssessmentType()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_COURSE_ID,
                                                                         assessment.getCourseId()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_COURSE_NAME,
                                                                         assessment.getCourseName()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_TERM_ID,
                                                                         assessment.getTermId()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_TERM_NAME,
                                                                         assessment.getTermName()
                                                                                );
                                                                 intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_START_DATE,
                                                                                 assessment.getAssessmentStartDate()
                                                                                );
                                                                 intent.putExtra(
                                                                         AddEditAssessmentActivity.EXTRA_ASSESSMENT_END_DATE,
                                                                         assessment.getAssessmentEndDate()
                                                                                );


                                                                 intent.putExtra(
                                                                         EXTRA_ASSESSMENT_START_ALARM_DATE_TIME,
                                                                         assessment.getAssessmentStartDateTimeAlarm()
                                                                                );


                                                                 intent.putExtra(
                                                                         EXTRA_ASSESSMENT_END_ALARM_DATE_TIME,
                                                                         assessment.getAssessmentEndDateTimeAlarm()
                                                                                );

                                                                 startActivity(intent);
                                                             });

                // Set up an observer to observe the list of Assessments
                // Use the AssessmentAdapter to re-set the AssessmentsList every time there is a change
                mAssessmentViewModel.getAllAssessments().observe(this, assessmentAdapter::setAssessmentsList);
            }
    }
