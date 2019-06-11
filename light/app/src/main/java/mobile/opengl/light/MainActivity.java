package mobile.opengl.light;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // openGL view
        mGLView = findViewById(R.id.glview);
        // request an OpenGL ES 2.0 compatible context
        mGLView.setEGLContextClientVersion(2);
        GLRenderer glRenderer = new GLRenderer(this);
        mGLView.setRenderer(glRenderer);

        // simple UI
        mGLView.setOnTouchListener(new GLTouchListener(this, mGLView, glRenderer));
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
}
