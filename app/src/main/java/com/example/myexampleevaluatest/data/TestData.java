package com.example.myexampleevaluatest.data;

import java.io.Serializable;

public class TestData implements Serializable {


    //definimos las variables
    private String userID;

    private String strTestTitle;
    private String strTestDuration;
    private String strTestCategory;
    private String courseDescription;

    private String testTypeTest;
    private int id;
    private String identificadorUnicoDelTest;
    public String getIdentificadorUnicoDelTest() {
        return identificadorUnicoDelTest;
    }

    public void setIdentificadorUnicoDelTest(String identificadorUnicoDelTest) {
        this.identificadorUnicoDelTest = identificadorUnicoDelTest;
    }



    // creating getter and setter methods
    public String getStrTestTitle() { return strTestTitle; }

    public void setStrTestTitle(String strTestTitle)
    {
        this.strTestTitle = strTestTitle;
    }

    public String getStrTestDuration()
    {
        return strTestDuration;
    }

    public void setStrTestDuration(String strTestDuration)
    {
        this.strTestDuration = strTestDuration;
    }

    public String getStrTestCategory() { return strTestCategory; }

    public void setStrTestCategory(String strTestCategory)
    {
        this.strTestCategory = strTestCategory;
    }

    public String getCourseDescription()
    {
        return courseDescription;
    }

    public void
    setCourseDescription(String courseDescription)
    {
        this.courseDescription = courseDescription;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUserID() {
        return userID;
    }

    public String getTestTypeTest() {
        return testTypeTest;
    }

    public void setTestTypeTest(String testTypeTest) {
        this.testTypeTest = testTypeTest;
    }

    public void setUserID(String userID) {
        userID = userID;
    }

    // constructor
    public TestData(String strTestTitle,
                       String strTestDuration,
                       String strTestCategory,
                       String courseDescription,
                       String userID,
                       String typeTest,
                        String idUnico)
    {
        this.strTestTitle = strTestTitle;
        this.strTestDuration = strTestDuration;
        this.strTestCategory = strTestCategory;
        this.courseDescription = courseDescription;
        this.userID = userID;
        this.testTypeTest = typeTest;
        this.identificadorUnicoDelTest = idUnico;
    }


}
