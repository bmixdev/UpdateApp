package com.wind.updateapp;

/**
 * Created by wind on 16/5/10.
 */
public class UpdateInfo {

    private String latestVersion;

    private String latestAppSize;

    private String latestUpdateContent;


    private String latestAppUrl;

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getLatestAppSize() {
        return latestAppSize;
    }

    public void setLatestAppSize(String latestAppSize) {
        this.latestAppSize = latestAppSize;
    }

    public String getLatestUpdateContent() {
        return latestUpdateContent;
    }

    public void setLatestUpdateContent(String latestUpdateContent) {
        this.latestUpdateContent = latestUpdateContent;
    }

    public String getLatestAppUrl() {
        return latestAppUrl;
    }

    public void setLatestAppUrl(String latestAppUrl) {
        this.latestAppUrl = latestAppUrl;
    }
}
