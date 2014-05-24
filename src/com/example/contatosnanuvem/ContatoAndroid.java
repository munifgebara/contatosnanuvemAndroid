package com.example.contatosnanuvem;

import java.util.Arrays;

public class ContatoAndroid {
	
	private String pin;
	private String nome;
	private String emails[];
	private String telefones[];
	
	public ContatoAndroid(){
		pin="0";
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public String[] getTelefones() {
		return telefones;
	}

	public void setTelefones(String[] telefones) {
		this.telefones = telefones;
	}

	@Override
	public String toString() {
		return "ContatoAndroid [pin=" + pin + ", nome=" + nome + ", emails="
				+ Arrays.toString(emails) + ", telefones="
				+ Arrays.toString(telefones) + "]";
	}
	
	
	
	
	

}
