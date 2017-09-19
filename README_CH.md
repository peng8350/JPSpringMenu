# JPSpringMenu
SpringMenu是一款滑动菜单,类似AndroidResidemenu,SlidingMenu,和其他滑动菜单之间
最大的区别是:它提供的灵活性弹跳，结合了Facebook提供的强大动画库
[Rebound](https://github.com/facebook/rebound) 

### 效果图
![SpringMenu](https://github.com/peng8350/JPSpringMenu/blob/master/art/main.gif)
<br>
### 用法
1.Gradle 
```
dependencies {
    compile 'com.jpeng.jpspringmenu:$latestVersion'
}
```
2.在Activity内初始化Menu
```
    // R.layout.view_menu 是你自定义的Menu View的资源ID
    SpringMenu menu = new SpringMenu(this,R.layout.view_menu);
    
    // 为菜单做各种各样的设置...
```
3. 不要忘记在Activity重写dispatchTouchEvent
```
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
         return menu.dispatchTouchEvent(ev);
    }
```

### 感谢
[AndroidResideMenu](https://github.com/SpecialCyCi/AndroidResideMenu)

### License
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