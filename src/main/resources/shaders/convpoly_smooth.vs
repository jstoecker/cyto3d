/*
 * Per-pixel diffuse lighting using gl_Color property instead of materials.
 * Justin Stoecker
 */

#version 120

varying vec3 normal;
varying vec4 m_color;
varying vec3 lightDir;
varying vec3 halfVec;

void main()
{
	normal = normalize(gl_NormalMatrix * gl_Normal);

	lightDir = normalize(vec3(gl_LightSource[0].position));
	halfVec = gl_LightSource[0].halfVector.xyz;
	m_color = gl_Color;

	gl_Position = ftransform();
} 
