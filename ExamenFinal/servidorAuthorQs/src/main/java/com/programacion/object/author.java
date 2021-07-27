package com.programacion.object;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQuery(name = "Authors.findAll", query = "SELECT a FROM author a ORDER BY a.id")
public class author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int age;
	private String genre;
	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
