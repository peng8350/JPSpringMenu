package com.jpeng.jpspringmenu;

/**
 * Date: 17-9-10 下午1:37
 * @E-mail peng8350@gmail.com
 * @Description If you want do something after the menu is closed or opened.
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
     * When the menu is opening or closing,the method will call back(contain dragging arc)
     * @value: 0f-2f,0f indicates that the menu is closed,2f indicates that opening
     * @bouncing: this Boolean value is used to determine whether or not it is in bouncing,
     * when in bouncing,The value is infinitely close to 2f,else is 0f
     */
    void onProgressUpdate(float value,boolean bouncing);

}
