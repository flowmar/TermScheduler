package com.ebookfrenzy.termscheduler.courses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ebookfrenzy.termscheduler.R;
import com.ebookfrenzy.termscheduler.databinding.ActivityAddEditCourseBinding;
import com.ebookfrenzy.termscheduler.terms.Term;
import com.ebookfrenzy.termscheduler.terms.TermViewModel;
import com.ebookfrenzy.termscheduler.utilities.CourseAlertReceiver;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/******************
 * Project: TermScheduler
 * Created At: 2023-12-03 11:56PM
 * Created By: flowmar
 *
 *
 *******************/
public class AddEditCourseActivity extends AppCompatActivity
    {
        private static final String TAG = "com.ebookfrenzy.termscheduler.courses.AddEditCourseActivity";

        static final         String EXTRA_COURSE_NAME             =
                TAG + "EXTRA_COURSE_NAME";
        static final         String EXTRA_COURSE_START_DATE       =
                TAG + "EXTRA_COURSE_START_DATE";
        static final         String EXTRA_COURSE_END_DATE         =
                TAG + "EXTRA_COURSE_END_DATE";
        static final         String EXTRA_COURSE_INSTRUCTOR_NAME  =
                TAG + "EXTRA_COURSE_INSTRUCTOR_NAME";
        static final         String EXTRA_COURSE_INSTRUCTOR_EMAIL =
                TAG + "EXTRA_COURSE_INSTRUCTOR_EMAIL";
        static final         String EXTRA_COURSE_INSTRUCTOR_PHONE =
                TAG + "EXTRA_COURSE_INSTRUCTOR_PHONE";
        static final         String EXTRA_COURSE_STATUS           =
                TAG + "EXTRA_COURSE_STATUS";
        static final         String EXTRA_COURSE_TERM_ID          =
                TAG + "EXTRA_COURSE_TERM_ID";
        static final         String EXTRA_COURSE_TERM_NAME        =
                TAG + "EXTRA_COURSE_TERM_NAME";
        static final         String EXTRA_COURSE_NOTE             =
                TAG + "EXTRA_COURSE_NOTE";
        static final         String EXTRA_START_ALARM_DATETIME    =
                TAG + "EXTRA_START_ALARM_DATETIME";
        static final         String EXTRA_END_ALARM_DATETIME      =
                TAG + "EXTRA_END_ALARM_DATETIME";
        static final         String EXTRA_COURSE_ID               =
                TAG + "EXTRA_COURSE_ID";
        private static final String dformat                       = "yyyy-MM-dd";

        private ActivityAddEditCourseBinding binding;
        private TermViewModel                mTermViewModel;
        private List<Term>                   termsList;
        private CourseViewModel              mCourseViewModel;

        private Button buttonSelectCourseStartAlarm;

        private Button buttonSelectCourseEndAlarm;

        private Button buttonCancelCourseStartAlarm;

        private Button buttonCancelCourseEndAlarm;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                binding = ActivityAddEditCourseBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);

                // Change the up navigation icon
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);

                mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
                termsList      = mTermViewModel.getTermsList();

                // Instantiate the button variables for starting and canceling alarms
                buttonSelectCourseStartAlarm = binding.setCourseStartAlarmButton;
                buttonSelectCourseEndAlarm   = binding.setCourseEndAlarmButton;
                buttonCancelCourseStartAlarm = binding.courseStartAlarmCancelButton;
                buttonCancelCourseEndAlarm   = binding.courseEndAlarmCancelButton;

                // Get the Intent that started this activity and check for the course ID extra
                Intent intent = getIntent();
                if (intent.hasExtra(EXTRA_COURSE_ID))
                    {
                        // Change the title in the ActionBar
                        setTitle("Edit Course");

                        // Set the data from the Intent into the UI
                        binding.addEditCourseEditTextCourseName.setText(intent.getStringExtra(EXTRA_COURSE_NAME));
                        binding.addEditCourseTextviewSelectedCourseStartDate.setText(
                                intent.getStringExtra(EXTRA_COURSE_START_DATE));
                        binding.addEditCourseTextviewSelectedCourseEndDate.setText(
                                intent.getStringExtra(EXTRA_COURSE_END_DATE));
                        binding.addEditCourseTextViewInstructorName.setText(
                                intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_NAME));
                        binding.addEditCourseTextViewInstructorEmail.setText(
                                intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_EMAIL));
                        binding.addEditCourseTextViewInstructorPhone.setText(
                                intent.getStringExtra(EXTRA_COURSE_INSTRUCTOR_PHONE));
                        binding.addEditCourseTextViewCourseStatus.setText(
                                intent.getStringExtra(EXTRA_COURSE_STATUS));


                        // Get the termID and termName and concatenate them into a single string
                        String termName     = intent.getStringExtra(EXTRA_COURSE_TERM_NAME);
                        int    termId       = intent.getIntExtra(EXTRA_COURSE_TERM_ID, -1);
                        String fullTermName = termId + "-" + termName;
                        // Set the combined string into the UI
                        binding.addEditCourseTextviewSelectedTerm.setText(fullTermName);

                        // If the intent has a note, set it into the UI
                        if (intent.hasExtra(EXTRA_COURSE_NOTE))
                            {
                                binding.addEditCourseTextViewCourseNote.setText(
                                        intent.getStringExtra(EXTRA_COURSE_NOTE));
                            }

                        if (intent.hasExtra(EXTRA_START_ALARM_DATETIME) && intent.getStringExtra(EXTRA_START_ALARM_DATETIME) != null)
                            {
                                binding.courseStartAlarmDateTimeTextView.setText(
                                        intent.getStringExtra(EXTRA_START_ALARM_DATETIME));
                                binding.courseStartAlarmCancelButton.setVisibility(View.VISIBLE);
                                binding.courseStartAlarmCancelButton.setEnabled(true);
                                setCourseStartAlarmCancelButtonClickListener();
                            }


                        if (intent.hasExtra(EXTRA_END_ALARM_DATETIME) && intent.getStringExtra(EXTRA_END_ALARM_DATETIME) != null)
                            {
                                binding.courseEndAlarmDateTimeTextView.setText(
                                        intent.getStringExtra(EXTRA_END_ALARM_DATETIME));
                                binding.courseEndAlarmCancelButton.setVisibility(View.VISIBLE);
                                binding.courseEndAlarmCancelButton.setEnabled(true);
                                setCourseEndAlarmCancelButtonClickListener();
                            }
                    }
                else
                    {
                        // Change the title in the ActionBar
                        setTitle("Add Course");
                    }


                // Set up the DatePickers
                setUpStartDatePicker();
                setUpEndDatePicker();

                // Set up Course Start Alarm Pickers
                setUpCourseStartAlarmPicker();
                setUpCourseEndAlarmPicker();

                // Set up Course End Alarm Pickers

                // Set up the Menus
                setUpTermMenu();
                setUpCourseStatusMenu();
            }

        private void setCourseEndAlarmCancelButtonClickListener()
            {
                buttonCancelCourseEndAlarm.setOnClickListener(view ->
                                                                  {
                                                                      binding.courseEndAlarmDateTimeTextView.setText(
                                                                              null);
                                                                      buttonCancelCourseEndAlarm.setVisibility(
                                                                              View.INVISIBLE);
                                                                      buttonCancelCourseEndAlarm.setEnabled(
                                                                              false);
                                                                  });

            }

        private void setCourseStartAlarmCancelButtonClickListener()
            {
                buttonCancelCourseStartAlarm.setOnClickListener(view ->
                                                                    {
                                                                        binding.courseStartAlarmDateTimeTextView.setText(
                                                                                null);
                                                                        buttonCancelCourseStartAlarm.setVisibility(
                                                                                View.INVISIBLE);
                                                                        buttonCancelCourseStartAlarm.setEnabled(
                                                                                false);
                                                                    });
            }

        private void setUpCourseEndAlarmPicker()
            {
                // Set a click listener on the courseEndAlarmSelectButton
                buttonSelectCourseEndAlarm.setOnClickListener(view -> showCourseEndDateTimePicker());
            }

        private void showCourseEndDateTimePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> courseEndAlarmDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                courseEndAlarmDatePickerBuilder.setTitleText("Select a course end alarm date");
                // Build the courseEndAlarmDatePicker
                MaterialDatePicker<Long> courseEndAlarmDatePicker = courseEndAlarmDatePickerBuilder.build();
                // Show the date picker
                courseEndAlarmDatePicker.show(getSupportFragmentManager(), "COURSE_END_ALARM_PICKER");

                // Add a listener to the courseEndAlarmDatePicker's OK button
                courseEndAlarmDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                // Get a Calendar instance in Central Time
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                Date myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                SimpleDateFormat sdf           = new SimpleDateFormat(dformat, Locale.US);
                                String           formattedDate = sdf.format(myDate);
                                // binding.courseStartAlarmDateTimeTextView.setText(formattedDate);
                                showCourseEndAlarmTimePicker(formattedDate);
                            }

                        private void showCourseEndAlarmTimePicker(String formattedDate)
                            {
                                // Create a MaterialTimePicker Builder instance
                                MaterialTimePicker.Builder courseEndAlarmTimePickerBuilder = new MaterialTimePicker.Builder();
                                // Set the title text
                                courseEndAlarmTimePickerBuilder.setTitleText("Select a course end alarm time");
                                // Set the input mode
                                courseEndAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
                                // Set the Time format
                                courseEndAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
                                // Build the courseEndAlarmTimePicker
                                MaterialTimePicker courseEndTimeAlarmPicker =
                                        courseEndAlarmTimePickerBuilder.build();
                                // Show the time picker
                                courseEndTimeAlarmPicker.show(getSupportFragmentManager(),
                                                              "COURSE_END_ALARM_TIME_PICKER"
                                                             );

                                courseEndTimeAlarmPicker.addOnPositiveButtonClickListener(
                                        new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View v)
                                                    {
                                                        int hour   = courseEndTimeAlarmPicker.getHour();
                                                        int minute = courseEndTimeAlarmPicker.getMinute();
                                                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d",
                                                                                             hour, minute
                                                                                            );
                                                        String formattedDateTime = formattedDate + " " + formattedTime;
                                                        binding.courseEndAlarmDateTimeTextView.setText(formattedDateTime);
                                                        buttonCancelCourseEndAlarm.setVisibility(View.VISIBLE);
                                                        buttonCancelCourseEndAlarm.setEnabled(true);
                                                        setCourseEndAlarmCancelButtonClickListener();
                                                    }

                                                private void setCourseEndAlarmCancelButtonClickListener()
                                                    {
                                                        binding.courseEndAlarmCancelButton.setOnClickListener(view ->
                                                                                                                  {
                                                                                                                      binding.courseEndAlarmDateTimeTextView.setText(
                                                                                                                              null);
                                                                                                                      buttonCancelCourseEndAlarm.setVisibility(
                                                                                                                              View.INVISIBLE);
                                                                                                                      buttonCancelCourseEndAlarm.setEnabled(
                                                                                                                              false);
                                                                                                                  });
                                                    }
                                            });

                            }
                    });
            }

        private void setUpCourseStartAlarmPicker()
            {

                // Set a click listener on the courseStartAlarmSelectButton
                buttonSelectCourseStartAlarm.setOnClickListener(view -> showCourseStartDateTimePicker());
            }

        private void showCourseStartDateTimePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> courseStartAlarmDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                courseStartAlarmDatePickerBuilder.setTitleText("Select a course start alarm date");
                // Build the courseStartAlarmDatePicker
                MaterialDatePicker<Long> courseStartAlarmDatePicker = courseStartAlarmDatePickerBuilder.build();
                // Show the date picker
                courseStartAlarmDatePicker.show(getSupportFragmentManager(), "COURSE_START_ALARM_PICKER");

                // Add a listener to the courseStartAlarmDatePicker's OK button
                courseStartAlarmDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                // Get a Calendar instance in Central Time
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                Date myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                SimpleDateFormat sdf           = new SimpleDateFormat(dformat, Locale.US);
                                String           formattedDate = sdf.format(myDate);
                                // binding.courseStartAlarmDateTimeTextView.setText(formattedDate);
                                showCourseStartAlarmTimePicker(formattedDate);
                            }

                        private void showCourseStartAlarmTimePicker(String formattedDate)
                            {
                                // Create a MaterialTimePicker Builder instance
                                MaterialTimePicker.Builder courseStartAlarmTimePickerBuilder = new MaterialTimePicker.Builder();
                                // Set the title text
                                courseStartAlarmTimePickerBuilder.setTitleText("Select a course start alarm time");
                                // Set the input mode
                                courseStartAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
                                // Set the Time format
                                courseStartAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
                                // Build the courseStartAlarmTimePicker
                                MaterialTimePicker courseStartTimeAlarmPicker =
                                        courseStartAlarmTimePickerBuilder.build();
                                // Show the time picker
                                courseStartTimeAlarmPicker.show(getSupportFragmentManager(),
                                                                "COURSE_START_ALARM_TIME_PICKER"
                                                               );

                                courseStartTimeAlarmPicker.addOnPositiveButtonClickListener(
                                        new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View v)
                                                    {
                                                        int hour   = courseStartTimeAlarmPicker.getHour();
                                                        int minute = courseStartTimeAlarmPicker.getMinute();
                                                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d",
                                                                                             hour, minute
                                                                                            );
                                                        String formattedDateTime = formattedDate + " " + formattedTime;
                                                        binding.courseStartAlarmDateTimeTextView.setText(formattedDateTime);
                                                        binding.courseStartAlarmCancelButton.setVisibility(View.VISIBLE);
                                                        binding.courseStartAlarmCancelButton.setEnabled(true);
                                                        setCourseStartAlarmCancelButtonClickListener();
                                                    }

                                                private void setCourseStartAlarmCancelButtonClickListener()
                                                    {
                                                        binding.courseStartAlarmCancelButton.setOnClickListener(view ->
                                                                                                                    {
                                                                                                                        binding.courseStartAlarmDateTimeTextView.setText(
                                                                                                                                null);
                                                                                                                        binding.courseStartAlarmCancelButton.setVisibility(
                                                                                                                                View.INVISIBLE);
                                                                                                                        binding.courseStartAlarmCancelButton.setEnabled(
                                                                                                                                false);
                                                                                                                    });
                                                    }
                                            });

                            }
                    });

            }

        private void setUpCourseStatusMenu()
            {
                // Add all course statuses to the menu
                // assert binding.addEditCourseTextViewCourseStatus != null;
                binding.addEditCourseTextViewCourseStatus.setSimpleItems(
                        getResources().getStringArray(R.array.course_statuses));
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
                        // assert binding.addEditCourseTextviewSelectedTerm != null;
                        binding.addEditCourseTextviewSelectedTerm.setSimpleItems(
                                concatTerms.toArray(new String[concatTerms.size()]));
                    }
            }

        private void setUpEndDatePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> endDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                endDatePickerBuilder.setTitleText("Select a course end date");
                // Build the endDatePicker
                MaterialDatePicker<Long> endDatePicker = endDatePickerBuilder.build();

                // Set a click listener on the endDate select button
                Objects.requireNonNull(binding.buttonSelectCourseEndDate).setOnClickListener(view ->
                                                                                                     // Show the date picker
                                                                                                     endDatePicker.show(
                                                                                                             getSupportFragmentManager(),
                                                                                                             "END_DATE_PICKER"
                                                                                                                       ));

                // Add a listener to the End Date Picker's OK button
                endDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>()
                    {
                        @Override
                        public void onPositiveButtonClick(Long selection)
                            {
                                //
                                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
                                calendar.setTimeInMillis(selection);
                                Date myDate = calendar.getTime();
                                myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                SimpleDateFormat sdf           = new SimpleDateFormat(dformat, Locale.US);
                                String           formattedDate = sdf.format(myDate);
                                binding.addEditCourseTextviewSelectedCourseEndDate.setText(
                                        formattedDate);
                            }
                    });

            }

        private void setUpStartDatePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder<Long> startDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                startDatePickerBuilder.setTitleText("Select a course start date");
                // Build the startDatePicker
                MaterialDatePicker<Long> startDatePicker = startDatePickerBuilder.build();

                // Set a click listener on the startDate select button
                binding.buttonSelectCourseStartDate.setOnClickListener(view ->

                                                                               // Show the date picker
                                                                               startDatePicker.show(
                                                                                       getSupportFragmentManager(),
                                                                                       "START_DATE_PICKER"
                                                                                                   )
                                                                      );

                // Add a listener to the Start Date Picker's OK button
                startDatePicker.addOnPositiveButtonClickListener(selection ->
                                                                     {
                                                                         // Get a Calendar instance in Central Time
                                                                         Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"
                                                                                                                                      ));
                                                                         calendar.setTimeInMillis(selection);
                                                                         Date myDate = calendar.getTime();
                                                                         myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
                                                                         SimpleDateFormat sdf = new SimpleDateFormat(dformat
                                                                                 , Locale.US);
                                                                         String formattedDate = sdf.format(myDate);
                                                                         binding.addEditCourseTextviewSelectedCourseStartDate.setText(
                                                                                 formattedDate);
                                                                     }
                                                                );
            }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
            {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.add_course_menu, menu);
                return true;
            }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
            {
                if (item.getItemId() == R.id.save_course)
                    {
                        saveCourse();
                        return true;
                    }
                return super.onOptionsItemSelected(item);
            }

        private void saveCourse()
            {
                // Using the binding, extract the data entered by the user
                String courseName      = binding.addEditCourseEditTextCourseName.getText().toString();
                String courseStartDate = binding.addEditCourseTextviewSelectedCourseStartDate.getText().toString();
                String courseEndDate   = binding.addEditCourseTextviewSelectedCourseEndDate.getText().toString();
                Log.i("Converted Date: ", courseStartDate + " start - end " + courseEndDate);
                String   selectedTerm = binding.addEditCourseTextviewSelectedTerm.getText().toString();
                String[] termSplit    = selectedTerm.split("-");
                int      termId       = Integer.parseInt(termSplit[0]);
                String   termName     = termSplit[1];

                String instructorName  = binding.addEditCourseTextViewInstructorName.getText().toString();
                String instructorPhone = binding.addEditCourseTextViewInstructorPhone.getText().toString();
                String instructorEmail = binding.addEditCourseTextViewInstructorEmail.getText().toString();
                String courseStatus    = binding.addEditCourseTextViewCourseStatus.getText().toString();
                String courseNote      = binding.addEditCourseTextViewCourseNote.getText().toString();

                //! TODO: Run a function to set an Alarm using AlarmManager if these values are not equal to null or ""
                //! TODO: Edit Assessment Model
                //! TODO: Remove all old Assessment model code dealing with alarms
                //! TODO: Edit Add/Edit Assessment Activity to include the new alarm fields
                //! TODO: Edit the AssessmentViewModel to include the new alarm fields
                //! TODO: Set up click Listeners on the Add/Edit Assessment Activity to save to database, and to set alarms.
                String courseStartAlarmDatetime = binding.courseStartAlarmDateTimeTextView.getText().toString();
                String courseEndAlarmDatetime   = binding.courseEndAlarmDateTimeTextView.getText().toString();
                Log.i("COURSE START ALARM", courseStartAlarmDatetime);
                Log.i("COURSE END ALARM", courseEndAlarmDatetime);


                // Check to make sure that all the fields are filled out
                if (courseName.trim().isEmpty() || courseStartDate.trim().isEmpty() || courseEndDate.trim().isEmpty() ||
                        selectedTerm.trim().isEmpty() || instructorName.trim().isEmpty() ||
                        instructorPhone.trim().isEmpty() || instructorEmail.trim().isEmpty() ||
                        courseStatus.trim().isEmpty())
                    {
                        // If any field is empty, display a toast error message
                        Toast.makeText(this, "All fields except course note are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                int id = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);

                if (id != -1)
                    {

                        // Create a new Course object using the data entered by the user
                        Course editedCourse = new Course(courseName, courseStartDate, courseEndDate, courseStatus,
                                                         instructorName, instructorPhone, instructorEmail, courseNote,
                                                         termName, termId, courseStartAlarmDatetime, courseEndAlarmDatetime
                        );
                        editedCourse.setId(id);

                        // Get an instance of the CourseViewModel
                        mCourseViewModel = new ViewModelProvider(this).get(
                                com.ebookfrenzy.termscheduler.courses.CourseViewModel.class);

                        // Update the course in the database
                        mCourseViewModel.update(editedCourse);

                    }
                else
                    {
                        // Create a new object using the data entered by the user
                        Course newCourse = new Course(courseName, courseStartDate, courseEndDate, courseStatus,
                                                      instructorName, instructorPhone, instructorEmail, courseNote,
                                                      termName, termId, courseStartAlarmDatetime, courseEndAlarmDatetime
                        );

                        // If the courseStartAlarmDatetime or courseEndAlarmDatetime is not null, schedule the course alert
                        if ((courseStartAlarmDatetime != "") || (courseEndAlarmDatetime != ""))
                            {
                                scheduleCourseAlert(getApplicationContext(), newCourse);
                            }


                        // Get an instance of the CourseViewModel
                        mCourseViewModel = new ViewModelProvider(this).get(
                                com.ebookfrenzy.termscheduler.courses.CourseViewModel.class);
                        // Add the new Course to the database
                        mCourseViewModel.insert(newCourse);

                    }
                finish();
            }

        private void scheduleCourseAlert(Context context, Course course)
            {

                // Get an instance of the alarmManager service
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                // Create an Intent to send to the CourseAlertReceiver
                Intent startIntent = new Intent(this, CourseAlertReceiver.class);
                // Create an instance of PendingIntent which will be used to send the broadcast to the CourseAlertReceiver
                PendingIntent startAlarmIntent = PendingIntent.getBroadcast(context, String.valueOf(course.getId()).hashCode(), startIntent,
                                                                            PendingIntent.FLAG_UPDATE_CURRENT
                                                                           );

                String           triggerStartDateTimeString = course.getCourseStartAlarmDatetime();
                SimpleDateFormat sdf                        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                long             triggerStartDateTime;
                try
                    {
                        Date dateString = sdf.parse(triggerStartDateTimeString);
                        triggerStartDateTime = dateString.getTime();
                        Log.i("Set Alarm", "Trigger Date Time: " + triggerStartDateTime);

                        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerStartDateTime, startAlarmIntent);
                        alarmManager.setAlarmClock(alarmClockInfo, startAlarmIntent);
                    }
                catch (ParseException e)
                    {
                        Log.e("Set Alarm", e.getMessage());
                    }


            }


    }
