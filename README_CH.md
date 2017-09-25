# JPSpringMenu
SpringMenu是一款滑动菜单,类似AndroidResidemenu,SlidingMenu,和其他滑动菜单之间
最大的区别是:它提供的灵活性弹跳，结合了Facebook提供的强大动画库
[Rebound](https://github.com/facebook/rebound) 

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
在Activity内初始化Menu
```
    // R.layout.view_menu 是你自定义的Menu View的资源ID
    SpringMenu menu = new SpringMenu(this,R.layout.view_menu);
    
    // 为菜单做各种各样的设置...
```
不要忘记在Activity重写dispatchTouchEvent
```
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
         return menu.dispatchTouchEvent(ev);
    }
```
通过SpringConfig，你可以改变菜单和子布局的弹性速度和力量
```
    // 另一种方式来构建SpringConfig是frombouncinessandspeed
    mSpringMenu.setMenuSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20,3));
    mSpringMenu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20, 5));
```
如果有滑块控件和菜单的一些冲突，你可以试试addignoreview忽略它们
```
    mSpringMenu.addIgnoreView(...);
```
MenuListener可以用来监听菜单事件的变化,里面有三种回调方法:
```
    //打开后的回调
    void onMenuOpen();
    //关闭后的回调
    void onMenuClose();
    
    /**
     * 当菜单正在被打开或者被关闭时,这个方法将会被回调(包含拖动弧度)
     * @value: 0f-2f,0f表示菜单关闭,2f则表示打开
     * @bouncing: 这个布尔值用来判断菜单是否在反弹状态
     * 当处于反弹状态时,这个值无限趋近于2f,否则就是0f
     */
    void onProgressUpdate(float value,boolean bouncing);

```
剩余部分较为重要的Api
```
     // 内容页变暗的效果
     setFadeEnable(boolean);
     
     // 允许菜单开始拖动的距离
     setDragOffset(float);
     
     setMenuListener(MenuListener);
     
     setDirection(int direction);
```

## Thanks
[AndroidResideMenu](https://github.com/SpecialCyCi/AndroidResideMenu)

## Contact
如果您发现错误或有一些建议，请随时与我联系。<br>
我的电子邮箱:peng8350@gmail.com

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