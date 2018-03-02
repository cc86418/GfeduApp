package cn.jun.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import jc.cici.android.R;

public class SynchronizationDialog extends Dialog  {
    private Context context;

    public SynchronizationDialog(Context context, String content) {
        super(context, R.style.SynchronizationDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.synchornization_progressbar);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }




}
