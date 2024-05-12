package com.ebookfrenzy.termscheduler.courses;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import com.ebookfrenzy.termscheduler.R;
import com.ebookfrenzy.termscheduler.databinding.ActivityAddEditCourseBinding;
import com.ebookfrenzy.termscheduler.terms.Term;
import com.ebookfrenzy.termscheduler.terms.TermViewModel;
import com.ebookfrenzy.termscheduler.utilities.CourseAlertReceiver;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
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
 */
public class AddEditCourseActivity extends AppCompatActivity {

  /** The {@code EXTRA_COURSE_NAME} */
  static final String EXTRA_COURSE_NAME = TAG + "EXTRA_COURSE_NAME";
  /** The {@code EXTRA_COURSE_START_DATE} */
  static final String EXTRA_COURSE_START_DATE = TAG + "EXTRA_COURSE_START_DATE";
  /** The {@code EXTRA_COURSE_END_DATE} */
  static final String EXTRA_COURSE_END_DATE = TAG + "EXTRA_COURSE_END_DATE";
  /** The {@code EXTRA_COURSE_INSTRUCTOR_NAME} */
  static final String EXTRA_COURSE_INSTRUCTOR_NAME = TAG + "EXTRA_COURSE_INSTRUCTOR_NAME";
  /** The {@code EXTRA_COURSE_INSTRUCTOR_EMAIL} */
  static final String EXTRA_COURSE_INSTRUCTOR_EMAIL = TAG + "EXTRA_COURSE_INSTRUCTOR_EMAIL";
  /** The {@code EXTRA_COURSE_INSTRUCTOR_PHONE} */
  static final String EXTRA_COURSE_INSTRUCTOR_PHONE = TAG + "EXTRA_COURSE_INSTRUCTOR_PHONE";
  /** The {@code EXTRA_COURSE_STATUS} */
  static final String EXTRA_COURSE_STATUS = TAG + "EXTRA_COURSE_STATUS";
  /** The {@code EXTRA_COURSE_TERM_ID} */
  static final String EXTRA_COURSE_TERM_ID = TAG + "EXTRA_COURSE_TERM_ID";
  /** The {@code EXTRA_COURSE_TERM_NAME} */
  static final String EXTRA_COURSE_TERM_NAME = TAG + "EXTRA_COURSE_TERM_NAME";
  /** The {@code EXTRA_COURSE_NOTE} */
  static final String EXTRA_COURSE_NOTE = TAG + "EXTRA_COURSE_NOTE";
  /** The {@code EXTRA_IS_COURSE_START_ALARM_SET} */
  static final String EXTRA_IS_COURSE_START_ALARM_SET = TAG + "EXTRA_IS_START_ALARM_SET";
  /** The {@code EXTRA_IS_COURSE_END_ALARM_SET} */
  static final String EXTRA_IS_COURSE_END_ALARM_SET = TAG + "EXTRA_IS_END_ALARM_SET";
  /** The {@code EXTRA_COURSE_START_ALARM_DATETIME} */
  static final String EXTRA_COURSE_START_ALARM_DATETIME = TAG + "EXTRA_START_ALARM_DATETIME";
  /** The {@code EXTRA_COURSE_END_ALARM_DATETIME} */
  static final String EXTRA_COURSE_END_ALARM_DATETIME = TAG + "EXTRA_END_ALARM_DATETIME";
  /** The {@code EXTRA_COURSE_ID} */
  static final String EXTRA_COURSE_ID = TAG + "EXTRA_COURSE_ID";
  /** The {@code DFORMAT} */
  private static final String DFORMAT = "yyyy-MM-dd";
  /** The {@code REQUEST_CODE_ALARM_PERMISSION} */
  private static final int REQUEST_CODE_ALARM_PERMISSION = 1238804;
  /** The {@code TAG_FOR_TIME} */
  private static final String TAG_FOR_TIME = "AddEditCourseActivity Alarm Setting";
  /** The {@code TAG} */
  private final String TAG = "com.ebookfrenzy.termscheduler.courses.AddEditCourseActivity";
  /** The {@code courseEndAlarmDatetime} */
  private String courseEndAlarmDatetime;

