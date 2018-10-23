package org.nick.java.model;

public class Variables {

    private boolean bpm_sendEmailNotifications;
    private String bpm_workflowPriority;
    private String bpm_assignee;

    public Variables(boolean bpm_sendEmailNotifications, String bpm_workflowPriority, String bpm_assignee) {
        this.bpm_sendEmailNotifications = bpm_sendEmailNotifications;
        this.bpm_workflowPriority = bpm_workflowPriority;
        this.bpm_assignee = bpm_assignee;
    }

    public boolean getBpm_sendEmailNotifications() {
        return bpm_sendEmailNotifications;
    }

    public void setBpm_sendEmailNotifications(boolean bpm_sendEmailNotifications) {
        this.bpm_sendEmailNotifications = bpm_sendEmailNotifications;
    }

    public String getBpm_workflowPriority() {
        return bpm_workflowPriority;
    }

    public void setBpm_workflowPriority(String bpm_workflowPriority) {
        this.bpm_workflowPriority = bpm_workflowPriority;
    }

    public String getBpm_assignee() {
        return bpm_assignee;
    }

    public void setBpm_assignee(String bpm_assignee) {
        this.bpm_assignee = bpm_assignee;
    }
}
