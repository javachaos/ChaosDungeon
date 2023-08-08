#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texture_coord;

uniform mat4 view;
uniform mat4 transformation;
uniform mat4 projection;

out vec2 texture;

void main() {
	texture = texture_coord;
	gl_Position = projection * view * transformation * vec4(position, 1);
}
