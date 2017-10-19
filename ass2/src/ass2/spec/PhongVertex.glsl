#version 130

// We simply pass all the ingredients that the Phong Fragment
// shader will need to determine the lighting at each fragment
// These values will be interpolated for each fragment

out vec3 N;  //We want the normal transformed into camera coords
out vec4 v;  //We want the vertex transformed into camera coords


void main (void) {	
    v = gl_ModelViewMatrix * gl_Vertex;
    N = vec3(normalize(gl_NormalMatrix * normalize(gl_Normal)));
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;	
}

