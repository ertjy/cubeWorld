#version 120
in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;

varying out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLightVector = normalize(toLightVector);

    float dotProduct = dot(unitNormal, unitToLightVector);
    float brightness = max(dotProduct, 0);
    vec3 diffuseColor = brightness * lightColor;

    outColor = vec4(diffuseColor, 1) * texture2D(textureSampler, passTextureCoordinates);
}
