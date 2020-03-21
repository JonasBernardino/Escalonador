package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;

public class FachadaEscalonador {
	TipoEscalonador tipo;

	private Escalonador escalonador = new Escalonador();

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		if (tipoEscalonador == null) {
			throw new EscalonadorException();
		} 
		
		if (tipoEscalonador.equals(escalonador.escalonadorRoundRobin())) {
			escalonador = new EscalonadorRoundRobin(tipoEscalonador);
		}
		else if (tipoEscalonador.equals(escalonador.escalonadorPrioridade())) {
			escalonador = new EscalonadorPrioridade(tipoEscalonador);
		}
		else { 
			if(tipoEscalonador.equals(TipoEscalonador.MaisCurtoPrimeiro)) {
			escalonador = new EscalonadorMaisCurtoPrimeiro(tipoEscalonador);}
		
			else {
			escalonador = new EscalonadorFIFO(tipoEscalonador);
		}
	}}

	public FachadaEscalonador(TipoEscalonador tipoEscalonador, int quantum) {
		if (tipoEscalonador.equals(escalonador.escalonadorRoundRobin())) {
			escalonador = new EscalonadorRoundRobin(quantum);
		} else if (tipoEscalonador.equals(escalonador.escalonadorPrioridade())) {
			escalonador = new EscalonadorPrioridade(quantum);
		} else {
			if(tipoEscalonador.equals(TipoEscalonador.MaisCurtoPrimeiro)) {
			escalonador = new EscalonadorMaisCurtoPrimeiro(0);
			}else {
				escalonador = new EscalonadorFIFO(quantum);
			}
		}
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
		escalonador.adicionarProcesso(nomeProcesso, prioridade);
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

	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao) {
		escalonador.adicionarProcessoTempoFixo(nomeProcesso, duracao);
	}
}