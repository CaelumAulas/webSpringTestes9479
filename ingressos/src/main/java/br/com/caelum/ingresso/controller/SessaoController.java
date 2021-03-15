package br.com.caelum.ingresso.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.ingresso.client.ImagemCapa;
import br.com.caelum.ingresso.client.OmdbClient;
import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.Carrinho;
import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.GerenciadorDeSessao;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import br.com.caelum.ingresso.model.TipoDeIngresso;
import br.com.caelum.ingresso.model.form.SessaoForm;

@Controller
public class SessaoController {
	
	@Autowired
	private SalaDao salaDao;
	
	@Autowired
	private FilmeDao filmeDao;
	
	@Autowired
	private SessaoDao sessaoDao;
	
	@Autowired
	private OmdbClient client;
	
	@Autowired
	private Carrinho carrinho;
	
	
	@GetMapping("/admin/sessao")
	public ModelAndView form(@RequestParam("salaId") Integer salaId, SessaoForm form) {
		
		ModelAndView mv = new ModelAndView("sessao/sessao");
	
		Sala sala = salaDao.findOne(salaId);
		List<Filme> filmes = filmeDao.findAll();
		
		mv.addObject("sala", sala);
		mv.addObject("filmes", filmes);
		mv.addObject("form", form);
		
		return mv;
	}
	
	@PostMapping("/admin/sessao")
	@Transactional
	public ModelAndView salvar(@Valid SessaoForm sessaoForm, BindingResult result) {
		
		if (result.hasErrors()) {
			return form(sessaoForm.getSalaId(), sessaoForm);
		}
		
		Sessao sessao = sessaoForm.toSessao(salaDao, filmeDao);
		
		List<Sessao> sessoesDaSala = sessaoDao.buscaSessoesDaSala(sessao.getSala());
		
		GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoesDaSala);
		
		if (gerenciador.cabe(sessao)) {
			
			sessaoDao.save(sessao);
			return new ModelAndView("redirect:/admin/sala/" + sessaoForm.getSalaId() + "/sessoes/");
			
		}

		return form(sessaoForm.getSalaId(), sessaoForm);
	}
	
	@GetMapping("sessao/{sessaoId}/lugares")
	public ModelAndView lugaresNaSessao(@PathVariable("sessaoId") Integer sessaoId) {
		
		ModelAndView modelAndView = new ModelAndView("sessao/lugares");
		
		Sessao sessao = sessaoDao.findOne(sessaoId);
		
		Optional<ImagemCapa> optionalImagemCapa = client.request(sessao.getFilme(), ImagemCapa.class);
		
		modelAndView.addObject("sessao", sessao);
		modelAndView.addObject("carrinho", carrinho);
		modelAndView.addObject("imagemCapa", optionalImagemCapa.orElse(new ImagemCapa()));
		modelAndView.addObject("tiposDeIngressos", TipoDeIngresso.values());
		
		return modelAndView;
		
	}
	

}
