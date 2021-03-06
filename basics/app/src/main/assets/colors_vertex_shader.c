precision mediump float;

attribute vec2 vPosition;
attribute vec4 vColor;

varying vec4 fColor;

void main() {
    gl_Position = vec4(vPosition.x, vPosition.y, 0.0, 1.0);
    fColor = vColor;
}