package isep.fr.collegeinformationsystem.model;

import java.io.Serializable;

public class StudentModel implements Serializable, Comparable<StudentModel> {

    private String userId;
    private String userName;
    private String userMail;
    private String userCourse;
    private String userType;
    private String userMobile;
    private String userGender;
    private String userProfile;
    private String userPass;

    public StudentModel() {

    }


    public StudentModel(String userId, String userName, String userMail, String userCourse, String userType, String userGender, String userMobile, String userProfile,String userPass) {

        this.userId = userId;
        this.userName = userName;
        this.userMail = userMail;
        this.userType = userType;
        this.userCourse = userCourse;
        this.userGender = userGender;
        this.userMobile = userMobile;
        this.userProfile = userProfile;
        this.userPass = userPass;


    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserCourse() {
        return userCourse;
    }

    public void setUserCourse(String userCourse) {
        this.userCourse = userCourse;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userCourse='" + userCourse + '\'' +
                ", userType='" + userType + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userPass='" + userPass + '\'' +
                '}';
    }




    @Override
    public int compareTo(StudentModel students) {

        if (students.getUserId() != null && this.getUserId() != null) {

            return this.getUserId().compareToIgnoreCase(students.getUserId());
        } else {
            return 0;
        }

    }
}
