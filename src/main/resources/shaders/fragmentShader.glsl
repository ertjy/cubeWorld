#version 120
in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in vec3 toLightVectors[16];

varying out vec4 outColor;

uniform sampler2D textureSampler;
uniform int lightCount;
uniform vec3 lights[48];
uniform float reflectivity;
uniform float shineDamper;

float ambientBrightness = 0.1;

vec3 lightWithSpotLight(vec3 unitNormal, vec3 unitToCameraVector, vec3 lightPosition, vec3 lightColor, vec3 toLightVector) {
    vec3 totalColor = vec3(0);

    vec3 unitToLightVector = normalize(toLightVector);

    float diffuseDotProduct = dot(unitNormal, unitToLightVector);
    float diffuseBrightness = max(diffuseDotProduct, 0);
    vec3 diffuseColor = diffuseBrightness * lightColor;
    totalColor += diffuseColor;

    if (diffuseBrightness != 0) {
        vec3 reflectedLightVector = reflect(-unitToLightVector, unitNormal);
        float specularDotProduct = dot(reflectedLightVector, unitToCameraVector);
        float specularBrightness = max(specularDotProduct, 0);
        float specularDampedBrightness = pow(specularBrightness, shineDamper);
        vec3 specularColor = specularDampedBrightness * reflectivity * lightColor;
        totalColor += specularColor;
    }

    vec3 ambientColor = ambientBrightness * lightColor;
    totalColor += ambientColor;

    return totalColor;
}

vec3 lightWithDirectionalLight(vec3 unitNormal, vec3 unitToCameraVector, vec3 lightDirection, vec3 lightColor) {
    vec3 totalColor = vec3(0);

    vec3 unitToLightVector = normalize(-lightDirection);

    float diffuseDotProduct = dot(unitNormal, unitToLightVector);
    float diffuseBrightness = max(diffuseDotProduct, 0);
    vec3 diffuseColor = diffuseBrightness * lightColor;
    totalColor += diffuseColor;

    if (diffuseBrightness != 0) {
        vec3 reflectedLightVector = reflect(-unitToLightVector, unitNormal);
        float specularDotProduct = dot(reflectedLightVector, unitToCameraVector);
        float specularBrightness = max(specularDotProduct, 0);
        float specularDampedBrightness = pow(specularBrightness, shineDamper);
        vec3 specularColor = specularDampedBrightness * reflectivity * lightColor;
        totalColor += specularColor;
    }

    vec3 ambientColor = ambientBrightness * lightColor;
    totalColor += ambientColor;

    return totalColor;
}

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToCameraVector = normalize(toCameraVector);

    vec3 totalColor = vec3(0);

    for (int i = 0; i < lightCount; i++) {
        vec3 lightPositionOrDirection = lights[i * 3];
        vec3 lightColor = lights[i * 3 + 1];
        vec3 lightProperties = lights[i * 3 + 2];

        int lightType = int(lightProperties.x);
        float ambientBrightness = lightProperties.y;

        switch (lightType) {
            case 0: // spot light
                totalColor += lightWithSpotLight(unitNormal, unitToCameraVector, lightPositionOrDirection, lightColor, toLightVectors[i]);
            case 1: // directional light
                totalColor += lightWithDirectionalLight(unitNormal, unitToCameraVector, lightPositionOrDirection, lightColor);
            default:
                break;
        }
    }

    outColor = vec4(totalColor, 1) * texture2D(textureSampler, passTextureCoordinates);
}
