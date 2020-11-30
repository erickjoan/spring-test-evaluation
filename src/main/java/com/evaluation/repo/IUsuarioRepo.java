package com.evaluation.repo;

import com.evaluation.model.Usuario;

import reactor.core.publisher.Mono;

public interface IUsuarioRepo extends IGenericRepo<Usuario, String>{
	
	Mono<Usuario> findOneByUsuario(String usuario);
}
