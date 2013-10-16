/*
 * Shader: Convex Polyhedron
 *
 * Renders a convex polyhedron with flat shading and uniform scaling relative
 * to the polyhedron's origin.
 *
 * Justin Stoecker
 */

#version 120

varying vec4 m_color;
varying vec3 lightDir;
varying vec3 ec_pos;

void main()
{
	lightDir = normalize(vec3(gl_LightSource[0].position));
	m_color = gl_Color;
	vec4 ec_vertex = gl_ModelViewMatrix * gl_Vertex;
	ec_pos = ec_vertex.xyz;
	gl_Position = gl_ProjectionMatrix * ec_vertex;
} 