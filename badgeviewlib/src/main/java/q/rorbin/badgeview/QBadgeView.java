package q.rorbin.badgeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chqiu
 *         Email:qstumn@163.com
 */

public class QBadgeView extends View implements Badge {
    protected int mColorBackground;
    protected int mColorBadgeNumber;
    protected float mBadgeNumberSize;
    protected float mBadgePadding;
    protected int mBadgeNumber;
    protected String mBadgeText;
    protected boolean mDraggable;
    protected boolean mDragging;
    protected boolean mExact;
    protected boolean mShowShadow;
    protected int mBadgeGravity;
    protected int mGravityOffset;

    protected float mDefalutRadius;
    protected float mFinalDragDistance;
    protected int mDragQuadrant;
    protected boolean mDragOutOfRange;

    protected Rect mBadgeNumberRect;
    protected RectF mBadgeBackgroundRect;
    protected Path mDragPath;

    protected PointF mBadgeCenter;
    protected PointF mDragCenter;
    protected PointF mRowBadgeCenter;
    protected PointF mControlPoint;

    protected List<PointF> mInnertangentPoints;

    protected View mTargetView;

    protected int mWidth;
    protected int mHeight;

    protected TextPaint mBadgeNumberPaint;
    protected Paint mBadgeBackgroundPaint;

    protected BadgeAnimator mAnimator;

    protected OnDragStateChangedListener mDragStateChangedListener;

    protected ViewGroup mActivityRoot;

    public QBadgeView(Context context) {
        this(context, null);
    }

