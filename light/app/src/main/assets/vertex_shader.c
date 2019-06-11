precision mediump float;

uniform mat4 uMatrix;
uniform vec3 uLightPosition;

attribute vec3 vPosition;
attribute vec4 vColor;
attribute vec3 vNormal;

varying vec4 fColor;
varying vec3 fNormal;

void main() {
    vec4 position = vec4(vPosition.x, vPosition.y, vPosition.z, 1.0);
    gl_Position = uMatrix * position;

    float ambiant = 0.3;

    float diffuse = (1.0 - ambiant) * max(0.0, -dot(uLightPosition, vNormal));

    // vec3 lightReflect = reflect(uLightPosition, vNormal);
    // vec3 lightReflectVP = normalize(mat3(uMatrix) * lightReflect);
    // vec3 eyeDirection = vec3(0.0, 0.0, -1.0);
    // float specular = pow(max(0.0, dot(lightReflectVP, eyeDirection)), 8.0);

    float factor = ambiant + diffuse;
    fColor = factor * vColor ;
    // fColor = factor * vColor + specular * vec4(1.0);
    fNormal = vNormal;
}