package com.programacion.object;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQuery(name = "Books.findAll", query = "SELECT b FROM books b ORDER BY b.id")
@NamedQuery(name = "Books.findbyAuthor", query = "SELECT b FROM books b WHERE b.idauthor = :idauthor")
public class books {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String isbn;
	private String title;
	
	@Transient
	public author author;
	private Long idauthor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getIdauthor() {
		return idauthor;
	}

	public void setIdauthor(Long idauthor) {
		this.idauthor = idauthor;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public author getAuthor() {
		return author;
	}

	public void setAuthor(author author) {
		this.author = author;
	}

}
