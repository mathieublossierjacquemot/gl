precision mediump float;

uniform mat4 uMatrix;

attribute vec3 vPosition;
attribute vec4 vColor;
attribute vec2 vTexture;

varying vec4 fColor;
varying vec2 fTexture;

void main() {
    vec4 position = vec4(vPosition.x, vPosition.y, vPosition.z, 1.0);
    gl_Position = uMatrix * position;
    fColor = vColor;
    fTexture = vTexture;
}