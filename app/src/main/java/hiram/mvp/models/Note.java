package hiram.mvp.models;

import android.content.ContentValues;

import hiram.mvp.data.DBSchema;

public class Note {
    private int id = -1;
    private String text;
    private String date;

    public Note() {
    }

    public Note(int id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public Note(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public ContentValues getValues(){
        ContentValues cv = new ContentValues();
        if (id!=-1) cv.put(DBSchema.TB_NOTES.ID, id);
        cv.put(DBSchema.TB_NOTES.NOTE, text);
        cv.put(DBSchema.TB_NOTES.DATE, date);
        return cv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
