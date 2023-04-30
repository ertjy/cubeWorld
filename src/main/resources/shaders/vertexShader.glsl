#version 120

in vec3 position;
in vec2 textureCoords;

varying out vec2 pass_textureCoords;
varying out vec3 color;

uniform mat4 transformationMatrix;

void main() {
    pass_textureCoords = textureCoords;
    gl_Position = transformationMatrix * vec4(position, 1.0);
}
