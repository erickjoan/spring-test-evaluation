package com.evaluation.controller;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.evaluation.model.Estudiante;
import com.evaluation.service.IEstudianteService;
import com.evaluation.pagination.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

	@Autowired
	private IEstudianteService service;
	
	@Value("${ruta.subida}")
	private String RUTA_SUBIDA;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Estudiante>>> listar(){
		Flux<Estudiante> fxEstudiante = service.getAllOrderByEdad();
		
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fxEstudiante)
				);
	}
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Estudiante>> listarPorId(@PathVariable("id") String id){
		return service.listarPorId(id) 
					.map(p -> ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(p)
					)
					.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Estudiante>> registrar(@Valid @RequestBody Estudiante estudiante, final ServerHttpRequest req){
		
		return service.registrar(estudiante)
				.map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				);
	}
	
	@PutMapping
	public Mono<ResponseEntity<Estudiante>> modificar(@Valid @RequestBody Estudiante estudiante) {	
		return service.modificar(estudiante)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				);
	}	
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable("id") String id){
		return service.listarPorId(id)
				.flatMap(p -> {
					return service.eliminar(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));						
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
					
	}
	@PostMapping("subir/{id}")
	public Mono<ResponseEntity<Estudiante>> subir(@PathVariable String id, @RequestPart FilePart file){
		return service.listarPorId(id)
				.flatMap(c -> {
					c.setUrlFoto(UUID.randomUUID() + "-" + file.filename());
					return file.transferTo(new File(RUTA_SUBIDA + c.getUrlFoto())).then(service.registrar(c));
				})
				.map(c -> ResponseEntity.ok(c))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/pageable")
	public Mono<ResponseEntity<PageSupport<Estudiante>>> listarPagebale(
			@RequestParam(name = "page", defaultValue = "0") int page,
		    @RequestParam(name = "size", defaultValue = "5") int size
			){
		
		Pageable pageRequest = PageRequest.of(page, size);
		
		return service.listarPage(pageRequest)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)	
						)
				.defaultIfEmpty(ResponseEntity.notFound().build());
		
		
	}
}
 