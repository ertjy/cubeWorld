#version 120
in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in vec3 toLightVectors[16];

varying out vec4 outColor;

uniform sampler2D textureSampler;
uniform int lightCount;
uniform vec3 lights[32];
uniform float reflectivity;
uniform float shineDamper;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToCameraVector = normalize(toCameraVector);

    vec3 totalColor = vec3(0);

    for (int i = 0; i < lightCount; i++) {
        vec3 lightPosition = lights[i * 2];
        vec3 lightColor = lights[i * 2 + 1];

        vec3 unitToLightVector = normalize(toLightVectors[i]);

        float diffuseDotProduct = dot(unitNormal, unitToLightVector);
        float diffuseBrightness = max(diffuseDotProduct, 0);
        vec3 diffuseColor = diffuseBrightness * lightColor;

        vec3 reflectedLightVector = reflect(-unitToLightVector, unitNormal);
        float specularDotProduct = dot(reflectedLightVector, unitToCameraVector);
        float specularBrightness = max(specularDotProduct, 0);
        float specularDampedBrightness = pow(specularBrightness, shineDamper);
        vec3 specularColor = specularDampedBrightness * reflectivity * lightColor;

        totalColor += diffuseColor + specularColor;
    }

    outColor = vec4(totalColor, 1) * texture2D(textureSampler, passTextureCoordinates);
}
