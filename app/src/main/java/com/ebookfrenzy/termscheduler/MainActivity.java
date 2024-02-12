package com.ebookfrenzy.termscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.ebookfrenzy.termscheduler.assessments.AssessmentListActivity;
import com.ebookfrenzy.termscheduler.courses.CourseListActivity;
import com.ebookfrenzy.termscheduler.databinding.ActivityMainBinding;
import com.ebookfrenzy.termscheduler.notes.CourseNotesListActivity;
import com.ebookfrenzy.termscheduler.terms.TermListActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    {


        private ActivityMainBinding binding;


        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                binding = ActivityMainBinding.inflate(getLayoutInflater());
                View view = binding.getRoot();
                setContentView(view);

                // NotificationManager notificationManager = getSystemService(NotificationManager.class);
                // if (notificationManager.getNotificationChannels().size() == 0)
                //     {
                //         // Create a notification channel
                //         createNotificationChannel();
                //     }

                // Context context = view.getContext();

                // Center the title in the ActionBar
                centerTitle();

                // Set up listeners on the Buttons
                listenerSetUp();


            }

        /**
         * Creates a notification channel.
         */
        // private void createNotificationChannel()
        //     {
        //
        //         NotificationManager notificationManager = getSystemService(NotificationManager.class);
        //         // notificationManager.deleteNotificationChannel("default");
        //         CharSequence        name        = "TermScheduler Notification Channel";
        //         String              description = "Shows notifications from TermScheduler";
        //         int                 importance  = NotificationManager.IMPORTANCE_HIGH;
        //         NotificationChannel channel     = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
        //         channel.setDescription(description);
        //         notificationManager.createNotificationChannel(channel);
        //     }

        /**
         * Sets up the listeners for the buttons in the MainActivity.
         */
        private void listenerSetUp()
            {
                binding.termsButton.setOnClickListener(v ->
                                                           {
                                                               Intent intent = new Intent(MainActivity.this, TermListActivity.class);
                                                               startActivity(intent);
                                                           });

                binding.coursesButton.setOnClickListener(v ->
                                                             {
                                                                 Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
                                                                 startActivity(intent);
                                                             });

                binding.assessmentsButton.setOnClickListener(v ->
                                                                 {
                                                                     Intent intent = new Intent(MainActivity.this, AssessmentListActivity.class);
                                                                     startActivity(intent);
                                                                 });

                binding.courseNotesButton.setOnClickListener(v ->
                                                                 {
                                                                     Intent intent = new Intent(MainActivity.this, CourseNotesListActivity.class);
                                                                     startActivity(intent);
                                                                 });
            }

        /**
         * Centers the title of the view.
         */
        private void centerTitle()
            {
                ArrayList<View> textViews = new ArrayList<>();

                getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

                if (textViews.size() > 0)
                    {
                        AppCompatTextView compatTextView = null;

                        if (textViews.size() == 1)
                            {
                                compatTextView = (AppCompatTextView) textViews.get(0);
                            }
                        else
                            {
                                for (View vw : textViews)
                                    {
                                        if (vw.getParent() instanceof Toolbar)
                                            {
                                                compatTextView = (AppCompatTextView) vw;
                                                break;
                                            }
                                    }
                            }

                        if (compatTextView != null)
                            {
                                ViewGroup.LayoutParams layoutParams = compatTextView.getLayoutParams();
                                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                compatTextView.setLayoutParams(layoutParams);
                                compatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            }
                    }
            }

    }
