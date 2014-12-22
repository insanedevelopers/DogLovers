package com.insanedevelopers.doglovers.quickblox;

import java.util.ArrayList;
import java.util.List;

import com.quickblox.content.model.QBFile;
import com.quickblox.customobjects.model.QBCustomObject;

public class DataHolder {

    private static DataHolder dataHolder;
    private int signInUserId;
    private List<QBFile> qbFileList;
    private List<Note> noteList;

    public static synchronized DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }

    public int getSignInUserId() {
        return signInUserId;
    }

    public void setSignInUserId(int signInUserId) {
        this.signInUserId = signInUserId;
    }

    public void setQbFileList(List<QBFile> qbFileList) {
        this.qbFileList = qbFileList;
    }

    public int getQbFileListSize() {
        return qbFileList.size();
    }

    public String getPublicUrl(int position) {
        return qbFileList.get(position).getUid();
    }
    
    public String getName(int position) {
        return qbFileList.get(position).getName();
    }

    public void addQbFile(QBFile qbFile) {
        qbFileList.add(qbFile);
    }
    


    //Tips Section
    public int getNoteListSize() {
        if (noteList == null) {
            noteList = new ArrayList<Note>();
        }
        return noteList.size();
    }

    public String getNoteTitle(int position) {
        return noteList.get(position).getTitle();
    }
    
    public String getNoteContent(int position) {
        return noteList.get(position).getContent();
    }
    
    public int getNotePosition(int position) {
        return noteList.get(position).getPosition();
    }

    public String getNoteDate(int position) {
        return noteList.get(position).getDate();
    }

    public String getNoteStatus(int position) {
        return noteList.get(position).getStatus();
    }

    public String getNoteId(int position) {
        return noteList.get(position).getId();
    }

    public void setNoteToNoteList(int position, Note note) {
        noteList.set(position, note);
    }
    
    public void removeNoteFromList(int position) {
        noteList.remove(position);
    }

    public void clear() {
        noteList.clear();
    }

    public int size() {
        if (noteList != null) {
            return noteList.size();
        }
        return 0;
    }

    public void addNoteToList(QBCustomObject customObject) {
        if (noteList == null) {
            noteList = new ArrayList<Note>();
        }
        noteList.add(new Note(customObject));
    }
}
