package com.ebookfrenzy.termscheduler.courses;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.ebookfrenzy.termscheduler.terms.Term;

/******************
 * Project: TermScheduler
 * Created At: 2023-11-15 8:46AM
 * Created By: flowmar
 *
 *
 *******************/
@Entity(
		tableName = "courses",
		foreignKeys = {
				@ForeignKey(
						entity = Term.class,
						parentColumns = "term_id",
						childColumns = "termId",
						onDelete = ForeignKey.RESTRICT)
		},
		indices = {@Index("termId")})
public class Course {
	// **********************************************Fields********************************************/
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "course_id")
	private int id;

	@ColumnInfo(name = "course_name")
	private String courseName;

	@ColumnInfo(name = "status")
	private String status;

	@ColumnInfo(name = "instructor_name")
	private String instructorName;

	@ColumnInfo(name = "instructor_phone_number")
	private String instructorPhoneNumber;

	@ColumnInfo(name = "instructor_email")
	private String instructorEmail;

	@ColumnInfo(name = "course_note")
	@Nullable
	private String courseNote;

	@ColumnInfo(name = "termId")
	private int termId;

	@ColumnInfo(name = "termName")
	private String termName;

	@ColumnInfo(name = "course_start_date")
	private String startDate;

	@ColumnInfo(name = "course_end_date")
	private String endDate;

	@ColumnInfo(name = "course_start_alarm_datetime")
	private String courseStartAlarmDatetime;

	@ColumnInfo(name = "course_end_alarm_datetime")
	private String courseEndAlarmDatetime;

	// **********************************************Constructors********************************************/
	public Course(
			String courseName,
			String startDate,
			String endDate,
			String status,
			String instructorName,
			String instructorPhoneNumber,
			String instructorEmail,
			@Nullable String courseNote,
			String termName,
			int termId,
			String courseStartAlarmDatetime,
			String courseEndAlarmDatetime) {

		this.courseName               = courseName;
		this.startDate                = startDate;
		this.endDate                  = endDate;
		this.status                   = status;
		this.instructorName           = instructorName;
		this.instructorPhoneNumber    = instructorPhoneNumber;
		this.instructorEmail          = instructorEmail;
		this.courseNote               = courseNote;
		this.termName                 = termName;
		this.termId                   = termId;
		this.courseStartAlarmDatetime = courseStartAlarmDatetime;
		this.courseEndAlarmDatetime   = courseEndAlarmDatetime;
	}

	public Course() {}

	public String getCourseStartAlarmDatetime() {
		return courseStartAlarmDatetime;
	}

	public void setCourseStartAlarmDatetime(String courseStartAlarmDatetime) {
		this.courseStartAlarmDatetime = courseStartAlarmDatetime;
	}

	// **********************************************Methods********************************************/

	public String getCourseEndAlarmDatetime() {
		return courseEndAlarmDatetime;
	}

	public void setCourseEndAlarmDatetime(String courseEndAlarmDatetime) {
		this.courseEndAlarmDatetime = courseEndAlarmDatetime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	String getStartDate() {
		return startDate;
	}

	void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	String getEndDate() {
		return endDate;
	}

	void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	String getStatus() {
		return status;
	}

	void setStatus(String status) {
		this.status = status;
	}

	public String getInstructorName() {
		return instructorName;
	}

	void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	String getInstructorPhoneNumber() {
		return instructorPhoneNumber;
	}

	void setInstructorPhoneNumber(String instructorPhoneNumber) {
		this.instructorPhoneNumber = instructorPhoneNumber;
	}

	String getInstructorEmail() {
		return instructorEmail;
	}

	void setInstructorEmail(String instructorEmail) {
		this.instructorEmail = instructorEmail;
	}

	@Nullable
	public String getCourseNote() {
		return courseNote;
	}

	void setCourseNote(@Nullable String courseNote) {
		this.courseNote = courseNote;
	}

	int getTermId() {
		return termId;
	}

	void setTermId(int termId) {
		this.termId = termId;
	}
}
