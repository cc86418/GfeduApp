package cn.jun.menory.manage_activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jun.menory.bean.SectionItemBean;
import cn.jun.menory.bean.VideoClassBean;
import cn.jun.menory.bean.VideoClassStageBean;
import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.polyv.IjkVideoActicity;
import jc.cici.android.R;


public class VideoCachedFragment extends Fragment {
    // classId到阶段列表的映射
    private Map<String, List<SectionItemBean>> mClassStageMap = new HashMap<>();
    private ListView lvLeft;
    private HnBaseAdapter<VideoClassBean> leftAdapter;
    private ListView lvRight;
    private VideoCachedListAdapter mRightAdapter;

    //Line
    private View view_line;
    //数据源
    private List<VideoClassBean> mLeftData = new ArrayList<>();
    private List<SectionItemBean> list;
    private VideoDownloadManager vm;
    private Cursor Classcursor;
    private Cursor StageCursor;
    private Cursor DownLoadListCursor;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_cached, container, false);
        lvLeft = (ListView) view.findViewById(R.id.lv_left);
        initData();
        initView(view);
//        initDataTest();

        return view;
    }

    private void initData() {
        vm = VideoDownloadManager.getInstance();
        Classcursor = vm.getVideoClassCursor();
        VideoClassBean vc = null;
        while (Classcursor.moveToNext()) {
            vc = new VideoClassBean();
            vc.classid = Classcursor.getString(Classcursor.getColumnIndex("classid"));
            vc.classname = Classcursor.getString(Classcursor.getColumnIndex("classname"));
            // 根据classid创建 阶段学习列表
            List<VideoClassStageBean> classStages = generateClassStagedList(vc.classid);
            // 将 stageBean 转为 列表能够实用的 数据格式
            List<SectionItemBean> sectionItemBeenList = stageBean2SectionItemBean(classStages);
            mClassStageMap.put(vc.classid, sectionItemBeenList);

            int count = vm.getAgaginVideoClassCursor(vc.classid, "2");
            Log.i("classid - - - count == ", "" + count);
            if (0 != count && count > 0) {
                mLeftData.add(vc);
                Gson s = new Gson();
                Log.i(" ==== ","" + s.toJson(mLeftData.toArray()));
            }


//            mLeftData.add(vc);
        }
//        Gson aa = new Gson();
//        Log.i("mLeft <---> ", "" + aa.toJson(mLeftData).toString());
//        Log.i("mClassStageMap <---> ", "" + aa.toJson(mClassStageMap).toString());

        if (null != mLeftData && !"".equals(mLeftData)) {
            if (mClassStageMap != null && mClassStageMap.size() > 0) {
//            Gson s = new Gson();
//            Log.i("下载的信息 ==== > ", "" + s.toJson(mClassStageMap.get(vc.classid).get(0).videoClassStageBean.getItemBean()).toString());
                if (!"".equals(mClassStageMap.get(vc.classid).get(0).videoClassStageBean.getItemBean())
                        && mClassStageMap.get(vc.classid).get(0).videoClassStageBean.getItemBean() != null) {
                    leftAdapter = new HnBaseAdapter<VideoClassBean>(R.layout.item_for_left_list_view, mLeftData) {
                        @Override
                        public void convert(VH holder, VideoClassBean item, int position) {
                            CheckedTextView checkedTextView = holder.getView(R.id.ctv_text);
                            checkedTextView.setText(item.classname);
                            checkedTextView.setChecked(item.checked);
                        }
                    };
                    lvLeft.setAdapter(leftAdapter);

                    if (mLeftData.size() > 0) {
                        // 默认选中第一个
                        mLeftData.get(0).checked = true;
                    }

                    leftAdapter.setOnItemClickListener(new HnBaseAdapter.OnItemClickListener<VideoClassBean>() {
                        @Override
                        public void onItemClick(VideoClassBean item, int position) {
                            // 更改选中状态并
                            int size = leftAdapter.getCount();
                            for (int i = 0; i < size; i++) {
                                VideoClassBean vc = (VideoClassBean) leftAdapter.getItem(i);
                                if (vc != null) {
                                    vc.checked = false;
                                }
                            }
                            // 当前被点击的item设置为选中
                            item.checked = true;
                            leftAdapter.notifyDataSetChanged();
                            // 更新右边列表内容
                            updateRightListView(item.getClassid());

                        }
                    });
                }
            }
        }
    }

    private void updateRightListView(String classid) {
        list = mClassStageMap.get(classid);
        Gson s = new Gson();
        Log.i("下载的视频信息", "" + s.toJson(list).toString());

        if (list.get(0).videoClassStageBean.getItemBean().size() != 0) {
            mRightAdapter.setNewData(list);
            lvRight.setAdapter(mRightAdapter);
        }

    }

    private List<SectionItemBean> stageBean2SectionItemBean(List<VideoClassStageBean> classStages) {
        List<SectionItemBean> list = new ArrayList<>();
        for (VideoClassStageBean vcs : classStages) {
            // 头部
            SectionItemBean sectionItemBeanHeader = new SectionItemBean(vcs);
            list.add(sectionItemBeanHeader);
            // 普通节点
            ArrayList<VideoItemBean> beanList = vcs.getItemBean();
            if (beanList != null) {
                for (VideoItemBean videoItemBean : beanList) {
                    list.add(new SectionItemBean(videoItemBean));
                }
                // 分类下面的节点数
                sectionItemBeanHeader.totalItem = beanList.size();
            }
        }
        return list;
    }

    // 模拟数据
    private List<VideoClassStageBean> generateClassStagedList(String classid) {
        List<VideoClassStageBean> retList = new ArrayList<>();
        StageCursor = vm.getVideoStageTable(classid);
        while (StageCursor.moveToNext()) {
            VideoClassStageBean videoClassStageBean = new VideoClassStageBean();
            videoClassStageBean.setClassId(classid);
            videoClassStageBean.setStageName(StageCursor.getString(StageCursor.getColumnIndex("stagename")));
            videoClassStageBean.setItemBean(generateItemBeanList());
            retList.add(videoClassStageBean);
        }
        return retList;
    }


    private ArrayList<VideoItemBean> generateItemBeanList() {
        ArrayList<VideoItemBean> retList = new ArrayList<>();
//        DownLoadListCursor = vm.getVideoDownListTable(StageCursor.getString(StageCursor.getColumnIndex("stageid")));
        DownLoadListCursor = vm.getVideoDownListTableFinish(StageCursor.getString(StageCursor.getColumnIndex("stageid")));
        while (DownLoadListCursor.moveToNext()) {
            VideoItemBean itemBean = new VideoItemBean();
            itemBean.vid = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("vid"));
            itemBean.lessonid = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("lessonid"));
            itemBean.lessonname = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("lessonname"));
            itemBean.subjectid = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("subjectid"));
            itemBean.subjectname = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("subjectname"));
            itemBean.current = DownLoadListCursor.getInt(DownLoadListCursor.getColumnIndex("current"));
            itemBean.total = DownLoadListCursor.getInt(DownLoadListCursor.getColumnIndex("total"));
            itemBean.status = DownLoadListCursor.getInt(DownLoadListCursor.getColumnIndex("status"));

            itemBean.classid = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("classid"));
            itemBean.stageid = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("stageid"));


            itemBean.videoId = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("videoId"));
            itemBean.isStudy = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("isStudy"));
            itemBean.keyValue = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("keyValue"));

            itemBean.StageProblemStatus = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("StageProblemStatus"));
            itemBean.StageNoteStatus = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("StageNoteStatus"));
            itemBean.StageInformationStatus = DownLoadListCursor.getString(DownLoadListCursor.getColumnIndex("StageInformationStatus"));

