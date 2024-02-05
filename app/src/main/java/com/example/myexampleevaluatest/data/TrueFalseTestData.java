package com.example.myexampleevaluatest.data;

public class TrueFalseTestData {

    //variables para el test de TrueFalse

    private String question;
    private String correctAnswer;
    private int id;
    private String totalQuestion;

    private String identificadorUnicoDelTestTF;
    public String getIdentificadorUnicoDelTestTF() {
        return identificadorUnicoDelTestTF;
    }

    public void setIdentificadorUnicoDelTestTF(String identificadorUnicoDelTestTF) {
        this.identificadorUnicoDelTestTF = identificadorUnicoDelTestTF;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
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

    public TrueFalseTestData(int id, String question, String identificadorUnicoDelTestTF, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.identificadorUnicoDelTestTF = identificadorUnicoDelTestTF;
        this.correctAnswer = correctAnswer;
    }

}
