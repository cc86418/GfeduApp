package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 笔记实体类
 * Created by atom on 2017/6/6.
 */

public class NoteBean {
    // 笔记列表数量
    private int NotesListCount;
    // 总页数
    private int PageCount;
    // 笔记列表
    private ArrayList<Note> NotesList;

    public int getNotesListCount() {
        return NotesListCount;
    }

    public void setNotesListCount(int notesListCount) {
        NotesListCount = notesListCount;
    }

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }

    public ArrayList<Note> getNotesList() {
        return NotesList;
    }

    public void setNotesList(ArrayList<Note> notesList) {
        NotesList = notesList;
    }
}
