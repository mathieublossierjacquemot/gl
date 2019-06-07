precision mediump float;

uniform sampler2D uTexture0;

varying vec4 fColor;
varying vec2 fTexture;

void main() {
    gl_FragColor = texture2D(uTexture0, fTexture) * fColor;
    if (gl_FragColor.a < 0.01) {
        discard;
    }
}