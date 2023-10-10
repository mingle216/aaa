package com.wisedu.amp.card.sys.searchResults.model.faq;


public class Faq {

    private String pinyingFirstChar;

    private String question;
    private String answer;
    private String serviceItemName;
    private String serviceItemWid;

    private String headChar; //首字母
    private String order;//自定义：排序值（用于本地排序）

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public String getServiceItemWid() {
        return serviceItemWid;
    }

    public void setServiceItemWid(String serviceItemWid) {
        this.serviceItemWid = serviceItemWid;
    }

    public String getPinyingFirstChar() {
        return pinyingFirstChar;
    }

    public void setPinyingFirstChar(String pinyingFirstChar) {
        this.pinyingFirstChar = pinyingFirstChar;
    }

    public String getHeadChar() {
        return headChar;
    }

    public void setHeadChar(String headChar) {
        this.headChar = headChar;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
