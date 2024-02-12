package com.ebookfrenzy.termscheduler.terms;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/******************
 * Created Using: Android Studio
 * Project: TermScheduler
 * Package: com.ebookfrenzy.termscheduler.terms
 * Path: app/src/main/java/com/ebookfrenzy/termscheduler/terms
 * Created At: 2023-11-26 9:40 AM
 * Created By: flowmar
 *******************/
public class TermViewModel extends AndroidViewModel
    {

        //**********************************************Fields**************************************************
        private final TermRepository       mTermRepository;
        private final LiveData<List<Term>> allTerms;

        private final List<Term> termsList;


        //**********************************************Constructors**************************************************
        public TermViewModel(@NonNull Application application)
            {
                super(application);
                mTermRepository = new TermRepository(application);

                allTerms  = mTermRepository.getAllTerms();
                termsList = mTermRepository.getTermsList();
            }

        /**********************************************Methods**************************************************/
        public void insert(Term term)
            {
                mTermRepository.insert(term);
            }

        public void update(Term term)
            {
                mTermRepository.update(term);
            }

        public void delete(Context context, Term term)
            {
                mTermRepository.delete(context, term);
            }

        public LiveData<List<Term>> getAllTerms()
            {
                return allTerms;
            }

        public List<Term> getTermsList() {return termsList;}
    }
