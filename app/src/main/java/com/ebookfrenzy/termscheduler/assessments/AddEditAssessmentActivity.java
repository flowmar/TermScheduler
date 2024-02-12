package com.ebookfrenzy.termscheduler.assessments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ebookfrenzy.termscheduler.R;
import com.ebookfrenzy.termscheduler.courses.Course;
import com.ebookfrenzy.termscheduler.courses.CourseViewModel;
import com.ebookfrenzy.termscheduler.databinding.ActivityAddEditAssessmentBinding;
import com.ebookfrenzy.termscheduler.terms.Term;
import com.ebookfrenzy.termscheduler.terms.TermViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AddEditAssessmentActivity extends AppCompatActivity
    {

        static final         String                           EXTRA_ASSESSMENT_START_ALARM_DATE_TIME
                                                                                        = "com.ebookfrenzy.termscheduler.assessments" +
                ".AddEditAssessmentActivity.EXTRA_ASSESSMENT_START_ALARM_DATE_TIME";
        static final         String                           EXTRA_ASSESSMENT_ID       =
                "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivityEXTRA_ASSESSMENT_ID";
        static final         String                           EXTRA_ASSESSMENT_TITLE    =
                "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE";
        static final         String                           EXTRA_ASSESSMENT_TYPE     =
                "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE";
        static final         String                           EXTRA_ASSESSMENT_START_DATE
                                                                                        =
                "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity" +
                        ".EXTRA_ASSESSMENT_START_DATE";
        static final         String                           EXTRA_ASSESSMENT_END_DATE =
                "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity.EXTRA_ASSESSMENT_END_DATE";
        static final         String                           EXTRA_ASSESSMENT_COURSE_ID
                                                                                        = "com.ebookfrenzy.termscheduler.assessments" +
                ".AddEditAssessmentActivity.EXTRA_ASSESSMENT_COURSE_ID";
        static final         String                           EXTRA_ASSESSMENT_COURSE_NAME
                                                                                        = "com.ebookfrenzy.termscheduler.assessments" +
                ".AddEditAssessmentActivity.EXTRA_ASSESSMENT_COURSE_NAME";
        static final         String                           EXTRA_ASSESSMENT_TERM_ID
                                                                                        = "com.ebookfrenzy.termscheduler.assessments" +
                ".AddEditAssessmentActivity.EXTRA_ASSESSMENT_TERM_ID";
        static final         String                           EXTRA_ASSESSMENT_TERM_NAME
                                                                                        = "com.ebookfrenzy.termscheduler.assessments" +
                ".AddEditAssessmentActivity.EXTRA_ASSESSMENT_TERM_NAME";
        static final         String                           EXTRA_ASSESSMENT_END_ALARM_DATE_TIME
                                                                                        =
                "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity" +
                        ".EXTRA_ASSESSMENT_END_ALARM_DATE_TIME";
        private static final String                           END_DATE_PICKER_TAG       = "END_DATE_PICKER";
        private static final String                           START_DATE_PICKER_TAG     = "START_DATE_PICKER";
        private              ActivityAddEditAssessmentBinding binding;

        private List<Term> termsList;

        private List<Course> coursesList;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                binding = ActivityAddEditAssessmentBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);

                // Change the up navigation icon
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);

                // Get an instance of the TermViewModel
                TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
                // Get a list of all the terms
                termsList = termViewModel.getTermsList();

                // Get an instance of the CourseViewModel
                CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
                // Get a list of all the courses
                coursesList = courseViewModel.getCoursesList();


                // Get the Intent that started this activity and check for the assessment ID extra
                Intent intent = getIntent();
                if (intent.hasExtra(EXTRA_ASSESSMENT_ID))
                    {
                        // Change the title in the ActionBar
                        setTitle("Edit Assessment");
                        binding.addEditAssessmentTitle.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TITLE));
                        binding.addAssessmentSelectedType.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TYPE));
                        binding.addEditAssessmentStartDate.setText(intent.getStringExtra(EXTRA_ASSESSMENT_START_DATE));
                        binding.addEditAssessmentEndDate.setText(intent.getStringExtra(EXTRA_ASSESSMENT_END_DATE));

                        // Get the courseId and courseName from the Intent and concatenate them
                        String courseId     = String.valueOf(intent.getIntExtra(EXTRA_ASSESSMENT_COURSE_ID, -1));
                        String courseName   = intent.getStringExtra(EXTRA_ASSESSMENT_COURSE_NAME);
                        String concatCourse = courseId + "-" + courseName;
                        // Set the concatenated course in the UI
                        binding.addEditAssessmentSelectedCourse.setText(concatCourse);

                        // Get the termId and termName from the Intent and concatenate them
                        String termId     = String.valueOf(intent.getIntExtra(EXTRA_ASSESSMENT_TERM_ID, -1));
                        String termName   = intent.getStringExtra(EXTRA_ASSESSMENT_TERM_NAME);
                        String concatTerm = termId + "-" + termName;
                        // Set the concatenated term in the UI
                        binding.addEditAssessmentSelectedTerm.setText(concatTerm);

                        // Get the start and end alarm date times from the Intent
                        if (intent.hasExtra(EXTRA_ASSESSMENT_START_ALARM_DATE_TIME) && intent.getStringExtra(
                                EXTRA_ASSESSMENT_START_ALARM_DATE_TIME) != null)
                            {
                                binding.assessmentStartAlarmDateTimeTextView.setText(
                                        intent.getStringExtra(EXTRA_ASSESSMENT_START_ALARM_DATE_TIME));
                                binding.assessmentStartAlarmCancelButton.setVisibility(View.VISIBLE);
                                binding.assessmentStartAlarmCancelButton.setEnabled(true);
                                setCourseStartAlarmCancelButtonListener();
                            }

                        if (intent.hasExtra(EXTRA_ASSESSMENT_END_ALARM_DATE_TIME) && intent.getStringExtra(EXTRA_ASSESSMENT_START_ALARM_DATE_TIME) !=
                                null)
                            {
                                binding.assessmentEndAlarmDateTimeTextView.setText(
                                        intent.getStringExtra(EXTRA_ASSESSMENT_END_ALARM_DATE_TIME));
                                binding.assessmentEndAlarmCancelButton.setVisibility(View.VISIBLE);
                                binding.assessmentEndAlarmCancelButton.setEnabled(true);
                                setCourseEndAlarmCancelButtonListener();

                            }
                    }
                else
                    {
                        // Change the title in the ActionBar
                        setTitle("Add Assessment");
                    }


                // Set up the DatePicker
                setUpStartDatePicker();
                setUpEndDatePicker();

                // Set up the Alarm DateTime Pickers
                setUpStartAlarmDateTimePicker();
                setUpEndAlarmDateTimePicker();

                // Set up the menus
                setUpTermMenu();
                setUpCourseMenu();
                setUpTypeMenu();


            }

        private void setCourseEndAlarmCancelButtonListener()
            {
                binding.assessmentEndAlarmCancelButton.setOnClickListener(view ->
                                                                              {
                                                                                  binding.assessmentEndAlarmDateTimeTextView.setText(
                                                                                          null);
                                                                                  binding.assessmentEndAlarmCancelButton.setVisibility(
                                                                                          View.INVISIBLE);
                                                                                  binding.assessmentEndAlarmCancelButton.setEnabled(
                                                                                          false);
                                                                              });
            }

        private void setCourseStartAlarmCancelButtonListener()
            {
                binding.assessmentStartAlarmCancelButton.setOnClickListener(view ->
                                                                                {
                                                                                    binding.assessmentStartAlarmDateTimeTextView.setText(
                                                                                            null);
                                                                                    binding.assessmentStartAlarmCancelButton.setVisibility(
                                                                                            View.INVISIBLE);
                                                                                    binding.assessmentStartAlarmCancelButton.setEnabled(
                                                                                            false);
                                                                                });

            }

        private void setUpEndAlarmDateTimePicker()
            {
                binding.setAssessmentEndAlarmButton.setOnClickListener(view ->
                                                                               showAssessmentEndAlarmDateTimePicker());
            }

        private void showAssessmentEndAlarmDateTimePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> endAlarmDateTimePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                endAlarmDateTimePickerBuilder.setTitleText("Select a assessment end date");
                // Build the endDatePicker
                MaterialDatePicker<Long> endAlarmDateTimePicker = endAlarmDateTimePickerBuilder.build();

                // Show the date picker
                endAlarmDateTimePicker.show(
                        getSupportFragmentManager(),
                        END_DATE_PICKER_TAG
                                           );

                // Add a listener to the End Date Picker's OK button
                endAlarmDateTimePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                SimpleDateFormat sdf    = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                                Date             myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                String formattedDate = sdf.format(myDate);
                                // binding.addEditAssessmentEndDate.setText(formattedDate);
                                showAssessmentEndAlarmTimePicker(formattedDate);
                            }

                        private void showAssessmentEndAlarmTimePicker(String formattedDate)
                            {
                                MaterialTimePicker.Builder assessmentEndAlarmTimePickerBuilder = new MaterialTimePicker.Builder();
                                // Set the title text
                                assessmentEndAlarmTimePickerBuilder.setTitleText("Select a assessment end date");
                                // Set the input mode
                                assessmentEndAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
                                // Set the Time format
                                assessmentEndAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
                                // Build the startTimePicker
                                MaterialTimePicker assessmentEndAlarmTimePicker = assessmentEndAlarmTimePickerBuilder.build();

                                // Show the time picker
                                assessmentEndAlarmTimePicker.show(
                                        getSupportFragmentManager(),
                                        "END_DATE_PICKER"
                                                                 );

                                // Add a listener to the End Date Picker's OK button
                                assessmentEndAlarmTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                            {
                                                int hour   = assessmentEndAlarmTimePicker.getHour();
                                                int minute = assessmentEndAlarmTimePicker.getMinute();
                                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour,
                                                                                     minute
                                                                                    );
                                                String formattedDateTime = formattedDate + " " + formattedTime;
                                                binding.assessmentEndAlarmDateTimeTextView.setText(formattedDateTime);
                                                binding.assessmentEndAlarmCancelButton.setVisibility(View.VISIBLE);
                                                binding.assessmentEndAlarmCancelButton.setEnabled(true);
                                                setCourseEndAlarmCancelButtonListener();
                                            }

                                        private void setCourseEndAlarmCancelButtonListener()
                                            {
                                                binding.assessmentEndAlarmCancelButton.setOnClickListener(view ->
                                                                                                              {
                                                                                                                  binding.assessmentEndAlarmDateTimeTextView.setText(
                                                                                                                          null);
                                                                                                                  binding.assessmentEndAlarmCancelButton.setVisibility(
                                                                                                                          View.INVISIBLE);
                                                                                                                  binding.assessmentEndAlarmCancelButton.setEnabled(
                                                                                                                          false);
                                                                                                              });
                                            }
                                    });
                            }
                    });
            }

        private void setUpStartAlarmDateTimePicker()
            {
                binding.setAssessmentStartAlarmButton.setOnClickListener(view ->
                                                                                 showAssessmentStartAlarmDateTimePicker());
            }

        private void showAssessmentStartAlarmDateTimePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> startAlarmDateTimePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                startAlarmDateTimePickerBuilder.setTitleText("Select a assessment start date");
                // Build the startDatePicker
                MaterialDatePicker<Long> startAlarmDateTimePicker = startAlarmDateTimePickerBuilder.build();


                // Show the date picker
                startAlarmDateTimePicker.show(
                        getSupportFragmentManager(),
                        "START_DATE_PICKER"
                                             );


                // Add a listener to the Start Date Picker's OK button
                startAlarmDateTimePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                SimpleDateFormat sdf    = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                                Date             myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                String formattedDate = sdf.format(myDate);
                                // binding.addEditAssessmentStartDate.setText(formattedDate);
                                showAssessmentStartAlarmTimePicker(formattedDate);
                            }

                        private void showAssessmentStartAlarmTimePicker(String formattedDate)
                            {
                                MaterialTimePicker.Builder assessmentStartAlarmTimePickerBuilder = new MaterialTimePicker.Builder();
                                // Set the title text
                                assessmentStartAlarmTimePickerBuilder.setTitleText("Select a assessment start date");
                                // Set the input mode
                                assessmentStartAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
                                // Set the Time format
                                assessmentStartAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
                                // Build the startTimePicker
                                MaterialTimePicker assessmentStartAlarmTimePicker = assessmentStartAlarmTimePickerBuilder.build();


                                // Show the date picker
                                assessmentStartAlarmTimePicker.show(
                                        getSupportFragmentManager(),
                                        "START_DATE_PICKER"
                                                                   );

                                // Add a listener to the Start Date Picker's OK button
                                assessmentStartAlarmTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                            {
                                                int hour   = assessmentStartAlarmTimePicker.getHour();
                                                int minute = assessmentStartAlarmTimePicker.getMinute();
                                                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour,
                                                                                     minute
                                                                                    );
                                                String formattedDateTime = formattedDate + " " + formattedTime;
                                                binding.assessmentStartAlarmDateTimeTextView.setText(formattedDateTime);
                                                binding.assessmentStartAlarmCancelButton.setVisibility(View.VISIBLE);
                                                binding.assessmentStartAlarmCancelButton.setEnabled(true);
                                                setCourseStartAlarmCancelButtonListener();
                                            }

                                        private void setCourseStartAlarmCancelButtonListener()
                                            {
                                                binding.assessmentStartAlarmCancelButton.setOnClickListener(view ->
                                                                                                                {
                                                                                                                    binding.assessmentStartAlarmDateTimeTextView.setText(
                                                                                                                            null);
                                                                                                                    binding.assessmentStartAlarmCancelButton.setVisibility(
                                                                                                                            View.INVISIBLE);
                                                                                                                    binding.assessmentStartAlarmCancelButton.setEnabled(
                                                                                                                            false);
                                                                                                                });
                                            }
                                    });
                            }
                    });
            }

        private void setUpStartDatePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> startDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                startDatePickerBuilder.setTitleText("Select a assessment start date");
                // Build the startDatePicker
                MaterialDatePicker<Long> startDatePicker = startDatePickerBuilder.build();

                // Set a click listener on the startDate select button
                binding.buttonSelectAssessmentStartDate.setOnClickListener(view ->
                                                                               {
                                                                                   // Show the date picker
                                                                                   startDatePicker.show(
                                                                                           getSupportFragmentManager(),
                                                                                           START_DATE_PICKER_TAG
                                                                                                       );
                                                                               });

                // Add a listener to the Start Date Picker's OK button
                startDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                SimpleDateFormat sdf    = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                                Date             myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                String formattedDate = sdf.format(myDate);
                                binding.addEditAssessmentStartDate.setText(
                                        formattedDate);
                            }
                    });
            }

        private void setUpTypeMenu()
            {
                binding.addAssessmentSelectedType.setSimpleItems(
                        getResources().getStringArray(R.array.assessment_types));
            }

        private void setUpCourseMenu()
            {
                ArrayList<String> concatCourses = new ArrayList<>();

                // Get the course ID and the Course Name and concatenate them into a single string
                for (Course course : coursesList)
                    {
                        concatCourses.add(course.getId() + "-" + course.getCourseName());
                    }

                if (!concatCourses.isEmpty())
                    {
                        // Add all the courses to the menu
                        binding.addEditAssessmentSelectedCourse.setSimpleItems(
                                concatCourses.toArray(new String[concatCourses.size()]));
                    }
            }

        private void setUpTermMenu()
            {
                ArrayList<String> concatTerms = new ArrayList<>();

                // Get the term ID and the Term Name and concatenate them into a single string
                for (Term term : termsList)
                    {
                        concatTerms.add(term.getId() + "-" + term.getName());
                    }

                if (!concatTerms.isEmpty())
                    {
                        // Add all the terms to the menu
                        binding.addEditAssessmentSelectedTerm.setSimpleItems(
                                concatTerms.toArray(new String[concatTerms.size()]));
                    }
            }

        private void setUpEndDatePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder endDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                endDatePickerBuilder.setTitleText("Select a assessment end date");
                // Build the endDatePicker
                MaterialDatePicker<Long> endDatePicker = endDatePickerBuilder.build();

                // Set a click listener on the endDate select button
                binding.buttonSelectAssessmentEndDate.setOnClickListener(view ->
                                                                             {
                                                                                 // Show the date picker
                                                                                 endDatePicker.show(
                                                                                         getSupportFragmentManager(),
                                                                                         END_DATE_PICKER_TAG
                                                                                                   );
                                                                             });

                // Add a listener to the End Date Picker's OK button
                endDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                SimpleDateFormat sdf    = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                                Date             myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                String formattedDate = sdf.format(myDate);
                                binding.addEditAssessmentEndDate.setText(
                                        formattedDate);
                            }
                    });
            }


        @Override
        public boolean onCreateOptionsMenu(Menu menu)
            {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.add_assessment_menu, menu);
                return true;
            }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
            {
                if (item.getItemId() == R.id.save_assessment)
                    {
                        saveAssessment();
                        return true;
                    }
                return super.onOptionsItemSelected(item);
            }

        private void saveAssessment()
            {
                // Using the binding, extract the data entered by the user
                String assessmentTitle = Objects.requireNonNull(binding.addEditAssessmentTitle.getText())
                                                .toString();
                String assessmentType      = binding.addAssessmentSelectedType.getText().toString();
                String assessmentStartDate = binding.addEditAssessmentStartDate.getText().toString();
                String assessmentEndDate   = binding.addEditAssessmentEndDate.getText().toString();
                // Get the courseID and courseName from the UI
                String   selectedCourse = binding.addEditAssessmentSelectedCourse.getText().toString();
                String[] splitCourse    = selectedCourse.split("-");
                int      courseId       = Integer.parseInt(splitCourse[0]);
                String
                        courseName = splitCourse[1];

                // Get the termID and termName from the UI
                String   selectedTerm = binding.addEditAssessmentSelectedTerm.getText().toString();
                String[] splitTerm    = selectedTerm.split("-");
                int      termId       = Integer.parseInt(splitTerm[0]);
                String   termName     = splitTerm[1];


                // Check to make sure that all the fields are filled out
                if (assessmentTitle.trim().isEmpty() || assessmentType.trim().isEmpty() ||
                        assessmentEndDate.trim().isEmpty() || String.valueOf(courseId).trim().isEmpty() ||
                        String.valueOf(termId).trim().isEmpty() || courseName.trim().isEmpty() ||
                        termName.trim().isEmpty())
                    {
                        // If any field is empty, display a toast error message
                        Toast.makeText(this, "All fields are required except for alarms", Toast.LENGTH_SHORT).show();
                        return;
                    }

                int                 id = getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, -1);
                AssessmentViewModel assessmentViewModel;

                String assessmentStartAlarmDatetime = binding.assessmentStartAlarmDateTimeTextView.getText().toString();
                String assessmentEndAlarmDatetime   = binding.assessmentEndAlarmDateTimeTextView.getText().toString();

                // boolean isStartAlarmOn = this.binding.assessmentStartAlarmToggle.isActivated();
                // boolean isEndAlarmOn   = this.binding.assessmentEndAlarmToggle.isActivated();

                if (id != -1)
                    {
                        // Create a new Assessment object using the data entered by the user
                        Assessment editedAssessment = new Assessment(assessmentType, assessmentTitle, assessmentStartDate,
                                                                     assessmentEndDate, courseId, courseName, termId,
                                                                     termName, assessmentStartAlarmDatetime,
                                                                     assessmentEndAlarmDatetime
                        );

                        editedAssessment.setId(id);
                        // Get an instance of the AssessmentViewModel
                        assessmentViewModel = new ViewModelProvider(this).get(
                                com.ebookfrenzy.termscheduler.assessments.AssessmentViewModel.class);
                        // Update the assessment in the database
                        assessmentViewModel.update(editedAssessment);
                    }
                else
                    {
                        // Create a new     object using the data entered by the user
                        Assessment newAssessment = new Assessment(assessmentType, assessmentTitle, assessmentStartDate,
                                                                  assessmentEndDate, courseId, courseName, termId,
                                                                  termName, assessmentStartAlarmDatetime, assessmentEndAlarmDatetime
                        );
                        // Get an instance of the AssessmentViewModel
                        assessmentViewModel = new ViewModelProvider(this).get(
                                com.ebookfrenzy.termscheduler.assessments.AssessmentViewModel.class);
                        // Add the new Assessment to the database
                        assessmentViewModel.insert(this, newAssessment, courseId);
                    }
                // Close the activity
                finish();
            }
    }
