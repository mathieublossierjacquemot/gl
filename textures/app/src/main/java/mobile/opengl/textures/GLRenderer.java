package mobile.opengl.textures;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{
    private Util util;

    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_SHORT = 2;

    private static final int ATTRIBUTE_POSITION = 0;
    private static final int ATTRIBUTE_COLOR = 1;
    private static final int FLOATS_PER_POSITION = 3;
    private static final int FLOATS_PER_COLOR = 4;

    private int shaderProgram;
    private int vertexID;
    private int indexID;

    private int uniformLocationMatrix;
    private float[] projectionMatrix;
    private float[] rotYMatrix;
    private float[] rotXMatrix;
    private float[] viewMatrix;
    private float[] vpMatrix;

    private float angleY;
    private float angleYOld;
    private float angleX;
    private float angleXOld;

    private static final float ANGLE_FACTOR = 2 * ((float) Math.PI) / 180f;

    public GLRenderer(Util util) {
        this.util = util;
        projectionMatrix = new float[16];
        rotYMatrix = new float[16];
        rotXMatrix = new float[16];
        viewMatrix = new float[16];
        vpMatrix = new float[16];
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // clear color (frame background color)
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        // Shader program: vertex shader, fragment shader
        shaderProgram = GLES20.glCreateProgram();
        util.loadShader("vertex_shader.c", GLES20.GL_VERTEX_SHADER, shaderProgram);
        util.loadShader("fragment_shader.c", GLES20.GL_FRAGMENT_SHADER, shaderProgram);

        // inputs location
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_POSITION, "vPosition");
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_COLOR, "vColor");

        // link program
        GLES20.glLinkProgram(shaderProgram);

        // get uniforms locations
        uniformLocationMatrix = GLES20.glGetUniformLocation(shaderProgram, "uMatrix");

        // vertices to render: values are x, y, z,  r, g, b, a
        float z = 0.1f;
        float[] vertices = {
                0.2f, -0.8f, -z,  1.0f, 0.0f, 0.0f, 1.0f,
                0.6f, -0.8f, -z,  1.0f, 0.0f, 0.0f, 1.0f,
                0.6f,  0.8f,  z,  1.0f, 0.0f, 0.0f, 1.0f,
                0.2f,  0.8f,  z,  1.0f, 0.0f, 0.0f, 1.0f,

                -0.8f, -0.6f, -z,  0.0f, 1.0f, 0.0f, 1.0f,
                0.8f, -0.6f,  z,  0.0f, 1.0f, 0.0f, 1.0f,
                0.8f, -0.2f,  z,  0.0f, 1.0f, 0.0f, 1.0f,
                -0.8f, -0.2f, -z,  0.0f, 1.0f, 0.0f, 1.0f,

                -0.6f, -0.8f,  z,  0.0f, 0.0f, 1.0f, 1.0f,
                -0.2f, -0.8f,  z,  0.0f, 0.0f, 1.0f, 1.0f,
                -0.2f,  0.8f, -z,  0.0f, 0.0f, 1.0f, 1.0f,
                -0.6f,  0.8f, -z,  0.0f, 0.0f, 1.0f, 1.0f,

                -0.8f,  0.2f,  z,  0.5f, 0.5f, 0.5f, 1.0f,
                0.8f,  0.2f, -z,  0.5f, 0.5f, 0.5f, 1.0f,
                0.8f,  0.6f, -z,  0.5f, 0.5f, 0.5f, 1.0f,
                -0.8f,  0.6f,  z,  0.5f, 0.5f, 0.5f, 1.0f
        };
        FloatBuffer verticesBuffer = FloatBuffer.wrap(vertices);

        // triangles to render: vertices A, B, C
        short[] indices = {
                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7,

                8, 9, 10,
                8, 10, 11,

                12, 13, 14,
                12, 14, 15
        };
        ShortBuffer indicesBuffer = ShortBuffer.wrap(indices);

        // create buffers in openGL
        int[] gen = new int[2];
        GLES20.glGenBuffers(2, gen, 0);
        vertexID = gen[0];
        indexID = gen[1];

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


        // openGL selects indices buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexID);
        // send data to openGL (to indices buffer)
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                indices.length * BYTES_PER_SHORT,
                indicesBuffer,
                GLES20.GL_STATIC_DRAW);

        // view matrix
        angleY = 0f;
        angleX = 0f;
        updateViewMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Util.setMatrix(projectionMatrix,
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, (float) width / height, 0.0f, 0.0f,
                0.0f, 0.0f, -1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        );
        updateVPMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // set shader program to use
        GLES20.glUseProgram(shaderProgram);
        // clear the frame color buffer and depth buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // enable depth test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // select vertex buffer and enable attributes
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexID);
        GLES20.glEnableVertexAttribArray(ATTRIBUTE_POSITION);
        GLES20.glEnableVertexAttribArray(ATTRIBUTE_COLOR);

        // select indices buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexID);

        // set openGL matrix
        GLES20.glUniformMatrix4fv(uniformLocationMatrix, 1, false, vpMatrix, 0);

        // draw triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 24,
                GLES20.GL_UNSIGNED_SHORT, 0);

        // disable attributes
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_POSITION);
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_COLOR);
    }

    private void updateViewMatrix() {
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

        // update view matrix
        Matrix.multiplyMM(viewMatrix, 0, rotXMatrix, 0, rotYMatrix, 0);

    }

    private void updateVPMatrix() {

        // update VP matrix
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }


    public void onActionDown() {
        angleYOld = angleY;
        angleXOld = angleX;
    }

    public void onActionMove(float dx, float dy) {
        angleY = angleYOld + dx * ANGLE_FACTOR;
        angleX = angleXOld + dy * ANGLE_FACTOR;
        updateViewMatrix();
        updateVPMatrix();
    }

}
