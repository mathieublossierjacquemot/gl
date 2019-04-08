package mobile.opengl.basics.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

    private Context context;

    public Util(Context context) {
        this.context = context;
    }

    public void loadShader(String name, int type, int program) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(name);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            GLES20.glAttachShader(program, loadShader(type, stringBuilder.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    static public void setMatrix(float[] matrix,
                                  float a11, float a12, float a13, float a14,
                                  float a21, float a22, float a23, float a24,
                                  float a31, float a32, float a33, float a34,
                                  float a41, float a42, float a43, float a44) {

        matrix[0] = a11;
        matrix[1] = a21;
        matrix[2] = a31;
        matrix[3] = a41;

        matrix[4] = a12;
        matrix[5] = a22;
        matrix[6] = a32;
        matrix[7] = a42;

        matrix[8] = a13;
        matrix[9] = a23;
        matrix[10] = a33;
        matrix[11] = a43;

        matrix[12] = a14;
        matrix[13] = a24;
        matrix[14] = a34;
        matrix[15] = a44;
    }

}
