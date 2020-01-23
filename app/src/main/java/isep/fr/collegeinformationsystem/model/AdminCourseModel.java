package isep.fr.collegeinformationsystem.model;

import java.io.Serializable;

public class AdminCourseModel implements Serializable, Comparable<AdminCourseModel> {


    private String courseId;
    private String courseName;
    private String courseShortName;
    private String coursePdfFile;
    private String subjects;

    public AdminCourseModel(String courseId, String courseName, String courseShortName, String coursePdfFile, String subjects) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseShortName = courseShortName;
        this.coursePdfFile = coursePdfFile;
        this.subjects = subjects;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseShortName() {
        return courseShortName;
    }

    public void setCourseShortName(String courseShortName) {
        this.courseShortName = courseShortName;
    }

    public String getCoursePdfFile() {
        return coursePdfFile;
    }

    public void setCoursePdfFile(String coursePdfFile) {
        this.coursePdfFile = coursePdfFile;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "AdminCourseModel{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseShortName='" + courseShortName + '\'' +
                ", coursePdfFile='" + coursePdfFile + '\'' +
                ", subjects='" + subjects + '\'' +
                '}';
    }


    @Override
    public int compareTo(AdminCourseModel adminCourseModel) {

        if (adminCourseModel.getCourseShortName() != null && this.getCourseShortName() != null) {

            return this.getCourseShortName().compareToIgnoreCase(adminCourseModel.getCourseShortName());
        } else {
            return 0;
        }

    }


}
