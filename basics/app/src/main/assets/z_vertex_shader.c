precision mediump float;

attribute vec3 vPosition;
attribute vec4 vColor;

varying vec4 fColor;

void main() {
    gl_Position = vec4(vPosition.x, vPosition.y, vPosition.z, 1.0);
    fColor = vColor;
}