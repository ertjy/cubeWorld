#version 120

varying vec2 pass_textureCoords;

varying out vec4 out_colour;

uniform sampler2D textureSampler;

void main() {
    out_colour = texture2D(textureSampler, pass_textureCoords);
}
