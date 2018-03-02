package cn.jun.menory.service;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

import cn.jun.bean.ClassListDB;
import cn.jun.bean.SqlNextLastBean;
import cn.jun.bean.StageListDB;
import cn.jun.menory.bean.VideoClassBean;
import cn.jun.menory.bean.VideoClassStageBean;
import cn.jun.menory.bean.VideoItemBean;

public class VideoDBservice {
    private static final String TAG = "DBservice";
    private VideoDBOpenHepler dbOpenHepler;
    private SQLiteDatabase db;


    public VideoDBservice(Context context) {
        // 1 -> database version
        dbOpenHepler = VideoDBOpenHepler.getInstance(context, 1);
    }


    /**
     * 添加下载文件
     *
     * @param info
     */
    public void addDownloadFile(VideoItemBean info) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "insert into downloadlist(vid,classid,lessonid,lessonname,subjectid,subjectname,stageid,speed,bitrate,current,total,videoId,isStudy,keyValue,StageProblemStatus,StageNoteStatus,StageInformationStatus) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{info.vid, info.classid, info.lessonid, info.lessonname, info.subjectid, info.subjectname, info.stageid,
                info.speed, info.bitrate, info.current, info.total, info.videoId, info.isStudy, info.keyValue, info.StageProblemStatus, info.StageNoteStatus, info.StageInformationStatus});


    }

    /**
     * 添加班级数据库
     *
     * @param classListDB
     */
    public void addClassListDB(ClassListDB classListDB) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "insert into classlist(classid,classname,classcount) values(?,?,?)";
        db.execSQL(sql, new Object[]{classListDB.getClassid(), classListDB.getClassname(), classListDB.getClasscount()});
        Log.i("添加Class == > ", classListDB.getClassid() + " , " + classListDB.getClassname());
    }

    /**
     * 添加阶段数据库
     *
     * @param stageListDB
     */
    public void addStageListDB(StageListDB stageListDB) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "insert into stagelist(stageid,stagename,classid,stagecount) values(?,?,?,?)";
        db.execSQL(sql, new Object[]{stageListDB.getStageid(), stageListDB.getStagename(), stageListDB.getClassid(), stageListDB.getStagecount()});
        Log.i("添加Stage == > ", stageListDB.getStageid() + " , " + stageListDB.getStagename() + " , " + stageListDB.getClassid());
    }

    // 删除下载文件
    public void deleteDownloadFile(String vid) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "delete from downloadlist where vid=?";
        db.execSQL(sql, new Object[]{vid});
    }

    // 更新状态信息
    public void updateStatus(String vid, int status) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "update downloadlist set status=? where vid=?";
        db.execSQL(sql, new Object[]{status, vid});
    }


    // 更新进度信息
    public void updateProgress(VideoItemBean info, long current, long total) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "update downloadlist set current=?,total=? where vid=?";
        db.execSQL(sql, new Object[]{current, total, info.vid});
    }

    // 是否已经在下载任务队列
    public boolean isAdd(String vid) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select vid,duration,filesize,bitrate from downloadlist where vid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{vid});
        if (cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    // CLassID是否已经记录
    public boolean isClassIdAdd(String classId) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select classid from classlist where classid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{classId});
        if (cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    // StageID是否已经记录
    public boolean isStageIdAdd(String stageid) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select stageid from stagelist where stageid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{stageid});
        if (cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    // 是否达到最大下载数(最大下载数5)
    public boolean isMaxDownload() {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select vid from downloadlist where status="
                + VideoItemBean.STATUS_DOWNLOADING + " or status="
                + VideoItemBean.STATUS_PAUSED + " or status="
                + VideoItemBean.STATUS_FAILED;
        Cursor cursor = db.rawQuery(sql, null);
        Log.i("是否达到最大下载 --- > ", "" + cursor.getCount());
        return (cursor.getCount() >= 5);
    }

    // 获取正在下载文件列表
    public LinkedList<VideoItemBean> getBufferingVideos() {
        LinkedList<VideoItemBean> infos = new LinkedList<VideoItemBean>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select * from downloadlist where status<>"
                + VideoItemBean.STATUS_SUCCESS;

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            VideoItemBean info = new VideoItemBean();
            info.vid = cursor.getString(cursor.getColumnIndex("vid"));
//            info.planid = cursor.getInt(cursor.getColumnIndex("planid"));
            // info.speed = cursor.getString(cursor.getColumnIndex("speed"));
            info.classid = cursor.getString(cursor.getColumnIndex("classid"));
            info.stageid = cursor.getString(cursor.getColumnIndex("stageid"));
            info.lessonid = cursor.getString(cursor.getColumnIndex("lessonid"));
            info.lessonname = cursor.getString(cursor.getColumnIndex("lessonname"));
            info.subjectid = cursor.getString(cursor.getColumnIndex("subjectid"));
            info.subjectname = cursor.getString(cursor.getColumnIndex("subjectname"));
//            info.desc = cursor.getString(cursor.getColumnIndex("desc"));
//            info.tasktype = cursor.getString(cursor.getColumnIndex("tasktype"));
            // info.bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
            info.current = cursor.getInt(cursor.getColumnIndex("current"));
            info.total = cursor.getInt(cursor.getColumnIndex("total"));
            info.status = cursor.getInt(cursor.getColumnIndex("status"));

            info.videoId = cursor.getString(cursor.getColumnIndex("videoId"));
            info.isStudy = cursor.getString(cursor.getColumnIndex("isStudy"));
            info.keyValue = cursor.getString(cursor.getColumnIndex("keyValue"));

            info.StageProblemStatus = cursor.getString(cursor.getColumnIndex("StageProblemStatus"));
            info.StageNoteStatus = cursor.getString(cursor.getColumnIndex("StageNoteStatus"));
            info.StageInformationStatus = cursor.getString(cursor.getColumnIndex("StageInformationStatus"));

//            info.desc = cursor.getString(cursor.getColumnIndex("desc"));
            infos.addLast(info);
        }
        return infos;
    }


    // 获取已经下载完成的文件列表
    public LinkedList<VideoItemBean> getBufferedVideos() {
        LinkedList<VideoItemBean> videos = new LinkedList<VideoItemBean>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select * from downloadlist where status="
                + VideoItemBean.STATUS_SUCCESS;
//        +VideoItemBean.STATUS_FAILED;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            VideoItemBean info = new VideoItemBean();
            info.vid = cursor.getString(cursor.getColumnIndex("vid"));
//            info.planid = cursor.getInt(cursor.getColumnIndex("planid"));
            // info.speed = cursor.getString(cursor.getColumnIndex("speed"));
            info.classid = cursor.getString(cursor.getColumnIndex("classid"));
            info.stageid = cursor.getString(cursor.getColumnIndex("stageid"));
            info.lessonid = cursor.getString(cursor.getColumnIndex("lessonid"));
            info.lessonname = cursor.getString(cursor.getColumnIndex("lessonname"));
            info.subjectid = cursor.getString(cursor.getColumnIndex("subjectid"));
            info.subjectname = cursor.getString(cursor.getColumnIndex("subjectname"));
//            info.desc = cursor.getString(cursor.getColumnIndex("desc"));
//            info.tasktype = cursor.getString(cursor.getColumnIndex("tasktype"));
            // info.bitrate = cursor.getInt(cursor.getColumnIndex("bitrate"));
            info.current = cursor.getInt(cursor.getColumnIndex("current"));
            info.total = cursor.getInt(cursor.getColumnIndex("total"));
            info.status = cursor.getInt(cursor.getColumnIndex("status"));

            info.videoId = cursor.getString(cursor.getColumnIndex("videoId"));
            info.isStudy = cursor.getString(cursor.getColumnIndex("isStudy"));
            info.keyValue = cursor.getString(cursor.getColumnIndex("keyValue"));

            info.StageProblemStatus = cursor.getString(cursor.getColumnIndex("StageProblemStatus"));
            info.StageNoteStatus = cursor.getString(cursor.getColumnIndex("StageNoteStatus"));
            info.StageInformationStatus = cursor.getString(cursor.getColumnIndex("StageInformationStatus"));

            videos.addLast(info);
            Log.i("---- >> ", "" + videos.size());
        }
        return videos;
    }

    // 查新数据库中是否存在视频VID信息
    public String SelectBufferedVideosItems(String vid) {
        String _vid = null;
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select vid from downloadlist where vid='" + vid + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            _vid = cursor.getString(cursor.getColumnIndex("vid"));
        }

        if (cursor.getCount() != 0) {
            return _vid;
        } else {
            return "";
        }

    }

    // 单条查新数据库视频的下载状态
    public String SelectBufferedVideosItemsStatus(String vid) {
        String _status = null;
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select status from downloadlist where vid='" + vid + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            _status = cursor.getString(cursor.getColumnIndex("status"));
        }

        if (cursor.getCount() != 0) {
            return _status;
        } else {
            return "";
        }

    }

    public int deleteVideo(String vid) {
        db = dbOpenHepler.getWritableDatabase();
        return db.delete("downloadlist", "vid=?", new String[]{vid});
    }

    /**
     * 删除downloadlist数据
     **/
    public int deleteDownloadVideo(String vid) {
        db = dbOpenHepler.getWritableDatabase();
        return db.delete("downloadlist", "vid=?", new String[]{vid});
    }


    /**
     * 删除stagelist数据
     **/
    public int deleteStageVideo(String classid) {
        db = dbOpenHepler.getWritableDatabase();
        return db.delete("stagelist", "classid=?", new String[]{classid});
    }

    /**
     * 删除classlist数据
     **/
    public int deleteClassVideo(String classid) {
        db = dbOpenHepler.getWritableDatabase();
        return db.delete("classlist", "classid=?", new String[]{classid});
    }

    /**
     * 查询本地视频是否已经下载
     *
     * @param vid
     * @return
     */
    public boolean getIsFinishDownload(String vid) {
        String status = null;
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select status from downloadlist where vid='" + vid + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            status = cursor.getString(cursor.getColumnIndex("status"));
        }
        if ("2".equals(status)) {
            return true;
        }

        return false;
    }

    /**
     * 多表联查数据
     */
    public LinkedList<VideoClassStageBean> SelectMultiTableQuery() {
        LinkedList<VideoClassStageBean> VideoClassStages = new LinkedList<VideoClassStageBean>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select classlist.classid,classlist.classname,stagelist.stageid,stagelist.stagename,stagelist.stagecount FROM downloadlist INNER JOIN stagelist ON downloadlist.stageid = stagelist.stageid INNER JOIN classlist ON classlist.classid = stagelist.classid";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            VideoClassStageBean vcs = new VideoClassStageBean();
            vcs.setClassId(cursor.getString(cursor.getColumnIndex("classid")));
            vcs.setClassName(cursor.getString(cursor.getColumnIndex("classname")));
            vcs.setStageId(cursor.getString(cursor.getColumnIndex("stageid")));
            vcs.setStageName(cursor.getString(cursor.getColumnIndex("stagename")));
            vcs.setStageCount(cursor.getInt(cursor.getColumnIndex("stagecount")));
            VideoClassStages.add(vcs);

            Log.i("数据库二表联查数据", "" + VideoClassStages.size());
        }
        return VideoClassStages;
    }

    /**
     * 更新Stage表的课程数量
     *
     * @param StageId
     * @param StageCount
     */
    public void updateStageConust(String StageId, int StageCount) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "update stagelist set stagecount=? where vid=?";
        db.execSQL(sql, new Object[]{StageCount, StageId});
    }

    /**
     * 查询Class表中的数据
     */
    public ArrayList<VideoClassBean> SelectVideoClassTable() {
        ArrayList<VideoClassBean> VideoClassBean = new ArrayList<VideoClassBean>();
        db = dbOpenHepler.getWritableDatabase();
        String sql = "SELECT * FROM classlist";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            VideoClassBean vcs = new VideoClassBean();
            vcs.setClassid(cursor.getString(cursor.getColumnIndex("classid")));
            vcs.setClassname(cursor.getString(cursor.getColumnIndex("classname")));
            vcs.setClasscount(cursor.getString(cursor.getColumnIndex("classcount")));
            VideoClassBean.add(vcs);
            Log.i("数据库Class表查询", "" + VideoClassBean.size());
        }
        return VideoClassBean;
    }

    /**
     * 查询Class表中的数据(返回Cursor)
     */
    public Cursor SelectVideoClassCursor() {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "SELECT * FROM classlist";
        Cursor cursor = db.rawQuery(sql, null);
        Log.i("", "");
        return cursor;
    }

    /**
     * 查询Class表中的数据(返回Cursor,已完成下载的数据)
     */
