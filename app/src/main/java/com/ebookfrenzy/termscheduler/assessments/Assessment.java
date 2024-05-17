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
    tableName = "assessments",
    foreignKeys = {
      @ForeignKey(
          entity = Course.class,
          parentColumns = "course_id",
          childColumns = "courseId",
          onDelete = ForeignKey.RESTRICT)
    },
    indices = {@Index("courseId"), @Index("termId")})
public class Assessment {

  // **********************************************Fields********************************************/

  /** The {@code id} */
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "assessment_id")
  private int id;

  /** The {@code assessmentType} */
  @ColumnInfo(name = "assessment_type")
  private String assessmentType;

  /** The {@code assessmentTitle} */
  @ColumnInfo(name = "assessment_title")
  private String assessmentTitle;

  /** The {@code assessmentStartDate} */
  @ColumnInfo(name = "assessment_start_date")
  private String assessmentStartDate;

  /** The {@code assessmentEndDate} */
  @ColumnInfo(name = "assessment_end_date")
  private String assessmentEndDate;

  /** The {@code courseId} */
  @ColumnInfo(name = "courseId")
  private int courseId;

  /** The {@code courseName} */
  @ColumnInfo(name = "courseName")
  private String courseName;

  /** The {@code termName} */
  @ColumnInfo(name = "termName")
  private String termName;

  /** The {@code termId} */
  @ColumnInfo(name = "termId")
  private int termId;

  /** The {@code assessmentStartDateTimeAlarm} */
  @ColumnInfo(name = "assessmentStartDateTimeAlarm")
  private String assessmentStartDateTimeAlarm;

  /** The {@code assessmentEndDateTimeAlarm} */
  @ColumnInfo(name = "assessmentEndDateTimeAlarm")
  private String assessmentEndDateTimeAlarm;

  /** Constructor to initialize {@link Assessment} */
  // **********************************************Constructors********************************************/
  public Assessment(
      String assessmentType,
      String assessmentTitle,
      String assessmentStartDate,
      String assessmentEndDate,
      int courseId,
      String courseName,
      int termId,
      String termName,
      String assessmentStartDateTimeAlarm,
      String assessmentEndDateTimeAlarm) {
    this.assessmentType = assessmentType;
    this.assessmentTitle = assessmentTitle;
    this.assessmentStartDate = assessmentStartDate;
    this.assessmentEndDate = assessmentEndDate;
    this.courseId = courseId;
    this.courseName = courseName;
    this.termName = termName;
    this.termId = termId;
    this.assessmentStartDateTimeAlarm = assessmentStartDateTimeAlarm;
    this.assessmentEndDateTimeAlarm = assessmentEndDateTimeAlarm;
  }

  /** Constructor to initialize {@link Assessment} */
  public Assessment() {}

  // **********************************************Methods********************************************/

  private static long getStartDateAsLong(String startDate) {
    return Long.parseLong(String.valueOf(Integer.parseInt(startDate)));
  }

  private static long getEndDateAsLong(String endDate) {
    return Long.parseLong(String.valueOf(Integer.parseInt(endDate)));
  }

  /**
   * @return the id as int
   */
  public int getId() {
    return id;
  }

  /**
   * Set the {@code id}
   *
   * @param id: The {@code id}
   */
  public void setId(@NonNull int id) {
    this.id = id;
  }

  /**
   * @return the assessmentType as {@link String}
   */
  String getAssessmentType() {
    return assessmentType;
  }

  /**
   * Set the {@code assessmentType}
   *
   * @param assessmentType: The {@code assessmentType}
   */
  void setAssessmentType(String assessmentType) {
    this.assessmentType = assessmentType;
  }

  /**
   * @return the assessmentTitle as {@link String}
   */
  String getAssessmentTitle() {
    return assessmentTitle;
  }

  /**
   * Set the {@code assessmentTitle}
   *
   * @param assessmentTitle: The {@code assessmentTitle}
   */
  void setAssessmentTitle(String assessmentTitle) {
    this.assessmentTitle = assessmentTitle;
  }

  /**
   * @return the assessmentStartDate as {@link String}
   */
  String getAssessmentStartDate() {
    return assessmentStartDate;
  }

  /**
   * Set the {@code assessmentStartDate}
   *
   * @param assessmentStartDate: The {@code assessmentStartDate}
   */
  void setAssessmentStartDate(String assessmentStartDate) {
    this.assessmentStartDate = assessmentStartDate;
  }

  /**
   * @return the assessmentEndDate as {@link String}
   */
  String getAssessmentEndDate() {
    return assessmentEndDate;
  }

  /**
   * Set the {@code assessmentEndDate}
   *
   * @param assessmentEndDate: The {@code assessmentEndDate}
   */
  void setAssessmentEndDate(String assessmentEndDate) {
    this.assessmentEndDate = assessmentEndDate;
  }

  /**
   * @return the courseId as int
   */
  public int getCourseId() {
    return courseId;
  }

  /**
   * Set the {@code courseId}
   *
   * @param courseId: The {@code courseId}
   */
  public void setCourseId(int courseId) {
    this.courseId = courseId;
  }

  /**
   * @return the courseName as {@link String}
   */
  public String getCourseName() {
    return courseName;
  }

  /**
   * Set the {@code courseName}
   *
   * @param courseName: The {@code courseName}
   */
  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  /**
   * @return the termName as {@link String}
   */
  public String getTermName() {
    return termName;
  }

  /**
   * Set the {@code termName}
   *
   * @param termName: The {@code termName}
   */
  public void setTermName(String termName) {
    this.termName = termName;
  }

  /**
   * @return the termId as int
   */
  int getTermId() {
    return termId;
  }

  /**
   * Set the {@code termId}
   *
   * @param termId: The {@code termId}
   */
  void setTermId(int termId) {
    this.termId = termId;
  }

  /**
   * @return the assessmentStartDateTimeAlarm as {@link String}
   */
  public String getAssessmentStartDateTimeAlarm() {
    return assessmentStartDateTimeAlarm;
  }

  /**
   * Set the {@code assessmentStartDateTimeAlarm}
   *
   * @param assessmentStartDateTimeAlarm: The {@code assessmentStartDateTimeAlarm}
   */
  public void setAssessmentStartDateTimeAlarm(String assessmentStartDateTimeAlarm) {
    this.assessmentStartDateTimeAlarm = assessmentStartDateTimeAlarm;
  }

  /**
   * @return the assessmentEndDateTimeAlarm as {@link String}
   */
  public String getAssessmentEndDateTimeAlarm() {
    return assessmentEndDateTimeAlarm;
  }

  /**
   * Set the {@code assessmentEndDateTimeAlarm}
   *
   * @param assessmentEndDateTimeAlarm: The {@code assessmentEndDateTimeAlarm}
   */
  public void setAssessmentEndDateTimeAlarm(String assessmentEndDateTimeAlarm) {
    this.assessmentEndDateTimeAlarm = assessmentEndDateTimeAlarm;
  }
}
