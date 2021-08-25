package com.jeffmony.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeffmony.videocache.utils.ProxyCacheUtils;

import java.util.Map;

public class MainActivity extends Activity {

    private EditText mVideoUrlEditText;
    private Button mVideoPlayBtn;
    private Button mVideoCacheBtn;
    private CheckBox mLocalProxyBox;
    private CheckBox mUseOkHttpBox;

    private RadioGroup mRadioGroup;
    private RadioButton mExoBtn;
    private RadioButton mIjkBtn;

    private boolean mIsExoSelected;
    private boolean mIsIjkSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoUrlEditText = findViewById(R.id.video_url_edit_text);
        mVideoPlayBtn = findViewById(R.id.video_play_btn);
        mVideoCacheBtn = findViewById(R.id.video_cache_btn);
        mLocalProxyBox = findViewById(R.id.local_proxy_box);
        mUseOkHttpBox = findViewById(R.id.okhttp_box);
        mRadioGroup = findViewById(R.id.player_group);
        mExoBtn = findViewById(R.id.exo_play_btn);
        mIjkBtn = findViewById(R.id.ijk_play_btn);
        mExoBtn.setChecked(true);
        mIsExoSelected = mExoBtn.isChecked();
        mIsIjkSelected = mIjkBtn.isChecked();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mIsExoSelected = mExoBtn.isChecked();
                mIsIjkSelected = mIjkBtn.isChecked();
            }
        });
        String videoUrl = mVideoUrlEditText.getText().toString();
        mVideoPlayBtn.setOnClickListener(view -> {
            if (TextUtils.isEmpty(videoUrl)) {
                Toast.makeText(MainActivity.this, "The video url is empty", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_url", videoUrl);
                intent.putExtra("local_proxy_enable", mLocalProxyBox.isChecked());
                intent.putExtra("use_okttp_enable", mUseOkHttpBox.isChecked());
                int type;
                if (mIsExoSelected) {
                    type = 1;
                } else {
                    type = 2;
                }
                intent.putExtra("player_type", type);
                startActivity(intent);
            }
        });

        mVideoCacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(videoUrl)) {
                    Toast.makeText(MainActivity.this, "The video url is empty", Toast.LENGTH_LONG).show();
                } else {
                    ProxyCacheUtils.getConfig().setUseOkHttp(mUseOkHttpBox.isChecked());
                    SimpleCacheManager cacheManager = new SimpleCacheManager(videoUrl);
                    Map<String, String> headers = new ArrayMap<>();
                    // 在播放之前，缓存 100kb
                    // 场景就是抖音播放当前视频的时候，会缓存下一个视频
                    headers.put("Range", "bytes=0-102400");
                    cacheManager.startCache(headers, null);
                }
            }
        });
    }
}