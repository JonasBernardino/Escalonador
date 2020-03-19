package br.ufpb.dcx.aps.escalonador;

import java.util.*;

public class EscalonadorMaisCurtoPrimeiro extends Escalonador { 

	StatusMaisCurtoPrimeiro status = new StatusMaisCurtoPrimeiro();

	private int tick;
	private List<String> lista = new ArrayList<>();
	private List<Integer> tempo = new ArrayList<>();

	private String processoRodando;

	private int duracaoFixa ;
	private int duracaoRodando ;

	public EscalonadorMaisCurtoPrimeiro() {
	}

	public EscalonadorMaisCurtoPrimeiro(TipoEscalonador tipoEscalonador) {
		super(TipoEscalonador.MaisCurtoPrimeiro);
	}

	public EscalonadorMaisCurtoPrimeiro(int quantum) {
		super(TipoEscalonador.MaisCurtoPrimeiro, quantum);
	}

	public String getStatus() {
		if (processoRodando == null && lista.size() == 0) {
			return status.statusInicialMaisCurto(TipoEscalonador.MaisCurtoPrimeiro, 0, tick);
		}
		if (processoRodando == null && lista.size() > 0) {
			return status.statusFilaMaisCurto(TipoEscalonador.MaisCurtoPrimeiro, lista, 0, tick);
		}
		if (tick > 0 && lista.size() == 0) {
			return status.statusRodandoMaisCurto(TipoEscalonador.MaisCurtoPrimeiro, processoRodando, 0, tick);
		}
		return status.statusProcessoRodandoFilaMaisCurto(TipoEscalonador.MaisCurtoPrimeiro, processoRodando, lista, 0, tick);
	}

	public void tick() {

		tick++;
		
		rodarPrimeiroProcessoMCP();
		
		rodarNovoProcesso();

	}

	private void rodarPrimeiroProcessoMCP() {
		if (lista.size() > 0) {
			if (processoRodando == null) {

				processoRodando = lista.remove(0);
				duracaoRodando = tempo.remove(0);
				duracaoFixa = tick + duracaoRodando;
			}
		}
	}

	private void rodarNovoProcesso() {
		if (duracaoFixa == tick && processoRodando != null) {
			if (lista.size() > 0) {
				processoRodando = lista.remove(0);
				duracaoRodando = tempo.remove(0);
			} else {
				processoRodando = null;
				duracaoRodando = 0;
			}
			if (duracaoRodando > 0) {
				duracaoFixa = tick + duracaoRodando;
			}
		}
	}

	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao) {
		if (lista.contains(nomeProcesso) || nomeProcesso == null) {
			throw new EscalonadorException();
		}
		if (duracao < 1) {
			throw new EscalonadorException();
		}
		adicionarProcessoMCP(nomeProcesso, duracao);

	}

	private void adicionarProcessoMCP(String nomeProcesso, int duracao) {
		int maisCurto = Integer.MAX_VALUE;
		if (lista.size() == 0) {
			lista.add(nomeProcesso);
			tempo.add(duracao);
		} else {
			int menorPosicao = guardarPosicaoMenor(nomeProcesso, duracao, maisCurto);

			ordenarFilaProcessos(menorPosicao);
		}
	}

	private void ordenarFilaProcessos(int menorPosicao) {
		if (menorPosicao > 0) {
			String filaTemp = lista.remove(menorPosicao);
			Integer duracaoTemp = tempo.remove(menorPosicao);

			lista.add(0, filaTemp);
			tempo.add(0, duracaoTemp);
		}
	}

	private int guardarPosicaoMenor(String nomeProcesso, int duracao, int maisCurto) {
		lista.add(nomeProcesso);
		tempo.add(duracao);
		int menorPosicao = 0;
		for (int i = 0; i < tempo.size(); i++) {
			if (tempo.get(i) < maisCurto) {
				maisCurto = tempo.get(i);
				menorPosicao = i;
			}
		}
		return menorPosicao;
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		throw new EscalonadorException();
	}

}
