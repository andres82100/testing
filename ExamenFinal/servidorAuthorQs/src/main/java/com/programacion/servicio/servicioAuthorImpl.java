package com.programacion.servicio;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import com.programacion.object.author;

@ApplicationScoped
public class servicioAuthorImpl implements servicioAuthor {
	@Inject
	EntityManager em;

	@Override
	public author buscar(Long id) {

		author per = em.find(author.class, id);

		if (per != null) {
			return per;
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public author crear(author autor) {
		em.persist(autor);
		return autor;
	}

	@Override
	public author actualizar(author autor) {
		author autores = em.find(author.class, autor.getId());
		if (autores == null)
			throw new WebApplicationException(autor.getId() + "no existe", 404);
		autores.setName(autor.getName());
		autores.setAge(autor.getAge());
		autores.setGenre(autor.getGenre());
		return autor;
	}

	@Override
	public void eliminar(Long id) {
		author autores = em.find(author.class, id);
		em.remove(autores);

	}

	@Override
	public List<author> listar() {
		return em.createNamedQuery("Authors.findAll", author.class).getResultList();
	}

}
