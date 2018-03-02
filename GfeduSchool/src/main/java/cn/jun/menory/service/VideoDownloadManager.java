package cn.jun.menory.service;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cn.jun.bean.ClassListDB;
import cn.jun.bean.SqlNextLastBean;
import cn.jun.bean.StageListDB;
import cn.jun.menory.bean.VideoClassBean;
import cn.jun.menory.bean.VideoClassStageBean;
import cn.jun.menory.bean.VideoItemBean;

public class VideoDownloadManager {
    private VideoDBservice dbService;
    private ConcurrentHashMap<String, PolyvDownloadProgressListener> downloadProgressListenerMap = new ConcurrentHashMap<String, PolyvDownloadProgressListener>();
    private Context mContext;

    private VideoDownloadManager() {
    }

    // 必须使用前调用一次，注意请在威力视频下载初始化之后调用一次
    public void init(Context context) {
        dbService = new VideoDBservice(context);
        mContext = context;
        List<VideoItemBean> list = getBufferingVideos();
        if (list != null) {
            for (VideoItemBean item : list) {
                Log.i("bad-boy", item.toString());
                addDownloadTask(item);
            }
        }
    }

    public static VideoDownloadManager getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        static VideoDownloadManager instance = new VideoDownloadManager();
    }

    public void putDownloadListener(String vid,
                                    PolyvDownloadProgressListener listener) {
        downloadProgressListenerMap.put(vid, listener);
    }

    public void removeDownloadListener(String vid) {
        downloadProgressListenerMap.remove(vid);
    }

    public void clearDownloadListener() {
        downloadProgressListenerMap.clear();
    }

    // 删除视频
    public int deleteVideo(VideoItemBean vi) {
        PolyvDownloader polyvDownloader = PolyvDownloaderManager
                .getPolyvDownloader(vi.vid, vi.bitrate, vi.hlsSpeedType);
        if (polyvDownloader != null) {
            polyvDownloader.deleteVideo();
        }
        return dbService.deleteVideo(vi.vid);
    }

    public int deleteDownloadVideo(String vid) {
        return dbService.deleteDownloadVideo(vid);
    }

    public int deleteClassVideo(String classid) {
        return dbService.deleteClassVideo(classid);
    }

    public int deleteStageVideo(String classid) {
        return dbService.deleteStageVideo(classid);
    }

    //获取Class表中数据
    public ArrayList<VideoClassBean> getVideoClassTable() {
        return dbService.SelectVideoClassTable();
    }

    //获取Class表中数据(返回游标Cursor)
    public Cursor getVideoClassCursor() {
        return dbService.SelectVideoClassCursor();
    }

    //重新整理列表左边下载完成的数据
    public int getAgaginVideoClassCursor(String classid, String status) {
        return dbService.SelectAgaginVideoClassCursor(classid, status);
    }

    //获取stage表中数据
    public Cursor getVideoStageTable(String Classid) {
        return dbService.SelectVideoStageTable(Classid);
    }

    //获取downlist表中数据
    public Cursor getVideoDownListTable(String Stageid) {
        return dbService.SelectDownListTable(Stageid);
    }

    //获取downlist表中数据(已完成的数据)
    public Cursor getVideoDownListTableFinish(String Stageid) {
        return dbService.SelectDownListTableFinish(Stageid);
    }

    // 获取多表联查数据
    public List<VideoClassStageBean> getMultiTableVideos() {
        return dbService.SelectMultiTableQuery();
    }

    /**
     * 通过vid获取_id
     **/
    public int SelectBufferedVideos_id(String vid) {
        return dbService.SelectBufferedVideos_id(vid);
    }

    /**
     * 通过_id和标识，获取上一条Or下一条数据
     **/
    public SqlNextLastBean getSqlNextLastVideos(int _id, String flag) {
        return dbService.getSqlNextLastVideos(_id, flag);
    }


    // 获取已经下载的视频列表
    public List<VideoItemBean> getBufferedVideos() {
        return dbService.getBufferedVideos();
    }

    // 获取正在下载的视频列表
    public List<VideoItemBean> getBufferingVideos() {
        return dbService.getBufferingVideos();
    }

    // 查新数据库中是否存在视频VID信息
    public String SelectBufferedVideosItems(String vid) {
        return dbService.SelectBufferedVideosItems(vid);
    }

    // 单条查新数据库中视频的下载状态
    public String SelectBufferedVideosItemsStatus(String vid) {
        return dbService.SelectBufferedVideosItemsStatus(vid);
    }

    public boolean addClassIdTassk(String classid, String classname) {
        final ClassListDB cld = new ClassListDB();
        cld.setClassid(classid);
        cld.setClassname(classname);

        if (dbService.isClassIdAdd(classid)) {
            return true;
        }

        //班级
        dbService.addClassListDB(cld);
        return true;
    }

    public boolean addStageIdTassk(String classid, String stageid, String stagename) {
        final StageListDB sld = new StageListDB();
        sld.setStageid(stageid);
        sld.setStagename(stagename);
        sld.setClassid(classid);

        if (dbService.isStageIdAdd(stageid)) {
            return true;
        }


        //阶段
        dbService.addStageListDB(sld);
        return true;
    }

    // 添加下载任务，如果失败则返回false，否则true
    // 注意：调用之前需要判断一下存储器写权限
    //  public boolean addVideoTask(final String vid, String lessonid, String name,
    //                                String desc, String tasktype) {
    public boolean addVideoTask
    (final String vid,
     String classid,
     String lessonid,
     String lessonname,
     String subjectid,
     String subjectname,
     String stageid,
     String videoId,
     String isStudy,
     String keyValue,
     String StageProblemStatus,
     String StageNoteStatus,
     String StageInformationStatus) {
        final VideoItemBean vi = new VideoItemBean();
        vi.vid = vid;
        vi.classid = classid;
        vi.lessonid = lessonid;
        vi.lessonname = lessonname;
        vi.subjectid = subjectid;
        vi.subjectname = subjectname;
        vi.stageid = stageid;

        vi.videoId = videoId;
        vi.isStudy = isStudy;
        vi.keyValue = keyValue;

        vi.StageProblemStatus = StageProblemStatus;
        vi.StageNoteStatus = StageNoteStatus;
        vi.StageInformationStatus = StageInformationStatus;


        if (dbService.isAdd(vid)) {
            Log.i("isAdd", "已存在下载队列");
            return true;
        }

        // 是否已经达到5个
        if (dbService.isMaxDownload()) {
            Log.i("isMaxDownload", "超过最大下载数");
            return false;
        }

        dbService.addDownloadFile(vi);

        //加入到保利威视下载队列
        addDownloadTask(vi);

        return true;
    }


    // 添加到下载管理器
    private void addDownloadTask(final VideoItemBean vi) {
        Log.i("addDownloadTask", "添加到下载管理器");
        PolyvDownloader polyvDownloader = PolyvDownloaderManager
                .getPolyvDownloader(vi.vid, vi.bitrate, vi.hlsSpeedType);
        polyvDownloader
                .setPolyvDownloadProressListener(new PolyvDownloadProgressListener() {

                    @Override
                    public void onDownloadSuccess() {
                        dbService.updateStatus(vi.vid,
                                VideoItemBean.STATUS_SUCCESS);
                        PolyvDownloadProgressListener listener = downloadProgressListenerMap
                                .get(vi.vid);
                        if (listener != null) {
                            listener.onDownloadSuccess();
                        }
                        Log.i("bad-boy", "onDownloadSuccess");
                    }

                    @Override
                    public void onDownloadFail(
                            PolyvDownloaderErrorReason errorReason) {
                        Log.i("bad-boy", "onDownloadFail, "
                                + errorReason.getCause().getMessage());
                        dbService.updateStatus(vi.vid,
                                VideoItemBean.STATUS_FAILED);
                        PolyvDownloadProgressListener listener = downloadProgressListenerMap
                                .get(vi.vid);
                        if (listener != null) {
                            listener.onDownloadFail(errorReason);
                        }
                    }

                    @Override
                    public void onDownload(long current, long total) {
                        dbService.updateProgress(vi, current, total);
                        Log.i("bad-boy", "vid: " + vi.vid + ", current: "
                                + current + ", total: " + total);
                        PolyvDownloadProgressListener listener = downloadProgressListenerMap
                                .get(vi.vid);
                        if (listener != null) {
                            listener.onDownload(current, total);
                        }
                    }
                });
        if (vi.status == VideoItemBean.STATUS_DOWNLOADING) {
            polyvDownloader.start();
        } else {
            polyvDownloader.stop();
        }

    }

    // 更新状态
    public void updateStatus(String vid, int status) {
        dbService.updateStatus(vid, status);
    }


    // 更新状态
    public void updateStageConust(String StageId, int StageCount) {
        dbService.updateStageConust(StageId, StageCount);
    }


    public void deleteClassTable() {
        dbService.deleteClassTable();
    }

    public void deleteStageTable() {
        dbService.deleteStageTable();
    }

    public void deleteDownLoadTable() {
        dbService.deleteDownLoadTable();
    }


}