    private QBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private QBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, mBadgeBackgroundPaint);
        mBadgeNumberRect = new Rect();
        mBadgeBackgroundRect = new RectF();
        mDragPath = new Path();
        mBadgeCenter = new PointF();
        mDragCenter = new PointF();
        mRowBadgeCenter = new PointF();
        mControlPoint = new PointF();
        mInnertangentPoints = new ArrayList<>();
        mBadgeNumberPaint = new TextPaint();
        mBadgeNumberPaint.setAntiAlias(true);
        mBadgeNumberPaint.setSubpixelText(true);
        mBadgeNumberPaint.setFakeBoldText(true);
        mBadgeBackgroundPaint = new Paint();
        mBadgeBackgroundPaint.setAntiAlias(true);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);
        mColorBackground = 0xFFE84E40;
        mColorBadgeNumber = 0xFFFFFFFF;
        mBadgeNumberSize = DisplayUtil.dp2px(getContext(), 10);
        mBadgePadding = DisplayUtil.dp2px(getContext(), 4f);
        mBadgeNumber = 0;
        mBadgeGravity = Gravity.END | Gravity.TOP;
        mGravityOffset = DisplayUtil.dp2px(getContext(), 5);
        mFinalDragDistance = DisplayUtil.dp2px(getContext(), 100);
        mShowShadow = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslationZ(1000);
        }
    }

    @Override
    public Badge bindTarget(View targetView) {
        if (targetView == null) {
            throw new IllegalStateException("targetView can not be null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent targetParent = targetView.getParent();
        if (targetParent != null && targetParent instanceof ViewGroup) {
            mTargetView = targetView;
            if (targetParent instanceof FrameLayout) {
                ((FrameLayout) targetParent).addView(this, new FrameLayout.LayoutParams(((FrameLayout) targetParent).getWidth(),
                        ((FrameLayout) targetParent).getHeight()));
            } else {
                ViewGroup targetContainer = (ViewGroup) targetParent;
                int index = targetContainer.indexOfChild(targetView);
                ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
                targetContainer.removeView(targetView);
                final FrameLayout badgeContainer = new FrameLayout(getContext());
                targetContainer.addView(badgeContainer, index, targetParams);
                badgeContainer.addView(targetView, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                badgeContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        badgeContainer.addView(QBadgeView.this, new FrameLayout.LayoutParams(badgeContainer.getWidth(),
                                badgeContainer.getHeight()));
                    }
                });
            }
        } else {
            throw new IllegalStateException("targetView must have a parent");
        }
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mActivityRoot == null) findActivityRoot(mTargetView);
    }

    private void findActivityRoot(View view) {
        if (view.getParent() != null && view.getParent() instanceof View) {
            findActivityRoot((View) view.getParent());
        } else if (view instanceof ViewGroup) {
            mActivityRoot = (ViewGroup) view;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mDraggable && event.getPointerId(event.getActionIndex()) == 0
                        && getPointDistance(mBadgeCenter, new PointF(event.getX(), event.getY()))
                        <= DisplayUtil.dp2px(getContext(), 10) && mBadgeNumber != 0) {
                    initRowBadgeCenter();
                    mDragging = true;
                    updataListener(OnDragStateChangedListener.STATE_START);
                    mDefalutRadius = DisplayUtil.dp2px(getContext(), 7);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    screenFromWindow(true);
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    mDragCenter.x = event.getRawX();
                    mDragCenter.y = event.getRawY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (event.getPointerId(event.getActionIndex()) == 0 && mDragging) {
                    mDragging = false;
                    onPointerUp();
                }
                break;
        }
        return mDragging || super.onTouchEvent(event);
    }

    private void onPointerUp() {
        if (mDragOutOfRange) {
            animateHide(mDragCenter);
            updataListener(OnDragStateChangedListener.STATE_SUCCEED);
        } else {
            reset();
            updataListener(OnDragStateChangedListener.STATE_CANCELED);
        }
    }

    protected Bitmap createBadgeBitmap() {
        Rect rect = new Rect();
        mBadgeNumberPaint.getTextBounds(mBadgeText.toCharArray(), 0, mBadgeText.length(), rect);
        Bitmap bitmap = Bitmap.createBitmap((int) (rect.width() + mBadgePadding * 2),
                (int) (rect.width() + mBadgePadding * 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, canvas.getWidth() / 2f, mBadgeBackgroundPaint);
        canvas.drawText(mBadgeText, canvas.getWidth() / 2f, canvas.getHeight() / 2f + rect.height() / 2f, mBadgeNumberPaint);
        return bitmap;
    }

    protected void screenFromWindow(boolean screen) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (screen) {
            mActivityRoot.addView(this, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            bindTarget(mTargetView);
        }
    }

    private void showShadowImp(boolean showShadow) {
        int x = DisplayUtil.dp2px(getContext(), 1);
        int y = DisplayUtil.dp2px(getContext(), 1.5f);
        switch (mDragQuadrant) {
            case 1:
                x = DisplayUtil.dp2px(getContext(), 1);
                y = DisplayUtil.dp2px(getContext(), -1.5f);
                break;
            case 2:
                x = DisplayUtil.dp2px(getContext(), -1);
                y = DisplayUtil.dp2px(getContext(), -1.5f);
                break;
            case 3:
                x = DisplayUtil.dp2px(getContext(), -1);
                y = DisplayUtil.dp2px(getContext(), 1.5f);
                break;
            case 4:
                x = DisplayUtil.dp2px(getContext(), 1);
                y = DisplayUtil.dp2px(getContext(), 1.5f);
                break;
        }
        mBadgeBackgroundPaint.setShadowLayer(showShadow ? DisplayUtil.dp2px(getContext(), 2f)
                : 0, x, y, 0x33000000);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.draw(canvas);
            return;
        }
        if (mBadgeNumber != 0) {
            showShadowImp(mShowShadow);
            float badgeRadius = getBadgeCircleRadius();
            float startCircleRadius = mDefalutRadius * (1 - getPointDistance(mRowBadgeCenter, mDragCenter) / mFinalDragDistance);
            if (mDraggable && mDragging) {
                mDragQuadrant = getQuadrant(mDragCenter, mRowBadgeCenter);
                showShadowImp(mShowShadow);
                if (mDragOutOfRange = startCircleRadius < DisplayUtil.dp2px(getContext(), 1.5f)) {
                    updataListener(OnDragStateChangedListener.STATE_DRAGGING_OUT_OF_RANGE);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                } else {
                    updataListener(OnDragStateChangedListener.STATE_DRAGGING);
                    drawDragging(canvas, startCircleRadius, badgeRadius);
                    drawBadge(canvas, mDragCenter, badgeRadius);
                }
            } else {
                findBadgeCenter();
                drawBadge(canvas, mBadgeCenter, getBadgeCircleRadius());
            }
        }
    }

    private void drawDragging(Canvas canvas, float startRadius, float badgeRadius) {
        float dy = mDragCenter.y - mRowBadgeCenter.y;
        float dx = mDragCenter.x - mRowBadgeCenter.x;
        mInnertangentPoints.clear();
        if (dx != 0) {
            double k1 = dy / dx;
            double k2 = -1 / k1;
            getInnertangentPoints(mDragCenter, badgeRadius, k2);
            getInnertangentPoints(mRowBadgeCenter, startRadius, k2);
        } else {
            getInnertangentPoints(mDragCenter, badgeRadius, 0d);
            getInnertangentPoints(mRowBadgeCenter, startRadius, 0d);
        }
        mDragPath.reset();
        mDragPath.addCircle(mRowBadgeCenter.x, mRowBadgeCenter.y, startRadius,
                mDragQuadrant == 1 || mDragQuadrant == 2 ? Path.Direction.CCW : Path.Direction.CW);
        mControlPoint.x = (mRowBadgeCenter.x + mDragCenter.x) / 2.0f;
        mControlPoint.y = (mRowBadgeCenter.y + mDragCenter.y) / 2.0f;
        mDragPath.moveTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(0).x, mInnertangentPoints.get(0).y);
        mDragPath.lineTo(mInnertangentPoints.get(1).x, mInnertangentPoints.get(1).y);
        mDragPath.quadTo(mControlPoint.x, mControlPoint.y, mInnertangentPoints.get(3).x, mInnertangentPoints.get(3).y);
        mDragPath.lineTo(mInnertangentPoints.get(2).x, mInnertangentPoints.get(2).y);
        mDragPath.close();
        mBadgeBackgroundPaint.setColor(mColorBackground);
        canvas.drawPath(mDragPath, mBadgeBackgroundPaint);
    }

    private void drawBadge(Canvas canvas, PointF center, float radius) {
        if (center.x == -1000 && center.y == -1000) {
            return;
        }
        mBadgeBackgroundPaint.setColor(mColorBackground);
        mBadgeNumberPaint.setColor(mColorBadgeNumber);
        mBadgeNumberPaint.setTextAlign(Paint.Align.CENTER);
        if (mBadgeNumber < 0) {
            canvas.drawCircle(center.x, center.y, mBadgePadding, mBadgeBackgroundPaint);
        } else if (mBadgeNumber <= 9) {//draw circle badge
            canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
            canvas.drawText(mBadgeText, center.x, center.y + mBadgeNumberRect.height() / 2f, mBadgeNumberPaint);
        } else {//>9draw rect badge
            float padding = mBadgeNumber <= 99 ? 1.2f : 1.0f;
            mBadgeBackgroundRect.left = center.x - (mBadgeNumberRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.top = center.y - (mBadgeNumberRect.height() / 2f + mBadgePadding / padding);
            mBadgeBackgroundRect.right = center.x + (mBadgeNumberRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.bottom = center.y + (mBadgeNumberRect.height() / 2f + mBadgePadding / padding);
            canvas.drawRoundRect(mBadgeBackgroundRect,
                    DisplayUtil.dp2px(getContext(), 10), DisplayUtil.dp2px(getContext(), 10),
                    mBadgeBackgroundPaint);
            canvas.drawText(mBadgeText, center.x, center.y + mBadgeNumberRect.height() / 2f, mBadgeNumberPaint);
        }
    }

    private float getBadgeCircleRadius() {
        float radius = mBadgeBackgroundRect.height() / 2f;
        if (mBadgeNumber < 0) {
            radius = mBadgePadding;
        } else if (mBadgeNumber <= 9) {
            radius = mBadgeNumberRect.height() > mBadgeNumberRect.width() ?
                    mBadgeNumberRect.height() / 2f + mBadgePadding : mBadgeNumberRect.width() / 2f + mBadgePadding;
        }
        return radius;
    }

    private void findBadgeCenter() {
        mBadgeNumberPaint.setTextSize(mBadgeNumberSize);
        char[] chars = mBadgeText.toCharArray();
        mBadgeNumberPaint.getTextBounds(chars, 0, chars.length, mBadgeNumberRect);
        int rectWidth = mBadgeNumberRect.height() > mBadgeNumberRect.width() ?
                mBadgeNumberRect.height() : mBadgeNumberRect.width();
        switch (mBadgeGravity) {
            case Gravity.START | Gravity.TOP:
                mBadgeCenter.x = mGravityOffset + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f;
                break;
            case Gravity.END | Gravity.TOP:
                mBadgeCenter.x = mWidth - (mGravityOffset + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f;
                break;
            case Gravity.START | Gravity.BOTTOM:
                mBadgeCenter.x = mGravityOffset + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f);
                break;
            case Gravity.END | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth - (mGravityOffset + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight - (mGravityOffset + mBadgePadding + mBadgeNumberRect.height() / 2f);
                break;
            case Gravity.CENTER:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
        }
        initRowBadgeCenter();
    }

    private void initRowBadgeCenter() {
        int[] screenPoint = new int[2];
        getLocationOnScreen(screenPoint);
        mRowBadgeCenter.x = mBadgeCenter.x + screenPoint[0];
        mRowBadgeCenter.y = mBadgeCenter.y + screenPoint[1];
    }

    protected void animateHide(PointF center) {
        if (mBadgeNumber == 0) {
            return;
        }
        if (mAnimator == null || !mAnimator.isRunning()) {
            screenFromWindow(true);
            mAnimator = BadgeAnimator.start(createBadgeBitmap(), center, this);
            setBadgeNumber(0);
        }
    }

    public void reset() {
        mDragCenter.x = -1000;
        mDragCenter.y = -1000;
        mDragQuadrant = 4;
        screenFromWindow(false);
        getParent().requestDisallowInterceptTouchEvent(false);
        invalidate();
    }

    @Override
    public void hide(boolean animate) {
        if (animate) {
            animateHide(mRowBadgeCenter);
        } else {
            setBadgeNumber(0);
        }
    }

    /**
     * @param badgeNumber equal to zero badge will be hidden, less than zero show dot
     */
    @Override
    public Badge setBadgeNumber(int badgeNumber) {
        mBadgeNumber = badgeNumber;
        if (mBadgeNumber < 0) {
            mBadgeText = "";
        } else if (mBadgeNumber > 99) {
            mBadgeText = mExact ? String.valueOf(mBadgeNumber) : "99+";
        } else if (mBadgeNumber > 0 && mBadgeNumber <= 99) {
            mBadgeText = String.valueOf(mBadgeNumber);
        }
        invalidate();
        return this;
    }

    @Override
    public int getBadgeNumber() {
        return mBadgeNumber;
    }

    @Override
    public Badge setExactMode(boolean isExact) {
        mExact = isExact;
        setBadgeNumber(mBadgeNumber);
        return this;
    }

    @Override
    public boolean isExactMode() {
        return mExact;
    }

    @Override
    public Badge setShowShadow(boolean showShadow) {
        mShowShadow = showShadow;
        invalidate();
        return this;
    }

    @Override
    public boolean isShowShadow() {
        return mShowShadow;
    }

    @Override
    public Badge setBadgeBackgroundColor(int color) {
        mColorBackground = color;
        invalidate();
        return this;
    }

    @Override
    public int getBadgeBackgroundColor() {
        return mColorBackground;
    }

    @Override
    public Badge setBadgeNumberColor(int color) {
        mColorBadgeNumber = color;
        invalidate();
        return this;
    }

    @Override
    public int getBadgeNumberColor() {
        return mColorBadgeNumber;
    }

    @Override
    public Badge setBadgeNumberSize(float size, boolean isSpValue) {
        mBadgeNumberSize = isSpValue ? DisplayUtil.dp2px(getContext(), size) : size;
        invalidate();
        return this;
    }

    @Override
    public float getBadgeNumberSize(boolean isSpValue) {
        return isSpValue ? DisplayUtil.px2dp(getContext(), mBadgeNumberSize) : mBadgeNumberSize;
    }

    @Override
    public Badge setBadgePadding(float padding, boolean isDpValue) {
        mBadgePadding = isDpValue ? DisplayUtil.dp2px(getContext(), padding) : padding;
        invalidate();
        return this;
    }

    @Override
    public float getBadgePadding(boolean isDpValue) {
        return isDpValue ? DisplayUtil.px2dp(getContext(), mBadgePadding) : mBadgePadding;
    }

    @Override
    public boolean isDraggable() {
        return mDraggable;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER
     */
    @Override
    public Badge setBadgeGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER)) {
            mBadgeGravity = gravity;
            invalidate();
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER");
        }
        return this;
    }

    @Override
    public int getBadgeGravity() {
        return mBadgeGravity;
    }

    @Override
    public Badge setGravityOffset(int offset, boolean isDpValue) {
        mGravityOffset = isDpValue ? DisplayUtil.dp2px(getContext(), offset) : offset;
        invalidate();
        return this;
    }

    @Override
    public int getGravityOffset(boolean isDpValue) {
        return isDpValue ? DisplayUtil.px2dp(getContext(), mGravityOffset) : mGravityOffset;
    }

    private float getPointDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static int getQuadrant(PointF p, PointF center) {
        if (p.x > center.x) {
            if (p.y > center.y) {
                return 4;
            } else if (p.y < center.y) {
                return 1;
            }
        } else if (p.x < center.x) {
            if (p.y > center.y) {
                return 3;
            } else if (p.y < center.y) {
                return 2;
            }
        }
        return -1;
    }

    /**
     * this formula is designed by mabeijianxi
     * website : http://blog.csdn.net/mabeijianxi/article/details/50560361
     *
     * @param circleCenter The circle center point.
     * @param radius       The circle radius.
     * @param slopeLine    The slope of line which cross the pMiddle.
     */
    private void getInnertangentPoints(PointF circleCenter, float radius, Double slopeLine) {
        float radian, xOffset, yOffset;
        if (slopeLine != null) {
            radian = (float) Math.atan(slopeLine);
            xOffset = (float) (Math.cos(radian) * radius);
            yOffset = (float) (Math.sin(radian) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        mInnertangentPoints.add(new PointF(circleCenter.x + xOffset, circleCenter.y + yOffset));
        mInnertangentPoints.add(new PointF(circleCenter.x - xOffset, circleCenter.y - yOffset));
    }

    private void updataListener(int state) {
        if (mDragStateChangedListener != null)
            mDragStateChangedListener.onDragStateChanged(state, this, mTargetView);
    }

    @Override
    public Badge setOnDragStateChangedListener(OnDragStateChangedListener l) {
        mDraggable = l != null;
        mDragStateChangedListener = l;
        return this;
    }

    @Override
    public PointF getDragCenter() {
        if (mDraggable && mDragging) return mDragCenter;
        return null;
    }
}
