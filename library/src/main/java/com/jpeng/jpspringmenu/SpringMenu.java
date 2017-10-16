package com.jpeng.jpspringmenu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.facebook.rebound.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author jpeng
 * Date: 17-9-10 下午1:41
 * E-mail:peng8350@gmail.com
 * Description:
 */
public class SpringMenu extends RelativeLayout implements SpringListener {
    private static final float START_VALUE=0f;
    private static final float END_VALUE =2f;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    /**
     *  endX:The dividing X between the content page and the menu page
     */
    private float endX = 800;
    /**
      *  point value when touch down
      */
    private int lastDownX, lastDownY;
    /**
     *   distance of enable menu drag
     */
    private int mDragoffset;
    private int arcDrawY;
    private int statusHeight;
    private int mDirection;
    /**
     *   views which intercept touch events
     */
    private List<View> mIgnores;

    /**
     *  if the content need dark effect
     **/
    private boolean mNeedFade;
    /**
     * check if user is in dragging
     */
    private boolean isDragging = false;
    private boolean isOpen;
    /**
     *  use to disable Bouncing when closing the menu
     */
    private boolean isBouncing;
    private boolean disableDrag;
    /**
     *  main controller of bouncing animate
     */
    private Spring mSpring;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private MenuView mMenuView;
    private ViewGroup mContent;
    /**
     * Black mask View
     */
    private View mFadeView;
    private ViewGroup mDecorView;

    private MenuListener menuListener;

    public SpringMenu(Context context, int layoutRes) {
        super(context);
        initSpring();
        initView(context, layoutRes);
        attachToActivity((Activity) context);
        initData();
    }

    private void initData() {
        mIgnores = new ArrayList<>();
        mDragoffset = (int) (0.3f * getScreenWidth());
        statusHeight = getStatusBarHeight();
        setMenuWidth(0.75f);
    }

    private void initView(Context context, int layoutRes) {
        mDecorView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        mContent = (ViewGroup) mDecorView.getChildAt(0);
        mMenuView = new MenuView(getContext(), layoutRes);
        mFadeView = new View(context);
        mFadeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFadeView.setBackgroundColor(Color.argb(180, 0, 0, 0));
        mFadeView.setAlpha(0);
    }

