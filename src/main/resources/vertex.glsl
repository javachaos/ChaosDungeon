#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texture_coord;
layout (location = 2) in vec3 color_in;

uniform mat4 view;//world
uniform mat4 model;//object
uniform mat4 projection;//camera

out vec2 texture;
out vec3 color;

void main() {
	texture = texture_coord;
	color = color_in;
	gl_Position = projection * view * model * vec4(position, 1);
}
