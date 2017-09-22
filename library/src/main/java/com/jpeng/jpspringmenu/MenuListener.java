package com.jpeng.jpspringmenu;

/**
 * Author: jpeng
 * Date: 17-9-10 下午1:37
 * E-mail:peng8350@gmail.com
 * Description:If you want do something after the menu is closed or opened.
 * you can use this listener to listen open and close event
 */
public interface MenuListener {

    /**
     * Menu open Callback
     */
    void onMenuOpen();

    /**
     * Menu close Callback
     */
    void onMenuClose();

    /**
     * Menu Progress update CallBack
     */
    void onProgressUpdate(float value,boolean bouncing);

}
