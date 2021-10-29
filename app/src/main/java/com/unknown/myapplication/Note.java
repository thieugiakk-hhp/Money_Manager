package com.unknown.myapplication;

import java.io.Serializable;

public class Note  implements Serializable {
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public int getIncomeOrSpend() {
        return incomeOrSpend;
    }

    public void setIncomeOrSpend(int incomeOrSpend) {
        this.incomeOrSpend = incomeOrSpend;
    }

    private int noteId;
    private String noteTitle;
    private int money;
    private String noteContent;

    private int incomeOrSpend;

    public Note() {

    }

    public Note(int noteId, String noteTitle, int money, String noteContent, int incomeOrSpend) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.money = money;
        this.noteContent = noteContent;
        this.incomeOrSpend = incomeOrSpend;
    }

    public Note(String noteTitle, int money, String noteContent, int incomeOrSpend) {
        this.noteTitle = noteTitle;
        this.money = money;
        this.noteContent = noteContent;
        this.incomeOrSpend = incomeOrSpend;
    }

    @Override
    public String toString()  {
        return this.noteTitle;
    }
}
