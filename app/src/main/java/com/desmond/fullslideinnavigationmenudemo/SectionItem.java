package com.desmond.fullslideinnavigationmenudemo;

/**
 * Created by desmond on 28/7/14.
 */
public class SectionItem {
    private long id;
    private String title;
    private String icon;

    public SectionItem() {}

    public SectionItem(long id, String title, String icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
