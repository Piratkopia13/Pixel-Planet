varying vec4 color;
varying vec2 texCoord0;

in vec2 texCoord;

void main(){

    color = ftransform() + 0.2;
    texCoord0 = texCoord;
	gl_Position = ftransform();
	
}