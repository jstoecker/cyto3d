/*
 * Color-based shader that performs flat shading. The position of the vertex
 * in eye coordinates is stored to get the normal vector in the fragment shader.
 *
 * Justin Stoecker
 */

#version 120

varying vec4 m_color;
varying vec3 lightDir;
varying vec3 ec_pos;

void main()
{
  vec3 n = normalize(cross(dFdx(ec_pos), dFdy(ec_pos)));

  float k_diffuse = max(dot(n,lightDir),0.0);
  float k_ambient = gl_LightModel.ambient.x;

  gl_FragColor = m_color * max(k_diffuse, k_ambient);
}