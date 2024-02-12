package com.ebookfrenzy.termscheduler.courses;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/******************
 * Project: CourseScheduler
 * Created At: 2023-12-03 4:31 PM
 * Created By: flowmar
 *
 *
 *******************/
public class CourseViewModel extends AndroidViewModel
    {
        //**********************************************Fields**************************************************
        private final CourseRepository       mCourseRepository;
        private final LiveData<List<Course>> allCourses;

        private final List<Course>             coursesList;
        private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

        //**********************************************Constructors**************************************************
        public CourseViewModel(@NonNull Application application) throws InterruptedException
            {
                super(application);
                mCourseRepository = new CourseRepository(application);

                allCourses  = mCourseRepository.getAllCourses();
                coursesList = mCourseRepository.getCoursesList();
            }


        /**********************************************Methods**************************************************/
        public void insert(Course course)
            {
                mCourseRepository.insert(course);
            }


        public void update(Course course)
            {
                mCourseRepository.update(course);
            }

        public void delete(Context context, Course course)
            {
                mCourseRepository.delete(context, course);
            }

        public LiveData<Boolean> getDeleteResult()
            {
                return deleteResult;
            }


        LiveData<List<Course>> getAllCourses()
            {
                return allCourses;
            }

        public List<Course> getCoursesList() {return coursesList;}
    }
