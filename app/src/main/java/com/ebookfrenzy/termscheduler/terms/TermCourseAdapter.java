package com.ebookfrenzy.termscheduler.terms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.R;
import com.ebookfrenzy.termscheduler.courses.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} for displaying a list of {@link Course} objects.
 */
public class TermCourseAdapter extends RecyclerView.Adapter<TermCourseAdapter.TermCourseViewHolder> {

	private List<Course> courseList = new ArrayList<>();

	/**
	 * Updates the adapter's data set with a new list of courses.
	 *
	 * @param newCourseList The new list of courses to display.
	 */
	void submitList(List<Course> newCourseList) {
		courseList = newCourseList;
		notifyDataSetChanged();
	}

	/**
	 * Creates a new {@link TermCourseViewHolder} instance.
	 *
	 * @param parent   The parent {@link ViewGroup} that the new view will be attached to.
	 * @param viewType The view type of the new view.
	 *
	 * @return A new {@link TermCourseViewHolder} instance.
	 */
	@NonNull
	@Override
	public TermCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//		/* Create a view holder */
//		LinearLayout layout = new LinearLayout(parent.getContext());
//		layout.setOrientation(LinearLayout.VERTICAL);
//		layout.setLayoutParams(new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//		layout.setPadding(16, 16, 16, 16);
//
//		TextView courseNameTextView       = new TextView(parent.getContext());
//		TextView courseInstructorTextView = new TextView(parent.getContext());
//		layout.addView(courseNameTextView);
//		layout.addView(courseInstructorTextView);
//
//		return new TermCourseViewHolder(layout);
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_course_list_item, parent, false);
		return new TermCourseViewHolder(itemView);
	}

	/**
	 * Binds data to a {@link TermCourseViewHolder} at a given position.
	 *
	 * @param holder   The {@link TermCourseViewHolder} to bind data to.
	 * @param position The position of the item in the data set.
	 */
	@Override
	public void onBindViewHolder(@NonNull TermCourseViewHolder holder, int position) {
		Course course = courseList.get(position);
		holder.courseNameTextView.setText(course.getCourseName());
		holder.courseInstructorTextView.setText(course.getInstructorName());
	}

	/**
	 * Returns the total number of items in the data set.
	 *
	 * @return The total number of items.
	 */
	@Override
	public int getItemCount() {
		return courseList.size();
	}

	/**
	 * A view holder for a single course item.
	 */
	static class TermCourseViewHolder extends RecyclerView.ViewHolder {
		TextView courseNameTextView;
		TextView courseInstructorTextView;

		/**
		 * Constructor.
		 *
		 * @param itemView The view for this view holder.
		 */
		TermCourseViewHolder(View itemView) {
			super(itemView);
			courseNameTextView       = itemView.findViewById(R.id.term_course_list_course_name_text_view);
			courseInstructorTextView = itemView.findViewById(R.id.term_course_list_instructor_name_text_view);
		}
	}
}