    private void initSpring() {
        SpringSystem springSystem = SpringSystem.create();
        mSpring = springSystem.createSpring();
        setMenuSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20, 3));
        mSpring.addListener(this);
    }

    /**
     * If the motion event was relative to the view
     * which in ignored view list,return true;
     */
    private boolean isInIgnoredView(MotionEvent ev) {
        Rect rect = new Rect();
        for (View v : mIgnores) {
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    /**
     * make the Spring close Bouncing when the widget is still in
     * Bouncing
     */
    private void endSpring() {
        if (!mSpring.isAtRest()) {
            mSpring.setAtRest();
        }
    }

    /**
     * Controls the drawing of Beckinsale curves when touching
     *
     * @param x The drag distance ratio which takes up endX
     */
    private void drawArc(float x) {
        if (x < 0) {
            return;
        }
        float nowValue = x / endX;
        if (!isOpen) {
            mSpring.setCurrentValue(nowValue >= 1f ? 0.99f : nowValue);
        } else {
            mSpring.setCurrentValue(nowValue >= 1f ? 1.01f : END_VALUE - nowValue);
        }
    }

    /**
     * reset the menu state when user drag the menu but the drag distance
     * is smaller than the requirement
     */
    private void resumeMenu() {
        if (!isOpen) {
            mSpring.setEndValue(START_VALUE);
        } else {
            mSpring.setEndValue(END_VALUE);
        }
    }

    /**
     * make menu bind up with the activity
     */
    public void attachToActivity(Activity act) {
        mDecorView.removeViewAt(0);
        addView(mContent);
        addView(mFadeView, 1);
        addView(mMenuView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mDecorView.addView(this);
    }

    public void openMenu() {
        mMenuView.setDisableTouch(false);
        mSpring.setEndValue(END_VALUE);
        mMenuView.toggleItems(true);
    }

    public void closeMenu() {
        mMenuView.setDisableTouch(true);
        mSpring.setEndValue(START_VALUE);
        mMenuView.toggleItems(false);
    }

    /**
     * get the MainLayout in MenuView
     */
    public View getMenuView() {
        if (mMenuView != null) {
            return mMenuView.getLayout();
        }
        return null;
    }

    /**
     * If there were some view you don't want reside menu
     * to intercept their touch event, you could add it to
     * ignored views.
     */
    public void addIgnoredView(View v) {
        mIgnores.add(v);
    }

    public void removeIgnoredView(View v) {
        mIgnores.remove(v);
    }

    public void clearIgnoredViewList() {
        mIgnores.clear();
    }

    public boolean isOpened() {
        return isOpen;
    }

    /**
     * set menu layout's width
     */
    public void setMenuWidth(int width) {
        endX = width;
        mMenuView.getLayoutParams().width = width + 100;
    }

    public void setMenuListener(MenuListener listener) {
        menuListener = listener;
    }

    /**
     * If enable,It will make mContentView have a darker cell view when opening menu
     */
    public void setFadeEnable(boolean enable) {
        mNeedFade = enable;
    }

    /**
     * set drag distance to open
     *
     * @param ratio the ratio of screenwidth
     */
    public void setDragOffset(float ratio) {
        mDragoffset = (int) (ratio * getScreenWidth());
    }

    /**
     * set drag distance to open
     *
     * @param width menuwidth
     */
    public void setDragOffset(int width) {
        mDragoffset = width;
    }

    public void setMenuWidth(float screenRatio) {
        int width = (int) (getScreenWidth() * screenRatio);
        setMenuWidth(width);
    }

    /**
     * change the Menu Scroll direction
     *
     * @param direction left or right
     */
    public void setDirection(int direction) {
        this.mDirection = direction;
        RelativeLayout.LayoutParams params = (LayoutParams) mMenuView.getLayoutParams();
        // This code can be replaced as removeRule to remove,but the api>= 17
        params.addRule(direction == DIRECTION_LEFT ? ALIGN_PARENT_RIGHT : ALIGN_PARENT_LEFT, 0);
        params.addRule(direction == DIRECTION_LEFT ? ALIGN_PARENT_LEFT : ALIGN_PARENT_RIGHT);
        mMenuView.requestLayout();
    }

    /**
     * set the bouncing power and speed,just SpringConfig
     */
    public void setMenuSpringConfig(SpringConfig config) {
        mSpring.setSpringConfig(config);
    }

    /**
     * This configuration affects the speed at
     * which child controls appear on the menu
     */
    public void setChildSpringConfig(SpringConfig config) {
        mMenuView.setAttachmentConfig(config);
    }

    private int getScreenHeight() {
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getScreenWidth() {
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        arcDrawY = 0;
        isBouncing = false;
    }

    @Override
    public void onSpringActivate(Spring spring) {
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        mMenuView.updateByProgress(value
        );
        if (!isOpen && value >= END_VALUE) {
            isOpen = true;
            if (menuListener != null) {
                menuListener.onMenuOpen();
            }
        } else if (isOpen && value <= 0) {
            isOpen = false;
            if (menuListener != null) {
                menuListener.onMenuClose();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE || ev.getAction() == MotionEvent.ACTION_UP) {
            //If Action_Down is in IgnoreViews
            if (disableDrag) {
                return super.dispatchTouchEvent(ev);
            }
            if (!isOpen) {
                //If touch down X is over Offset distance,It will interfere with the touch event of the menu
                if (mDirection == DIRECTION_RIGHT && lastDownX < getScreenWidth() - mDragoffset) {
                    return super.dispatchTouchEvent(ev);
                }
                else if(mDirection == DIRECTION_LEFT && lastDownX > mDragoffset){
                    return super.dispatchTouchEvent(ev);
                }
            }
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastDownX = (int) ev.getRawX();
                lastDownY = (int) ev.getRawY();
                disableDrag = isInIgnoredView(ev) && !isOpen;
                if (isOpen) {
                    if(mDirection == DIRECTION_RIGHT&&lastDownX < getWidth() - endX) {
                        return true;
                    }
                    else if(mDirection == DIRECTION_LEFT && lastDownX > endX ){
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int xOffset = (int) (ev.getRawX() - lastDownX);
                int yOffset = (int) (ev.getRawY() - lastDownY);
                if (!isDragging) {
                    if (yOffset > 25 || yOffset < -25 || xOffset > 25 || xOffset < -25) {
                        isDragging = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else {
                    arcDrawY = (int) ev.getRawY() - statusHeight;
                    // If arcDrawY < 0,It will make ArcPath is not Convex
                    arcDrawY = arcDrawY < 0 ? 1 : arcDrawY;
                    endSpring();
                    float dis = !isOpen ? ev.getRawX() - lastDownX : -ev.getRawX() + lastDownX;
                    drawArc(mDirection == DIRECTION_LEFT ? dis : -dis);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                // When the menu is opened, the menu closes if the user touches the content page
                if (!isDragging && isOpen) {
                    if(mDirection == DIRECTION_LEFT && lastDownX > endX) {
                        closeMenu();
                    }
                    else if(mDirection == DIRECTION_RIGHT&&lastDownX < getWidth() - endX){
                        closeMenu();
                    }
                } else if (isDragging) {
                    if (!isOpen) {
                        if (mSpring.getCurrentValue() > 0.55f) {
                            openMenu();
                        } else if (mSpring.getCurrentValue() > START_VALUE) {
                            resumeMenu();
                        }
                    } else {
                        if (mSpring.getCurrentValue() < 1.5f) {
                            closeMenu();
                        } else {
                            resumeMenu();
                        }
                    }
                }
                isDragging = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        int bottomPadding = mContent.getPaddingBottom() + insets.bottom;
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        if (!hasBackKey && !hasHomeKey) {
            bottomPadding += getNavigationBarHeight();
        }
        this.setPadding(mContent.getPaddingLeft() + insets.left,
                mContent.getPaddingTop() + insets.top,
                mContent.getPaddingRight() + insets.right,
                bottomPadding);
        insets.left = insets.top = insets.right = insets.bottom = 0;
        return true;
    }

    private class MenuView extends FrameLayout {
        private Path mArcPath;
        /**
         *  when menu is open,the value is false,
         *  else true,use to Intercept menu Touch
         */
        private boolean disableTouch = true;

        private ViewGroup mLayout;

        private SpringChain mSpringChain;

        public MenuView(Context context, int layoutRes) {
            super(context);
            init(context, layoutRes);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setElevation(36);
                setClipToOutline(true);
                setOutlineProvider(new BgOutLineProvider());
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        class BgOutLineProvider extends ViewOutlineProvider {
            @Override
            public void getOutline(View view, Outline outline) {
                if (mArcPath.isConvex()) {
                    outline.setConvexPath(mArcPath);
                }
            }
        }

        /**
          * use to draw the Beckinsale curves when open menu (0f-2f)
          */
        private void updateByProgress(float progress) {
            mArcPath.reset();
            float startX = mDirection == DIRECTION_LEFT ? 0 : getWidth();

            if (progress >= START_VALUE && progress <= END_VALUE && mNeedFade) {
                mFadeView.setAlpha(progress / 2);
            }
            if (progress >= 1f) {
                float progressX = endX * (progress - 1f);
                float realX = mDirection == DIRECTION_LEFT ? progressX : startX - progressX;
                mArcPath.moveTo(startX, 0);
                mArcPath.lineTo(realX, 0);
                mArcPath.quadTo(mDirection == DIRECTION_LEFT ? endX : startX - endX, arcDrawY == 0 ? getHeight() / 2 : arcDrawY, realX, getHeight());
                mArcPath.lineTo(startX, getHeight());
                mContent.setTranslationX(mDirection == DIRECTION_LEFT ? progressX : -progressX);
                if (progress >= END_VALUE) {
                    isBouncing = true;
                }
            } else {
                mArcPath.moveTo(startX, 0);
                if (isOpen) {
                    mArcPath.quadTo(mDirection == DIRECTION_LEFT ? endX * progress : getScreenWidth() - endX * progress, arcDrawY == 0 ? getHeight() / 2 : arcDrawY, startX, getHeight());
                    if (progress <= START_VALUE) {
                        isBouncing = true;
                    }
                    mContent.setTranslationX(0);
                } else {
                    mArcPath.quadTo(mDirection == DIRECTION_LEFT ? endX * 2 * progress : startX - (endX * 2 * progress), arcDrawY == 0 ? getHeight() / 2 : arcDrawY, startX, getHeight());
                }

            }
            mArcPath.close();
            if (menuListener != null) {
                menuListener.onProgressUpdate(progress, isBouncing);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mSpring.getCurrentValue() <= END_VALUE) {
                    invalidateOutline();
                }
            }
            postInvalidate();
        }

        private void init(Context context, int layoutRes) {
            mLayout = (ViewGroup) LayoutInflater.from(context).inflate(layoutRes, null);
            mSpringChain = SpringChain.create();
            addView(mLayout);
            addSpringForChild(mLayout);
            mSpringChain.setControlSpringIndex(0);
            List<Spring> springs = mSpringChain.getAllSprings();
            for (Spring s : springs) {
                s.setCurrentValue(1f);
            }
            toggleItems(false);
            mArcPath = new Path();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }

        private void addSpringForChild(ViewGroup group) {
            for (int i = 0; i < group.getChildCount(); i++) {
                final View v = group.getChildAt(i);
                mSpringChain.addSpring(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        float value = (float) spring.getCurrentValue();
                        v.setTranslationX(mDirection == DIRECTION_LEFT ? -endX + value * endX : endX - value * endX);
                    }
                });
            }
        }

        private void setAttachmentConfig(SpringConfig config) {
            List<Spring> springs = mSpringChain.getAllSprings();
            for (Spring s : springs) {
                s.setSpringConfig(config);
            }
        }

        /**
         * make the child widgets of menuLayout showing or hiding
         */
        private void toggleItems(boolean show) {
            mSpringChain.getControlSpring().setEndValue(show ? 1f : 0f);
        }

        private ViewGroup getLayout() {
            return mLayout;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return disableTouch || isDragging;
        }

        private void setDisableTouch(boolean disableTouch) {
            this.disableTouch = disableTouch;
        }

        /**
         * Clipping Region from the path
         */
        @Override
        protected void dispatchDraw(Canvas canvas) {
            canvas.save();
            canvas.clipPath(mArcPath);
            super.dispatchDraw(canvas);
            canvas.restore();
        }
    }
}
