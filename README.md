# JPSpringMenu
SpringMenu is a sliding menu, similar to AndroidResidemenu, SlidingMenu,
The biggest difference between SpringMenu and other menus is that it gives flexibility bouncing, 
and this project combines the powerful animation elastic library called 
[Rebound](https://github.com/facebook/rebound) provided by Facebook<Br>
## [中文文档](https://github.com/peng8350/JPSpringMenu/blob/master/README_CH.md)

## Design Sketch
![SpringMenu](https://github.com/peng8350/JPSpringMenu/blob/master/art/main.gif)
<br>
## Usage
### Gradle 
```
dependencies {
    compile 'com.jpeng:jpspringmenu:$latestVersion'
}
```
in Activity init
```
    // R.layout.view_menu is your custom menu Layout resourceId
    SpringMenu menu = new SpringMenu(this,R.layout.view_menu);
    
    // do something config for menu...
```
Don't forget to Rewrite dispatchTouchEvent in Activity
```
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
         return menu.dispatchTouchEvent(ev);
    }
```
With SpringConfig, you can change the power and speed of the menu and child layout
```
    // Another way to construct SpringConfig is that fromBouncinessAndSpeed
    mSpringMenu.setMenuSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20,3));
    mSpringMenu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20, 5));
```
If there are some conflicts with slider controls and menus, you can try 
addIgnoreView to ignore them
```
    mSpringMenu.addIgnoreView(...);
```
MenuListener can use to listening the menu event,there are three callbacks in it:
```
    //opened CallBack
    void onMenuOpen();
    //closed CallBack
    void onMenuClose();
    
    /**
     * When the menu is opening or closing,the method will call back(contain dragging arc)
     * @value: 0f-2f,0f indicates that the menu is closed,2f indicates that opening
     * @bouncing: this Boolean value is used to determine whether or not it is in bouncing,
     * when in bouncing,The value is infinitely close to 2f,else is 0f
     */
    void onProgressUpdate(float value,boolean bouncing);

```
The remaining part of the more important Api
```
     //Content Page dark effect
     setFadeEnable(boolean);
     
     // distance of Allow the menu to begin dragging
     setDragOffset(float);
     
     setMenuListener(MenuListener);
     
     setDirection(int direction);
```


## Thanks
[AndroidResideMenu](https://github.com/SpecialCyCi/AndroidResideMenu)

## Contact
If you find bugs or have some advices,please feel free to contact me.<br>
My E-mail:peng8350@gmail.com

## License
```
MIT License

Copyright (c) 2017 Mr.peng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```