//    public Cursor SelectAgaginVideoClassCursor(String classid, String status) {
    public int SelectAgaginVideoClassCursor(String classid, String status) {
//        db = dbOpenHepler.getWritableDatabase();
//        String sql = "SELECT * from downloadlist where classid = '" + classid + "' and status='" + status + "'";
//        Cursor cursor = db.rawQuery(sql, null);
//        Log.i("", "");
//        return cursor;
        int count = 0;
        db = dbOpenHepler.getWritableDatabase();
        String sql = "SELECT * from downloadlist where classid = '" + classid + "' and status='" + status + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        if (cursor.getCount() != 0) {
            return count;
        } else {
            return 0;
        }
    }


    /**
     * 查询Stage表中的数据
     */
    public Cursor SelectVideoStageTable(String Classid) {
        String CLASSID = "classid";
        String where = CLASSID + "=?";
        String[] whereArgs = {Classid};
        db = dbOpenHepler.getWritableDatabase();
        Cursor cursor = db.query("stagelist", null, where,
                whereArgs, null, null, null);
        return cursor;
    }

    /**
     * 查询DownList表中的数据
     */
    public Cursor SelectDownListTable(String Stageid) {
        String STAGEID = "stageid";
        String where = STAGEID + "=?";
        String[] whereArgs = {Stageid};
        db = dbOpenHepler.getWritableDatabase();
        Cursor cursor = db.query("downloadlist", null, where,
                whereArgs, null, null, null);

        return cursor;
    }

    public Cursor SelectDownListTableFinish(String Stageid) {
        db = dbOpenHepler.getWritableDatabase();
        String sql = "SELECT * FROM downloadlist where stageid ='" + Stageid + "' and status = 2";
        Cursor cursor = db.rawQuery(sql, null);

        return cursor;

    }


    //删除classlist表
    public void dropClassTable(SQLiteDatabase db) {
        db.execSQL("drop from classlist");
    }

    //删除stagelist表
    public void dropStageTable(SQLiteDatabase db) {
        db.execSQL("drop from stagelist");
    }

    //删除downloadlist表
    public void dropDownLoadTable(SQLiteDatabase db) {
        db.execSQL("drop from downloadlist");
    }

    //清空ClassList表
    public void deleteClassTable() {
        db = dbOpenHepler.getWritableDatabase();
        db.execSQL("delete from classlist");
    }

    //清空StageList表
    public void deleteStageTable() {
        db = dbOpenHepler.getWritableDatabase();
        db.execSQL("delete from stagelist");
    }

    //清空DownLoadList表
    public void deleteDownLoadTable() {
        db = dbOpenHepler.getWritableDatabase();
        db.execSQL("delete from downloadlist");
    }


    /**
     * 查询数据库视频的_id
     **/
    public int SelectBufferedVideos_id(String vid) {
        int _id = 0;
        db = dbOpenHepler.getWritableDatabase();
        String sql = "select _id from downloadlist where vid='" + vid + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            _id = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        if (cursor.getCount() != 0) {
            return _id;
        } else {
            return 0;
        }

    }


    /**
     * 查询数据库指定ID的上一条Or下一条数据
     **/
    public SqlNextLastBean getSqlNextLastVideos(int _id, String flag) {
//        LinkedList<SqlNextLastBean> infos = new LinkedList<SqlNextLastBean>();
        SqlNextLastBean info = null;
        db = dbOpenHepler.getWritableDatabase();
        Cursor cursor = null;
        if ("last".equals(flag)) {//查询指定ID上一条数据
            String LastSQL = "SELECT * from downloadlist where _id<" + _id + " ORDER BY _id DESC limit 0,1";
            cursor = db.rawQuery(LastSQL, null);
        } else if ("next".equals(flag)) {//查询指定ID下一条数据
            String NextSQL = "SELECT * from Downloadlist where _id>" + _id + " ORDER BY _id limit 0,1";
            cursor = db.rawQuery(NextSQL, null);
        }
        while (cursor.moveToNext()) {
            info = new SqlNextLastBean();
            info.vid = cursor.getString(cursor.getColumnIndex("vid"));
            info.classid = cursor.getString(cursor.getColumnIndex("classid"));
            info.stageid = cursor.getString(cursor.getColumnIndex("stageid"));
            info.lessonid = cursor.getString(cursor.getColumnIndex("lessonid"));
            info.lessonname = cursor.getString(cursor.getColumnIndex("lessonname"));
            info.subjectid = cursor.getString(cursor.getColumnIndex("subjectid"));
            info.subjectname = cursor.getString(cursor.getColumnIndex("subjectname"));
            info.current = cursor.getInt(cursor.getColumnIndex("current"));
            info.total = cursor.getInt(cursor.getColumnIndex("total"));
            info.status = cursor.getInt(cursor.getColumnIndex("status"));
            info.videoid = cursor.getString(cursor.getColumnIndex("videoId"));
            info.isStudy = cursor.getString(cursor.getColumnIndex("isStudy"));
            info.keyValue = cursor.getString(cursor.getColumnIndex("keyValue"));

            info.StageProblemStatus = cursor.getString(cursor.getColumnIndex("StageProblemStatus"));
            info.StageNoteStatus = cursor.getString(cursor.getColumnIndex("StageNoteStatus"));
            info.StageInformationStatus = cursor.getString(cursor.getColumnIndex("StageInformationStatus"));

//            infos.addLast(info);
        }
        return info;
    }

}
