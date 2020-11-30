package com.evaluation.service;

import com.evaluation.model.Usuario;
import com.evaluation.security.User;

import reactor.core.publisher.Mono;

public interface IUsuarioService extends ICRUD<Usuario, String>{
	
	Mono<User> buscarPorUsuario(String usuario);
}
