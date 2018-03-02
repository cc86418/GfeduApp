package cn.jun.menory;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import cn.jun.menory.bean.VideoClassBean;
import cn.jun.menory.bean.VideoClassStageBean;
import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.service.VideoDownloadManager;
import jc.cici.android.R;

public class Fragment_pro_type extends Fragment {
    //    private ArrayList<Type> list;
    private ArrayList<VideoClassStageBean> VideoStageList;
    private ArrayList<VideoClassBean> VideoClassList;
    private ImageView hint_img;
    private ExpandableListView listView;
    private static VideoListBufferedExpandableAdapter adapter;
    //    private Type type;
    private ProgressBar progressBar;
    private String typename;

    private Cursor StageCursor;
    private Cursor DownLoadListCursor;
    private int currentItem;

    private VideoClassStageBean vcs;
    private ArrayList<VideoItemBean> VideoStageBeanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_type, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        hint_img = (ImageView) view.findViewById(R.id.hint_img);
        listView = (ExpandableListView) view.findViewById(R.id.listView);
        typename = getArguments().getString("typename");
        currentItem = getArguments().getInt("currentItem");
        VideoClassList = (ArrayList<VideoClassBean>) getArguments().getSerializable("VideoClassList");
//        VideoStageList = (ArrayList<VideoClassStageBean>)getArguments().getSerializable("VideoStageList");
//        ((TextView) view.findViewById(R.id.toptype)).setText(typename);
//        GetTypeList();

        SelectDataFristAdapter();
        adapter = new VideoListBufferedExpandableAdapter(getActivity(), VideoStageList);
        listView.setAdapter(adapter);
        //默认展开
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
        progressBar.setVisibility(View.GONE);

        return view;
    }


    public void SelectDataFristAdapter() {
        VideoStageList = new ArrayList<>();
        final VideoDownloadManager vm = VideoDownloadManager.getInstance();
        StageCursor = vm.getVideoStageTable(VideoClassList.get(currentItem).getClassid());
        while (StageCursor.moveToNext()) {
            vcs = new VideoClassStageBean();
            vcs.setStageId(StageCursor.getString(StageCursor.getColumnIndex("stageid")));
            vcs.setStageName(StageCursor.getString(StageCursor.getColumnIndex("stagename")));
            vcs.setStageCount(StageCursor.getInt(StageCursor.getColumnIndex("stagecount")));
            vcs.setClassId(StageCursor.getString(StageCursor.getColumnIndex("classid")));
            VideoStageBeanList = new ArrayList<>();
            DownLoadListCursor = vm.getVideoDownListTable(StageCursor.getString(StageCursor.getColumnIndex("stageid")));
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
                VideoStageBeanList.add(itemBean);
                vcs.setItemBean(VideoStageBeanList);
            }
            VideoStageList.add(vcs);
        }
    }


    public static void ooo(boolean editMode){
        adapter.setEditMode(editMode);
    }
}



