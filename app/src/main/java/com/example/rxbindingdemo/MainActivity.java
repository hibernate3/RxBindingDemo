package com.example.rxbindingdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.ogaclejapan.rx.binding.RxAction;
import com.ogaclejapan.rx.binding.RxProperty;
import com.ogaclejapan.rx.binding.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.btn)
    Button mBtn;

    private RxProperty<String> spContent = RxProperty.create();
    private SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSP = getSharedPreferences("my_sharedpreference", MODE_PRIVATE);
        mSP.registerOnSharedPreferenceChangeListener(spListener);

        mSP.edit().putString("key", "我是原值").apply();

        RxView.of(mTv).bind(spContent, new RxAction<TextView, String>() {
            @Override
            public void call(TextView target, String s) {
                target.setText(s);
            }
        });
    }

    @OnClick(R.id.btn)
    public void onClick() {
        mSP.edit().putString("key", "我是改后的值").apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSP.unregisterOnSharedPreferenceChangeListener(spListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener spListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            spContent.set(sharedPreferences.getString(key, ""));
        }
    };
}
