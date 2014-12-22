package com.insanedevelopers.doglovers.quickblox;

import java.util.Locale;

public final class Constants {

    public static final  int APP_ID = 17572;
    public static final  String AUTH_KEY = "RUvUF9K9GwXKvUw";
    public static final  String AUTH_SECRET = "U9ZFOJgrGVQ3Rcy";
    
    

    public static final boolean CONFIG_DEVELOPER_MODE = false;
    public static final String URL_S3 = "http://qbprod.s3.amazonaws.com/";
    
    public static final String NOTE_CLASS_NAME = "Petting";
    public static final String NOTE_HEADING = "Heading";
    public static final String NOTE_CONTENT = "Article";
    
    static String fbid=SplashActivity.ids;
    static String fbfname=SplashActivity.firstName.toLowerCase(Locale.ENGLISH);
    public static final String USER_LOGIN = fbid;
    public static final String USER_PASSWORD =fbid+fbfname ;

    public static final String STATUS_NEW = "New";
    public static final String STATUS = "status";
    public static final String STATUS_IN_PROCESS = "In Process";
    public static final String STATUS_DONE = "Done";
    /*public static final String FACEBOOK_CLASS_NAME = "FbUserData";*/
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String IDS = "ids";
    public static final String LINK = "link";
    public static final String GENDER = "gender";
    public static final String TIMEZONE = "timezone";
}