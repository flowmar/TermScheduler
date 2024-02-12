package com.ebookfrenzy.termscheduler.terms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.ebookfrenzy.termscheduler.databinding.ActivityTermListBinding;

public class TermListActivity extends AppCompatActivity
    {
    
        ActivityTermListBinding binding;
        private TermViewModel mTermViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                this.binding = ActivityTermListBinding.inflate(this.getLayoutInflater());
                View view = this.binding.getRoot();
                this.setContentView(view);

                // Create a listener for the AddNewTerm button
                this.binding.buttonAddTerm.setOnClickListener(v ->
                                                                  {
                                                                      Intent intent = new Intent(this,
                                                                                                 AddEditTermActivity.class
                                                                      );
                                                                      this.startActivity(intent);
                                                                  });


                // Create a reference to the RecyclerView
                RecyclerView termRecyclerView = this.binding.termListRecyclerview;

                // Set a LinearLayoutManager on the RecyclerView
                termRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                // Set the size of the RecyclerView to be fixed (size on the screen, not the number of items)
                termRecyclerView.setHasFixedSize(true);

                // Create a TermAdapter and set it to manage the RecyclerView
                TermAdapter termAdapter = new TermAdapter();
                termRecyclerView.setAdapter(termAdapter);

                // Get a reference to the TermViewModel by creating a new ViewModelProvider instance
                mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);

                // Create an ItemTouchHelper to implement the swipe to delete functionality
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
                    {


                        @Override
                        public boolean onMove(
                                @NonNull RecyclerView recyclerView,
                                @NonNull ViewHolder viewHolder,
                                @NonNull ViewHolder target
                                             )
                            {
                                return false;
                            }

                        /**
                         * Called when a ViewHolder is swiped by the user.
                         *
                         * @param viewHolder The ViewHolder which has been swiped by the user.
                         * @param direction  The direction to which the ViewHolder is swiped.
                         */
                        @Override
                        public void onSwiped(@NonNull ViewHolder viewHolder, int direction)
                            {
                                int  position = viewHolder.getAbsoluteAdapterPosition();
                                Term term     = termAdapter.getTermAt(position);

                                TermListActivity.this.mTermViewModel.delete(
                                        getApplicationContext(), term);

                                termAdapter.notifyItemChanged(position);

                            }
                    }).attachToRecyclerView(termRecyclerView);

                // Set an OnItemClickListener on the individual items in the RecyclerView
                termAdapter.setOnItemClickListener(term ->
                                                       {
                                                           // Create a new intent to launch the AddEditTermActivity
                                                           // (Previously, AddTermActivity.
                                                           // When using view binding, if you change the name of a
                                                           // class, you must also update
                                                           // the name of the corresponding layout file (in this
                                                           // case: add_term_activity.xml ->
                                                           // add_edit_term_activity.xml) or you will get an error
                                                           // that the Binding class cannot
                                                           // be found.
                                                           Intent intent = new Intent(TermListActivity.this,
                                                                                      AddEditTermActivity.class
                                                           );

                                                           // Put the various Term object fields into the Intent as
                                                           // Intent Extras.
                                                           intent.putExtra(AddEditTermActivity.EXTRA_TERM_ID,
                                                                           term.getId()
                                                                          );
                                                           intent.putExtra(AddEditTermActivity.EXTRA_TERM_NAME,
                                                                           term.getName()
                                                                          );
                                                           intent.putExtra(AddEditTermActivity.EXTRA_TERM_START_DATE,
                                                                           term.getStartDate()
                                                                          );
                                                           intent.putExtra(AddEditTermActivity.EXTRA_TERM_END_DATE,
                                                                           term.getEndDate()
                                                                          );
                                                           TermListActivity.this.startActivity(intent);
                                                       });


                // Set up an observer to observe the list of terms (pass in the list of terms)
                // A lambda is used here, but a new Observer<List<Term>> is created and
                // public void onChanged(List<Term> terms) is overridden
                // Use the termAdapter to re-set the TermsList every time there is a change
                mTermViewModel.getAllTerms().observe(this, termAdapter::setTermsList);
            }
    }
