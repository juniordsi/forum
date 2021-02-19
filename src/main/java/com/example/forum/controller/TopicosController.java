package com.example.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.forum.controller.dto.DetalhesTopicoDto;
import com.example.forum.controller.dto.TopicoDto;
import com.example.forum.controller.form.AtualizarTopicoForm;
import com.example.forum.controller.form.TopicoForm;
import com.example.forum.model.Topico;
import com.example.forum.repository.CursoRepository;
import com.example.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	/// BUSCAR TÓPICOS OU TÓPICO POR NOME
	@GetMapping
	@Cacheable(value = "buscarTopicos")
	public Page<TopicoDto> buscarTopicos(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(page=0, size=10, sort="id", direction=Direction.DESC) Pageable paginacao) {
		Page<Topico> topicos;
		if (nomeCurso == null) {
			topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.convert(topicos);
		}
		topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
		return TopicoDto.convert(topicos);
	}
	
	/// BUSCAR TÓPICO POR ID
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> buscarTopico(@PathVariable long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent())
			return ResponseEntity.ok().body(new DetalhesTopicoDto(topico.get()));
		return ResponseEntity.notFound().build();
	}
	
	/// CADASTRAR NOVO TÓPICO
	@PostMapping
	@Transactional
	@CacheEvict(value = "buscarTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrarTopico(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.toTopico(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "buscarTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizarTopico(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok().body(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
		
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "buscarTopicos", allEntries = true)
	public ResponseEntity<?> excluirTopico(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
}