  /** The {@code courseStartAlarmDatetime} */
  private String courseStartAlarmDatetime;

  /** The {@code binding} */
  private ActivityAddEditCourseBinding binding;

  /** The {@code termsList} */
  private List<Term> termsList;

  /** The {@code buttonSelectCourseStartAlarm} */
  private Button buttonSelectCourseStartAlarm;

  /** The {@code buttonSelectCourseEndAlarm} */
  private Button buttonSelectCourseEndAlarm;

  /** The {@code buttonCancelCourseStartAlarm} */
  private Button buttonCancelCourseStartAlarm;

  /** The {@code buttonCancelCourseEndAlarm} */
  private Button buttonCancelCourseEndAlarm;

  /**
   * On create.
   *
   * @param savedInstanceState the saved instance state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    TermViewModel mTermViewModel;
    super.onCreate(savedInstanceState);
    binding = ActivityAddEditCourseBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    // Change the up navigation icon
    if (getSupportActionBar() != null)
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);

    mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
    termsList = mTermViewModel.getTermsList();

    // Instantiate the button variables for starting and canceling alarms
    buttonSelectCourseStartAlarm = binding.setCourseStartAlarmButton;
    buttonSelectCourseEndAlarm = binding.setCourseEndAlarmButton;
    buttonCancelCourseStartAlarm = binding.courseStartAlarmCancelButton;
    buttonCancelCourseEndAlarm = binding.courseEndAlarmCancelButton;
  }

  /** On start. */
  @Override
  protected void onStart() {
    super.onStart();

    // Get the Intent that started this activity and check for the course ID extra
    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_COURSE_ID)) {
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
      binding.addEditCourseTextViewCourseStatus.setText(intent.getStringExtra(EXTRA_COURSE_STATUS));

      // Get the termID and termName and concatenate them into a single string
      String termName = intent.getStringExtra(EXTRA_COURSE_TERM_NAME);
      int termId = intent.getIntExtra(EXTRA_COURSE_TERM_ID, -1);
      String fullTermName = termId + "-" + termName;
      // Set the combined string into the UI
      binding.addEditCourseTextviewSelectedTerm.setText(fullTermName);

      // If the intent has a note, set it into the UI
      if (intent.hasExtra(EXTRA_COURSE_NOTE)) {
        binding.addEditCourseTextViewCourseNote.setText(intent.getStringExtra(EXTRA_COURSE_NOTE));
      }

      if (intent.getBooleanExtra(EXTRA_IS_COURSE_START_ALARM_SET, false)) {
        binding.courseStartAlarmDateTimeTextView.setText(
            intent.getStringExtra(EXTRA_COURSE_START_ALARM_DATETIME));
        binding.courseStartAlarmCancelButton.setVisibility(View.VISIBLE);
        binding.courseStartAlarmCancelButton.setEnabled(true);
        setCourseStartAlarmCancelButtonClickListener();
      }

      if (intent.getBooleanExtra(EXTRA_IS_COURSE_END_ALARM_SET, false)) {
        binding.courseEndAlarmDateTimeTextView.setText(
            intent.getStringExtra(EXTRA_COURSE_END_ALARM_DATETIME));
        binding.courseEndAlarmCancelButton.setVisibility(View.VISIBLE);
        binding.courseEndAlarmCancelButton.setEnabled(true);
        setCourseEndAlarmCancelButtonClickListener();
      }
    } else {
      // Change the title in the ActionBar
      setTitle("Add Course");
    }

    // Set up the DatePickers
    setUpStartDatePicker();
    setUpEndDatePicker();

    // Set up Course Start Alarm Pickers
    setUpCourseStartAlarmPicker();
    setUpCourseEndAlarmPicker();

    // Set up the Menus
    setUpTermMenu();
    setUpCourseStatusMenu();
  }

  /**
   * The function sets a click listener for a cancel button related to a course start alarm in Java.
   */
  private void setCourseStartAlarmCancelButtonClickListener() {
    buttonCancelCourseStartAlarm.setOnClickListener(
        view -> {
          binding.courseStartAlarmDateTimeTextView.setText(null);
          buttonCancelCourseStartAlarm.setVisibility(View.INVISIBLE);
          buttonCancelCourseStartAlarm.setEnabled(false);
        });
  }

  // It looks like the code snippet you provided is not valid Java code. The characters "u" and "
  private void setCourseEndAlarmCancelButtonClickListener() {
    buttonCancelCourseEndAlarm.setOnClickListener(
        view -> {
          binding.courseEndAlarmDateTimeTextView.setText(null);
          buttonCancelCourseEndAlarm.setVisibility(View.INVISIBLE);
          buttonCancelCourseEndAlarm.setEnabled(false);
        });
  }

  /**
   * The setUpStartDatePicker function creates a MaterialDatePicker for selecting a course start
   * date and updates the UI with the selected date in a specific format.
   */
  private void setUpStartDatePicker() {
    // Create a MaterialDatePicker Builder instance
    Builder<Long> startDatePickerBuilder = Builder.datePicker();
    // Set the title text
    startDatePickerBuilder.setTitleText("Select a course start date");
    // Build the startDatePicker
    MaterialDatePicker<Long> startDatePicker = startDatePickerBuilder.build();

    // Set a click listener on the startDate select button
    binding.buttonSelectCourseStartDate.setOnClickListener(
        view ->

            // Show the date picker
            startDatePicker.show(getSupportFragmentManager(), "START_DATE_PICKER"));

    // Add a listener to the Start Date Picker's OK button
    startDatePicker.addOnPositiveButtonClickListener(
        selection -> {
          // Get a Calendar instance in Central Time
          Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
          calendar.setTimeInMillis(selection);
          Date myDate = calendar.getTime();
          myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
          SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
          String formattedDate = sdf.format(myDate);
          binding.addEditCourseTextviewSelectedCourseStartDate.setText(formattedDate);
        });
  }

  // The above code is a comment in Java. Comments are used to provide explanations or notes within
  // the code for better understanding. In this
  // case, the comment appears to be indicating a section of code or a specific character.
  private void setUpEndDatePicker() {
    // Create a MaterialDatePicker Builder instance
    Builder<Long> endDatePickerBuilder = Builder.datePicker();
    // Set the title text
    endDatePickerBuilder.setTitleText("Select a course end date");
    // Build the endDatePicker
    MaterialDatePicker<Long> endDatePicker = endDatePickerBuilder.build();

    // Set a click listener on the endDate select button
    Objects.requireNonNull(binding.buttonSelectCourseEndDate)
        .setOnClickListener(
            view ->
                // Show the date picker
                endDatePicker.show(getSupportFragmentManager(), "END_DATE_PICKER"));

    // Add a listener to the End Date Picker's OK button
    endDatePicker.addOnPositiveButtonClickListener(
        selection -> {
          //
          Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
          calendar.setTimeInMillis(selection);
          Date myDate = calendar.getTime();
          myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
          SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
          String formattedDate = sdf.format(myDate);
          binding.addEditCourseTextviewSelectedCourseEndDate.setText(formattedDate);
        });
  }

  /**
   * The function `setUpCourseStartAlarmPicker` sets a click listener on a button to show a date and
   * time picker for selecting a course start alarm.
   */
  private void setUpCourseStartAlarmPicker() {

    // Set a click listener on the courseStartAlarmSelectButton
    buttonSelectCourseStartAlarm.setOnClickListener(view -> showCourseStartDateTimePicker());
  }

  /**
   * The function setUpCourseEndAlarmPicker sets a click listener on a button to show a date and
   * time picker for course end alarm selection.
   */
  private void setUpCourseEndAlarmPicker() {
    // Set a click listener on the courseEndAlarmSelectButton
    buttonSelectCourseEndAlarm.setOnClickListener(view -> showCourseEndDateTimePicker());
  }

  /**
   * Sets up a term menu by concatenating the ID and Name of each term in the `termsList` into a
   * single string format and storing them in an `ArrayList` named `concatTerms`.
   */
  private void setUpTermMenu() {
    ArrayList<String> concatTerms = new ArrayList<>();

    // Get the term ID and the Term Name and concatenate them into a single string
    for (Term term : termsList) {
      concatTerms.add(term.getId() + "-" + term.getName());
    }

    if (!concatTerms.isEmpty()) {
      // Add all the terms to the menu
      binding.addEditCourseTextviewSelectedTerm.setSimpleItems(
          concatTerms.toArray(new String[concatTerms.size()]));
    }
  }

  /**
   * The function setUpCourseStatusMenu adds all course statuses to a menu using a string array
   * resource.
   */
  private void setUpCourseStatusMenu() {
    // Add all course statuses to the menu
    binding.addEditCourseTextViewCourseStatus.setSimpleItems(
        getResources().getStringArray(R.array.course_statuses));
  }

  /**
   * The `showCourseStartDateTimePicker` method in Java creates and displays a MaterialDatePicker
   * for selecting a course start alarm date, followed by a MaterialTimePicker for selecting a
   * course start alarm time, and updates the UI accordingly.
   */
  private void showCourseStartDateTimePicker() {
    // Create a MaterialDatePicker Builder instance
    Builder<Long> courseStartAlarmDatePickerBuilder = Builder.datePicker();
    // Set the title text
    courseStartAlarmDatePickerBuilder.setTitleText("Select a course start alarm date");
    // Build the courseStartAlarmDatePicker
    MaterialDatePicker<Long> courseStartAlarmDatePicker = courseStartAlarmDatePickerBuilder.build();
    // Show the date picker
    courseStartAlarmDatePicker.show(getSupportFragmentManager(), "COURSE_START_ALARM_PICKER");

    // Add a listener to the courseStartAlarmDatePicker's OK button
    courseStartAlarmDatePicker.addOnPositiveButtonClickListener(
        new MaterialPickerOnPositiveButtonClickListener<Long>() {
          @Override
          public void onPositiveButtonClick(Long selection) {
            // Get a Calendar instance in Central Time
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
            calendar.setTimeInMillis(selection);
            Date myDate = calendar.getTime();
            myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
            SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
            String formattedDate = sdf.format(myDate);
            // binding.courseStartAlarmDateTimeTextView.setText(formattedDate);
            showCourseStartAlarmTimePicker(formattedDate);
          }

          /**
           * The `showCourseStartAlarmTimePicker` method displays a MaterialTimePicker dialog for
           * selecting a course start alarm time and updates the UI accordingly.
           *
           * @param formattedDate The `formattedDate` parameter in the
           *     `showCourseStartAlarmTimePicker` method is a string that represents a date in a
           *     specific format. It is used to display the selected date along with the time chosen
           *     by the user in the time picker dialog.
           */
          private void showCourseStartAlarmTimePicker(String formattedDate) {
            // Create a MaterialTimePicker Builder instance
            MaterialTimePicker courseStartTimeAlarmPicker = getCourseStartTimeAlarmPicker();
            // Show the time picker
            courseStartTimeAlarmPicker.show(
                getSupportFragmentManager(), "COURSE_START_ALARM_TIME_PICKER");

            courseStartTimeAlarmPicker.addOnPositiveButtonClickListener(
                new OnClickListener() {
                  @Override
                  // The code snippet provided is a method in Java that is likely part of an
                  // OnClickListener implementation for handling click
                  // events on a View. The method signature indicates that it takes a View object as
                  // a parameter and does not return any value
                  // (void). Inside the method, the actual implementation logic for handling the
                  // click event should be written.
                  public void onClick(View v) {
                    int hour = courseStartTimeAlarmPicker.getHour();
                    int minute = courseStartTimeAlarmPicker.getMinute();
                    String formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    String formattedDateTime = formattedDate + " " + formattedTime;
                    binding.courseStartAlarmDateTimeTextView.setText(formattedDateTime);
                    binding.courseStartAlarmCancelButton.setVisibility(View.VISIBLE);
                    binding.courseStartAlarmCancelButton.setEnabled(true);
                    setCourseStartAlarmCancelButtonClickListener();
                  }

                  /**
                   * The function sets a click listener for a cancel button that clears a text view
                   * and hides the button.
                   */
                  private void setCourseStartAlarmCancelButtonClickListener() {
                    binding.courseStartAlarmCancelButton.setOnClickListener(
                        view -> {
                          binding.courseStartAlarmDateTimeTextView.setText(null);
                          binding.courseStartAlarmCancelButton.setVisibility(View.INVISIBLE);
                          binding.courseStartAlarmCancelButton.setEnabled(false);
                        });
                  }
                });
          }

          @NonNull
          private MaterialTimePicker getCourseStartTimeAlarmPicker() {
            MaterialTimePicker.Builder courseStartAlarmTimePickerBuilder =
                new MaterialTimePicker.Builder();
            // Set the title text
            courseStartAlarmTimePickerBuilder.setTitleText("Select a course start alarm time");
            // Set the input mode
            courseStartAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
            // Set the Time format
            courseStartAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
            // Build the courseStartAlarmTimePicker
            return courseStartAlarmTimePickerBuilder.build();
          }
        });
  }

  /**
   * The `showCourseEndDateTimePicker` function in Java creates and displays a MaterialDatePicker
   * for selecting a course end alarm date, followed by a MaterialTimePicker for selecting a course
   * end alarm time, updating the UI accordingly.
   */
  private void showCourseEndDateTimePicker() {
    // Create a MaterialDatePicker Builder instance
    Builder<Long> courseEndAlarmDatePickerBuilder = Builder.datePicker();
    // Set the title text
    courseEndAlarmDatePickerBuilder.setTitleText("Select a course end alarm date");
    // Build the courseEndAlarmDatePicker
    MaterialDatePicker<Long> courseEndAlarmDatePicker = courseEndAlarmDatePickerBuilder.build();
    // Show the date picker
    courseEndAlarmDatePicker.show(getSupportFragmentManager(), "COURSE_END_ALARM_PICKER");

    // Add a listener to the courseEndAlarmDatePicker's OK button
    courseEndAlarmDatePicker.addOnPositiveButtonClickListener(
        new MaterialPickerOnPositiveButtonClickListener<Long>() {
          /**
           * The function converts a timestamp to a formatted date string in Central Time and then
           * displays a time picker for setting a course end alarm.
           *
           * @param selection It looks like the code snippet you provided is a part of an Android
           *     application where a positive button click event is being handled. The
           *     `onPositiveButtonClick` method is called when a positive button is clicked, and it
           *     receives a `Long` parameter named `selection`.
           */
          @Override
          public void onPositiveButtonClick(Long selection) {
            // Get a Calendar instance in Central Time
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
            calendar.setTimeInMillis(selection);
            Date myDate = calendar.getTime();
            myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
            SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
            String formattedDate = sdf.format(myDate);
            // binding.courseStartAlarmDateTimeTextView.setText(formattedDate);
            showCourseEndAlarmTimePicker(formattedDate);
          }

          /**
           * The `showCourseEndAlarmTimePicker` function displays a MaterialTimePicker for selecting
           * the end time of a course and updates the UI accordingly.
           *
           * @param formattedDate The `formattedDate` parameter in the
           *     `showCourseEndAlarmTimePicker` method is a String that represents a date in a
           *     specific format. It is used to display the selected date along with the time in the
           *     UI when setting the course end alarm time.
           */
          private void showCourseEndAlarmTimePicker(String formattedDate) {
            // Create a MaterialTimePicker Builder instance
            MaterialTimePicker courseEndTimeAlarmPicker = getCourseEndTimeAlarmPicker();
            // Show the time picker
            courseEndTimeAlarmPicker.show(
                getSupportFragmentManager(), "COURSE_END_ALARM_TIME_PICKER");

            courseEndTimeAlarmPicker.addOnPositiveButtonClickListener(
                new OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    int hour = courseEndTimeAlarmPicker.getHour();
                    int minute = courseEndTimeAlarmPicker.getMinute();
                    String formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    String formattedDateTime = formattedDate + " " + formattedTime;
                    binding.courseEndAlarmDateTimeTextView.setText(formattedDateTime);
                    buttonCancelCourseEndAlarm.setVisibility(View.VISIBLE);
                    buttonCancelCourseEndAlarm.setEnabled(true);
                    setCourseEndAlarmCancelButtonClickListener();
                  }

                  /**
                   * The function sets a click listener for a cancel button related to a course end
                   * alarm in a Java application.
                   */
                  private void setCourseEndAlarmCancelButtonClickListener() {
                    binding.courseEndAlarmCancelButton.setOnClickListener(
                        view -> {
                          binding.courseEndAlarmDateTimeTextView.setText(null);
                          buttonCancelCourseEndAlarm.setVisibility(View.INVISIBLE);
                          buttonCancelCourseEndAlarm.setEnabled(false);
                        });
                  }
                });
          }

          /**
           * The function `getCourseEndTimeAlarmPicker` creates and returns a MaterialTimePicker for
           * selecting a course end alarm time.
           *
           * @return The method `getCourseEndTimeAlarmPicker()` is returning an instance of
           *     `MaterialTimePicker` that is configured with specific settings such as title text,
           *     input mode, and time format.
           */
          @NonNull
          private MaterialTimePicker getCourseEndTimeAlarmPicker() {
            MaterialTimePicker.Builder courseEndAlarmTimePickerBuilder =
                new MaterialTimePicker.Builder();
            // Set the title text
            courseEndAlarmTimePickerBuilder.setTitleText("Select a course end alarm time");
            // Set the input mode
            courseEndAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
            // Set the Time format
            courseEndAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
            // Build the courseEndAlarmTimePicker
            return courseEndAlarmTimePickerBuilder.build();
          }
        });
  }

  /**
   * On create options menu boolean.
   *
   * @param menu the menu
   * @return the boolean
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.add_course_menu, menu);
    return true;
  }

  /**
   * On options item selected boolean.
   *
   * @param item the item
   * @return the boolean
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.save_course) {
      saveCourse();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void saveCourse() {
    boolean isCourseEndAlarmSet;
    boolean isCourseStartAlarmSet;
    int courseId;
    CourseViewModel mCourseViewModel;
    String courseNote;
    String courseStatus;
    String courseInstructorPhone;
    String courseInstructorEmail;
    String courseInstructorName;
    String courseEndDate;
    String courseStartDate;
    String courseName;
    // Using the binding, extract the data entered by the user
    courseName = binding.addEditCourseEditTextCourseName.getText().toString();
    courseStartDate = binding.addEditCourseTextviewSelectedCourseStartDate.getText().toString();
    courseEndDate = binding.addEditCourseTextviewSelectedCourseEndDate.getText().toString();
    Log.i("Converted Date: ", courseStartDate + " start - end " + courseEndDate);
    String selectedTerm = binding.addEditCourseTextviewSelectedTerm.getText().toString();
    String[] termSplit = selectedTerm.split("-");
    int termId = Integer.parseInt(termSplit[0]);
    String termName = termSplit[1];

    courseInstructorName = binding.addEditCourseTextViewInstructorName.getText().toString();
    courseInstructorPhone = binding.addEditCourseTextViewInstructorPhone.getText().toString();
    courseInstructorEmail = binding.addEditCourseTextViewInstructorEmail.getText().toString();
    courseStatus = binding.addEditCourseTextViewCourseStatus.getText().toString();
    courseNote = binding.addEditCourseTextViewCourseNote.getText().toString();

    // ! TODO: Run a function to set an Alarm using AlarmManager if these values are not equal to
    // null or ""
    // ! TODO: Edit Assessment Model
    // ! TODO: Remove all old Assessment model code dealing with alarms
    // ! TODO: Edit Add/Edit Assessment Activity to include the new alarm fields
    // ! TODO: Edit the AssessmentViewModel to include the new alarm fields
    // ! TODO: Set up click Listeners on the Add/Edit Assessment Activity to save to database, and
    // to set alarms.

    courseStartAlarmDatetime = binding.courseStartAlarmDateTimeTextView.getText().toString();
    courseEndAlarmDatetime = binding.courseEndAlarmDateTimeTextView.getText().toString();

    isCourseStartAlarmSet = !courseStartAlarmDatetime.isEmpty();
    isCourseEndAlarmSet = !courseEndAlarmDatetime.isEmpty();

    Log.i("COURSE START ALARM", courseStartAlarmDatetime);
    Log.i("COURSE END ALARM", courseEndAlarmDatetime);

    // Check to make sure that all the required fields are filled out
    if (courseName.trim().isEmpty()
        || courseStartDate.trim().isEmpty()
        || courseEndDate.trim().isEmpty()
        || selectedTerm.trim().isEmpty()
        || courseInstructorName.trim().isEmpty()
        || courseInstructorPhone.trim().isEmpty()
        || courseInstructorEmail.trim().isEmpty()
        || courseStatus.trim().isEmpty()) {
      // If any field is empty, display a toast error message
      Toast.makeText(
              this, "All fields except course note and alarms are required", Toast.LENGTH_SHORT)
          .show();
      return;
    }
    courseId = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);

    Course editedCourse =
        new Course(
            courseName,
            courseStartDate,
            courseEndDate,
            courseStatus,
            courseInstructorName,
            courseInstructorPhone,
            courseInstructorEmail,
            courseNote,
            termName,
            termId,
            isCourseStartAlarmSet,
            isCourseEndAlarmSet,
            courseStartAlarmDatetime,
            courseEndAlarmDatetime);
    if (courseId != -1) {

      // Create a new Course object using the data entered by the user
      editedCourse.setId(courseId);

      // Get an instance of the CourseViewModel
      mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

      // Update the course in the database
      mCourseViewModel.update(editedCourse);

    } else {
      // Create a new object using the data entered by the user

      // If the user has not selected a start alarm, cancel the alarm
      // Otherwise schedule the alarm
      isCourseStartAlarmSet = !courseStartAlarmDatetime.isEmpty();
      scheduleCourseStartAlert(getApplicationContext(), editedCourse, isCourseStartAlarmSet);

      // If the user has not selected an end alarm, cancel the alarm
      // Otherwise schedule the alarm
      isCourseEndAlarmSet = !courseEndAlarmDatetime.isEmpty();
      scheduleCourseEndAlert(getApplicationContext(), editedCourse, isCourseEndAlarmSet);

      // Get an instance of the CourseViewModel
      mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
      // Add the new Course to the database
      mCourseViewModel.insert(editedCourse);
    }
    finish();
  }

  private void scheduleCourseStartAlert(Context context, Course course, boolean isAlarmSet) {
    // Get an instance of the alarmManager service
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    // Request alarm permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      if (!alarmManager.canScheduleExactAlarms()) {
        ActivityCompat.requestPermissions(
            this,
            new String[] {Manifest.permission.SCHEDULE_EXACT_ALARM},
            REQUEST_CODE_ALARM_PERMISSION);
        return;
      }
    }

    // If an alarm is set...
    if (isAlarmSet) {

      // Create an Intent to send to the CourseAlertReceiver
      Intent startIntent = new Intent(this, CourseAlertReceiver.class);

      // Put the necessary Extras into the Intent
      startIntent.putExtra("COURSE_ID", course.getId());
      startIntent.putExtra("COURSE_NAME", course.getCourseName());
      startIntent.putExtra("ALERT_TITLE", "Course Starting Soon!");
      startIntent.putExtra(
          "ALERT_MESSAGE", "Your course, " + course.getCourseName() + ", is starting soon!");
      startIntent.putExtra("COURSE_ID", course.getId());

      // Create an instance of PendingIntent which will be used to send the broadcast to the
      // CourseAlertReceiver
      PendingIntent startAlarmIntent =
          PendingIntent.getBroadcast(
              context,
              ("" + course.getId()).hashCode(),
              startIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      // Get the alarm datetime that the user selected
      String triggerStartDateTimeString = course.getCourseStartAlarmDatetime();
      // Create a SimpleDateFormat instance to parse the string into a Date object
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

      // Create a long variable to hold the time in milliseconds
      long triggerStartDateTime = 0;

      try {
        // Try to parse the string into a Date object
        Date startDateString = sdf.parse(triggerStartDateTimeString);
        // Get the time in milliseconds
        if (startDateString != null) triggerStartDateTime = startDateString.getTime();

        // Log the time in milliseconds
        Log.i(TAG_FOR_TIME, "Trigger DateTime: " + triggerStartDateTime);

        // Set the alarm
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, triggerStartDateTime, startAlarmIntent);

        // Show a confirmation message that the alarm was set
        Log.i(TAG_FOR_TIME, "Alarm set for " + startDateString + "....hope this works!");
        Toast.makeText(this, "Alarm set for " + startDateString + "!", Toast.LENGTH_SHORT).show();
      } catch (ParseException e) {
        // Show an error message if the string cannot be parsed
        Toast.makeText(this, "Invalid date/time format.", Toast.LENGTH_SHORT).show();
        // Log the error message
        if (e.getMessage() != null) Log.e(TAG_FOR_TIME, e.getMessage());
      }

    } else {
      Log.i(TAG_FOR_TIME, "Alarm not set...cancelling...");
      courseStartAlarmDatetime = "";
      Intent startIntent = new Intent(context, CourseAlertReceiver.class);
      // Create the PendingIntent to cancel the alarm
      PendingIntent cancelIntent =
          PendingIntent.getBroadcast(
              context,
              ("" + course.getId()).hashCode(),
              startIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
      // Cancel the alarm with the cancelIntent
      alarmManager.cancel(cancelIntent);
      // Show a confirmation message that the alarm was cancelled
      Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
      Log.d(TAG, "scheduleCourseStartAlert: alarm cancelled!");
    }
  }

  private void scheduleCourseEndAlert(
      Context applicationContext, Course course, boolean isEndAlarmSet) {

    // Get an instance of the alarmManager service
    AlarmManager alarmManager =
        (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);

    // Request alarm permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      if (!alarmManager.canScheduleExactAlarms()) {
        ActivityCompat.requestPermissions(
            this,
            new String[] {Manifest.permission.SCHEDULE_EXACT_ALARM},
            REQUEST_CODE_ALARM_PERMISSION);
        return;
      }
    }

    if (isEndAlarmSet) {
      // Create an Intent to send to the CourseAlertReceiver
      Intent endIntent = new Intent(applicationContext, CourseAlertReceiver.class);
      // Create an instance of PendingIntent which will be used to send the broadcast to the
      // CourseAlertReceiver
      PendingIntent endAlarmIntent =
          PendingIntent.getBroadcast(
              applicationContext,
              course.getCourseName().hashCode(),
              endIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      // Get the alarm datetime that the user selected
      String triggerEndDateTimeString = course.getCourseEndAlarmDatetime();
      // Create a SimpleDateFormat instance to parse the string into a Date object
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

      // Create a long variable to hold the time in milliseconds
      long triggerEndDateTime = 0;

      try {
        // Try to parse the string into a Date object
        Date endDateString = sdf.parse(triggerEndDateTimeString);

        // Get the time in milliseconds
        if (endDateString != null && !endDateString.toString().isEmpty())
          triggerEndDateTime = endDateString.getTime();

        Log.i("Set End Alarm", "Trigger Date Time End Alarm: " + triggerEndDateTimeString);

        // Create an instance of AlarmClockInfo and set the alarm
        AlarmClockInfo alarmClockInfo = new AlarmClockInfo(triggerEndDateTime, endAlarmIntent);
        alarmManager.setAlarmClock(alarmClockInfo, endAlarmIntent);

        Toast.makeText(this, "Alarm set for " + endDateString + "!", Toast.LENGTH_SHORT).show();
      } catch (ParseException e) {
        Toast.makeText(this, "Invalid date/time format.", Toast.LENGTH_SHORT).show();
        if (!Objects.requireNonNull(e.getMessage()).isEmpty())
          Log.e("Set Alarm Error", e.getMessage());
      }

    } else {
      courseEndAlarmDatetime = "";
      Intent endIntent = new Intent(applicationContext, CourseAlertReceiver.class);
      // Create the PendingIntent to cancel the alarm
      PendingIntent cancelIntent =
          PendingIntent.getBroadcast(
              applicationContext,
              course.getCourseName().hashCode(),
              endIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
      // Cancel the alarm
      alarmManager.cancel(cancelIntent);
      Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
      Log.d(TAG, "scheduleCourseEndAlert: alarm cancelled!");
    }
  }
}
