precision mediump float;

uniform mat4 uMatrix;

attribute vec3 vPosition;
attribute vec4 vColor;

varying vec4 fColor;

void main() {
    vec4 position = vec4(vPosition.x, vPosition.y, vPosition.z, 1.0);
    gl_Position = uMatrix * position;
    fColor = vColor;
}