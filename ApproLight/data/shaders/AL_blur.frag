#version 110

uniform sampler2D tex;
uniform vec2 screen;
uniform float scale;
uniform float gain;

vec2 coord = gl_TexCoord[0].xy;
float pixelDistance = 1.0 / screen.x;
float scaledPixelDistance = scale * pixelDistance;
vec2 screenEdge = vec2(pixelDistance * 0.5, (pixelDistance * -0.5) + screen.y);

float rand(vec2 co){
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

vec4 getFragment(in vec2 position) {
    if (position.x > screenEdge.y || position.x < screenEdge.x) {
        return vec4(0.0);
    }
    return texture2D(tex, position);
}

void main() {
    vec4 increasedColor = max(texture2D(tex, coord), 0.0) * 0.085;
    increasedColor += max(getFragment(coord + vec2(scaledPixelDistance, 0.0)), 0.0) * 0.083;
    increasedColor += max(getFragment(coord - vec2(scaledPixelDistance, 0.0)), 0.0) * 0.083;
    increasedColor += max(getFragment(coord + vec2(2.0 * scaledPixelDistance, 0.0)), 0.0) * 0.082;
    increasedColor += max(getFragment(coord - vec2(2.0 * scaledPixelDistance, 0.0)), 0.0) * 0.082;
    increasedColor += max(getFragment(coord + vec2(3.0 * scaledPixelDistance, 0.0)), 0.0) * 0.080;
    increasedColor += max(getFragment(coord - vec2(3.0 * scaledPixelDistance, 0.0)), 0.0) * 0.080;
    increasedColor += max(getFragment(coord + vec2(4.0 * scaledPixelDistance, 0.0)), 0.0) * 0.078;
    increasedColor += max(getFragment(coord - vec2(4.0 * scaledPixelDistance, 0.0)), 0.0) * 0.078;
    increasedColor += max(getFragment(coord + vec2(5.0 * scaledPixelDistance, 0.0)), 0.0) * 0.076;
    increasedColor += max(getFragment(coord - vec2(5.0 * scaledPixelDistance, 0.0)), 0.0) * 0.076;
    increasedColor += max(getFragment(coord + vec2(6.0 * scaledPixelDistance, 0.0)), 0.0) * 0.073;
    increasedColor += max(getFragment(coord - vec2(6.0 * scaledPixelDistance, 0.0)), 0.0) * 0.073;
    increasedColor += max(getFragment(coord + vec2(7.0 * scaledPixelDistance, 0.0)), 0.0) * 0.070;
    increasedColor += max(getFragment(coord - vec2(7.0 * scaledPixelDistance, 0.0)), 0.0) * 0.070;
    increasedColor += max(getFragment(coord + vec2(8.0 * scaledPixelDistance, 0.0)), 0.0) * 0.067;
    increasedColor += max(getFragment(coord - vec2(8.0 * scaledPixelDistance, 0.0)), 0.0) * 0.067;
    increasedColor += max(getFragment(coord + vec2(9.0 * scaledPixelDistance, 0.0)), 0.0) * 0.064;
    increasedColor += max(getFragment(coord - vec2(9.0 * scaledPixelDistance, 0.0)), 0.0) * 0.064;
    increasedColor += max(getFragment(coord + vec2(10.0 * scaledPixelDistance, 0.0)), 0.0) * 0.060;
    increasedColor += max(getFragment(coord - vec2(10.0 * scaledPixelDistance, 0.0)), 0.0) * 0.060;
    increasedColor += max(getFragment(coord + vec2(11.0 * scaledPixelDistance, 0.0)), 0.0) * 0.057;
    increasedColor += max(getFragment(coord - vec2(11.0 * scaledPixelDistance, 0.0)), 0.0) * 0.057;
    increasedColor += max(getFragment(coord + vec2(12.0 * scaledPixelDistance, 0.0)), 0.0) * 0.052;
    increasedColor += max(getFragment(coord - vec2(12.0 * scaledPixelDistance, 0.0)), 0.0) * 0.052;
    increasedColor += max(getFragment(coord + vec2(13.0 * scaledPixelDistance, 0.0)), 0.0) * 0.048;
    increasedColor += max(getFragment(coord - vec2(13.0 * scaledPixelDistance, 0.0)), 0.0) * 0.048;
    increasedColor += max(getFragment(coord + vec2(14.0 * scaledPixelDistance, 0.0)), 0.0) * 0.045;
    increasedColor += max(getFragment(coord - vec2(14.0 * scaledPixelDistance, 0.0)), 0.0) * 0.045;
    increasedColor += max(getFragment(coord + vec2(15.0 * scaledPixelDistance, 0.0)), 0.0) * 0.042;
    increasedColor += max(getFragment(coord - vec2(15.0 * scaledPixelDistance, 0.0)), 0.0) * 0.042;
    increasedColor += max(getFragment(coord + vec2(16.0 * scaledPixelDistance, 0.0)), 0.0) * 0.038;
    increasedColor += max(getFragment(coord - vec2(16.0 * scaledPixelDistance, 0.0)), 0.0) * 0.038;
    increasedColor += max(getFragment(coord + vec2(17.0 * scaledPixelDistance, 0.0)), 0.0) * 0.035;
    increasedColor += max(getFragment(coord - vec2(17.0 * scaledPixelDistance, 0.0)), 0.0) * 0.035;
    increasedColor += max(getFragment(coord + vec2(18.0 * scaledPixelDistance, 0.0)), 0.0) * 0.032;
    increasedColor += max(getFragment(coord - vec2(18.0 * scaledPixelDistance, 0.0)), 0.0) * 0.032;
    increasedColor += max(getFragment(coord + vec2(19.0 * scaledPixelDistance, 0.0)), 0.0) * 0.028;
    increasedColor += max(getFragment(coord - vec2(19.0 * scaledPixelDistance, 0.0)), 0.0) * 0.028;
    increasedColor += max(getFragment(coord + vec2(20.0 * scaledPixelDistance, 0.0)), 0.0) * 0.026;
    increasedColor += max(getFragment(coord - vec2(20.0 * scaledPixelDistance, 0.0)), 0.0) * 0.026;
    increasedColor += max(getFragment(coord + vec2(21.0 * scaledPixelDistance, 0.0)), 0.0) * 0.023;
    increasedColor += max(getFragment(coord - vec2(21.0 * scaledPixelDistance, 0.0)), 0.0) * 0.023;
    increasedColor += max(getFragment(coord + vec2(22.0 * scaledPixelDistance, 0.0)), 0.0) * 0.020;
    increasedColor += max(getFragment(coord - vec2(22.0 * scaledPixelDistance, 0.0)), 0.0) * 0.020;
    increasedColor += max(getFragment(coord + vec2(23.0 * scaledPixelDistance, 0.0)), 0.0) * 0.018;
    increasedColor += max(getFragment(coord - vec2(23.0 * scaledPixelDistance, 0.0)), 0.0) * 0.018;
    increasedColor += max(getFragment(coord + vec2(24.0 * scaledPixelDistance, 0.0)), 0.0) * 0.016;
    increasedColor += max(getFragment(coord - vec2(24.0 * scaledPixelDistance, 0.0)), 0.0) * 0.016;
    increasedColor += max(getFragment(coord + vec2(25.0 * scaledPixelDistance, 0.0)), 0.0) * 0.014;
    increasedColor += max(getFragment(coord - vec2(25.0 * scaledPixelDistance, 0.0)), 0.0) * 0.014;
    increasedColor += max(getFragment(coord + vec2(26.0 * scaledPixelDistance, 0.0)), 0.0) * 0.012;
    increasedColor += max(getFragment(coord - vec2(26.0 * scaledPixelDistance, 0.0)), 0.0) * 0.012;
    increasedColor += max(getFragment(coord + vec2(27.0 * scaledPixelDistance, 0.0)), 0.0) * 0.011;
    increasedColor += max(getFragment(coord - vec2(27.0 * scaledPixelDistance, 0.0)), 0.0) * 0.011;
    increasedColor += max(getFragment(coord + vec2(28.0 * scaledPixelDistance, 0.0)), 0.0) * 0.009;
    increasedColor += max(getFragment(coord - vec2(28.0 * scaledPixelDistance, 0.0)), 0.0) * 0.009;
    increasedColor += max(getFragment(coord + vec2(29.0 * scaledPixelDistance, 0.0)), 0.0) * 0.008;
    increasedColor += max(getFragment(coord - vec2(29.0 * scaledPixelDistance, 0.0)), 0.0) * 0.008;
    increasedColor += max(getFragment(coord + vec2(30.0 * scaledPixelDistance, 0.0)), 0.0) * 0.007;
    increasedColor += max(getFragment(coord - vec2(30.0 * scaledPixelDistance, 0.0)), 0.0) * 0.007;
    increasedColor += max(getFragment(coord + vec2(31.0 * scaledPixelDistance, 0.0)), 0.0) * 0.006;
    increasedColor += max(getFragment(coord - vec2(31.0 * scaledPixelDistance, 0.0)), 0.0) * 0.006;
    increasedColor += max(getFragment(coord + vec2(32.0 * scaledPixelDistance, 0.0)), 0.0) * 0.005;
    increasedColor += max(getFragment(coord - vec2(32.0 * scaledPixelDistance, 0.0)), 0.0) * 0.005;
    increasedColor += max(getFragment(coord + vec2(33.0 * scaledPixelDistance, 0.0)), 0.0) * 0.004;
    increasedColor += max(getFragment(coord - vec2(33.0 * scaledPixelDistance, 0.0)), 0.0) * 0.004;
    increasedColor += max(getFragment(coord + vec2(34.0 * scaledPixelDistance, 0.0)), 0.0) * 0.004;
    increasedColor += max(getFragment(coord - vec2(34.0 * scaledPixelDistance, 0.0)), 0.0) * 0.004;
    increasedColor += max(getFragment(coord + vec2(35.0 * scaledPixelDistance, 0.0)), 0.0) * 0.003;
    increasedColor += max(getFragment(coord - vec2(35.0 * scaledPixelDistance, 0.0)), 0.0) * 0.003;
    increasedColor += max(getFragment(coord + vec2(36.0 * scaledPixelDistance, 0.0)), 0.0) * 0.003;
    increasedColor += max(getFragment(coord - vec2(36.0 * scaledPixelDistance, 0.0)), 0.0) * 0.003;
    increasedColor *= min(gain * increasedColor.a, 1.0);
    gl_FragColor = vec4(increasedColor.rgb, 1.0);
}
