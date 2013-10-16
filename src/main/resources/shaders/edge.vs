/*
 * Edge billboard vertex shader
 * Justin Stoecker
 */

#version 120

// aspect ratio of texture being sampled (width / height)
uniform float textureAspect;

varying vec2 texCoords;

void main()
{
  // transform billboard center position to eye space
  vec4 vposES = gl_ModelViewMatrix * gl_MultiTexCoord1;
  
  // transform rotation axis to eye space (rotation axis is stored in normal)
  vec3 rightES = (gl_ModelViewMatrix * vec4(gl_Normal, 0.0)).xyz;
  vec3 upES = normalize(cross(rightES, -vposES.xyz));

  // translate billboard center according to view: texcoord's zw components
  // store length and height, respectively; xy components store actual texture
  // coordinates used to identify the vertex relative to the center
  vec2 scale = gl_MultiTexCoord2.xy * (gl_MultiTexCoord0.xy - 0.5);
  vposES.xyz += rightES * scale.x + upES * scale.y;
  
  float edgeAspect = gl_MultiTexCoord0.z / gl_MultiTexCoord0.w;
  gl_TexCoord[0] = gl_MultiTexCoord0;
  gl_TexCoord[0].x *= edgeAspect / textureAspect;

  gl_Position = gl_ProjectionMatrix * vposES;
  gl_FrontColor = gl_Color;
} 
