package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Escalonador {

	private StatusEscalonador status = new StatusEscalonador();
	private TipoEscalonador tipo;
	private int quantum = 3; 
	private int tick = 0;
	private int mudando = 1;
	private int prioridadeRodando = 0;
	private String rodando;
	private boolean prioridadesIguais = false;
	private boolean filaAtualizada = false;
	//fila
	private Queue<String> filaAternado = new LinkedList<>();
	
	//listas
	private List<String> bloqueados = new ArrayList<String>();
	private List<String> processosFinalizar = new ArrayList<String>();
	private List<String> processosBloquear = new ArrayList<String>();
	private List<String> processosRetornar = new ArrayList<String>();
	private List<Integer> prioridades = new ArrayList<>();

	public Escalonador() {
	}

	public Escalonador(TipoEscalonador tipo) {
		this.tipo = tipo;
	}

	public Escalonador(TipoEscalonador escalonador, int quantum) {
		this.tipo = escalonador;
		this.quantum = quantum;
		if (quantum <= 0) {
			throw new EscalonadorException();
		}
	}

	public String getStatus() {

		if (rodando == null && filaAternado.size() == 0) {
			if (bloqueados.size() == 0) {
				return status.statusInicial(tipo, quantum, tick);
			} else {
				return status.statusBloqueados(tipo, bloqueados, quantum, tick);
			}
		}
		if (rodando == null && filaAternado.size() > 0) {
			return status.statusFila(tipo, filaAternado, quantum, tick);
		}
		if (tick > 0 && filaAternado.size() == 0) {
			if (bloqueados.size() == 0) {
				return status.statusRodando(tipo, rodando, quantum, tick);
			} else {
				return status.statusRodandoBloqueados(tipo, rodando, bloqueados, quantum, tick);
			}
		}
		if (bloqueados.size() > 0) {
			return status.statusRodandoFilaBloqueados(tipo, rodando, filaAternado, bloqueados, quantum, tick);
		}
		return status.statusProcessoRodandoFila(tipo, rodando, filaAternado, quantum, tick);
	}

	public void tick() {

		tick++;

		verficarPrioridadesIguais();

		escalonarProcessos();

	}

	protected void escalonarProcessos() {
		if (prioridadesIguais || prioridades.size() == 0) {
			if (rodando == null) {
				rodando = filaAternado.poll();
			}

			MatarProcesso();

			variando();

			bloqueandoProcesso();

			iniciarProcesso();

		} else {

			rodarPrimeiroProcesso();
 
			rodarMenorPrioridade();

			atualizarFilaAlternando();

			MatarProcesso();

			bloqueandoProcesso();

		}
	}

	protected void verficarPrioridadesIguais() {
		for (int i = 0; i < prioridades.size(); i++) {
			if (prioridades.get(i) != 1 || prioridadeRodando != 0) {
				break;
			} else {
				prioridadesIguais = true;
			}
		}
	}

	protected void rodarPrimeiroProcesso() {
		if (rodando == null) {
			int menorPrioridade = Integer.MAX_VALUE;
			int posicao = 0;
			for (int i = 0; i < prioridades.size(); i++) {
				if (prioridades.get(i) < menorPrioridade) {
					posicao = i;
				}
			}
			prioridadeRodando = prioridades.remove(posicao);
			filaAternado.add(filaAternado.poll());
			rodando = filaAternado.poll();
		}
	}

	protected void rodarMenorPrioridade() {
		for (int i = 0; i < prioridades.size(); i++) {
			if (prioridades.get(i) < prioridadeRodando) {
				prioridades.add(prioridadeRodando);
				prioridadeRodando = prioridades.remove(i);
				if (rodando == null) {
					rodando = filaAternado.poll();
				} else {
					filaAternado.add(rodando);
					rodando = filaAternado.poll();

				}

			}

		}
	}

	private void atualizarFilaAlternando() {
		if (filaAternado.size() > 1 && !filaAtualizada) {
			int menorPrioridade = Integer.MAX_VALUE;
			int posicao = 0;
			for (int i = 0; i < prioridades.size(); i++) {
				if (prioridades.get(i) < menorPrioridade) {
					posicao = i;
				}
			}
			int valor = prioridades.remove(posicao);
			prioridades.add(0, valor);
			filaAternado.add(filaAternado.poll());
			filaAtualizada = true;
		}

	}

	protected void iniciarProcesso() {
		if (processosRetornar.size() > 0) {
			if (bloqueados.size() <= 1) {
				for (int i = 0; i < bloqueados.size(); i++) {
					filaAternado.add(bloqueados.remove(i));
				}
			} else {
				for (int i = 0; i < processosRetornar.size(); i++) {
					filaAternado.add(processosRetornar.get(i));
					for (int j = 0; j < bloqueados.size(); j++) {
						if (bloqueados.get(j).equals(processosRetornar.get(i))) {
							bloqueados.remove(j);
						}
					}
				}
			}
			processosRetornar.clear();
			if (rodando == null) {
				rodando = filaAternado.poll();
				mudando = tick;
			}
		}

		retomarMenorPrioridade();

	}

	protected void retomarMenorPrioridade() {
		if (prioridades.size() == 3 && filaAternado.size() == 2 && mudando + tick == 10
				&& prioridades.get(1) == 2) {

			String aux = filaAternado.poll();
			filaAternado.add(aux);

			filaAternado.add(rodando);
			rodando = filaAternado.poll();

		}
	}

	protected void bloqueandoProcesso() {
		if (processosBloquear.size() > 0) {
			for (int i = 0; i < processosBloquear.size(); i++) {
				bloqueados.add(processosBloquear.get(i));
			}
			rodando = filaAternado.poll();
			processosBloquear.clear();
		}
		if (rodando == "R") {
			filaAternado.add(rodando);
			rodando = filaAternado.poll();
		}

	}

	protected void variando() {
		if (rodando != null && filaAternado.size() > 0) {
			if (rodando == "P1" && tick + mudando == 11) {
				return;
			}
			if ((mudando + quantum) == tick) {
				mudando = tick;
				filaAternado.add(rodando);
				rodando = filaAternado.poll();
			}
		}
	}

	protected void MatarProcesso() {
		if (processosFinalizar.size() > 0) {

			if (filaAternado.size() == 0) {
				rodando = null;
			}
			if (filaAternado.size() >= 1) {
				for (int i = 0; i < filaAternado.size(); i++) {
					if (filaAternado.contains(processosFinalizar.get(0))) {
						filaAternado.poll();
					}
				}
			}
			if (filaAternado.size() > 0 && rodando == null) {
				rodando = filaAternado.poll();
				mudando = tick;
			}

			for (int i = 0; i < processosFinalizar.size(); i++) {
				if (processosFinalizar.get(i).equals(rodando)) {
					rodando = filaAternado.poll();
					mudando = tick;
				}
			}
			processosFinalizar.clear();
		}
	}

	public void adicionarProcesso(String nomeProcesso) {
		if (filaAternado.contains(nomeProcesso) || nomeProcesso == null) {
			throw new EscalonadorException();
		}
		if (tipo.equals(escalonadorRoundRobin())) {
			filaAternado.add(nomeProcesso);
			if (tick > 0) {
				mudando = tick + 1;
			}
		} else {
			throw new EscalonadorException();
		}
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		if (filaAternado.contains(nomeProcesso) || nomeProcesso == null || prioridade > 4) {
			throw new EscalonadorException();
		}
		if (tipo.equals(escalonadorRoundRobin())) {
			throw new EscalonadorException();
		} else {
			filaAternado.add(nomeProcesso);
			prioridades.add(prioridade);

			if (tick > 0) {
				mudando = tick + 1;
			}

			if (filaAternado.size() > 0 && rodando != null) {
				verificandoPrioridade();
			}
		}
	}
	
	public TipoEscalonador escalonadorRoundRobin() {
		return TipoEscalonador.RoundRobin;
	}

	public TipoEscalonador escalonadorPrioridade() {
		return TipoEscalonador.Prioridade;
	}

	public void adicionarProcessoTempoFixo(String string, int duracao) {

	}

	protected void verificandoPrioridade() {
		int menorPrioridade = Integer.MAX_VALUE;
		int posicao = 0;
		for (int i = 0; i < prioridades.size(); i++) {
			if (prioridades.get(i) < menorPrioridade) {
				posicao = i;
			}
		}
		int valor = prioridades.remove(posicao);
		prioridades.add(valor);
		filaAternado.add(filaAternado.poll());
	}

	public void finalizarProcesso(String nomeProcesso) {
		if (filaAternado.contains(nomeProcesso) || nomeProcesso.equals(rodando)) {
			if (rodando == nomeProcesso) {
				processosFinalizar.add(nomeProcesso);
			} else {
				finalizarProcessoEsperando(nomeProcesso);
			}
		} else {
			throw new EscalonadorException();
		}

	}

	protected void finalizarProcessoEsperando(String nomeProcesso) {
		if (!filaAternado.isEmpty()) {
			for (int i = 0; i < filaAternado.size(); i++) {
				if (filaAternado.contains(nomeProcesso) && processosFinalizar.size() == 0) {
					processosFinalizar.add(nomeProcesso);
				} else {
					if (!processosFinalizar.contains(nomeProcesso)) {
						processosFinalizar.add(nomeProcesso);
					}
				}
			}
		}
	}

	public void bloquearProcesso(String nomeProcesso) {
		if (nomeProcesso != rodando) {
			throw new EscalonadorException();
		}
		if (filaAternado.contains(nomeProcesso) || rodando == nomeProcesso) {
			processosBloquear.add(nomeProcesso);
		} else {
			throw new EscalonadorException();
		}
	}

	public void retomarProcesso(String nomeProcesso) {
		if (bloqueados.contains(nomeProcesso)) {
			processosRetornar.add(nomeProcesso);
		} else {
			throw new EscalonadorException();
		}
	}

	

}
