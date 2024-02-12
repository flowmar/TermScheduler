package com.ebookfrenzy.termscheduler.terms;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.ebookfrenzy.termscheduler.SchedulerRoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-24 1:30 AM
 * Created By: flowmar
 *
 *
 *******************/
public class TermRepository
    {

        // ------------------------------ FIELDS ------------------------------

        private static final int                  THREAD_SLEEP_TIME = 500;
        private final        TermDao              mTermDao;
        private final        LiveData<List<Term>> allTerms;
        private              List<Term>           termsList;

        // --------------------------- CONSTRUCTORS ---------------------------

        /**
         * Constructor
         *
         * @param application The application context
         */
        public TermRepository(Application application)
            {
                SchedulerRoomDatabase database = SchedulerRoomDatabase.getInstance(application);
                this.mTermDao  = database.termDao();
                this.allTerms  = this.mTermDao.getAllTerms();
                this.termsList = getTermsList();

                // Delay to ensure that the async tasks have time to be executed
                try
                    {
                        Thread.sleep(THREAD_SLEEP_TIME);
                    }
                catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
            }

        // -------------------------- OTHER METHODS --------------------------

        public List<Term> getTermsList()
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() ->
                                         termsList = this.mTermDao.getTermsList());
                List<Term> safeTermsList = termsList;
                executor.shutdown();
                return safeTermsList;
            }

        /**
         * Inserts a Term into the database.
         *
         * @param term the Term object to be inserted
         */
        public void insert(Term term)
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> this.mTermDao.insertTerm(term));
                executor.shutdown();
            }

        /**
         * Updates the specified term in the database.
         *
         * @param term the term to be updated
         */
        public void update(Term term)

            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> this.mTermDao.updateTerm(term));
                executor.shutdown();
            }

        /**
         * Deletes a term with the specified name.
         *
         * @param term the name of the term to be deleted
         */
        public void delete(Context context, Term term)
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() ->
                                     {
                                         boolean isDeletionSuccessful = false;
                                         try
                                             {
                                                 this.mTermDao.deleteTerm(term);
                                                 isDeletionSuccessful = true;
                                             }
                                         catch (SQLiteConstraintException e)
                                             {
                                                 String message = "Cannot delete term: " + term.getName() +
                                                         ", since it has courses associated with it.";
                                                 new Handler(Looper.getMainLooper()).post(
                                                         () -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
                                                 isDeletionSuccessful = false;
                                             }

                                         if (isDeletionSuccessful)
                                             {
                                                 new Handler(Looper.getMainLooper()).post(
                                                         () -> Toast.makeText(context, "Term deleted: " + term.getName(), Toast.LENGTH_LONG).show());
                                             }
                                     });
                executor.shutdown();
            }

        /**
         * Gets all terms.
         *
         * @return the list of terms
         */
        public LiveData<List<Term>> getAllTerms()
            {
                return this.allTerms;
            }
    }
