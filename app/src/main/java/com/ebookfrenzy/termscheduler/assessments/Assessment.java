package com.ebookfrenzy.termscheduler.assessments;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.ebookfrenzy.termscheduler.courses.Course;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-23 8:27AM
 * Created By: flowmar
 *
 *
 *******************/
@Entity(
        tableName = "assessments", foreignKeys = {
        @ForeignKey(entity = Course.class, parentColumns = "course_id"
                , childColumns = "courseId", onDelete = ForeignKey.RESTRICT)
}, indices = {@Index("courseId"), @Index("termId")})
public class Assessment
    {

        //**********************************************Fields********************************************/


        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "assessment_id")
        private int    id;
        @ColumnInfo(name = "assessment_type")
        private String assessmentType;
        @ColumnInfo(name = "assessment_title")
        private String assessmentTitle;

        @ColumnInfo(name = "assessment_start_date")
        private String assessmentStartDate;

        @ColumnInfo(name = "assessment_end_date")
        private String assessmentEndDate;
        @ColumnInfo(name = "courseId")
        private int    courseId;
        @ColumnInfo(name = "courseName")
        private String courseName;
        @ColumnInfo(name = "termName")
        private String termName;
        @ColumnInfo(name = "termId")
        private int    termId;

        @ColumnInfo(name = "assessmentStartDateTimeAlarm")
        private String assessmentStartDateTimeAlarm;

        @ColumnInfo(name = "assessmentEndDateTimeAlarm")
        private String assessmentEndDateTimeAlarm;

        //**********************************************Constructors********************************************/
        public Assessment(String assessmentType, String assessmentTitle, String assessmentStartDate, String assessmentEndDate, int courseId,
                          String courseName, int termId, String termName, String assessmentStartDateTimeAlarm, String assessmentEndDateTimeAlarm)
            {
                this.assessmentType               = assessmentType;
                this.assessmentTitle              = assessmentTitle;
                this.assessmentStartDate          = assessmentStartDate;
                this.assessmentEndDate            = assessmentEndDate;
                this.courseId                     = courseId;
                this.courseName                   = courseName;
                this.termName                     = termName;
                this.termId                       = termId;
                this.assessmentStartDateTimeAlarm = assessmentStartDateTimeAlarm;
                this.assessmentEndDateTimeAlarm   = assessmentEndDateTimeAlarm;

            }

        public Assessment() {}


        //**********************************************Methods********************************************/

        private static long getStartDateAsLong(String startDate)
            {
                return Long.parseLong(String.valueOf(Integer.parseInt(startDate)));
            }

        private static long getEndDateAsLong(String endDate)
            {
                return Long.parseLong(String.valueOf(Integer.parseInt(endDate)));
            }

        public int getId()
            {
                return id;
            }

        public void setId(@NonNull int id)
            {
                this.id = id;
            }

        String getAssessmentType()
            {
                return assessmentType;
            }

        void setAssessmentType(String assessmentType)
            {
                this.assessmentType = assessmentType;
            }

        String getAssessmentTitle()
            {
                return assessmentTitle;
            }

        void setAssessmentTitle(String assessmentTitle)
            {
                this.assessmentTitle = assessmentTitle;
            }

        String getAssessmentStartDate()                         {return assessmentStartDate;}

        void setAssessmentStartDate(String assessmentStartDate) {this.assessmentStartDate = assessmentStartDate;}

        String getAssessmentEndDate()
            {
                return assessmentEndDate;
            }

        void setAssessmentEndDate(String assessmentEndDate)
            {
                this.assessmentEndDate = assessmentEndDate;
            }

        public int getCourseId()
            {
                return courseId;
            }

        public void setCourseId(int courseId)
            {
                this.courseId = courseId;
            }

        public String getCourseName()
            {
                return courseName;
            }

        public void setCourseName(String courseName)
            {
                this.courseName = courseName;
            }

        public String getTermName()
            {
                return termName;
            }

        public void setTermName(String termName)
            {
                this.termName = termName;
            }

        int getTermId()
            {
                return termId;
            }

        void setTermId(int termId)
            {
                this.termId = termId;
            }

        public String getAssessmentStartDateTimeAlarm()
            {
                return assessmentStartDateTimeAlarm;
            }

        public void setAssessmentStartDateTimeAlarm(String assessmentStartDateTimeAlarm)
            {
                this.assessmentStartDateTimeAlarm = assessmentStartDateTimeAlarm;
            }

        public String getAssessmentEndDateTimeAlarm()
            {
                return assessmentEndDateTimeAlarm;
            }

        public void setAssessmentEndDateTimeAlarm(String assessmentEndDateTimeAlarm)
            {
                this.assessmentEndDateTimeAlarm = assessmentEndDateTimeAlarm;
            }


    }
