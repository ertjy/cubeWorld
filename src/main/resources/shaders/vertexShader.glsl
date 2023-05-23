#version 120

in vec3 position;
in vec2 textureCoords;

varying out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    pass_textureCoords = textureCoords;
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}
