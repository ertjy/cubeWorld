#version 130

in vec3 position;

out vec3 passTextureCoordinates;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1);

    passTextureCoordinates = position;
}
