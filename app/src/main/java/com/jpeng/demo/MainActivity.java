package com.jpeng.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.facebook.rebound.SpringConfig;
import com.jpeng.jpspringmenu.MenuListener;
import com.jpeng.jpspringmenu.SpringMenu;

public class MainActivity extends Activity implements MenuListener,
        RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    SpringMenu mSpringMenu;

    TitleBar mTitleBar;

    SeekBar mTensionbar, mFrictionBar;

    TextView mTvTension, mTvFriction;

    ImageView mIvIgnore;

    RadioGroup mRgFade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mRgFade = (RadioGroup) findViewById(R.id.rg_enablefade);
        mFrictionBar = (SeekBar) findViewById(R.id.sb_friction);
        mTensionbar = (SeekBar) findViewById(R.id.sb_tension);
        mTvFriction = (TextView) findViewById(R.id.tv_friction);
        mTvTension = (TextView) findViewById(R.id.tv_tension);
        mIvIgnore = (ImageView) findViewById(R.id.iv_ignore);
        mRgFade.setOnCheckedChangeListener(this);
        mTensionbar.setOnSeekBarChangeListener(this);
        mFrictionBar.setOnSeekBarChangeListener(this);
        mFrictionBar.setMax(100);
        mTensionbar.setMax(100);
        //init SpringMenu
        mSpringMenu = new SpringMenu(this, R.layout.view_menu);
        mSpringMenu.setMenuListener(this);
        mSpringMenu.setFadeEnable(true);
        mSpringMenu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20, 5));
        mSpringMenu.setDragOffset(0.4f);
        mSpringMenu.addIgnoredView(mFrictionBar
        );
        mSpringMenu.addIgnoredView(mTensionbar);
        // init titlebar
        mTitleBar.setLeftText("Left");
        mTitleBar.setTitle(R.string.app_name);
        mTitleBar.setBackgroundColor(Color.parseColor("#64b4ff"));
        mTitleBar.setDividerColor(Color.GRAY);
        mTitleBar.setTitleColor(Color.WHITE);
        mTitleBar.setLeftTextColor(Color.WHITE);
        mTitleBar.setActionTextColor(Color.WHITE);
        mTitleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpringMenu.setDirection(SpringMenu.DIRECTION_LEFT);
                mSpringMenu.openMenu();
            }
        });
        mTitleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return "Right";
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                mSpringMenu.setDirection(SpringMenu.DIRECTION_RIGHT);
                mSpringMenu.openMenu();
            }
        });

        ListBean[] listBeen = {new ListBean(R.mipmap.menu5, "菜单选项一"), new ListBean(R.mipmap.menu1, "菜单选项二"), new ListBean(R.mipmap.menu2, "菜单选项三"), new ListBean(R.mipmap.menu3, "菜单选项四"), new ListBean(R.mipmap.menu4, "菜单选项五")};
        MyAdapter adapter = new MyAdapter(this, listBeen);
        ListView listView = (ListView) mSpringMenu.findViewById(R.id.test_listView);
        listView.setAdapter(adapter);

        mFrictionBar.setProgress(3);
        mTensionbar.setProgress(20);

        mSpringMenu.addIgnoredView(mIvIgnore);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mSpringMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onMenuOpen() {
        Toast.makeText(this, "菜单已经打开!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuClose() {
        Toast.makeText(this, "菜单已经关闭!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.radioButton) {
            mSpringMenu.setFadeEnable(true);
        } else {
            mSpringMenu.setFadeEnable(false);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mTensionbar) {
            mTvTension.setText("Tension:" + progress);
        } else {
            mTvFriction.setText("Fricsion:" + progress);
        }
        mSpringMenu.setMenuSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(mTensionbar.getProgress(), mFrictionBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
