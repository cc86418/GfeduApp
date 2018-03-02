package cn.jun.menory;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jc.cici.android.R;

public class WhiteDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private ImageView imgTips;
    private TextView tvTitle;
    private TextView tvContent;
    private View dialogLine;
    private Button btnConfirm;
    private Button btnCancel;
    private RelativeLayout view;

    private String title;
    private String content;
    private int iconRes;
    private String confrimString;
    private String cancelString;
    /** 如果只需要一个按钮，该值传false（默认保留确定按钮，隐藏取消按钮） */
    private boolean bothButton;

    /**
     *
     * @param context
     * @param title
     *            标题
     * @param content
     *            内容
     * @param iconRes
     *            内容上面的图标（和标题功能相似）,如果传0表示，不需要显示图标,传1表示显示默认图标,也可以穿图标的资源id
     * @param cancelString
     *            取消按钮的文案（不传则用默认文案：取消）
     * @param confirmString
     *            确定按钮的文案（不传则用默认文案：确定）
     * @param bothButton
     *            是否需要确定和取消两个按钮同时出现，true=两个都有，false=只保留一个确定按钮
     */
    public WhiteDialog(Context context, String title, String content,
                       int iconRes, String cancelString, String confirmString,
                       boolean bothButton) {
        super(context, R.style.WhiteDialog);
        this.context = context;
        this.title = title;
        this.content = content;
        this.iconRes = iconRes;
        this.cancelString = cancelString;
        this.confrimString = confirmString;
        this.bothButton = bothButton;
    }

    /**
     *
     * @param context
     * @param title
     *            标题
     * @param content
     *            内容
     * @param iconRes
     *            内容上面的图标（和标题功能相似）,如果传0表示，不需要显示图标,传1表示显示默认图标,也可以穿图标的资源id
     * @param bothButton
     *            是否需要确定和取消两个按钮同时出现，true=两个都有，false=只保留一个确定按钮
     */
    public WhiteDialog(Context context, String title, String content,
                       int iconRes, boolean bothButton) {
        super(context, R.style.WhiteDialog);
        this.title = title;
        this.content = content;
        this.iconRes = iconRes;
        this.bothButton = bothButton;
    }

    public WhiteDialog(Context context, String content) {
        super(context, R.style.WhiteDialog);
        this.title = "";
        this.content = content;
        this.iconRes = 0;
        this.bothButton = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);

        initView();
    }

    public void initView() {
        imgTips = (ImageView) findViewById(R.id.dc_dialog_icon);
        tvTitle = (TextView) findViewById(R.id.dc_dialog_txt_title);
        tvContent = (TextView) findViewById(R.id.dc_dialog_txt_content);
        dialogLine = findViewById(R.id.dc_dialog_view_line_v);
        btnCancel = (Button) findViewById(R.id.dc_dialog_btn_cancel);
        btnConfirm = (Button) findViewById(R.id.dc_dialog_btn_ok);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        } else {
            tvContent.setVisibility(View.GONE);
        }

        try {
            if (iconRes > 0) {
                if (iconRes > 1) {
                    imgTips.setImageResource(iconRes);
                }
            } else {
                imgTips.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            imgTips.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(cancelString)) {
            btnCancel.setText(cancelString);
        }
        if (!TextUtils.isEmpty(confrimString)) {
            btnConfirm.setText(confrimString);
        }
        if (!bothButton) {
            btnCancel.setVisibility(View.GONE);
            dialogLine.setVisibility(View.GONE);
            if (TextUtils.isEmpty(confrimString)) {
                btnConfirm.setText("确定");
            }
            btnConfirm
                    .setBackgroundResource(R.drawable.selector_bg_gray_bottom_corner);
        }
        initEvent();
        setCanceledOnTouchOutside(false);
    }

    private void initEvent() {
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dc_dialog_btn_cancel:
                if (this.listener != null) {
                    listener.onCancelClick();
                }
                dismiss();
                break;
            case R.id.dc_dialog_btn_ok:
                if (this.listener != null) {
                    listener.onConfirmClick();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    OnCommonDialogClickListener listener;

    public interface OnCommonDialogClickListener {
        void onCancelClick();

        void onConfirmClick();
    }

    /**
     * 设置事件监听
     *
     * @param lis
     */
    public void setOnCommonDialogClickListener(OnCommonDialogClickListener lis) {
        this.listener = lis;

    }


}
