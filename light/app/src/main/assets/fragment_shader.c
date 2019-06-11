precision mediump float;

uniform mat4 uMatrix;
uniform vec3 uLightPosition;

varying vec4 fColor;
varying vec3 lightReflectVP;
varying vec3 fNormal;

void main() {
    vec3 lightReflect = reflect(uLightPosition, normalize(fNormal));
    vec3 lightReflectVP = normalize(mat3(uMatrix) * lightReflect);
    vec3 eyeDirection = vec3(0.0, 0.0, -1.0);
    float specular = pow(max(0.0, dot(lightReflectVP, eyeDirection)), 16.0);

    gl_FragColor = fColor + specular * vec4(1.0);;
}