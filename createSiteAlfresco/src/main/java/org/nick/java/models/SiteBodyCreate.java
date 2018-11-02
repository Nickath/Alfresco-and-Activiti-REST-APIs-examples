package org.nick.java.models;

public class SiteBodyCreate {

    private String id;
    private String title;
    private String description;
    private String visibility;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public SiteBodyCreate(String id, String title, String description, String visibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.visibility = visibility;
    }

    public SiteBodyCreate(){

    }

}
