#version 330 core

uniform sampler2D sample;

out vec4 fragColor;

in vec2 texture;
in vec3 color;

void main() {
	fragColor = vec4(color, 1.0) * texture2D(sample, texture);
}