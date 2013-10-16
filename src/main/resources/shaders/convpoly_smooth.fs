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
  vec3 n = normalize(normal);

  float k_diffuse = max(dot(n,lightDir),0.0);
  float k_specular = max(pow(dot(n,halfVec),92.0),0.0);
  float k_ambient = gl_LightModel.ambient.x;
  
  vec3 c_diffuse = max(k_diffuse, k_ambient) * m_color.xyz;
  vec3 c_specular = k_specular * vec3(1.0, 1.0, 1.0);

  gl_FragColor = vec4(c_diffuse + c_specular, m_color.a);
}