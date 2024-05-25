#version 120

uniform sampler2D tex;
uniform float iTime;
uniform float alphaMult;
uniform vec3 iColor;
uniform float m1;
uniform float m2;
uniform float debug;

const vec2 d = vec2(0.0, 1.0);
const vec2 uv_center = vec2(0.5);

vec2 coord = gl_TexCoord[0].xy;

float rand(vec2 n) {
    return fract(sin(dot(n, vec2(12.9898, 12.1414))) * 83758.5453);
}

float noise(vec2 n) {
    vec2 b = floor(n);
    vec2 f = smoothstep(vec2(0.0), vec2(1.0), fract(n));
    return mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y);
}

float ramp(float t) {
    return 1.0/t;
}

vec2 polarMap(vec2 uv, float shift, float inner) {
    uv = uv_center - uv;
    float px = 1.0 - fract(atan(uv.y, uv.x) / 6.28 + 0.25) + shift;
    float py = (sqrt(uv.x * uv.x + uv.y * uv.y) * (1.0 + inner * 2.0) - inner) * 2.0;
    return vec2(px, py);
}
float fire(vec2 n) {
    return noise(n) + noise(n * 2.1) * .6 + noise(n * 5.4) * .21;
}

float shade(vec2 uv, float t) {
    uv.x += 23.0 + t * .035;
    uv.x *= 35.0;
    float q = fire(uv - t * .013) / 2.0;
    vec2 r = vec2(fire(uv + q / 2.0 + t - uv.x - uv.y), fire(uv + q - t));
    return pow((r.y * 2) * max(1.8, uv.y), 5.0);
}

float getAlpha(float grad) {
    grad = sqrt(grad);
    float tempAlpha = ramp(grad);
    tempAlpha /= (m2 + max(0, tempAlpha));
    return tempAlpha;
}

void main() {
    float t = iTime;
    vec2 uv = coord;
    float dist = distance(uv, uv_center);
    uv = uv * 2.0 - 0.5;
    float ff = 1.0 - uv.y;
    float inverseff = 1.0 - ff;
    vec2 uv2 = uv;
    uv2.y = 1.0 - uv2.y;

    uv = polarMap(uv, 1.3, m1);
    uv2 = polarMap(uv2, 1.9, m1);

    float c1 = getAlpha(shade(uv, t)) * ff;
    float c2 = getAlpha(shade(uv2, t)) * inverseff;

    uv.x = 1.0 - uv.x;
    uv2.x = 1.0 - uv2.x;

    float c3 = getAlpha(shade(uv, t)) * ff;
    float c4 = getAlpha(shade(uv2, t)) * inverseff;

    float alpha = (c1+c2+c3+c4) * 0.5;

    alpha *= clamp((0.5 - dist)/0.05, 0.0, 1.0);
    alpha *= clamp(dist/0.2, 0.0, 1.0);

    gl_FragColor = vec4(iColor.rgb, alpha * alphaMult);
}