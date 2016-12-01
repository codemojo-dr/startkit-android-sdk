package io.codemojo.sdk.models;

import java.io.Serializable;

/**
 * Created by shoaib on 02/12/16.
 */

public class Milestone implements Serializable {

    private int image = 0;
    private String milestoneText;

    public Milestone(){}

    public Milestone(String milestoneText) {
        this.milestoneText = milestoneText;
    }

    public Milestone(int image, String milestoneText) {
        this.image = image;
        this.milestoneText = milestoneText;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMilestoneText() {
        return milestoneText;
    }

    public void setMilestoneText(String milestoneText) {
        this.milestoneText = milestoneText;
    }
}
