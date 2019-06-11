package mobile.opengl.textures;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class GLTouchListener implements View.OnTouchListener {

    private GLSurfaceView glSurfaceView;
    private GLRenderer glRenderer;

    private DisplayMetrics displayMetrics;

    private float x0;
    private float y0;

    private float[] rotYMatrix;
    private float[] rotXMatrix;

    volatile private float angleY;
    private float angleYOld;
    volatile private float angleX;
    private float angleXOld;

    private static final float ANGLE_FACTOR = 2 * ((float) Math.PI) / 180f;

    public GLTouchListener(Context context, GLSurfaceView glSurfaceView, GLRenderer glRenderer) {
        this.glSurfaceView = glSurfaceView;
        this.glRenderer = glRenderer;
        displayMetrics = context.getResources().getDisplayMetrics();

        rotYMatrix = new float[16];
        rotXMatrix = new float[16];
        angleY = 0f;
        angleX = 0f;
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
                        onActionDown();
                    }
                });
                break;
            case MotionEvent.ACTION_MOVE:
                final float dx = (event.getX() - x0) / displayMetrics.density;
                final float dy = (event.getY() - y0) / displayMetrics.density;
                glSurfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        onActionMove(dx, dy);
                    }
                });
                break;
        }
        return true;
    }


    private void onActionDown() {
        angleYOld = angleY;
        angleXOld = angleX;
    }

    private void onActionMove(float dx, float dy) {
        angleY = angleYOld + dx * ANGLE_FACTOR;
        angleX = angleXOld + dy * ANGLE_FACTOR;

        // rotation around y axis
        float c = (float) Math.cos(angleY);
        float s = (float) Math.sin(angleY);
        Util.setMatrix(rotYMatrix,
                c, 0.0f, s, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                -s, 0.0f, c, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        );

        // rotation around x axis
        c = (float) Math.cos(angleX);
        s = (float) Math.sin(angleX);
        Util.setMatrix(rotXMatrix,
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, c, -s, 0.0f,
                0.0f, s, c, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        );

        // update view
        Matrix.multiplyMM(glRenderer.getViewMatrix(), 0, rotXMatrix, 0, rotYMatrix, 0);
        glRenderer.updateVPMatrix();
    }
}
