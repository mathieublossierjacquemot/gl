precision mediump float;

attribute vec2 vPosition;

void main() {
    gl_Position = vec4(vPosition.x, vPosition.y, 0.0, 1.0);
}