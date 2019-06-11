package mobile.opengl.light;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer{
    final private Context context;

    private static final int ATTRIBUTE_POSITION = 0;
    private static final int ATTRIBUTE_COLOR = 1;
    private static final int ATTRIBUTE_NORMAL = 2;

    private int shaderProgram;
    private int vertexID;
    private int indexID;

    private int indicesLength;

    private int uniformLocationMatrix;
    private float[] projectionMatrix;
    private float[] viewMatrix;
    private float[] vpMatrix;

    private int uniformLocationLightPosition;
    private float[] lightPosition;


    public GLRenderer(Context context) {
        this.context = context;
        projectionMatrix = new float[16];
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
        Util.loadShader(context,"vertex_shader.c", GLES20.GL_VERTEX_SHADER, shaderProgram);
        Util.loadShader(context,"fragment_shader.c", GLES20.GL_FRAGMENT_SHADER, shaderProgram);

        // inputs location
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_POSITION, "vPosition");
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_COLOR, "vColor");
        GLES20.glBindAttribLocation(shaderProgram, ATTRIBUTE_NORMAL, "vNormal");

        // link program
        GLES20.glLinkProgram(shaderProgram);

        // get uniforms locations
        uniformLocationMatrix = GLES20.glGetUniformLocation(shaderProgram, "uMatrix");
        uniformLocationLightPosition = GLES20.glGetUniformLocation(shaderProgram, "uLightPosition");

        Geometry geometry = new Sphere();

        // create buffers in openGL
        int[] gen = new int[2];
        GLES20.glGenBuffers(2, gen, 0);
        vertexID = gen[0];
        indexID = gen[1];

        // openGL selects vertex buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexID);
        // send data to openGL (to vertex buffer)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                geometry.getVerticesLength() * Util.BYTES_PER_FLOAT,
                geometry.getVertices(), GLES20.GL_STATIC_DRAW);
        // set attribute to find the data at the right place
        GLES20.glVertexAttribPointer(
                ATTRIBUTE_POSITION,
                geometry.getFloatsPerPosition(),
                GLES20.GL_FLOAT,
                false,
                geometry.getStride() * Util.BYTES_PER_FLOAT,
                0);
        GLES20.glVertexAttribPointer(
                ATTRIBUTE_COLOR,
                geometry.getFloatsPerColor(),
                GLES20.GL_FLOAT,
                false,
                geometry.getStride() * Util.BYTES_PER_FLOAT,
                geometry.getColorOffset() * Util.BYTES_PER_FLOAT);
        GLES20.glVertexAttribPointer(
                ATTRIBUTE_NORMAL,
                geometry.getFloatsPerNormal(),
                GLES20.GL_FLOAT,
                false,
                geometry.getStride() * Util.BYTES_PER_FLOAT,
                geometry.getNormalOffset() * Util.BYTES_PER_FLOAT);

        // openGL selects indices buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexID);
        // send data to openGL (to indices buffer)
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
                geometry.getIndicesLength() * Util.BYTES_PER_SHORT,
                geometry.getIndices(),
                GLES20.GL_STATIC_DRAW);

        indicesLength = geometry.getIndicesLength();

        // view matrix
        Util.setMatrix(viewMatrix,
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        );

        // light
        float l = (float) Math.sqrt(3);
        lightPosition = new float[] {
                -1f/l, -1f/l, -1f/l
        };
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
        GLES20.glEnableVertexAttribArray(ATTRIBUTE_NORMAL);

        // select indices buffer
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexID);

        // set openGL matrix
        GLES20.glUniformMatrix4fv(uniformLocationMatrix, 1, false, vpMatrix, 0);

        // set light
        GLES20.glUniform3fv(uniformLocationLightPosition, 1, lightPosition, 0);

        // draw triangles
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indicesLength,
                GLES20.GL_UNSIGNED_SHORT, 0);

        // disable attributes
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_POSITION);
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_COLOR);
        GLES20.glDisableVertexAttribArray(ATTRIBUTE_NORMAL);
    }


    public float[] getViewMatrix() {
        return viewMatrix;
    }

    public void updateVPMatrix() {
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

}
