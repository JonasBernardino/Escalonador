package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;

public class EscalonadorFIFO extends Escalonador{
		
	StatusFifo  sts = new StatusFifo();
	private String processoRodando;
	private int duracaoRodando ;
	private int duracaoFixa ;
	
	private int tick = 0;
	private int quantum = 0;
	private List<String> lista = new ArrayList<>();
	private List<Integer> tempo = new ArrayList<>();
	
	//Construtores
	public EscalonadorFIFO() {	
	}

	public EscalonadorFIFO(TipoEscalonador tipoEscalonador) {
		super(TipoEscalonador.Fifo);
	}

	public EscalonadorFIFO(int quantum) {
		super(TipoEscalonador.Fifo, quantum);
	}
	//Fim dos construtores

	public String getStatus() {
		if (processoRodando == null && lista.size() == 0) {
			return sts.inicio(TipoEscalonador.Fifo, quantum, tick);
		}
		if (processoRodando == null && lista.size() > 0) {
			return sts.statusFila(TipoEscalonador.Fifo, lista, quantum, tick);
		}
		if (tick > 0 && lista.size() == 0) {
			return sts.statusRodando(TipoEscalonador.Fifo,processoRodando, quantum, tick);
		}
		return  sts.inicio(TipoEscalonador.Fifo, quantum, tick);
	}
		
	

	public void tick() {
		tick ++;
		 rodarPrimeiroProcessoFifo();
		 rodarNovoProcesso();
	}
	
	private void rodarPrimeiroProcessoFifo() {
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


	public void adicionarProcesso(String nomeProcesso) {
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		if (lista.contains(nomeProcesso) || nomeProcesso == null) {
			throw new EscalonadorException();
		}
		if (prioridade < 1) {
			throw new EscalonadorException();
		}
	}

	public void finalizarProcesso(String nomeProcesso) {
	}

	public void bloquearProcesso(String nomeProcesso) {
	}

	public void retomarProcesso(String nomeProcesso) {
		
	}

	public void adicionarProcessoTempoFixo(String string, int duracao) {
		if (lista.contains(string) || string == null) {
			throw new EscalonadorException();
		}
		if (duracao < 1) {
			throw new EscalonadorException();
		}
		
		adicionarProcessoFifo(string,duracao);
	}
	private void adicionarProcessoFifo(String nomeProcesso, int duracao) {
		if (lista.size() == 0) {
			lista.add(nomeProcesso);
			tempo.add(duracao);
		}
}
		
	}

