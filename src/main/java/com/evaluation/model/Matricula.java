package com.evaluation.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;


@Document(collection = "matriculas")
public class Matricula {
	
	@Id
	private String id;
	
	@Size(min = 2)
	private String descripcion;
	
	@NotNull
	@Field(name = "estado")
	private Boolean estado;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime fecha_matricula = LocalDateTime.now();
	
	private Estudiante estudiante;
	
	private List<MatriculaCurso> listcurso;
   
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getFecha_matricula() {
		return fecha_matricula;
	}

	public void setFecha_matricula(LocalDateTime fecha_matricula) {
		this.fecha_matricula = fecha_matricula;
	}

	public Estudiante getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}



	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<MatriculaCurso> getListcurso() {
		return listcurso;
	}

	public void setListcurso(List<MatriculaCurso> listcurso) {
		this.listcurso = listcurso;
	}

    
	

}
