#version 330 core

uniform sampler2D sampleTexture;

out vec4 fragColor;

in vec2 texture;

void main() {
	fragColor = texture2D(sampleTexture, texture);
}