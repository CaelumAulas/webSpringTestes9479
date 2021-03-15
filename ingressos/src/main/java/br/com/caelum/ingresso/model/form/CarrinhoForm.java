package br.com.caelum.ingresso.model.form;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.caelum.ingresso.dao.LugarDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.Ingresso;
import br.com.caelum.ingresso.model.Lugar;
import br.com.caelum.ingresso.model.Sessao;

public class CarrinhoForm {
	
	private List<Ingresso> ingressos = new ArrayList<>();

	public List<Ingresso> getIngressos() {
		return ingressos;
	}

	public void setIngressos(List<Ingresso> ingressos) {
		this.ingressos = ingressos;
	}
	
//	public List<Ingresso> toIngressos(LugarDao lugarDao, SessaoDao sessaoDao){
//		
//		List<Ingresso> ingressosCompleto = new ArrayList<>();
//		
//		for(Ingresso ingresso : ingressos) {
//			
//			Lugar lugar = lugarDao.findOne(ingresso.getLugar().getId());
//			Sessao sessao = sessaoDao.findOne(ingresso.getSessao().getId());
//			
//			Ingresso ingressoCompleto = new Ingresso(sessao, ingresso.getTipoDeIngresso(), lugar);
//			
//			ingressosCompleto.add(ingressoCompleto);
//			
//		}
//		return ingressosCompleto;
//	}
	
	public List<Ingresso> toIngressos(LugarDao lugarDao, SessaoDao sessaoDao){
		return this.ingressos.stream().map(ingresso -> {
			Lugar lugar = lugarDao.findOne(ingresso.getLugar().getId());
			Sessao sessao = sessaoDao.findOne(ingresso.getSessao().getId());
			return new Ingresso(sessao, ingresso.getTipoDeIngresso(), lugar);
		}).collect(Collectors.toList());
	}

}
