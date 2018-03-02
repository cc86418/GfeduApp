package cn.jun.menory.manage_activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;

import java.util.List;

import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.service.VideoDownloadManager;
import jc.cici.android.R;


public class VideoCachingFragment extends Fragment {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private VideoItemBean info;
    private ListView lv_buffering;
    private VideoCachingListAdapter VideoCachingListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_caching, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        lv_buffering = (ListView) view.findViewById(R.id.lv_buffering);
        VideoCachingListAdapter = new VideoCachingListAdapter(getActivity());
    }

    private void initData() {
        final VideoDownloadManager vm = VideoDownloadManager.getInstance();
        /**正式数据**/
        final List<VideoItemBean> data = vm.getBufferingVideos();
//        /**测试用数据**/
//        List<VideoItemBean> data = vm.getBufferedVideos();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            info = data.get(i);
            MyPolyvDownloadProgressListener downloadListener = new MyPolyvDownloadProgressListener(
                    info);
            vm.putDownloadListener(info.vid, downloadListener);
        }
        VideoCachingListAdapter.setData(data);
        lv_buffering.setAdapter(VideoCachingListAdapter);
        lv_buffering.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("长按删除 ==== > ", "item");
                //判断读写权限
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 无权限----
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                } else {
                    VideoDownloadManager.getInstance().deleteVideo(data.get(position));
                    VideoCachingListAdapter.removeItem(position);
                    Toast.makeText(getActivity(), "该视频已删除", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

    }

    class MyPolyvDownloadProgressListener implements
            PolyvDownloadProgressListener {
        private VideoItemBean video;

        public MyPolyvDownloadProgressListener(VideoItemBean video) {
            this.video = video;
        }

        @Override
        public void onDownload(long current, long total) {
            video.current = current;
            video.total = total;
            Log.i("bad-boy", "cu: " + current + ", total: " + total);
            updateList();
        }

        @Override
        public void onDownloadFail(PolyvDownloaderErrorReason reson) {
            video.status = VideoItemBean.STATUS_FAILED;
            updateList();
        }

        @Override
        public void onDownloadSuccess() {
            VideoCachingListAdapter.removeItem(video);
            updateList();
        }
    }

    private boolean toUpdate = false;
    private Handler h = new Handler();

    private void updateList() {
        if (!toUpdate) {
            toUpdate = true;
            h.postDelayed(updateRunnable, 1000);
        }
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            toUpdate = false;
            VideoCachingListAdapter.notifyDataSetChanged();
        }
    };

}
