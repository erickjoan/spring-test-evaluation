package com.evaluation.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.evaluation.model.Estudiante;
import com.evaluation.service.IEstudianteService;
import com.evaluation.validators.RequestValidator;


import reactor.core.publisher.Mono;

@Component
public class EstudiantesHandler {

	@Autowired
	private IEstudianteService service;
	
	@Autowired
	private Validator validador;
	
	@Autowired
	private RequestValidator validadorGeneral;
		
	public Mono<ServerResponse> listar(ServerRequest req){
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar(), Estudiante.class);
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
	
		Mono<Estudiante> monoEstudiante = req.bodyToMono(Estudiante.class);

		return monoEstudiante				
				.flatMap(validadorGeneral::validate)
				.flatMap(service::registrar)
				.flatMap(p -> ServerResponse.created(URI.create(req.uri().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);
	}
	
	public Mono<ServerResponse> modificar(ServerRequest req) {
		/*
		Mono<Estudiante> monoEstudiante = req.bodyToMono(Estudiante.class);		
		Mono<Estudiante> monoBD = service.listarPorId(req.pathVariable("id"));
		return monoBD
				.zipWith(monoEstudiante, (bd, p) -> {				
					bd.setId(p.getId());
					bd.setNombre(p.getNombre());
					bd.setApellido(p.getApellido());
					bd.setDni(p.getDni());
					bd.setEdad(p.getEdad());
					bd.setUrlFoto(p.getUrlFoto());
					return bd;
				})
				.flatMap(validadorGeneral::validate)								
				.flatMap(service::modificar)
				.flatMap(p -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				)
				.switchIfEmpty(ServerResponse.notFound().build());
		 */
			Mono<Estudiante> monoEstudiante = req.bodyToMono(Estudiante.class);
		
			return monoEstudiante
				.flatMap(validadorGeneral::validate)
				.flatMap(service::modificar)
				.flatMap(p -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p))
				);
				
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
