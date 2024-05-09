package com.ebookfrenzy.termscheduler.courses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.databinding.ActivityCourseListBinding;

public class CourseListActivity extends AppCompatActivity
    {



        private CourseViewModel           mCourseViewModel;


        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                ActivityCourseListBinding binding;
                super.onCreate(savedInstanceState);
                binding = ActivityCourseListBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);

                // Create a listener for the AddNewCourse button
                binding.buttonAddCourse.setOnClickListener(v ->
                                                               {
                                                                   Intent intent = new Intent(
                                                                           CourseListActivity.this,
                                                                           AddEditCourseActivity.class
                                                                   );
                                                                   startActivity(intent);
                                                               });


                // Create a reference to the RecyclerView
                RecyclerView courseRecyclerView = binding.courseListRecyclerview;

                // Set a LinearLayoutManager on the RecyclerView
                courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                // Create a CourseAdapter and set it to manage the RecyclerView
                CourseAdapter courseAdapter = new CourseAdapter();
                courseRecyclerView.setAdapter(courseAdapter);

                // Get a reference to the CourseViewModel by creating a new ViewModelProvider instance
                mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

                // Create an ItemTouchHelper to implement the swipe to delete functionality
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                                                                       ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                )
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
                                int    position = viewHolder.getAbsoluteAdapterPosition();
                                Course course   = courseAdapter.getCourseAt(position);

                                // Call the CourseViewModel's delete method
                                mCourseViewModel.delete(
                                        getApplicationContext(), course);

                                courseAdapter.notifyItemChanged(position);


                            }
                    }).attachToRecyclerView(courseRecyclerView);

                // Set an OnItemClickListener on the individual items in the RecyclerView
                courseAdapter.setOnItemClickListener(course ->
                                                         {
                                                             Intent intent = new Intent(CourseListActivity.this,
                                                                                        AddEditCourseActivity.class
                                                             );
                                                             intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ID,
                                                                             course.getId()
                                                                            );
                                                             intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_NAME,
                                                                             course.getCourseName()
                                                                            );
                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_START_DATE,
                                                                     course.getStartDate()
                                                                            );
                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_END_DATE,
                                                                     course.getEndDate()
                                                                            );
                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_INSTRUCTOR_NAME,
                                                                     course.getInstructorName()
                                                                            );
                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_INSTRUCTOR_EMAIL,
                                                                     course.getInstructorEmail()
                                                                            );
                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_INSTRUCTOR_PHONE,
                                                                     course.getInstructorPhoneNumber()
                                                                            );
                                                             intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS,
                                                                             course.getStatus()
                                                                            );
                                                             intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TERM_ID,
                                                                             course.getTermId()
                                                                            );
                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_TERM_NAME,
                                                                     course.getTermName()
                                                                            );
                                                             intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_NOTE,
                                                                             course.getCourseNote()
                                                                            );

                                                             intent.putExtra(AddEditCourseActivity.EXTRA_IS_COURSE_START_ALARM_SET,
                                                                            course.isCourseStartAlarmSet());

                                                             intent.putExtra(AddEditCourseActivity.EXTRA_IS_COURSE_END_ALARM_SET,
                                                                             course.isCourseEndAlarmSet());

                                                             intent.putExtra(
                                                                     AddEditCourseActivity.EXTRA_COURSE_START_ALARM_DATETIME,
                                                                     course.getCourseStartAlarmDatetime()
                                                                            );

                                                             intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_END_ALARM_DATETIME,
                                                                             course.getCourseEndAlarmDatetime()
                                                                            );



                                                             startActivity(intent);
                                                         });
                // Set up an observer to observe the list of Courses
                // Use the CourseAdapter to re-set the CoursesList every time there is a change
                mCourseViewModel.getAllCourses().observe(this, courseAdapter::setCoursesList);
            }
    }
