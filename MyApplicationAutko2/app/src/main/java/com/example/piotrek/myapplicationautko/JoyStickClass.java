package com.example.piotrek.myapplicationautko2;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;s
        import android.graphics.Paint;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.ViewGroup.LayoutParams;

public class JoyStickClass {
    public static final int STICK_NONE = 0;
    public static final int STICK_UP = 1;
    public static final int STICK_UPRIGHT = 2;
    public static final int STICK_RIGHT = 3;
    public static final int STICK_DOWNRIGHT = 4;
    public static final int STICK_DOWN = 5;
    public static final int STICK_DOWNLEFT = 6;
    public static final int STICK_LEFT = 7;
    public static final int STICK_UPLEFT = 8;

    private int STICK_ALPHA = 200;
    private int LAYOUT_ALPHA = 200;

    private Context mContext;
    private ViewGroup mLayout;
    private LayoutParams params;
    private int stick_width, stick_height;

    private int position_x = 0, position_y = 0, min_distance = 0;
    private float distance = 0, angle = 0;


    private boolean touch_state = false;

    public JoyStickClass() {
    }

    public void drawStick() {
    }

    public int[] getPosition() {
        return new int[]{0, 0};
    }

    public int getX() {
        return 0;
    }

    public int getY() {
        return 0;
    }

    public float getAngle() {
        return 0;
    }

    public float getDistance() {
        return 0;
    }

    public int get8Direction() {
        return 0;
    }

    public int get4Direction() {
        return 0;
    }

    public void setStickWidth(int width) {
    }

    public void setStickHeight(int height) {
    }

    public int getStickWidth() {

        return stick_width;
    }

    public int getStickHeight() {

        return stick_height;
    }

    public void setLayoutSize(int width, int height) {
    }

    public int getLayoutWidth() {

        return params.width;
    }

    public int getLayoutHeight() {

        return params.height;
    }

    private double cal_angle(float x, float y) {
     return 0;
    }

    private void draw() {
    }

    private class DrawCanvas extends View {
    }
}
