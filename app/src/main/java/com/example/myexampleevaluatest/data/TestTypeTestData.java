package com.example.myexampleevaluatest.data;

public class TestTypeTestData {

    //variables para el test de TestType
    private int id;
    private String question;
    private String optionOne;

    private String optionTwo;
    private String optionThree;
    private String optionFour;

    private String totalQuestion;

    private String identificadorUnicoDelTestTT;

    public String getIdentificadorUnicoDelTestTT() {
        return identificadorUnicoDelTestTT;
    }

    public void setIdentificadorUnicoDelTestTF(String identificadorUnicoDelTestTF) {
        this.identificadorUnicoDelTestTT = identificadorUnicoDelTestTF;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public void setOptionOne(String optionOne) {
        this.optionOne = optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(String optionTwo) {
        this.optionTwo = optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public void setOptionThree(String optionThree) {
        this.optionThree = optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public void setOptionFour(String optionFour) {
        this.optionFour = optionFour;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public TestTypeTestData(int id, String question, String optionOne, String optionTwo,
                            String optionThree, String optionFour, String identificadorUnicoDelTestTT) {
        this.id = id;
        this.question = question;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.identificadorUnicoDelTestTT = identificadorUnicoDelTestTT;
    }

}
