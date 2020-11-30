package com.evaluation.service.impl;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evaluation.model.Estudiante;
import com.evaluation.repo.IEstudianteRepo;
import com.evaluation.repo.IGenericRepo;
import com.evaluation.service.IEstudianteService;

import reactor.core.publisher.Flux;



@Service
public class EstudianteServiceImpl extends CRUDImpl<Estudiante, String> implements IEstudianteService{

	@Autowired
	private IEstudianteRepo repo;
	
	@Override
	protected IGenericRepo<Estudiante, String> getRepo() {		
		return repo; 
	}
	
	@Override
	public Flux<Estudiante> getAllOrderByEdad() {
		return repo.findAll().sort(Comparator.comparing(Estudiante::getEdad).reversed());
	}

}
