package com.example.quizzer;

public class QuestionModel {
    private String question;
    private String optiona;

    public QuestionModel(String question, String optiona, int setNo, String optionb, String optionc, String optiond, String correctans) {
        this.question = question;
        this.optiona = optiona;
        this.setNo = setNo;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
        this.correctans = correctans;
    }

    public int getSetNo() {
        return setNo;
    }

    public void setSetNo(int setNo) {
        this.setNo = setNo;
    }

    private int setNo;


    public QuestionModel() {
    }

    private String optionb;
    private String optionc;
    private String optiond;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptiona() {
        return optiona;
    }

    public void setOptiona(String optiona) {
        this.optiona = optiona;
    }

    public String getOptionb() {
        return optionb;
    }

    public void setOptionb(String optionb) {
        this.optionb = optionb;
    }

    public String getOptionc() {
        return optionc;
    }

    public void setOptionc(String optionc) {
        this.optionc = optionc;
    }

    public String getOptiond() {
        return optiond;
    }

    public void setOptiond(String optiond) {
        this.optiond = optiond;
    }

    public String getCorrectans() {
        return correctans;
    }

    public void setCorrectans(String correctans) {
        this.correctans = correctans;
    }

    private String correctans;
}
