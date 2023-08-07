#version 330 core

uniform sampler2D sample;

out vec4 fragColor;
in vec2 texture;

void main() {
	fragColor = texture2D(sample, texture);
}