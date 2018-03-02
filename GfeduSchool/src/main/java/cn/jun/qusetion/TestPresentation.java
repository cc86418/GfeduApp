package cn.jun.qusetion;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import jc.cici.android.R;

public class TestPresentation extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_presentation);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
