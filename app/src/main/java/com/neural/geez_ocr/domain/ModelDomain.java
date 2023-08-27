package com.neural.geez_ocr.domain;

public class ModelDomain {
    private String title;
    private String pic;
    private String description;
    private String trained;


    private String scanBtn_text;

    public ModelDomain(String title, String pic, String description, String trained, String scanBtn_text) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.trained = trained;
        this.scanBtn_text = scanBtn_text;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrained() {
        return trained;
    }

    public void setTrained(String trained) {
        this.trained = trained;
    }

    public String getScanBtn_text() {
        return scanBtn_text;
    }

    public void setScanBtn_text(String scanBtn_text) {
        this.scanBtn_text = scanBtn_text;
    }

}
