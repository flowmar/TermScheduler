package com.ebookfrenzy.termscheduler.courses;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.databinding.CourseListItemBinding;

import java.util.ArrayList;
import java.util.List;

/******************
 * Project: CourseScheduler
 * Created At: 2023-12-03 11:30PM
 * Created By: flowmar
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
    {
        // ********************************************Fields**************************************************/

        private List<Course>                      coursesList = new ArrayList<>();
        private CourseAdapter.OnItemClickListener listener;


        // ********************************************Methods**************************************************/
        @Override
        @NonNull
        public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                LayoutInflater        layoutInflater = LayoutInflater.from(parent.getContext());
                CourseListItemBinding binding        = CourseListItemBinding.inflate(layoutInflater, parent, false);

                return new CourseAdapter.CourseViewHolder(binding);
            }

        @Override
        public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position)
            {
                Course currentCourse = this.coursesList.get(position);
                holder.binding.courseListCourseNameTextView.setText(currentCourse.getCourseName());
                holder.binding.courseListTermNameTextView.setText(currentCourse.getTermName());
            }

        @Override
        public int getItemCount()
            {
                return this.coursesList.size();
            }

        public void setCoursesList(List<Course> coursesList)
            {
                List<Course> safeCoursesList = new ArrayList<>(coursesList);

                this.coursesList = safeCoursesList;
                this.notifyDataSetChanged();
            }

        public void removeItem(int position)
            {
                // Remove the item from the data set
                this.coursesList.remove(position);
                // Notify the adapter about the removal
                notifyItemRemoved(position);
                // Update the view for the remaining items
                notifyItemRangeChanged(position, this.coursesList.size());
            }

        public Course getCourseAt(int position)
            {
                return this.coursesList.get(position);
            }

        public void setOnItemClickListener(CourseAdapter.OnItemClickListener listener)
            {
                this.listener = listener;
            }

        public interface OnItemClickListener
            {
                void onItemClick(Course course);
            }

        // ********************************************Static Classes**************************************************/
        class CourseViewHolder extends RecyclerView.ViewHolder
            {
                private final CourseListItemBinding binding;


                public CourseViewHolder(CourseListItemBinding binding)
                    {

                        super(binding.getRoot());
                        this.binding = binding;

                        binding.getRoot().setOnClickListener(v ->
                                                                 {
                                                                     int position = this.getAbsoluteAdapterPosition();
                                                                     if (CourseAdapter.this.listener != null &&
                                                                             position != RecyclerView.NO_POSITION)
                                                                         {
                                                                             CourseAdapter.this.listener.onItemClick(
                                                                                     CourseAdapter.this.coursesList.get(
                                                                                             position));
                                                                         }
                                                                 });
                    }
            }
    }
