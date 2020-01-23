package isep.fr.collegeinformationsystem.model;

import java.io.Serializable;

public class ProfessorModel implements Serializable, Comparable<ProfessorModel>{

    private String profId;
    private String profName;
    private String profMail;
    private String profQualify;
    private String profCourse;
    private String profSubject;
    private String profMobile;
    private String profGender;
    private String profProfile;

    public ProfessorModel(String profId, String profName, String profMail, String profQualify, String profCourse, String profSubject, String profMobile, String profGender, String profProfile) {
        this.profId = profId;
        this.profName = profName;
        this.profMail = profMail;
        this.profQualify = profQualify;
        this.profCourse = profCourse;
        this.profSubject = profSubject;
        this.profMobile = profMobile;
        this.profGender = profGender;
        this.profProfile = profProfile;
    }



    public String getProfId() {
        return profId;
    }

    public void setProfId(String profId) {
        this.profId = profId;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public String getProfMail() {
        return profMail;
    }

    public void setProfMail(String profMail) {
        this.profMail = profMail;
    }

    public String getProfQualify() {
        return profQualify;
    }

    public void setProfQualify(String profQualify) {
        this.profQualify = profQualify;
    }

    public String getProfCourse() {
        return profCourse;
    }

    public void setProfCourse(String profCourse) {
        this.profCourse = profCourse;
    }

    public String getProfSubject() {
        return profSubject;
    }

    public void setProfSubject(String profSubject) {
        this.profSubject = profSubject;
    }

    public String getProfMobile() {
        return profMobile;
    }

    public void setProfMobile(String profMobile) {
        this.profMobile = profMobile;
    }

    public String getProfGender() {
        return profGender;
    }

    public void setProfGender(String profGender) {
        this.profGender = profGender;
    }

    public String getProfProfile() {
        return profProfile;
    }

    public void setProfProfile(String profProfile) {
        this.profProfile = profProfile;
    }

    @Override
    public String toString() {
        return "ProfessorModel{" +
                "profId='" + profId + '\'' +
                ", profName='" + profName + '\'' +
                ", profMail='" + profMail + '\'' +
                ", profQualify='" + profQualify + '\'' +
                ", profCourse='" + profCourse + '\'' +
                ", profSubject='" + profSubject + '\'' +
                ", profMobile='" + profMobile + '\'' +
                ", profGender='" + profGender + '\'' +
                ", profProfile='" + profProfile + '\'' +
                '}';
    }


    @Override
    public int compareTo(ProfessorModel professorModel) {

        if (professorModel.getProfId() != null && this.getProfId() != null) {

            return this.getProfId().compareToIgnoreCase(professorModel.getProfId());
        } else {
            return 0;
        }

    }


}
