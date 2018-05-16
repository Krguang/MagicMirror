package com.yy.k.magicmirror;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

public class Monitor extends Activity {

    private MySurfaceView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mView = (MySurfaceView) findViewById(R.id.mView);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.monitor);
    }
}
