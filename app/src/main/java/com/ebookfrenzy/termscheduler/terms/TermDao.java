package com.ebookfrenzy.termscheduler.terms;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-23 3:45AM
 * Created By: flowmar
 *
 *******************/
@Dao
public interface TermDao
    {
        /**
         * Retrieves all terms from the database.
         *
         * @return A LiveData object containing a list of Term objects.
         */
        @Query("SELECT * FROM terms")
        LiveData<List<Term>> getAllTerms();

        @Query("SELECT * FROM terms")
        List<Term> getTermsList();

        /**
         * Retrieves a single term from the database.
         *
         * @param termName The name of the term to retrieve.
         *
         * @return A LiveData object containing a single Term object.
         */
        @Query("SELECT * FROM terms WHERE term_name = :termName")
        LiveData<Term> getTerm(String termName);

        /**
         * Inserts a new term into the database
         *
         * @param term The term to insert
         */
        @Insert
        void insertTerm(Term term);

        /**
         * Updates a term in the database
         */
        @Update
        void updateTerm(Term term);

        @Delete
        void deleteTerm(Term term) throws SQLiteConstraintException;

        /**
         * Deletes a term from the database
         *
         * @param termName Name of the term to delete
         */
        @Query("DELETE FROM terms WHERE term_name = :termName")
        void deleteTermByName(String termName);
    }
