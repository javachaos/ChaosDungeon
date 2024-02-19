#version 330 core

layout (location=0) in vec2 position; // Vertex position attribute

uniform vec2 pos; // Position of the UI element

void main() {
    // Transform the vertex position by adding the UI position
    vec2 finalPosition = position + pos;

    // Set the output position
    gl_Position = vec4(finalPosition, 0.0, 1.0); // Z-coordinate is 0 for 2D rendering
}