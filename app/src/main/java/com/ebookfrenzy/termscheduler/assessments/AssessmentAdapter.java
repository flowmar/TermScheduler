package com.ebookfrenzy.termscheduler.assessments;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.databinding.AssessmentListItemBinding;

import java.util.ArrayList;
import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-12-05 8:52PM
 * Created By: flowmar
 *
 *
 *******************/
public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
	// ********************************************Fields**************************************************/

	private List<Assessment>                      assessmentsList = new ArrayList<>();
	private AssessmentAdapter.OnItemClickListener listener;


	// ********************************************Methods**************************************************/
	@Override
	@NonNull
	public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		AssessmentListItemBinding binding = AssessmentListItemBinding.inflate(layoutInflater, parent,
																			  false
																			 );

		return new AssessmentAdapter.AssessmentViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
		Assessment currentAssessment = this.assessmentsList.get(position);
		holder.binding.assessmentListAssessmentNameTextView.setText(currentAssessment.getAssessmentTitle());
		holder.binding.assessmentListCourseNameTextView.setText(currentAssessment.getCourseName());
	}

	@Override
	public int getItemCount() {
		return this.assessmentsList.size();
	}

	public void setAssessmentsList(List<Assessment> assessmentsList) {
		List<Assessment> safeAssessmentsList = new ArrayList<>(assessmentsList);

		this.assessmentsList = safeAssessmentsList;
		this.notifyDataSetChanged();
	}

	public Assessment getAssessmentAt(int position) {
		return this.assessmentsList.get(position);
	}

	public void setOnItemClickListener(AssessmentAdapter.OnItemClickListener listener) {
		this.listener = listener;
	}

	public interface OnItemClickListener {
		void onItemClick(Assessment term);
	}

	// ********************************************Static Classes**************************************************/
	class AssessmentViewHolder extends RecyclerView.ViewHolder {
		private final AssessmentListItemBinding binding;


		public AssessmentViewHolder(AssessmentListItemBinding binding) {

			super(binding.getRoot());
			this.binding = binding;

			binding.getRoot().setOnClickListener(v ->
												 {
													 int position = this.getAbsoluteAdapterPosition();
													 if (AssessmentAdapter.this.listener != null &&
														 position != RecyclerView.NO_POSITION)
													 {
														 AssessmentAdapter.this.listener.onItemClick(
																 AssessmentAdapter.this.assessmentsList.get(
																		 position));
													 }
												 });
		}
	}
}
