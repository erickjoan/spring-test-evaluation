package com.evaluation.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import com.evaluation.model.Curso;
import com.evaluation.service.ICursoService;
import com.evaluation.validators.RequestValidator;

import reactor.core.publisher.Mono;

@Component
public class CursoHandler {

	@Autowired
	private ICursoService service;
	
	@Autowired
	private Validator validador;
	
	@Autowired
	private RequestValidator validadorGeneral;
		
	public Mono<ServerResponse> listar(ServerRequest req){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar(), Curso.class);
	}
	
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){		
		String id = req.pathVariable("id");
		return service.listarPorId(id)
				.flatMap(p -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)	
						.body(fromValue(p))
				)
				.switchIfEmpty(ServerResponse.notFound().build());			
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req) {
	
		Mono<Curso> monoCurso = req.bodyToMono(Curso.class);

		return monoCurso				 
				.flatMap(validadorGeneral::validate)
				.flatMap(service::registrar)
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req) {

		Mono<Curso> monoCurso = req.bodyToMono(Curso.class);		
		//Mono<Curso> monoBD = service.listarPorId(req.pathVariable("id"));
		
		//return monoBD
		return monoCurso
				//.zipWith(monoCurso, (bd, p) -> {				
				//	bd.setId(p.getId());
				//	bd.setNombre(p.getNombre());
				//	bd.setEstado(p.getEstado());
				//	return bd;
				//})
				//.flatMap(validadorGeneral::validate)								
				.flatMap(service::modificar)
				.flatMap(p -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
		
	}
	
	
	public Mono<ServerResponse> eliminar(ServerRequest req){

		String id = req.pathVariable("id");
			
		return service.listarPorId(id)
				.flatMap(p -> service.eliminar(p.getId())
						.then(ServerResponse.noContent().build())
				)
				.switchIfEmpty(ServerResponse.notFound().build());			
	}
	
}
