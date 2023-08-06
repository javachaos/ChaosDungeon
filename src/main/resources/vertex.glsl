#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texture_coord;
layout (location = 2) in vec3 color_in;

uniform mat4 transformWorld;
uniform mat4 transformObject;
uniform mat4 cameraProjection;

out vec2 texture;
out vec3 color;

void main() {
	texture = texture_coord;
	color = color_in;
	gl_Position = cameraProjection * transformWorld * transformObject * vec4(position, 1);
}
