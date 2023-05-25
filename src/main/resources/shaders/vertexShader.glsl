#version 120

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

varying out vec2 passTextureCoordinates;
varying out vec3 surfaceNormal;
varying out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    passTextureCoordinates = textureCoordinates;
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
}
