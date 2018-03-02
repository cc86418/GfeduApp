package cn.jun.menory.service;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoDBOpenHepler extends SQLiteOpenHelper {
    private static final String DATEBASENAME = "videodownloadlist.db";
    private static VideoDBOpenHepler instance = null;

    public static VideoDBOpenHepler getInstance(Context context, int version) {
        if (instance == null) {
            synchronized (VideoDBOpenHepler.class) {
                if (instance == null)
                    instance = new VideoDBOpenHepler(
                            context.getApplicationContext(), version);
            }
        }
        return instance;
    }

    public VideoDBOpenHepler(Context context, int version) {
        super(context, DATEBASENAME, null, version);
    }

    public VideoDBOpenHepler(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("create table if not exists downloadlist (vid varchar(20),planid int,speed varchar(15),name varchar(100),desc varchar(100),tasktype varchar(100),duration varchar(20),filesize int,bitrate int,current int default 0,total int default 0,status int default 0,primary key (vid))");
        //班级数据库
        db.execSQL("create table if not exists classlist (classid varchar(20),classname varchar(20),classcount int,primary key (classid))");
        //阶段数据库
        db.execSQL("create table if not exists stagelist (stageid varchar(20) ,stagename varchar(20),classid varchar(20),stagecount int,primary key (stageid))");
//        //视频条目数据库
//        db.execSQL("create table if not exists downloadlist (vid varchar(20),lessonid varchar(20) ,lessonname varchar(20),stageid varchar(20),subjectid varchar(20),subjectname varchar(20), int,speed varchar(15),duration varchar(20),filesize int,bitrate int,current int default 0,total int default 0,status int default 0,primary key (vid))");
        //视频条目数据库
        db.execSQL("create table if not exists downloadlist (_id integer primary key autoincrement,vid varchar(20),classid varchar(20), lessonid varchar(20) ,lessonname varchar(20),stageid varchar(20),subjectid varchar(20),subjectname varchar(20), int,speed varchar(15),duration varchar(20),filesize int,bitrate int,current int default 0,total int default 0,status int default 0,videoId varchar(20),isStudy varchar(20),keyValue varchar(20),StageProblemStatus varchar(20),StageNoteStatus varchar(20),StageInformationStatus varchar(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS downloadlist");
        onCreate(db);
    }


}
