package com.ebookfrenzy.termscheduler.terms;

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
import com.ebookfrenzy.termscheduler.databinding.ActivityAddEditTermBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

public class AddEditTermActivity extends AppCompatActivity
    {
        public static final String EXTRA_TERM_ID = "com.ebookfrenzy.termscheduler.terms.AddEditTermActivity" +
                "EXTRA_TERM_ID";

        public static final String EXTRA_TERM_NAME       = "com.ebookfrenzy.termscheduler.terms.AddEditTermActivity" +
                ".EXTRA_TERM_NAME";
        public static final String EXTRA_TERM_START_DATE =
                "com.ebookfrenzy.termscheduler.terms.AddEditTermActivity" + ".EXTRA_TERM_START_DATE";
        public static final String EXTRA_TERM_END_DATE   = "com.ebookfrenzy.termscheduler.terms.AddEditTermActivity" +
                ".EXTRA_TERM_END_DATE";
        ActivityAddEditTermBinding binding;
        private TermViewModel mTermViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                binding = ActivityAddEditTermBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);

                mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

                // Change the up navigation icon
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_icon);

                // Get the Intent that started this activity and check for the term ID extra
                Intent intent = getIntent();
                if (intent.hasExtra(EXTRA_TERM_ID))
                    {
                        // Change the title in the ActionBar
                        setTitle("Edit Term");
                        binding.editTextTermName.setText(intent.getStringExtra(EXTRA_TERM_NAME));
                        binding.textviewSelectedTermStartDate.setText(intent.getStringExtra(EXTRA_TERM_START_DATE));
                        binding.textviewSelectedTermEndDate.setText(intent.getStringExtra(EXTRA_TERM_END_DATE));
                    }
                else
                    {
                        // Change the title in the ActionBar
                        setTitle("Add Term");
                    }


                // Set up the DatePickers
                setUpStartDatePicker();
                setUpEndDatePicker();


            }

        private void setUpEndDatePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder endDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                endDatePickerBuilder.setTitleText("Select a term end date");
                // Build the endDatePicker
                MaterialDatePicker endDatePicker = endDatePickerBuilder.build();

                // Set a click listener on the endDate select button
                binding.buttonSelectTermEndDate.setOnClickListener(view ->
                                                                       {
                                                                           // Show the date picker
                                                                           endDatePicker.show(
                                                                                   getSupportFragmentManager(),
                                                                                   "END_DATE_PICKER"
                                                                                             );
                                                                       });

                // Add a listener to the End Date Picker's OK button
                endDatePicker.addOnPositiveButtonClickListener(selection -> binding.textviewSelectedTermEndDate.setText(
                        endDatePicker.getHeaderText()));
            }

        private void setUpStartDatePicker()
            {
                // Create a MaterialDatePicker Builder instance
                MaterialDatePicker.Builder startDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
                // Set the title text
                startDatePickerBuilder.setTitleText("Select a term start date");
                // Build the startDatePicker
                MaterialDatePicker startDatePicker = startDatePickerBuilder.build();

                // Set a click listener on the startDate select button
                binding.buttonSelectTermStartDate.setOnClickListener(view ->
                                                                         {
                                                                             // Show the date picker
                                                                             startDatePicker.show(
                                                                                     getSupportFragmentManager(),
                                                                                     "START_DATE_PICKER"
                                                                                                 );
                                                                         });

                // Add a listener to the Start Date Picker's OK button
                startDatePicker.addOnPositiveButtonClickListener(
                        selection -> binding.textviewSelectedTermStartDate.setText(
                                startDatePicker.getHeaderText()));
            }


        @Override
        public boolean onCreateOptionsMenu(Menu menu)
            {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.add_term_menu, menu);
                return true;
            }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
            {
                if (item.getItemId() == R.id.save_term)
                    {
                        saveTerm();
                        return true;
                    }
                return super.onOptionsItemSelected(item);
            }

        private void saveTerm()
            {
                // Using the binding, extract the data entered by the user
                String termName      = binding.editTextTermName.getText().toString();
                String termStartDate = binding.textviewSelectedTermStartDate.getText().toString();
                String termEndDate   = binding.textviewSelectedTermEndDate.getText().toString();

                // Check to make sure that all the fields are filled out
                if (termName.trim().isEmpty() || termStartDate.trim().isEmpty() || termEndDate.trim().isEmpty())
                    {
                        // If any field is empty, display a toast error message
                        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                int id = getIntent().getIntExtra(EXTRA_TERM_ID, -1);
                if (id != -1)
                    {
                        // Create a new Term object using the data entered by the user
                        Term editedTerm = new Term(termName, termStartDate, termEndDate);
                        editedTerm.setId(id);

                        // Update the term in the database
                        mTermViewModel.update(editedTerm);
                        // Close the Activity
                        finish();

                    }
                else
                    {
                        // Create a new Term object using the data entered by the user
                        Term newTerm = new Term(termName, termStartDate, termEndDate);

                        // Add the new Term to the database
                        mTermViewModel.insert(newTerm);
                        // Close the Activity
                        finish();
                    }
            }
    }
