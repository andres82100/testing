package com.programacion.servicio;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.programacion.object.author;
import com.programacion.object.books;
import com.programacion.rest.authorService;

@Traced
@ApplicationScoped
public class servicioBooksImpl implements servicioBooks {

	@Inject
	EntityManager em;

	@Inject
	@RestClient
	authorService autorServicio;

	@Override
	public books buscar(Long id) {

		books per = em.find(books.class, id);
		if (per != null) {
			return per;

		} else {
			return null;

		}
	}

	@Override
	@Transactional
	public books crear(books libro) {
		em.persist(libro);
		return libro;
	}

	@Override
	public books actualizar(books libro) {
		books libros = em.find(books.class, libro.getId());
		if (libros == null)
			throw new WebApplicationException(libro.getId() + "no existe", 404);
		libros.setTitle(libro.getTitle());
		libros.setIsbn(libro.getIsbn());
		libros.setIdauthor(libro.getIdauthor());
		return libro;
	}

	@Override
	public void eliminar(Long id) {
		books libros = em.find(books.class, id);
		em.remove(libros);

	}

	@Override

	public List<books> listar() {
		List<books> libros = em.createNamedQuery("Books.findAll", books.class).getResultList();
		List<books> listaAux = new ArrayList<>();

		for (books orders : libros) {

			orders.setAuthor(autorServicio.getByidAuthor(orders.getIdauthor()));
			System.out.println(autorServicio.getByidAuthor(orders.getIdauthor()));
			listaAux.add(orders);
		}

		return listaAux;

	}

	@Override
	public List<books> listarbyAuthor(Long idAuthor) {

		return em.createNamedQuery("Books.findbyAuthor", books.class).setParameter("idauthor", idAuthor)
				.getResultList();
	}

	@Override
	public List<author> listarAuthor() {
				
		return autorServicio.getByName();
		
	}

}
