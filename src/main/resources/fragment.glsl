#version 330 core

uniform sampler2D sampleTexture;

out vec4 fragColor;

in vec2 texture;
in vec3 color;

void main() {
	fragColor = texture2D(sampleTexture, texture) * vec4(color, 1.0);
}