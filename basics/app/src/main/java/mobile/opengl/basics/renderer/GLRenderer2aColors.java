package mobile.opengl.basics.renderer;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import mobile.opengl.basics.util.Util;

public class GLRenderer2aColors implements GLSurfaceView.Renderer{

    private Util util;

    private static final int BYTES_PER_FLOAT = 4;

    private static final int ATTRIBUTE_POSITION = 0;
    private static final int ATTRIBUTE_COLOR = 1;
    private static final int FLOATS_PER_POSITION = 2;
    private static final int FLOATS_PER_COLOR = 4;

    private int shaderProgram;
    private int vertexID;

    public GLRenderer2aColors(Util util) {
        this.util = util;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // clear color (frame background color)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Shader program: vertex shader, fragment shader
        shaderProgram = GLES20.glCreateProgram();
        util.loadShader("colors_vertex_shader.c", GLES20.GL_VERTEX_SHADER, shaderProgram);
        util.loadShader("colors_fragment_shader.c", GLES20.GL_FRAGMENT_SHADER, shaderProgram);

        // inputs location
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_POSITION, "vPosition");
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_COLOR, "vColor");

        // link program
        GLES20.glLinkProgram(shaderProgram);

        // vertices to render: values are x, y,  r, g, b, a
        float[] vertices = {
                -0.5f, -0.5f,  1.0f, 0.0f, 0.0f, 1.0f,
                 0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,
                 0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f
        };
        FloatBuffer verticesBuffer = FloatBuffer.wrap(vertices);

        // create buffers in openGL
        int[] gen = new int[1];
        GLES20.glGenBuffers(1, gen, 0);
        vertexID = gen[0];

        // openGL selects vertex buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexID);
        // send data to openGL (to vertex buffer)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                vertices.length * BYTES_PER_FLOAT,
                verticesBuffer, GLES20.GL_STATIC_DRAW);
        // set attribute to find the data at the right place
        GLES20.glVertexAttribPointer(
                ATTRIBUTE_POSITION,
                FLOATS_PER_POSITION,
                GLES20.GL_FLOAT,
                false,
                (FLOATS_PER_POSITION + FLOATS_PER_COLOR) * BYTES_PER_FLOAT,
                0);
        GLES20.glVertexAttribPointer(
                ATTRIBUTE_COLOR,
                FLOATS_PER_COLOR,
                GLES20.GL_FLOAT,
                false,
                (FLOATS_PER_POSITION + FLOATS_PER_COLOR) * BYTES_PER_FLOAT,
                FLOATS_PER_POSITION * BYTES_PER_FLOAT);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // set shader program to use
        GLES20.glUseProgram(shaderProgram);
        // clear the frame color buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // select vertex buffer and enable attributes
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexID);
        GLES20.glEnableVertexAttribArray(ATTRIBUTE_POSITION);
        GLES20.glEnableVertexAttribArray(ATTRIBUTE_COLOR);
        
        // draw triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        // disable attributes
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_POSITION);
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_COLOR);
    }

}
