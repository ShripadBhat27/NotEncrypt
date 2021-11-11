package com.example.notesapptutorial;

public class firebasemodel {

    private String title;
    private String content;
    private int dd,mm,yyyy;



    public firebasemodel()
    {

    }

    public firebasemodel(String title, String content, int dd, int mm, int yyyy) {
        this.title = title;
        this.content = content;
        this.dd = dd;
        this.mm = mm;
        this.yyyy = yyyy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public int getYyyy() {
        return yyyy;
    }

    public void setYyyy(int yyyy) {
        this.yyyy = yyyy;
    }
}

