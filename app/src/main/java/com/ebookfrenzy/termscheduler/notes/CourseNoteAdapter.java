package com.ebookfrenzy.termscheduler.notes;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.courses.Course;
import com.ebookfrenzy.termscheduler.databinding.NoteListItemBinding;

import java.util.ArrayList;
import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-12-16 2:29 PM
 * Created By: flowmar
 *
 *
 *******************/
public class CourseNoteAdapter extends RecyclerView.Adapter<CourseNoteAdapter.CourseNoteViewHolder>
    {

        private List<Course> coursesList = new ArrayList<>();

        // private CourseNoteAdapter.CourseItemClickListener courseItemClickListener;

        private OnButtonClickListener listener;

        public CourseNoteAdapter(OnButtonClickListener listener)
            {
                this.listener = listener;
            }

        public CourseNoteAdapter()
            {
            }

        @NonNull
        @Override
        public CourseNoteAdapter.CourseNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                LayoutInflater      layoutInflater = LayoutInflater.from(parent.getContext());
                NoteListItemBinding binding        = NoteListItemBinding.inflate(layoutInflater, parent, false);
                return new CourseNoteAdapter.CourseNoteViewHolder(binding);
            }

        @Override
        public void onBindViewHolder(@NonNull CourseNoteAdapter.CourseNoteViewHolder courseNoteViewHolder, int i)
            {

                ImageButton sendEmailButton = courseNoteViewHolder.binding.courseNoteListSendEmail;
                Course      currentCourse   = this.coursesList.get(i);
                String      courseName      = currentCourse.getCourseName();
                String      courseNote      = currentCourse.getCourseNote();
                courseNoteViewHolder.binding.courseNoteListCourseName.setText(courseName);
                courseNoteViewHolder.binding.courseNoteListCourseNote.setText(courseNote);

                courseNoteViewHolder.bind(currentCourse, listener);

                // Create an onClick listener to send a note as an email
                // sendEmailButton.setOnClickListener(v ->
                //                                        {
                //                                            // Toast.makeText(v.getContext(), "Clicked!",Toast.LENGTH_SHORT).show();
                //                                            if (courseItemClickListener != null)
                //                                                {
                //                                                    Toast.makeText(v.getContext(), "Clicked!",
                //                                                                   Toast.LENGTH_SHORT
                //                                                                  ).show();
                //                                                    // Create an Intent to send an email
                //                                                    Intent emailIntent = new Intent(
                //                                                            Intent.ACTION_SENDTO);
                //                                                    // Set the data and type of the intent
                //                                                    emailIntent.setDataAndType(
                //                                                            Uri.parse("mailto:"),
                //                                                            "text/plain"
                //                                                                              );
                //                                                    // Add the subject and body of the email to
                //                                                    // the intent
                //                                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                //                                                                         courseName + " Notes"
                //                                                                        );
                //                                                    emailIntent.putExtra(Intent.EXTRA_TEXT,
                //                                                                         courseNote
                //                                                                        );
                //
                //                                                    // Check to see if there is an email client
                //                                                    // that
                //                                                    // can handle the intent
                //                                                    if (emailIntent.resolveActivity(
                //                                                            v.getContext().getPackageManager()) !=
                //                                                            null)
                //                                                        {
                //                                                            startActivity(v.getContext(),
                //                                                                          emailIntent,
                //                                                                          new Bundle()
                //                                                                         );
                //                                                        }
                //                                                }
                //                                        });
            }

        @Override
        public int getItemCount()
            {
                return this.coursesList.size();
            }

        public void setCoursesList(List<Course> coursesList)
            {
                this.coursesList = coursesList;
                this.notifyDataSetChanged();
            }

        public Course getCourseAt(int position)
            {
                return this.coursesList.get(position);
            }

        // public void setOnItemClickListener(CourseNoteAdapter.CourseItemClickListener listener)
        //     {
        //         this.courseItemClickListener = listener;
        //     }

        // public interface CourseItemClickListener
        //     {
        //         void onCourseItemClick(Course course);
        //     }

        public interface OnButtonClickListener
            {
                void onEmailButtonClick(Course course);

                void onSmsButtonClick(Course course);
            }

        class CourseNoteViewHolder extends RecyclerView.ViewHolder
            {
                NoteListItemBinding binding;
                ImageButton         emailButton;

                public CourseNoteViewHolder(@NonNull NoteListItemBinding binding)
                    {
                        super(binding.getRoot());
                        this.binding = binding;

                        // this.binding.getRoot().setOnClickListener(v ->
                        //                                               {
                        //                                                   int position = this.getAbsoluteAdapterPosition();
                        //                                                   if (CourseNoteAdapter.this.courseItemClickListener != null &&
                        //                                                           position != RecyclerView.NO_POSITION)
                        //                                                       {
                        //                                                           CourseNoteAdapter.this.courseItemClickListener.onCourseItemClick(
                        //                                                                   CourseNoteAdapter.this.coursesList.get(position));
                        //                                                       }
                        //                                               });


                    }

                public void bind(Course course, OnButtonClickListener listener)
                    {

                        emailButton = binding.courseNoteListSendEmail;
                        emailButton.setOnClickListener(view ->
                                                           {
                                                               listener.onEmailButtonClick(course);


                                                               // Toast.makeText(view.getContext(), "Clicked!", Toast.LENGTH_SHORT).show();

                                                               // Create an Intent to send an email
                                                               Intent emailIntent = new Intent(
                                                                       Intent.ACTION_SENDTO);
                                                               // Set the data and type of the intent
                                                               emailIntent.setDataAndType(
                                                                       Uri.parse("mailto:"),
                                                                       "text/plain"
                                                                                         );
                                                               // Add the subject and body of the email to
                                                               // the intent
                                                               emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                                                                    course.getCourseName() + " Notes"
                                                                                   );
                                                               emailIntent.putExtra(Intent.EXTRA_TEXT,
                                                                                    course.getCourseNote()
                                                                                   );

                                                               // Check to see if there is an email client
                                                               // that
                                                               // can handle the intent
                                                               if (emailIntent.resolveActivity(
                                                                       view.getContext().getPackageManager()) !=
                                                                       null)
                                                                   {
                                                                       startActivity(view.getContext(),
                                                                                     emailIntent,
                                                                                     new Bundle()
                                                                                    );
                                                                   }
                                                           });

                        binding.courseNoteListSendSms.setOnClickListener(view -> listener.onSmsButtonClick(course));
                    }

            }
    }
