package cn.jun.menory.manage_activity;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;
import java.util.List;
import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.bean.ViewHolder;
import cn.jun.menory.service.VideoDownloadManager;
import jc.cici.android.R;

public class VideoCachingListAdapter extends BaseAdapter {
    private List<VideoItemBean> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public VideoCachingListAdapter(Context ctx) {
        this.context = ctx;
        layoutInflater = LayoutInflater.from(ctx);

    }

    public void setData(List<VideoItemBean> list) {
        this.list = list;
    }

    public void removeItem(VideoItemBean item) {
        if (list != null) {
            list.remove(item);
        }
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_buffering, parent,
                    false);
            vh = new ViewHolder(context, convertView, parent, position);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final VideoItemBean item = list.get(position);

        TextView tvName = vh.getView(R.id.tv_name);
//        tvName.setText(item.lessonname.replaceAll("&nbsp;", " "));
        tvName.setText(item.subjectname.replaceAll("&nbsp;", " "));
//        TextView tvDesc = vh.getView(R.id.tv_desc);
//        tvDesc.setText(item.desc.replaceAll("&nbsp;", " "));
//        final DonutProgress dp = vh.getView(R.id.progr    ess);
        final ProgressBar dp = vh.getView(R.id.progress);
        final TextView progress_tv = vh.getView(R.id.progress_tv);
        final ImageView iv = vh.getView(R.id.iv_right);
        final ImageView iv2 = vh.getView(R.id.iv_right2);
        final TextView tv_desc = vh.getView(R.id.tv_desc);//下载状态文字显示
        if (item.status == VideoItemBean.STATUS_DOWNLOADING) {
            tv_desc.setText("下载中");
            iv.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.GONE);
//            dp.setMax(Integer.parseInt(String.valueOf(Math.max(1, item.total))));
//            dp.setProgress(Integer.parseInt(String.valueOf(Math.max(1, item.current))));
            float float_current,float_total;
            float_current = item.current;
            float_total=item.total;
            float percent = (float_current / float_total) * 100;

            progress_tv.setText((int)percent + "%");
            dp.setProgress((int) percent);
//            switch (item.status) {
//                case VideoItemBean.STATUS_SUCCESS:
//                    iv2.setVisibility(View.GONE);
//                    iv.setBackgroundResource(0);
//                    iv.setText("下载成功");
//                    iv.setTextColor(Color.parseColor("#DD5555"));
//                    break;
//            }
        } else {
            iv.setVisibility(View.GONE);
            iv2.setVisibility(View.VISIBLE);
            switch (item.status) {
                case VideoItemBean.STATUS_FAILED:
                    iv2.setBackgroundResource(R.drawable.about_icon);
                    tv_desc.setText("下载失败");
                    break;
                case VideoItemBean.STATUS_PAUSED:
                    iv2.setBackgroundResource(R.drawable.ic_paused);
                    tv_desc.setText("暂停");
                    break;

            }
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("--->", "暂停");
                iv.setVisibility(View.GONE);
                iv2.setVisibility(View.VISIBLE);
                PolyvDownloader downloader = PolyvDownloaderManager
                        .getPolyvDownloader(item.vid, item.bitrate,
                                Video.HlsSpeedType.getHlsSpeedType(item.speed));
                if (downloader != null) {
                    downloader.stop();
                }

                item.status = VideoItemBean.STATUS_PAUSED;
                VideoDownloadManager.getInstance().updateStatus(item.vid,
                        item.status);

            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("--->", "继续");
                iv.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.GONE);
                PolyvDownloader downloader = PolyvDownloaderManager
                        .getPolyvDownloader(item.vid, item.bitrate,
                                Video.HlsSpeedType.getHlsSpeedType(item.speed));
                if (downloader != null) {
                    downloader.start();
                }

                item.status = VideoItemBean.STATUS_DOWNLOADING;
                VideoDownloadManager.getInstance().updateStatus(item.vid,
                        item.status);


            }
        });

        return convertView;
    }

    public void removeItem(int pos) {
        if (list == null || pos < 0 || pos >= list.size()) {
            return;
        }
        list.remove(pos);
        notifyDataSetChanged();
    }

}
