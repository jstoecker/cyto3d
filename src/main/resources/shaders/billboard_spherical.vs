/*
 * Spherical billboarding shader. Creates a plane that always faces the camera's
 * forward direction.
 *
 * Justin Stoecker
 */

#version 120

varying vec2 texCoords;

void main()
{
  vec4 vposES = gl_ModelViewMatrix * gl_Vertex;
  gl_TexCoord[0] = gl_MultiTexCoord0;
  vec3 rightES = normalize(cross(-vposES.xyz, vec3(0.0, 1.0, 0.0)));
  vec3 upES = normalize(cross(rightES, vposES.xyz));
  vec2 size = gl_MultiTexCoord0.zw * (gl_MultiTexCoord0.xy - 0.5);
  
  vposES.xyz += rightES * size.x + upES * size.y;

  gl_Position = gl_ProjectionMatrix * vposES;
  gl_FrontColor = gl_Color;
}