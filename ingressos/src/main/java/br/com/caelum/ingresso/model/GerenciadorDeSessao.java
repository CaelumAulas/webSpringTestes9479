package br.com.caelum.ingresso.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class GerenciadorDeSessao {

	private List<Sessao> sessoesExistentes;

	public GerenciadorDeSessao(List<Sessao> sessoesExistentes) {
		this.sessoesExistentes = sessoesExistentes;
	}

	public boolean cabe(Sessao novaSessao) {

		if (terminaAmanha(novaSessao)) {
			return false;
		}

		return sessoesExistentes.stream()
				.noneMatch(sessaoExistente -> horariosIsConflitantes(sessaoExistente, novaSessao));
	}

	private boolean horariosIsConflitantes(Sessao sessaoExistente, Sessao sessaoNova) {

		LocalDateTime inicioSessaoExistente = getInicioSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessaoNova);
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessaoNova);

		boolean sessaoNovaTerminaAntesDaExistente = terminoSessaoNova.isBefore(inicioSessaoExistente);
		boolean sessaoNovaComecaDepoisDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);

		if (sessaoNovaTerminaAntesDaExistente || sessaoNovaComecaDepoisDaExistente) {
			return false;
		}

		return true;
	}

	private LocalDateTime getInicioSessaoComDiaDeHoje(Sessao sessao) {
		LocalDate hoje = LocalDate.now();
		
		return sessao.getHorario().atDate(hoje);
	}

	private boolean terminaAmanha(Sessao sessao) {

		LocalDateTime horarioDeTermino = getTerminoSessaoComDiaDeHoje(sessao);
		LocalDateTime ultimoSegundoDoDia = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

		if (horarioDeTermino.isAfter(ultimoSegundoDoDia)) {
			return true;
		}

		return false;
	}

	private LocalDateTime getTerminoSessaoComDiaDeHoje(Sessao sessao) {
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessao);
		
		return inicioSessaoNova.plus(sessao.getFilme().getDuracao());
	}

}
