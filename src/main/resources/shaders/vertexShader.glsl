#version 120

in vec3 position;
in vec2 textureCoords;

varying out vec2 pass_textureCoords;

void main() {
    pass_textureCoords = textureCoords;
    gl_Position = vec4(position, 1.0);
}
