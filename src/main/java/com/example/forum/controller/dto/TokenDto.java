package com.example.forum.controller.dto;

public class TokenDto {

	private String token;
	private String tipo;

	public TokenDto(String token, String tipo) {
		this.token = token;
		this.tipo = tipo;
	}

	public String getString() {
		return tipo;
	}

	public void setString(String string) {
		this.tipo = string;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
