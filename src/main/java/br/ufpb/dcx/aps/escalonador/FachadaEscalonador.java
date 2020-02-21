package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;

public class FachadaEscalonador {
 
	private Escalonador escalonador = new Escalonador();

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		if (tipoEscalonador == null) {
			throw new EscalonadorException();
		}
		if (tipoEscalonador.equals(escalonador.escalonadorRoundRobin())) {
			escalonador = new EscalonadorRoundRobin(tipoEscalonador);}
		
	} 
	 
	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {
		escalonador = new EscalonadorRoundRobin(quantum);
		
	}

	public String getStatus() {
		return escalonador.getStatus();

	}

	public void tick() {
		escalonador.tick();
		

	}

	public void adicionarProcesso(String nomeProcesso) {
		escalonador.adicionarProcesso(nomeProcesso);
		
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
	}

	public void finalizarProcesso(String nomeProcesso) {
		escalonador.finalizarProcesso(nomeProcesso);
	}

	public void bloquearProcesso(String nomeProcesso) {
		escalonador.bloquearProcesso(nomeProcesso);
	}

	public void retomarProcesso(String nomeProcesso) {
		escalonador.retomarProcesso(nomeProcesso);

	}

	public void adicionarProcessoTempoFixo(String string, int duracao) {

	}
}
