package br.com.caelum.ingresso.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Sessao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private Filme filme;

	@ManyToOne
	private Sala sala;

	private LocalTime horario;

	private BigDecimal preco;
	
	@OneToMany(mappedBy = "sessao", fetch = FetchType.EAGER)
	private Set<Ingresso> ingressos = new HashSet<>();

	public Sessao(LocalTime horario, Filme filme, Sala sala) {
		this.filme = filme;
		this.sala = sala;
		this.horario = horario;
		this.preco = filme.getPreco().add(sala.getPreco());
	}
	
	public Map<String, List<Lugar>> getMapaDeLugares() {
		return this.sala.getMapaDeLugares();
	}
	
	public boolean isDisponivel(Lugar lugarSelecionado) {
		
//		Com for normal
//		for (Ingresso ingresso : ingressos) {
//			
//			if (ingresso.getLugar().equals(lugarSelecionado)) {
//				return false;
//			}
//		}
//		return true;
		
		return ingressos.stream().map(Ingresso::getLugar).noneMatch(lugar -> lugar.equals(lugarSelecionado));
	}

	/**
	 * @deprecated hibernate only
	 */
	public Sessao() {
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the filme
	 */
	public Filme getFilme() {
		return filme;
	}

	/**
	 * @param filme the filme to set
	 */
	public void setFilme(Filme filme) {
		this.filme = filme;
	}

	/**
	 * @return the sala
	 */
	public Sala getSala() {
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(Sala sala) {
		this.sala = sala;
	}

	/**
	 * @return the horario
	 */
	public LocalTime getHorario() {
		return horario;
	}

	/**
	 * @param horario the horario to set
	 */
	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public BigDecimal getPreco() {
		return preco.setScale(2, RoundingMode.HALF_UP);
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Set<Ingresso> getIngressos() {
		return ingressos;
	}

	public void setIngressos(Set<Ingresso> ingressos) {
		this.ingressos = ingressos;
	}

}
