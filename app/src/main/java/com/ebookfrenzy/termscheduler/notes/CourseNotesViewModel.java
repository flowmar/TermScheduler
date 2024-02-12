package com.ebookfrenzy.termscheduler.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ebookfrenzy.termscheduler.courses.Course;
import com.ebookfrenzy.termscheduler.courses.CourseRepository;

import java.util.List;

/******************
 * Project: TermScheduler
 * Created At: 2023-12-16 7:28 PM
 * Created By: flowmar
 *
 *
 *******************/
public class CourseNotesViewModel extends AndroidViewModel
    {
        private final List<Course>     coursesList;
        private final CourseRepository mCourseRepository = new CourseRepository(getApplication());

        public CourseNotesViewModel(@NonNull Application application) throws InterruptedException
            {
                super(application);
                LiveData<List<Course>> allCourses = mCourseRepository.getAllCourses();
                coursesList = allCourses.getValue();
            }

        public List<Course> getCoursesList()
            {
                return coursesList;
            }

        LiveData<List<Course>> getAllCourses()
            {
                return mCourseRepository.getAllCourses();
            }

    }

