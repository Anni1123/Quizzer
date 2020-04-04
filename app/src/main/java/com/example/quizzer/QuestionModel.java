package com.example.quizzer;

public class QuestionModel {
    private String question;
    private String optiona;

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

    public String getSetNo() {
        return setNo;
    }

    public void setSetNo(String setNo) {
        this.setNo = setNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    private String setNo;
    private String id;





    public QuestionModel() {
    }

    private String optionb;
    private String optionc;

    public QuestionModel(String question, String optiona, String setNo, String id, String optionb, String optionc, String optiond, String correctans) {
        this.question = question;
        this.optiona = optiona;
        this.setNo = setNo;
        this.id = id;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
        this.correctans = correctans;
    }

    private String optiond;

    private String correctans;
}