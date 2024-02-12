package com.ebookfrenzy.termscheduler.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.courses.Course;
import com.ebookfrenzy.termscheduler.databinding.ActivityNotesBinding;
import com.ebookfrenzy.termscheduler.notes.CourseNoteAdapter.OnButtonClickListener;

public class CourseNotesListActivity extends AppCompatActivity
    {
        ActivityNotesBinding binding;

        private CourseNotesViewModel mCourseNotesViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                binding = ActivityNotesBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);

                // Create a reference to the RecyclerView
                RecyclerView courseNoteRecyclerView = binding.courseNotesRecyclerview;

                // Set a LinearLayoutManager on the RecyclerView
                courseNoteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                // Set the size to be fixed
                courseNoteRecyclerView.setHasFixedSize(true);

                // Create a CourseAdapter and set it to manage the RecyclerView
                CourseNoteAdapter courseNoteAdapter = new CourseNoteAdapter(new OnButtonClickListener()
                    {
                        @Override
                        public void onEmailButtonClick(Course course)
                            {
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                emailIntent.setData(Uri.parse("mailto:"));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, course.getCourseName() + " Notes");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, course.getCourseNote());
                                startActivity(emailIntent);

                            }

                        @Override
                        public void onSmsButtonClick(Course course)
                            {
                                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                                smsIntent.setData(Uri.parse("smsto:"));
                                smsIntent.putExtra("sms_body", course.getCourseNote());
                                startActivity(smsIntent);

                            }
                    });


                courseNoteRecyclerView.setAdapter(courseNoteAdapter);

                // Get a reference to the CourseViewModel
                mCourseNotesViewModel = new ViewModelProvider(this).get(CourseNotesViewModel.class);

                mCourseNotesViewModel.getAllCourses().observe(this, courseNoteAdapter::setCoursesList);


            }
    }

