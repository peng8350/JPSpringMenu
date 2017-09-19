package com.jpeng.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.facebook.rebound.SpringConfig;
import com.jpeng.jpspringmenu.MenuListener;
import com.jpeng.jpspringmenu.SpringMenu;

public class MainActivity extends AppCompatActivity implements MenuListener {
    SpringMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = new SpringMenu(this, R.layout.view_menu);
        menu.setMenuListener(this);
        menu.setFadeEnable(true);
        menu.setDirection(SpringMenu.DIRECTION_RIGHT);
        menu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(181,10));
        ListBean[] listBeen = {new ListBean(R.mipmap.menu5,"菜单选项一"),new ListBean(R.mipmap.menu1,"菜单选项二"), new ListBean(R.mipmap.menu2,"菜单选项三"),new ListBean(R.mipmap.menu3,"菜单选项四"),new ListBean(R.mipmap.menu4,"菜单选项五")};
        MyAdapter adapter = new MyAdapter(this, listBeen);
        ListView listView = (ListView) menu.findViewById(R.id.test_listView);
        listView.setAdapter(adapter);

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.openMenu();
            }
        });
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
