package com.evaluation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evaluation.model.Matricula;
import com.evaluation.repo.IGenericRepo;
import com.evaluation.repo.IMatriculaRepo;
import com.evaluation.service.IMatriculaService;



@Service
public class MatriculaServiceImpl extends CRUDImpl<Matricula, String> implements IMatriculaService{

	@Autowired
	private IMatriculaRepo repo;
	
	@Override
	protected IGenericRepo<Matricula, String> getRepo() {		
		return repo; 
	}

}
