#version 120

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

varying out vec2 passTextureCoordinates;
varying out vec3 surfaceNormal;
varying out vec3 toCameraVector;
varying out vec3 toLightVectors[16];

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform int lightCount;
uniform vec3 lights[32];

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    passTextureCoordinates = textureCoordinates;
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toCameraVector = cameraPosition - worldPosition.xyz;

    for (int i = 0; i < lightCount; i++) {
        toLightVectors[i] = lights[i * 2] - worldPosition.xyz;
    }
}
