package br.ufpb.dcx.aps.escalonador;

import java.util.List;

public class StatusMaisCurtoPrimeiro {

	public String statusInicialMaisCurto(TipoEscalonador tipo, int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {};Quantum: " + quantum + ";Tick: " + tick;
	}

	public String statusFilaMaisCurto(TipoEscalonador tipo, List<String> fila, int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {Fila: " + fila + "};Quantum: " + quantum + ";Tick: " + tick;
	}

	public String statusRodandoMaisCurto(TipoEscalonador tipo, String rodando, int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + "};Quantum: " + quantum + ";Tick: " + tick;
	}

	public String statusProcessoRodandoFilaMaisCurto(TipoEscalonador tipo, String rodando, List<String> processos,
			int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + ", Fila: " + processos + "};Quantum: "
				+ quantum + ";Tick: " + tick;
	}
}