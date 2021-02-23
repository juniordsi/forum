package com.example.forum.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.forum.model.StatusTopico;
import com.example.forum.model.Topico;

public class DetalhesTopicoDto extends TopicoDto {
	
	private String nomeAutor;
	private StatusTopico status;
	private List<RespostaDto> respostas = new ArrayList<>();
	
	public DetalhesTopicoDto(Topico topico) {
		super(topico);
		this.nomeAutor = topico.getAutor().getNome();
		this.status = topico.getStatus();
		this.respostas.addAll(topico.getRespostas().stream().map(RespostaDto::new).collect(Collectors.toList()));
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public StatusTopico getStatus() {
		return status;
	}

	public List<RespostaDto> getRespostas() {
		return respostas;
	}

}
