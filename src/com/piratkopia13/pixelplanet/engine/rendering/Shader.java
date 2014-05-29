package com.piratkopia13.pixelplanet.engine.rendering;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int program;
    private int shader;

    public Shader() {
        this.program = glCreateProgram();

        if (program == 0){
            System.err.println("Shader creation failed: could not find valid memory location in constructor");
            System.exit(1);
        }
    }

    public void bind(){
        glUseProgram(program);
    }
    public void unBind(){
        glUseProgram(0);
    }

    public void addVertexShader(String text){
        addProgram(text, GL_VERTEX_SHADER);
    }

    public void addFragmentShader(String text){
        addProgram(text, GL_FRAGMENT_SHADER);
    }

    public void compileShader(){
        glLinkProgram(program);
        if (glGetProgram(program, GL_LINK_STATUS) == 0){
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);
        if (glGetProgram(program, GL_VALIDATE_STATUS) == 0){
            System.err.println(glGetShaderInfoLog(program, 1024));
            System.exit(1);
        }
    }

    private void addProgram(String text, int type){
        shader = glCreateShader(type);

        if (shader == 0){
            System.err.println("Shader creation failed: could not find valid memory location when adding shader");
            System.exit(1);
        }

        glShaderSource(shader, text);
        glCompileShader(shader);

        if (glGetShader(shader, GL_COMPILE_STATUS) == 0){
            System.err.println(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
    }

    public void dispose(){
        glDeleteProgram(program);
        glDeleteShader(shader);
    }

    public int getProgramID() {
        return program;
    }
}
