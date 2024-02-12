package com.ebookfrenzy.termscheduler.terms;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.termscheduler.databinding.TermListItemBinding;

import java.util.ArrayList;
import java.util.List;

/******************
 * Created Using: Android Studio
 * Project: TermScheduler
 * Package: com.ebookfrenzy.termscheduler.terms
 * Path: app/src/main/java/com/ebookfrenzy/termscheduler/terms
 * Created At: 2023-11-27 1:31 AM
 * Created By: flowmar
 *******************/
public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder>
    {

        // ********************************************Fields**************************************************/

        private List<Term>          termsList = new ArrayList<>();
        private OnItemClickListener listener;


        // ********************************************Methods**************************************************/
        @Override
        @NonNull
        public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                LayoutInflater      layoutInflater = LayoutInflater.from(parent.getContext());
                TermListItemBinding binding        = TermListItemBinding.inflate(layoutInflater, parent, false);

                return new TermViewHolder(binding);
            }

        @Override
        public void onBindViewHolder(@NonNull TermViewHolder holder, int position)
            {
                Term currentTerm = termsList.get(position);
                holder.binding.termListTermNameTextView.setText(currentTerm.getName());
            }

        @Override
        public int getItemCount()
            {
                return termsList.size();
            }

        public void setTermsList(List<Term> termsList)
            {
                List<Term> safeTermsList = new ArrayList<>(termsList);

                this.termsList = safeTermsList;
                notifyDataSetChanged();
            }

        public Term getTermAt(int position)
            {
                return termsList.get(position);
            }

        public void setOnItemClickListener(OnItemClickListener listener)
            {
                this.listener = listener;
            }

        public interface OnItemClickListener
            {
                void onItemClick(Term term);
            }

        // ********************************************Static Classes**************************************************/
        class TermViewHolder extends RecyclerView.ViewHolder
            {
                private final TermListItemBinding binding;


                public TermViewHolder(TermListItemBinding binding)
                    {

                        super(binding.getRoot());
                        this.binding = binding;

                        binding.getRoot().setOnClickListener(v -> {
                            int position = getAbsoluteAdapterPosition();
                            if (listener != null && position != RecyclerView.NO_POSITION)
                            {
                                listener.onItemClick(termsList.get(position));
                            }
                        });
                    }
            }
    }
