package com.example.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.controller.dto.TopicoDto;
import com.example.forum.model.Topico;
import com.example.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@GetMapping
	public List<TopicoDto> findCursos(String nomeCurso) {
		List<Topico> topicos;
		if (nomeCurso == null) {
			topicos = topicoRepository.findAll();
			return TopicoDto.convert(topicos);
		}
		topicos = topicoRepository.findByCursoNome(nomeCurso);
		System.out.println(topicos);
		return TopicoDto.convert(topicos);
	}
}

