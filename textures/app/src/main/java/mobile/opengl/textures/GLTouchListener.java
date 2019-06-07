package mobile.opengl.textures;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class GLTouchListener implements View.OnTouchListener {

    private GLSurfaceView glSurfaceView;
    private GLRenderer glRenderer;

    private DisplayMetrics displayMetrics;

    private float x0;
    private float y0;

    public GLTouchListener(Context context, GLSurfaceView glSurfaceView, GLRenderer glRenderer) {
        this.glSurfaceView = glSurfaceView;
        this.glRenderer = glRenderer;
        displayMetrics = context.getResources().getDisplayMetrics();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x0 = event.getX();
                y0 = event.getY();
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        glRenderer.onActionDown();
                    }
                });
                break;
            case MotionEvent.ACTION_MOVE:
                final float dx = (event.getX() - x0) / displayMetrics.density;
                final float dy = (event.getY() - y0) / displayMetrics.density;
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        glRenderer.onActionMove(dx, dy);
                    }
                });
                break;
        }
        return true;
    }
}
