#version 120

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;
in mat4 transformationMatrix;

varying out vec2 passTextureCoordinates;
varying out vec3 surfaceNormal;
varying out vec3 toCameraVector;
varying out vec3 toLightVectors[16];

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform int lightCount;
uniform vec3 lights[48];

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    passTextureCoordinates = textureCoordinates;
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toCameraVector = cameraPosition - worldPosition.xyz;

    for (int i = 0; i < lightCount; i++) {
        int lightType = int(lights[i * 3 + 2].x);

        if (lightType == 0) {
            toLightVectors[i] = lights[i * 3] - worldPosition.xyz;
        }
    }
}
