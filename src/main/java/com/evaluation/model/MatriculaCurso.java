package com.evaluation.model;


public class MatriculaCurso {
	
	private Curso curso;
	
	public MatriculaCurso() {
		
		
	}
	public MatriculaCurso( Curso curso) {
		this.curso = curso;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
}