//            Log.i("itemBean", "" + itemBean.StageProblemStatus);
//            Log.i("itemBean", "" + itemBean.StageNoteStatus);
//            Log.i("itemBean", "" + itemBean.StageInformationStatus);


            retList.add(itemBean);
        }
        return retList;
    }

    private void initView(View view) {
        view_line = (View) view.findViewById(R.id.view_line);
        lvRight = (ListView) view.findViewById(R.id.lv_right);
        Gson s = new Gson();
        Log.i("左边数据=====> ", "" + s.toJson(mLeftData).toString());

        mRightAdapter = new VideoCachedListAdapter(getActivity());
        lvRight.setAdapter(mRightAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SectionItemBean sectionItemBean = (SectionItemBean) mRightAdapter.getItem(position);
                if (sectionItemBean.editMode && !sectionItemBean.isSectionHeader) { //编辑状态
                    sectionItemBean.videoItemBean.checked = !sectionItemBean.videoItemBean.checked;
                    mRightAdapter.notifyDataSetInvalidated();

                } else {//非编辑状态
                    if (!sectionItemBean.isSectionHeader) {//非头部
                        String vid = sectionItemBean.videoItemBean.vid;
                        String title = sectionItemBean.videoItemBean.subjectname;
                        if (!"".equals(vid) && null != vid) {
//                            IjkVideoActicity.intentTo(getActivity(), IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, vid,
//                                    true, title);
                            /**播放缓存视频**/
                            String classid = sectionItemBean.videoItemBean.classid;
                            String stageId = sectionItemBean.videoItemBean.stageid;
                            String videoId = sectionItemBean.videoItemBean.videoId;
                            String StudyKey = sectionItemBean.videoItemBean.keyValue;

                            /**服务状态**/
                            String StageProblemStatus = sectionItemBean.videoItemBean.StageProblemStatus;
                            String StageNoteStatus = sectionItemBean.videoItemBean.StageNoteStatus;
                            String StageInformationStatus = sectionItemBean.videoItemBean.StageInformationStatus;

                            Log.i("StageProblemStatus", "" + StageProblemStatus);
                            Log.i("StageNoteStatus", "" + StageNoteStatus);
                            Log.i("StageInformationStatus", "" + StageInformationStatus);
                            IjkVideoActicity.intentTo(getActivity(), IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, vid,
                                    true, title, classid, stageId, videoId, StudyKey, vid, "1", StageProblemStatus, StageNoteStatus, StageInformationStatus);

                        }
                    }
                }
            }
        });

        if (null != mLeftData && !"".equals(mLeftData)) {
            if (mLeftData.size() > 0) {
                // 右侧列表，默认第一个
                updateRightListView(mLeftData.get(0).getClassid());
                view_line.setVisibility(View.VISIBLE);
            } else {
                view_line.setVisibility(View.GONE);
            }
        }


    }

    public void showManagerView(boolean editMode) {
        for (Map.Entry<String, List<SectionItemBean>> entry : mClassStageMap.entrySet()) {
            List<SectionItemBean> value = entry.getValue();
            if (value != null) {
                for (SectionItemBean sectionItem : value) {
                    sectionItem.editMode = editMode;
                    if (!editMode && !sectionItem.isSectionHeader) {
                        sectionItem.videoItemBean.checked = false;
                    }
                }
            }
        }
        mRightAdapter.notifyDataSetChanged();
    }

    public int getTotalVideosToDelete() {
        int sum = 0;
        for (Map.Entry<String, List<SectionItemBean>> entry : mClassStageMap.entrySet()) {
            List<SectionItemBean> value = entry.getValue();
            if (value != null) {
                for (SectionItemBean sectionItem : value) {
                    if (!sectionItem.isSectionHeader && sectionItem.videoItemBean.checked) {
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    public void doDeleteVideos() {
        List<SectionItemBean> needToDeleteList = new ArrayList<>();
        for (Map.Entry<String, List<SectionItemBean>> entry : mClassStageMap.entrySet()) {
            List<SectionItemBean> value = entry.getValue();
            if (value != null) {
                needToDeleteList.clear();
                for (SectionItemBean sectionItem : value) {
                    if (!sectionItem.isSectionHeader && sectionItem.videoItemBean.checked) {
                        needToDeleteList.add(sectionItem);
                    }
                }
                for (SectionItemBean sectionItem : needToDeleteList) {
                    value.remove(sectionItem);
                    //判断读写权限
                    int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // 无权限----
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        // 这里删除操作哦，切记
                        Log.i("选中的删除 vid ===> ", "" + sectionItem.videoItemBean.vid);
                        //TODO--删除downloadlist表中的信息
                        PolyvDownloader downloader = PolyvDownloaderManager
                                .getPolyvDownloader(sectionItem.videoItemBean.vid, sectionItem.videoItemBean.bitrate,
                                        Video.HlsSpeedType.getHlsSpeedType(sectionItem.videoItemBean.speed));
                        PolyvDownloaderManager.clearPolyvDownload(sectionItem.videoItemBean.vid,
                                sectionItem.videoItemBean.bitrate);
                        if (downloader != null) {
                            Log.i("删除的vid -- ", "" + sectionItem.videoItemBean.vid);
                            downloader.deleteVideo(sectionItem.videoItemBean.vid, sectionItem.videoItemBean.bitrate);
                            VideoDownloadManager.getInstance().deleteDownloadVideo(sectionItem.videoItemBean.vid);
                        }
                    }


                }
            }
        }
        // 重新计算一下，头部分类具有的节点数,为0则删除此头部
        for (Map.Entry<String, List<SectionItemBean>> entry : mClassStageMap.entrySet()) {
            List<SectionItemBean> value = entry.getValue();
            if (value != null) {
                SectionItemBean header = null; // 头部
                int sum = 0;
                needToDeleteList.clear();
                for (SectionItemBean sectionItem : value) {
                    if (sectionItem.isSectionHeader) {
                        if (header != null) {
                            header.totalItem = sum;
                            if (sum == 0) {
                                needToDeleteList.add(header);
                            }
                        }
                        header = sectionItem;
                        sum = 0;
                    } else {
                        sum++;
                    }
                }
                if (header != null) {
                    header.totalItem = sum;
                    if (sum == 0) {
                        needToDeleteList.add(header);
                    }
                }
                for (SectionItemBean sectionItem : needToDeleteList) {
                    value.remove(sectionItem);
                }
            }
        }
        mRightAdapter.notifyDataSetChanged();

        List<VideoClassBean> needToDelete = new ArrayList<>();
        // 如果需要删除此班级下
        for (Map.Entry<String, List<SectionItemBean>> entry : mClassStageMap.entrySet()) {
            List<SectionItemBean> value = entry.getValue();
            if (value == null || value.size() == 0) {
                for (VideoClassBean vcb : mLeftData) {
                    if (vcb.classid.equals(entry.getKey())) {
                        needToDelete.add(vcb);
                        Log.i("选中的删除 classid ==> ", "" + vcb.classid);
                        //TODO--删除class和stage表中的信息
                        VideoDownloadManager.getInstance().deleteStageVideo(vcb.classid);
                        VideoDownloadManager.getInstance().deleteClassVideo(vcb.classid);
                    }
                }
            }
        }
        for (VideoClassBean vcb : needToDelete) {
            mLeftData.remove(vcb);
        }
        needToDelete.clear();
        lvLeft.setAdapter(leftAdapter);

        Message msg = new Message();
        msg.what = 10000;
        ManagerActivity.DeleteVideoHandler.sendMessage(msg);
    }


}
