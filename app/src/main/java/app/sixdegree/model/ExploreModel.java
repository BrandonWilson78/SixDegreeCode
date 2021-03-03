package app.sixdegree.model;

public class ExploreModel {
    String imageLink;
    String date;
    String trails;

    public ExploreModel(String imageLink, String date, String trails) {
        this.imageLink = imageLink;
        this.date = date;
        this.trails = trails;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrails() {
        return trails;
    }

    public void setTrails(String trails) {
        this.trails = trails;
    }
}
