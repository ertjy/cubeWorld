#version 120

in vec3 position;
varying out vec3 colour;

uniform mat4 transformationMatrix;

void main() {
    gl_Position = vec4(position, 1.0);
    colour = vec3(position.x+0.5, 1.0, position.y+0.5);
}
