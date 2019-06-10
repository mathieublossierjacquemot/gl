package mobile.opengl.basics.activity;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;

import mobile.opengl.basics.R;
import mobile.opengl.basics.util.Util;

public class GLActivity extends AppCompatActivity {

    static final public String INTENT_SAMPLE_NAME = "INTENT_SAMPLE_NAME";

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gl);

        // renderer
        String sampleName = getIntent().getExtras().getString(INTENT_SAMPLE_NAME);
        GLSurfaceView.Renderer renderer = getRenderer(sampleName);

        // openGL view
        mGLView = new GLSurfaceView(this);
        // request an OpenGL ES 2.0 compatible context
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(renderer);

        // layout
        FrameLayout view = findViewById(R.id.view);
        mGLView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        view.addView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    private GLSurfaceView.Renderer getRenderer(String sampleName) {
        GLSurfaceView.Renderer renderer = null;
        try {
            Class<?> clazz = Class.forName("mobile.opengl.basics.renderer.GLRenderer"+sampleName);
            Constructor<?> constructor = clazz.getConstructor(Context.class);
            renderer = (GLSurfaceView.Renderer) constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return renderer;
    }
}
