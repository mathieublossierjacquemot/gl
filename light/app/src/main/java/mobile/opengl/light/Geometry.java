package mobile.opengl.light;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

interface Geometry {

    int FLOATS_PER_POSITION = 3;
    int FLOATS_PER_COLOR = 4;
    int FLOATS_PER_NORMAL = 3;

    int X = 0;
    int Y = 1;
    int Z = 2;

    int R = 0;
    int G = 1;
    int B = 2;
    int A = 3;

    int getVerticesLength();

    FloatBuffer getVertices();

    int getIndicesLength();

    ShortBuffer getIndices();

    int getStride();

    int getFloatsPerPosition();

    int getFloatsPerColor();

    int getColorOffset();

    int getFloatsPerNormal();

    int getNormalOffset();
}
