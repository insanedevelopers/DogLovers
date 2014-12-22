package com.insanedevelopers.doglovers.quickblox;

import android.content.Context;

import com.quickblox.customobjects.model.QBCustomObject;

public class Note {

    private String id;
    private String title;
    private String status;
    private int position;
    private String date;
    private String content;
    Context context;

    public Note(QBCustomObject customObject) {
        id = customObject.getCustomObjectId();
        title = parseField(Constants.NOTE_HEADING, customObject);
        content = parseField(Constants.NOTE_CONTENT, customObject);
        status = parseField(Constants.STATUS, customObject);
        date = customObject.getUpdatedAt().toString();
        position = parseIntField("Position", customObject);
    }

    private String parseField(String field, QBCustomObject customObject) {
        Object object = customObject.getFields().get(field);
        if (object != null) {
            return object.toString();
        }
        return null;
    }

    private int parseIntField(String field, QBCustomObject customObject) {
        int object = Integer.parseInt((String) customObject.getFields().get(field));
        if (object != 0) {
            return object;
        }
        return 0;
    }
    
    public String getContent() {
        return content;
    }
    
    public int getPosition() {
        return position;
    }
    
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }
}
