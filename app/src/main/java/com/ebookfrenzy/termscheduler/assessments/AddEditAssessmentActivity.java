package com.ebookfrenzy.termscheduler.assessments;

import android.Manifest.permission;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.ebookfrenzy.termscheduler.R;
import com.ebookfrenzy.termscheduler.courses.Course;
import com.ebookfrenzy.termscheduler.courses.CourseViewModel;
import com.ebookfrenzy.termscheduler.databinding.ActivityAddEditAssessmentBinding;
import com.ebookfrenzy.termscheduler.terms.Term;
import com.ebookfrenzy.termscheduler.terms.TermViewModel;
import com.ebookfrenzy.termscheduler.utilities.AssessmentAlertReceiver;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

/** The {@link AddEditAssessmentActivity} class */
public class AddEditAssessmentActivity extends AppCompatActivity {

  /** The {@code PERMISSIONS_REQUEST_CODE} */
  public static final int PERMISSIONS_REQUEST_CODE = 4321;

  /** The {@code TAG_ADD_EDIT_ASSESSMENT_ACTIVITY} */
  private static final String TAG_ADD_EDIT_ASSESSMENT_ACTIVITY =
      "com.ebookfrenzy.termscheduler.assessments.AddEditAssessmentActivity";

  /** The {@code EXTRA_ASSESSMENT_START_DATE} */
  static final String EXTRA_ASSESSMENT_START_DATE =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_START_DATE";

  /** The {@code EXTRA_ASSESSMENT_COURSE_ID} */
  static final String EXTRA_ASSESSMENT_COURSE_ID =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_COURSE_ID";

  /** The {@code EXTRA_ASSESSMENT_COURSE_NAME} */
  static final String EXTRA_ASSESSMENT_COURSE_NAME =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_COURSE_NAME";

  /** The {@code EXTRA_ASSESSMENT_TERM_ID} */
  static final String EXTRA_ASSESSMENT_TERM_ID =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_TERM_ID";

  /** The {@code EXTRA_ASSESSMENT_TERM_NAME} */
  static final String EXTRA_ASSESSMENT_TERM_NAME =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_TERM_NAME";

  /** The {@code EXTRA_ASSESSMENT_END_ALARM_DATE_TIME} */
  static final String EXTRA_ASSESSMENT_END_ALARM_DATE_TIME =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_END_ALARM_DATE_TIME";

  /** The {@code EXTRA_ASSESSMENT_START_ALARM_DATE_TIME} */
  static final String EXTRA_ASSESSMENT_START_ALARM_DATE_TIME =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_START_ALARM_DATE_TIME";

  /** The {@code EXTRA_ASSESSMENT_ID} */
  static final String EXTRA_ASSESSMENT_ID =
      TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_ID";

  /** The {@code EXTRA_ASSESSMENT_TITLE} */
  static final String EXTRA_ASSESSMENT_TITLE =
      AddEditAssessmentActivity.TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_TITLE";

  /** The {@code EXTRA_ASSESSMENT_TYPE} */
  static final String EXTRA_ASSESSMENT_TYPE =
      AddEditAssessmentActivity.TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_TYPE";

  /** The {@code EXTRA_ASSESSMENT_END_DATE} */
  static final String EXTRA_ASSESSMENT_END_DATE =
      AddEditAssessmentActivity.TAG_ADD_EDIT_ASSESSMENT_ACTIVITY + "EXTRA_ASSESSMENT_END_DATE";

  /** The {@code DFORMAT} */
  private static final String DFORMAT = "yyyy-MM-dd";

  /** The {@code END_DATE_PICKER_TAG} */
  private static final String END_DATE_PICKER_TAG = "END_DATE_PICKER";

  /** The {@code START_DATE_PICKER_TAG} */
  private static final String START_DATE_PICKER_TAG = "START_DATE_PICKER";

  /** The {@code TAG_FOR_TIME} */
  private static final String TAG_FOR_TIME = "Creating Alarm";

  /** The {@code alarmManager} */
  private AlarmManager alarmManager;

  /** The {@code binding} */
  private ActivityAddEditAssessmentBinding binding;

