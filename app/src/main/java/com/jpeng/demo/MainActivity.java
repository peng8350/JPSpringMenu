package com.jpeng.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.facebook.rebound.SpringConfig;
import com.jpeng.jpspringmenu.MenuListener;
import com.jpeng.jpspringmenu.SpringMenu;

public class MainActivity extends Activity implements MenuListener {
    SpringMenu menu;

    TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        //init SpringMenu
        menu = new SpringMenu(this, R.layout.view_menu);
        menu.setMenuListener(this);
        menu.setFadeEnable(true);
        menu.setDirection(SpringMenu.DIRECTION_RIGHT);
        menu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(181,10));
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
                menu.setDirection(SpringMenu.DIRECTION_LEFT);
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
                menu.setDirection(SpringMenu.DIRECTION_RIGHT);
                menu.openMenu();
            }
        });

        ListBean[] listBeen = {new ListBean(R.mipmap.menu5,"菜单选项一"),new ListBean(R.mipmap.menu1,"菜单选项二"), new ListBean(R.mipmap.menu2,"菜单选项三"),new ListBean(R.mipmap.menu3,"菜单选项四"),new ListBean(R.mipmap.menu4,"菜单选项五")};
        MyAdapter adapter = new MyAdapter(this, listBeen);
        ListView listView = (ListView) menu.findViewById(R.id.test_listView);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return menu.dispatchTouchEvent(ev);
    }

    @Override
    public void onMenuOpen() {
        Toast.makeText(this,"菜单已经打开!!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuClose() {
        Toast.makeText(this,"菜单已经关闭!!!",Toast.LENGTH_SHORT).show();
    }
}
