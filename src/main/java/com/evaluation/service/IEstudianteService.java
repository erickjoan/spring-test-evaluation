package com.evaluation.service;

import com.evaluation.model.Estudiante;

import reactor.core.publisher.Flux;

public interface IEstudianteService extends ICRUD<Estudiante, String>{
	
	public Flux<Estudiante> getAllOrderByEdad();
}
