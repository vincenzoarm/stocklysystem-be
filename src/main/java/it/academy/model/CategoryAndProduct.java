package it.academy.model;

import java.io.Serializable;

public class CategoryAndProduct implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Prodotto prodotto;
	private Categoria categoria;

	public CategoryAndProduct() {
		super();
	}

	public CategoryAndProduct(Prodotto prodotto, Categoria categoria) {
		super();
		this.prodotto = prodotto;
		this.categoria = categoria;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
