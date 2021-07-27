package com.programacion.servicio;
import java.util.List;

import com.programacion.object.author;
import com.programacion.object.books;


public interface servicioBooks  {
	public List<books> listar();	
	public List<author> listarAuthor();	
	public List<books> listarbyAuthor(Long idAuthor);	
	public books buscar(Long id);
	public books crear(books libro);
	public books actualizar(books libro);
	public void eliminar(Long id);
}
