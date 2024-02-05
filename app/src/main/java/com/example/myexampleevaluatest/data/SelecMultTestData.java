package com.example.myexampleevaluatest.data;

public class SelecMultTestData {

    //variables para el test de TrueFalse
    private int id;
    private String identificadorUnicoDelTestSM;
    private String question;
    private String option_one;
    private String option_two;
    private String option_three;
    private String option_four;

    private String totalQuestion;


    public String getIdentificadorUnicoDelTestTF() {
        return identificadorUnicoDelTestSM;
    }

    public void setIdentificadorUnicoDelTestTF(String identificadorUnicoDelTestTF) {
        this.identificadorUnicoDelTestSM = identificadorUnicoDelTestTF;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_one() {
        return option_one;
    }

    public void setOption_one(String option_one) {
        this.option_one = option_one;
    }

    public String getOption_two() {
        return option_two;
    }

    public void setOption_two(String option_two) {
        this.option_two = option_two;
    }

    public String getOption_three() {
        return option_three;
    }

    public void setOption_three(String option_three) {
        this.option_three = option_three;
    }

    public String getOption_four() {
        return option_four;
    }

    public void setOption_four(String option_four) {
        this.option_four = option_four;
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

    public SelecMultTestData(int id, String identificadorUnicoDelTestSM,String question, String option_one, String option_two,
                             String option_three, String option_four) {
        this.id = id;
        this.identificadorUnicoDelTestSM = identificadorUnicoDelTestSM;
        this.question = question;
        this.option_one = option_one;
        this.option_two = option_two;
        this.option_three = option_three;
        this.option_four = option_four;

    }

}
