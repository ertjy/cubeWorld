#version 120
in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in vec3 toLightVector;

varying out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float reflectivity;
uniform float shineDamper;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLightVector = normalize(toLightVector);
    vec3 unitToCameraVector = normalize(toCameraVector);

    float diffuseDotProduct = dot(unitNormal, unitToLightVector);
    float diffuseBrightness = max(diffuseDotProduct, 0);
    vec3 diffuseColor = diffuseBrightness * lightColor;

    vec3 reflectedLightVector = reflect(-unitToLightVector, unitNormal);
    float specularDotProduct = dot(reflectedLightVector, unitToCameraVector);
    float specularBrightness = max(specularDotProduct, 0);
    float specularDampedBrightness = pow(specularBrightness, shineDamper);
    vec3 specularColor = specularDampedBrightness * reflectivity * lightColor;

    vec3 totalColor = diffuseColor + specularColor;

    outColor = vec4(totalColor, 1) * texture2D(textureSampler, passTextureCoordinates);
}
