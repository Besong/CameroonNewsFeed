package com.example.besong_anongernest.cameroonnewsfeed;

/**
 * Created by Besong-Anong Ernest on 11/19/2017.
 */

public class Feed  {
    // Title of the web Content
    private String webTitle;

    // Section name of the web Content
    private String sectionName;

    //URL of the web Content
    private String webUrl;


    public Feed(String webTitle, String sectionName, String webUrl) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.webUrl =  webUrl;
    }

    //Return the content of the web Title
    public String getWebTitle() {
        return webTitle;
    }

    //Return the section Name content
    public String getSectionName() {
        return sectionName;
    }

    //Return the webUrl of content
    public String getWebUrl() {
        return webUrl;
    }

}
