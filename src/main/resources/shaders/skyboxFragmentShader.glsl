#version 130
in vec3 passTextureCoordinates;

out vec4 outColor;

uniform samplerCube textureSampler;

void main() {
    outColor = texture(textureSampler, passTextureCoordinates);
}
