//varying vec4 color;
varying vec2 texCoord0;

uniform sampler2D sampler;
uniform vec4 color;

void main(){

    vec4 texColor = texture2D(sampler, texCoord0.xy);
    if(color == 0){
	    gl_FragColor = texColor;
    } else
	    gl_FragColor = color;
	
}