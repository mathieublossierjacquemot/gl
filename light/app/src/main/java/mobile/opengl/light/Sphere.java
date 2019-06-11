package mobile.opengl.light;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sphere implements Geometry {

    private static final int LONGITUDES = 9;
    private static final int LATITUDES = 2 * LONGITUDES;

    private float[] vertices;
    private short[] indices;

    public Sphere() {
        // vertices to render: values are x, y, z,  r, g, b, a,
        vertices = new float[getStride() * LATITUDES * LONGITUDES];

        double longitudeFactor = (Math.PI) / (LONGITUDES - 1);
        double longitudeStart = - Math.PI / 2;
        double latitudeFactor = 2 * Math.PI / (LATITUDES - 1);

        for (int j = 0; j < LONGITUDES; j++) {
            float c2 = (float) Math.cos(longitudeStart + j * longitudeFactor);
            float s2 = (float) Math.sin(longitudeStart + j * longitudeFactor);
            for (int i = 0; i < LATITUDES; i++) {
                float c1 = (float) Math.cos(i * latitudeFactor);
                float s1 = (float) Math.sin(i * latitudeFactor);

                int vertexIndex = (j * LATITUDES + i) * getStride();

                int positionIndex = vertexIndex;
                vertices[positionIndex + X] = c1 * c2;
                vertices[positionIndex + Y] = s1 * c2;
                vertices[positionIndex + Z] = s2;

                int colorIndex = vertexIndex + getColorOffset();
                vertices[colorIndex + R] = 1f;//(float) i / LATITUDES;
                vertices[colorIndex + G] = 0f;
                vertices[colorIndex + B] = 0f;//(float) j / LONGITUDES;
                vertices[colorIndex + A] = 1f;

                int normalIndex = vertexIndex + getNormalOffset();
                vertices[normalIndex + X] = c1 * c2;
                vertices[normalIndex + Y] = s1 * c2;
                vertices[normalIndex + Z] = s2;
            }
        }

        // triangles to render: vertices A, B, C
        indices = new short[(LATITUDES - 1) * (LONGITUDES - 1) * 6];

        int index = 0;
        for (int j = 0; j < LONGITUDES - 1; j++) {
            for (int i = 0; i < LATITUDES - 1; i++) {
                int vertexIndex = j * LATITUDES + i;
                indices[index] = (short) vertexIndex;
                index++;
                indices[index] = (short) (vertexIndex + 1);
                index++;
                indices[index] = (short) (vertexIndex + LATITUDES);
                index++;

                indices[index] = (short) (vertexIndex + LATITUDES + 1);
                index++;
                indices[index] = (short) (vertexIndex + 1);
                index++;
                indices[index] = (short) (vertexIndex + LATITUDES);
                index++;
            }
        }
    }

    @Override
    public int getVerticesLength() {
        return vertices.length;
    }

    @Override
    public FloatBuffer getVertices() {
       return FloatBuffer.wrap(vertices);
    }

    @Override
    public int getIndicesLength() {
        return indices.length;
    }

    @Override
    public ShortBuffer getIndices() {
        return ShortBuffer.wrap(indices);
    }

    @Override
    public int getStride() {
        return FLOATS_PER_POSITION + FLOATS_PER_COLOR + FLOATS_PER_NORMAL;
    }

    @Override
    public int getFloatsPerPosition() {
        return FLOATS_PER_POSITION;
    }

    @Override
    public int getFloatsPerColor() {
        return FLOATS_PER_COLOR;
    }

    @Override
    public int getColorOffset() {
        return FLOATS_PER_POSITION;
    }

    @Override
    public int getFloatsPerNormal() {
        return FLOATS_PER_NORMAL;
    }

    @Override
    public int getNormalOffset() {
        return FLOATS_PER_POSITION + FLOATS_PER_COLOR;
    }

}