  /** The {@code termsList} */
  private List<Term> termsList;

  /** The {@code coursesList} */
  private List<Course> coursesList;

  /** The {@code assessmentStartAlarmDatetime} */
  private String assessmentStartAlarmDatetime;

  /** The {@code assessmentEndAlarmDatetime} */
  private String assessmentEndAlarmDatetime;

  /**
   * On create.
   *
   * @param savedInstanceState the saved instance state
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityAddEditAssessmentBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    setContentView(view);

    if (getSupportActionBar() != null) {
      // Change the up navigation icon
      getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);
    }

    // Get an instance of the TermViewModel
    TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
    // Get a list of all the terms
    termsList = termViewModel.getTermsList();

    // Get an instance of the CourseViewModel
    CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
    // Get a list of all the courses
    coursesList = courseViewModel.getCoursesList();

    // Instantiate the alarm manager
    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    // Check to see if the POST_NOTIFICATIONS permission has been granted
    checkNotificationsPermission();
  }

  private void checkNotificationsPermission() {
    // Check for POST_NOTIFICATIONS permission
    if (ContextCompat.checkSelfPermission(this, permission.POST_NOTIFICATIONS)
        != PackageManager.PERMISSION_GRANTED) {
      Log.i("Permission Check", "checkNotificationsPermission: Permission not yet granted");
      requestPermissions();
    } else {
      Log.i("Permission Check", "POST_NOTIFICATION permission already granted");
    }
  }

  private void requestPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(
          this, permission.POST_NOTIFICATIONS)) {
        // Show an explanation to the user as to why the app needs this permission
        new AlertDialog.Builder(this)
            .setTitle("Why do we need permission?")
            .setMessage("This permission is needed to show notifications in the foreground.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton(
                "OK",
                (dialog, which) ->
                    ActivityCompat.requestPermissions(
                        AddEditAssessmentActivity.this,
                        new String[] {permission.POST_NOTIFICATIONS},
                        PERMISSIONS_REQUEST_CODE))
            .create()
            .show();
      } else {
        // No explanation needed, request the permission
        ActivityCompat.requestPermissions(
            this, new String[] {permission.POST_NOTIFICATIONS}, PERMISSIONS_REQUEST_CODE);
      }
    }
  }

  /**
   * On request permissions result.
   *
   * @param requestCode the request code
   * @param permissions the permissions
   * @param grantResults the grant results
   */
  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSIONS_REQUEST_CODE
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      // Permission has been granted. Do something here.
      Log.i("Permission Check", "onRequestPermissionsResult: Permission granted.");
    } else if (shouldShowRequestPermissionRationale(Arrays.toString(permissions))) {
      requestPermissions();
    } else {
      // Permission request was denied.
      Log.i("Permission Check", "onRequestPermissionsResult: Permission request denied.");
    }
  }

  /** On start. */
  @Override
  protected void onStart() {
    super.onStart();

    // Get the Intent that started this activity and check for the assessment ID extra
    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_ASSESSMENT_ID)) {
      // Change the title in the ActionBar
      setTitle("Edit Assessment");

      // Set the data from the Intent into the UI
      binding.addEditAssessmentTitle.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TITLE));
      binding.addAssessmentSelectedType.setText(intent.getStringExtra(EXTRA_ASSESSMENT_TYPE));
      binding.addEditAssessmentStartDate.setText(
          intent.getStringExtra(EXTRA_ASSESSMENT_START_DATE));
      binding.addEditAssessmentEndDate.setText(intent.getStringExtra(EXTRA_ASSESSMENT_END_DATE));

      // Get the courseId and courseName from the Intent and concatenate them
      String courseId = String.valueOf(intent.getIntExtra(EXTRA_ASSESSMENT_COURSE_ID, -1));
      String courseName = intent.getStringExtra(EXTRA_ASSESSMENT_COURSE_NAME);
      String concatCourse = courseId + "-" + courseName;
      // Set the concatenated course in the UI
      binding.addEditAssessmentSelectedCourse.setText(concatCourse);

      // Get the termId and termName from the Intent and concatenate them
      String termId = String.valueOf(intent.getIntExtra(EXTRA_ASSESSMENT_TERM_ID, -1));
      String termName = intent.getStringExtra(EXTRA_ASSESSMENT_TERM_NAME);
      String concatTerm = termId + "-" + termName;
      // Set the concatenated term in the UI
      binding.addEditAssessmentSelectedTerm.setText(concatTerm);

      // Get the start and end alarm date times from the Intent
      if (intent.getStringExtra(EXTRA_ASSESSMENT_START_ALARM_DATE_TIME) != null) {
        binding.assessmentStartAlarmDateTimeTextView.setText(
            intent.getStringExtra(EXTRA_ASSESSMENT_START_ALARM_DATE_TIME));
        binding.assessmentStartAlarmCancelButton.setVisibility(View.VISIBLE);
        binding.assessmentStartAlarmCancelButton.setEnabled(true);
        setCourseStartAlarmCancelButtonListener();
      }

      if (intent.getStringExtra(EXTRA_ASSESSMENT_START_ALARM_DATE_TIME) != null) {
        binding.assessmentEndAlarmDateTimeTextView.setText(
            intent.getStringExtra(EXTRA_ASSESSMENT_END_ALARM_DATE_TIME));
        binding.assessmentEndAlarmCancelButton.setVisibility(View.VISIBLE);
        binding.assessmentEndAlarmCancelButton.setEnabled(true);
        setCourseEndAlarmCancelButtonListener();
      }
    } else {
      // Change the title in the ActionBar
      setTitle("Add Assessment");
    }

    // Check for schedule alarm permissions
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
      return;
    }

    // Set up the DatePickers
    setUpStartDatePicker();
    setUpEndDatePicker();

    // Set up Course Start Alarm Pickers
    setUpStartAlarmDateTimePicker();
    setUpEndAlarmDateTimePicker();

    // Set up the menus
    setUpTermMenu();
    setUpCourseMenu();
    setUpTypeMenu();
  }

  private void setCourseStartAlarmCancelButtonListener() {
    binding.assessmentStartAlarmCancelButton.setOnClickListener(
        view -> {
          binding.assessmentStartAlarmDateTimeTextView.setText(null);
          binding.assessmentStartAlarmCancelButton.setVisibility(View.INVISIBLE);
          binding.assessmentStartAlarmCancelButton.setEnabled(false);
        });
  }

  private void setCourseEndAlarmCancelButtonListener() {
    binding.assessmentEndAlarmCancelButton.setOnClickListener(
        view -> {
          binding.assessmentEndAlarmDateTimeTextView.setText(null);
          binding.assessmentEndAlarmCancelButton.setVisibility(View.INVISIBLE);
          binding.assessmentEndAlarmCancelButton.setEnabled(false);
        });
  }

  private void setUpStartDatePicker() {
    // Create a MaterialDatePicker Builder instance
    MaterialDatePicker.Builder<Long> startDatePickerBuilder =
        MaterialDatePicker.Builder.datePicker();
    // Set the title text
    startDatePickerBuilder.setTitleText("Select an assessment start date");
    // Build the startDatePicker
    MaterialDatePicker<Long> startDatePicker = startDatePickerBuilder.build();

    // Set a click listener on the startDate select button
    binding.buttonSelectAssessmentStartDate.setOnClickListener(
        view ->
            // Show the date picker
            startDatePicker.show(getSupportFragmentManager(), START_DATE_PICKER_TAG));

    // Add a listener to the Start Date Picker's OK button
    startDatePicker.addOnPositiveButtonClickListener(
        selection -> {
          Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
          calendar.setTimeInMillis(selection);
          SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
          Date myDate = calendar.getTime();
          myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
          String formattedDate = sdf.format(myDate);
          binding.addEditAssessmentStartDate.setText(formattedDate);
        });
  }

  private void setUpEndDatePicker() {
    // Create a MaterialDatePicker Builder instance
    Builder<Long> endDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
    // Set the title text
    endDatePickerBuilder.setTitleText("Select an assessment end date");
    // Build the endDatePicker
    MaterialDatePicker<Long> endDatePicker = endDatePickerBuilder.build();

    // Set a click listener on the endDate select button
    binding.buttonSelectAssessmentEndDate.setOnClickListener(
        view ->
            // Show the date picker
            endDatePicker.show(getSupportFragmentManager(), END_DATE_PICKER_TAG));

    // Add a listener to the End Date Picker's OK button
    endDatePicker.addOnPositiveButtonClickListener(
        selection -> {
          Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
          calendar.setTimeInMillis(selection);
          SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
          Date myDate = calendar.getTime();
          myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
          String formattedDate = sdf.format(myDate);
          binding.addEditAssessmentEndDate.setText(formattedDate);
        });
  }

  private void setUpStartAlarmDateTimePicker() {
    binding.setAssessmentStartAlarmButton.setOnClickListener(
        view -> showAssessmentStartAlarmDateTimePicker());
  }

  private void setUpEndAlarmDateTimePicker() {
    binding.setAssessmentEndAlarmButton.setOnClickListener(
        view -> showAssessmentEndAlarmDateTimePicker());
  }

  private void setUpTermMenu() {
    ArrayList<String> concatTerms = new ArrayList<>();

    // Get the term ID and the Term Name and concatenate them into a single string
    for (Term term : termsList) {
      concatTerms.add(term.getId() + "-" + term.getName());
    }

    if (!concatTerms.isEmpty()) {
      // Add all the terms to the menu
      binding.addEditAssessmentSelectedTerm.setSimpleItems(concatTerms.toArray(new String[0]));
    }
  }

  private void setUpCourseMenu() {
    ArrayList<String> concatCourses = new ArrayList<>();

    // Get the course ID and the Course Name and concatenate them into a single string
    for (Course course : coursesList) {
      concatCourses.add(course.getId() + "-" + course.getCourseName());
    }

    if (!concatCourses.isEmpty()) {
      // Add all the courses to the menu
      binding.addEditAssessmentSelectedCourse.setSimpleItems(concatCourses.toArray(new String[0]));
    }
  }

  private void setUpTypeMenu() {
    binding.addAssessmentSelectedType.setSimpleItems(
        getResources().getStringArray(R.array.assessment_types));
  }

  private void showAssessmentStartAlarmDateTimePicker() {
    // Create a MaterialDatePicker Builder instance
    MaterialDatePicker.Builder<Long> startAlarmDateTimePickerBuilder =
        MaterialDatePicker.Builder.datePicker();
    // Set the title text
    startAlarmDateTimePickerBuilder.setTitleText("Select a assessment start date");
    // Build the startDatePicker
    MaterialDatePicker<Long> startAlarmDateTimePicker = startAlarmDateTimePickerBuilder.build();

    // Show the date picker
    startAlarmDateTimePicker.show(getSupportFragmentManager(), START_DATE_PICKER_TAG);

    // Add a listener to the Start Date Picker's OK button
    startAlarmDateTimePicker.addOnPositiveButtonClickListener(
        new MaterialPickerOnPositiveButtonClickListener<Long>() {
          @Override
          public void onPositiveButtonClick(Long selection) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
            Date myDate = calendar.getTime();
            myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
            String formattedDate = sdf.format(myDate);
            showAssessmentStartAlarmTimePicker(formattedDate);
          }

          private void showAssessmentStartAlarmTimePicker(String formattedDate) {
            MaterialTimePicker assessmentStartAlarmTimePicker = getMaterialStartTimePicker();

            // Show the date picker
            assessmentStartAlarmTimePicker.show(getSupportFragmentManager(), START_DATE_PICKER_TAG);

            // Add a listener to the Start Date Picker's OK button
            assessmentStartAlarmTimePicker.addOnPositiveButtonClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    int hour = assessmentStartAlarmTimePicker.getHour();
                    int minute = assessmentStartAlarmTimePicker.getMinute();
                    String formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    String formattedDateTime = formattedDate + " " + formattedTime;
                    binding.assessmentStartAlarmDateTimeTextView.setText(formattedDateTime);
                    binding.assessmentStartAlarmCancelButton.setVisibility(View.VISIBLE);
                    binding.assessmentStartAlarmCancelButton.setEnabled(true);
                    setCourseStartAlarmCancelButtonListener();
                  }

                  private void setCourseStartAlarmCancelButtonListener() {
                    binding.assessmentStartAlarmCancelButton.setOnClickListener(
                        view -> {
                          binding.assessmentStartAlarmDateTimeTextView.setText(null);
                          binding.assessmentStartAlarmCancelButton.setVisibility(View.INVISIBLE);
                          binding.assessmentStartAlarmCancelButton.setEnabled(false);
                        });
                  }
                });
          }

          @NonNull
          private MaterialTimePicker getMaterialStartTimePicker() {
            MaterialTimePicker.Builder assessmentStartAlarmTimePickerBuilder =
                new MaterialTimePicker.Builder();
            // Set the title text
            assessmentStartAlarmTimePickerBuilder.setTitleText("Select a assessment start date");
            // Set the input mode
            assessmentStartAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
            // Set the Time format
            assessmentStartAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
            // Build the startTimePicker
            return assessmentStartAlarmTimePickerBuilder.build();
          }
        });
  }

  private void showAssessmentEndAlarmDateTimePicker() {
    // Create a MaterialDatePicker Builder instance
    MaterialDatePicker.Builder<Long> endAlarmDateTimePickerBuilder =
        MaterialDatePicker.Builder.datePicker();
    // Set the title text
    endAlarmDateTimePickerBuilder.setTitleText("Select a assessment end date");
    // Build the endDatePicker
    MaterialDatePicker<Long> endAlarmDateTimePicker = endAlarmDateTimePickerBuilder.build();

    // Show the date picker
    endAlarmDateTimePicker.show(getSupportFragmentManager(), END_DATE_PICKER_TAG);

    // Add a listener to the End Date Picker's OK button
    endAlarmDateTimePicker.addOnPositiveButtonClickListener(
        new MaterialPickerOnPositiveButtonClickListener<Long>() {
          @Override
          public void onPositiveButtonClick(Long selection) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CST"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat sdf = new SimpleDateFormat(DFORMAT, Locale.US);
            Date myDate = calendar.getTime();
            myDate.setMinutes(myDate.getMinutes() + myDate.getTimezoneOffset());
            String formattedDate = sdf.format(myDate);
            showAssessmentEndAlarmTimePicker(formattedDate);
          }

          private void showAssessmentEndAlarmTimePicker(String formattedDate) {
            MaterialTimePicker assessmentEndAlarmTimePicker = getMaterialEndTimePicker();

            // Show the time picker
            assessmentEndAlarmTimePicker.show(getSupportFragmentManager(), "END_DATE_PICKER");

            // Add a listener to the End Date Picker's OK button
            assessmentEndAlarmTimePicker.addOnPositiveButtonClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    int hour = assessmentEndAlarmTimePicker.getHour();
                    int minute = assessmentEndAlarmTimePicker.getMinute();
                    String formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    String formattedDateTime = formattedDate + " " + formattedTime;
                    binding.assessmentEndAlarmDateTimeTextView.setText(formattedDateTime);
                    binding.assessmentEndAlarmCancelButton.setVisibility(View.VISIBLE);
                    binding.assessmentEndAlarmCancelButton.setEnabled(true);
                    setCourseEndAlarmCancelButtonListener();
                  }

                  private void setCourseEndAlarmCancelButtonListener() {
                    binding.assessmentEndAlarmCancelButton.setOnClickListener(
                        view -> {
                          binding.assessmentEndAlarmDateTimeTextView.setText(null);
                          binding.assessmentEndAlarmCancelButton.setVisibility(View.INVISIBLE);
                          binding.assessmentEndAlarmCancelButton.setEnabled(false);
                        });
                  }
                });
          }
        });
  }

  /**
   * @return the {@code assessmentEndAlarmTimePickerBuilder.build()} as {@link MaterialTimePicker}
   */
  @NonNull
  private MaterialTimePicker getMaterialEndTimePicker() {
    MaterialTimePicker.Builder assessmentEndAlarmTimePickerBuilder =
        new MaterialTimePicker.Builder();
    // Set the title text
    assessmentEndAlarmTimePickerBuilder.setTitleText("Select a assessment end date");
    // Set the input mode
    assessmentEndAlarmTimePickerBuilder.setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK);
    // Set the Time format
    assessmentEndAlarmTimePickerBuilder.setTimeFormat(TimeFormat.CLOCK_12H);
    // Build the startTimePicker
    return assessmentEndAlarmTimePickerBuilder.build();
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
    menuInflater.inflate(R.menu.add_assessment_menu, menu);
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
    if (item.getItemId() == R.id.save_assessment) {
      saveAssessment();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void saveAssessment() {
    // Using the binding, extract the data entered by the user
    String assessmentTitle =
        Objects.requireNonNull(binding.addEditAssessmentTitle.getText()).toString();
    String assessmentType = binding.addAssessmentSelectedType.getText().toString();
    String assessmentStartDate = binding.addEditAssessmentStartDate.getText().toString();
    String assessmentEndDate = binding.addEditAssessmentEndDate.getText().toString();
    // Get the courseID and courseName from the UI
    String selectedCourse = binding.addEditAssessmentSelectedCourse.getText().toString();
    String[] splitCourse = selectedCourse.split("-");
    int courseId = Integer.parseInt(splitCourse[0]);
    String courseName = splitCourse[1];

    // Get the termID and termName from the UI
    String selectedTerm = binding.addEditAssessmentSelectedTerm.getText().toString();
    String[] splitTerm = selectedTerm.split("-");
    int termId = Integer.parseInt(splitTerm[0]);
    String termName = splitTerm[1];

    assessmentStartAlarmDatetime =
        binding.assessmentStartAlarmDateTimeTextView.getText().toString();
    assessmentEndAlarmDatetime = binding.assessmentEndAlarmDateTimeTextView.getText().toString();

    boolean isAssessmentStartAlarmSet = !assessmentStartAlarmDatetime.isEmpty();
    boolean isAssessmentEndAlarmSet = !assessmentEndAlarmDatetime.isEmpty();

    Log.i("ASSESSMENT START ALARM", assessmentStartAlarmDatetime);
    Log.i("ASSESSMENT END ALARM", assessmentEndAlarmDatetime);

    // Check to make sure that all the fields are filled out
    if (assessmentTitle.trim().isEmpty()
        || assessmentType.trim().isEmpty()
        || assessmentEndDate.trim().isEmpty()
        || String.valueOf(courseId).trim().isEmpty()
        || String.valueOf(termId).trim().isEmpty()
        || courseName.trim().isEmpty()
        || termName.trim().isEmpty()) {
      // If any field is empty, display a toast error message
      Toast.makeText(this, "All fields are required except for alarms", Toast.LENGTH_SHORT).show();
      return;
    }

    int id = getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, -1);
    AssessmentViewModel assessmentViewModel;

    String assessmentStartAlarmDatetime =
        binding.assessmentStartAlarmDateTimeTextView.getText().toString();
    String assessmentEndAlarmDatetime =
        binding.assessmentEndAlarmDateTimeTextView.getText().toString();

    // boolean isStartAlarmOn = this.binding.assessmentStartAlarmToggle.isActivated();
    // boolean isEndAlarmOn   = this.binding.assessmentEndAlarmToggle.isActivated();

    // Create a new Assessment object using the data entered by the user
    Assessment editedAssessment =
        new Assessment(
            assessmentType,
            assessmentTitle,
            assessmentStartDate,
            assessmentEndDate,
            courseId,
            courseName,
            termId,
            termName,
            assessmentStartAlarmDatetime,
            assessmentEndAlarmDatetime);
    if (id != -1) {
      // Set the assessmentId of the editedAssessment to the assessmentId of the original assessment
      editedAssessment.setId(id);

      // Schedule or cancel the start and end alarms
      scheduleAssessmentStartAlert(
          getApplicationContext(), editedAssessment, !assessmentStartAlarmDatetime.isEmpty());
      scheduleAssessmentEndAlert(
          getApplicationContext(), editedAssessment, !assessmentEndAlarmDatetime.isEmpty());

      // Get an instance of the AssessmentViewModel
      assessmentViewModel =
          new ViewModelProvider(this)
              .get(com.ebookfrenzy.termscheduler.assessments.AssessmentViewModel.class);
      // Update the assessment in the database
      assessmentViewModel.update(editedAssessment);
    } else {

      // Get an instance of the AssessmentViewModel
      assessmentViewModel =
          new ViewModelProvider(this)
              .get(com.ebookfrenzy.termscheduler.assessments.AssessmentViewModel.class);
      // Add the new Assessment to the database
      assessmentViewModel.insert(this, editedAssessment, courseId);
    }
    // Close the activity
    finish();
  }

  private void scheduleAssessmentStartAlert(
      Context context, Assessment editedAssessment, boolean isStartAlarmSet) {
    // Create an Intent to send to AssessmentAlertReceiver
    Intent startIntent = getStartIntent(editedAssessment);

    // If an alarm is set...
    if (isStartAlarmSet) {

      // Create a PendingIntent to send the broadcast to the AssessmentAlertReceiver
      PendingIntent startAlarmIntent =
          PendingIntent.getBroadcast(
              context,
              ("" + editedAssessment.getId()).hashCode(),
              startIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      // Get the alarm datetime from the UI
      String triggerStartDateTimeString = editedAssessment.getAssessmentStartDateTimeAlarm();

      // Create a SimpleDateFormat instance to parse the string
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

      try {
        // Try to parse the string
        Date startDateString = sdf.parse(triggerStartDateTimeString);
        // Get the time in milliseconds
        if (startDateString != null) {
          long triggerStartDateTime = startDateString.getTime();
          // Log the time in milliseconds
          Log.i(TAG_FOR_TIME, "Start Trigger DateTime: " + triggerStartDateTime);

          // Set the alarm
          alarmManager.setExactAndAllowWhileIdle(
              AlarmManager.RTC_WAKEUP, triggerStartDateTime, startAlarmIntent);

          // Show a confirmation message that the alarm was set
          Log.i(TAG_FOR_TIME, "Assessment Start Alarm set for " + startDateString + "!");
          Toast.makeText(
                  this,
                  "Assessment Start Alarm for "
                      + editedAssessment.getCourseName()
                      + " set for "
                      + startDateString
                      + "!",
                  Toast.LENGTH_SHORT)
              .show();
        }
      } catch (ParseException e) {
        // Show an error if the string cannot be parsed
        Toast.makeText(this, "Error parsing date/time", Toast.LENGTH_SHORT).show();
        // Log the error
        if (e.getMessage() != null)
          Log.e(TAG_FOR_TIME, "scheduleAssessmentStartAlert: " + e.getMessage());
      }

    } else {
      // If no alarm is set, cancel the alarm
      Log.i(
          TAG_FOR_TIME,
          "Assessment Start Alarm for " + editedAssessment.getAssessmentTitle() + " cancelled!");

      editedAssessment.setAssessmentStartDateTimeAlarm(null);

      // Create a PendingIntent to cancel the alarm
      PendingIntent cancelIntent =
          PendingIntent.getBroadcast(
              context,
              ("" + editedAssessment.getId()).hashCode(),
              startIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      // Cancel the alarm
      alarmManager.cancel(cancelIntent);

      Toast.makeText(
              this,
              "Assessment Start Alarm for " + editedAssessment.getAssessmentTitle() + " cancelled!",
              Toast.LENGTH_SHORT)
          .show();
      Log.i(
          TAG_ADD_EDIT_ASSESSMENT_ACTIVITY,
          "scheduleAssessmentStartAlert: Course Start Alarm cancelled!");
    }
  }

  private void scheduleAssessmentEndAlert(
      Context context, Assessment editedAssessment, boolean isEndAlarmSet) {

    // Create an Intent to send to AssessmentAlertReceiver
    Intent endIntent = getEndIntent(editedAssessment);

    if (isEndAlarmSet) {

      /* Create a PendingIntent to send the broadcast to the AssessmentAlertReceiver */
      PendingIntent endAlarmIntent =
          PendingIntent.getBroadcast(
              context,
              editedAssessment.getAssessmentTitle().hashCode(),
              endIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      // Get the alarm datetime from the UI
      String triggerEndDateTimeString = editedAssessment.getAssessmentEndDateTimeAlarm();
      // Create a SimpleDateFormat instance to parse the string
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

      try {
        // Try to parse the string into a Date object
        Date endDateString = sdf.parse(triggerEndDateTimeString);
        // Get the time in milliseconds
        if (endDateString != null) {
          long triggerEndDateTime = endDateString.getTime();
          // Log the time in milliseconds
          Log.i(TAG_FOR_TIME, "End Trigger DateTime:" + triggerEndDateTime);

          // Set the alarm
          alarmManager.setExactAndAllowWhileIdle(
              AlarmManager.RTC_WAKEUP, triggerEndDateTime, endAlarmIntent);

          // Show a confirmation message that the alarm was set
          Log.i(TAG_FOR_TIME, "Alarm set for" + endDateString + "!");
          Toast.makeText(
                  this,
                  "Assessment End Alarm for "
                      + editedAssessment.getAssessmentTitle()
                      + " set for "
                      + endDateString
                      + "!",
                  Toast.LENGTH_SHORT)
              .show();
        }
      } catch (ParseException e) {
        Toast.makeText(this, "Error parsing date/time", Toast.LENGTH_SHORT).show();
        if (!Objects.requireNonNull(e.getMessage()).isEmpty())
          Log.e(TAG_FOR_TIME, "scheduleAssessmentEndAlert: " + e.getMessage());
      }
    } else {
      // If no alarm is set, cancel the alarm
      Log.i(
          TAG_FOR_TIME,
          "Assessment End Alarm for " + editedAssessment.getAssessmentTitle() + "is not set!");
      editedAssessment.setAssessmentEndDateTimeAlarm(null);

      // Create the PendingIntent to cancel the alarm
      PendingIntent cancelIntent =
          PendingIntent.getBroadcast(
              context,
              editedAssessment.getAssessmentTitle().hashCode(),
              endIntent,
              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

      // Cancel the alarm
      alarmManager.cancel(cancelIntent);
      Toast.makeText(
              this,
              "Assessment End Alarm for " + editedAssessment.getAssessmentTitle() + " cancelled!",
              Toast.LENGTH_SHORT)
          .show();
      Log.i(
          TAG_ADD_EDIT_ASSESSMENT_ACTIVITY,
          "scheduleAssessmentEndAlert: Course End Alarm cancelled!");
    }
  }

  @NonNull
  private Intent getStartIntent(@NonNull Assessment editedAssessment) {
    Intent startIntent = new Intent(this, AssessmentAlertReceiver.class);

    // Put the necessary Extras into the Intent
    startIntent.putExtra("ASSESSMENT_ID", editedAssessment.getId());
    startIntent.putExtra("ASSESSMENT_NAME", editedAssessment.getAssessmentTitle());
    startIntent.putExtra("ALERT_TYPE", "Assessment Starting Soon!");
    startIntent.putExtra(
        "ALERT_MESSAGE",
        "Your assessment, "
            + editedAssessment.getAssessmentTitle()
            + ",for course "
            + editedAssessment.getCourseName()
            + " is starting on "
            + editedAssessment.getAssessmentStartDate());
    return startIntent;
  }

  @NonNull
  private Intent getEndIntent(@NonNull Assessment editedAssessment) {
    Intent endIntent = new Intent(this, AssessmentAlertReceiver.class);

    // Put the necessary Extras into the Intent
    endIntent.putExtra("ASSESSMENT_ID", editedAssessment.getId());
    endIntent.putExtra("ASSESSMENT_NAME", editedAssessment.getAssessmentTitle());
    endIntent.putExtra("ALERT_TITLE", "Assessment Ending Soon!");
    endIntent.putExtra(
        "ALERT_MESSAGE",
        "Your assessment, "
            + editedAssessment.getAssessmentTitle()
            + ",for course "
            + editedAssessment.getCourseName()
            + " is ending on "
            + editedAssessment.getAssessmentEndDate());

    return endIntent;
  }
}
