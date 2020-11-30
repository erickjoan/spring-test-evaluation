package com.evaluation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evaluation.model.Curso;
import com.evaluation.repo.ICursoRepo;
import com.evaluation.repo.IGenericRepo;
import com.evaluation.service.ICursoService;



@Service
public class CursoServiceImpl extends CRUDImpl<Curso, String> implements ICursoService{

	@Autowired
	private ICursoRepo repo;
	
	@Override
	protected IGenericRepo<Curso, String> getRepo() {		
		return repo; 
	}

}
