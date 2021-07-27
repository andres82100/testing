package com.programacion.servicio;
import java.util.List;
import com.programacion.object.author;

public interface servicioAuthor  {
	public List<author> listar();	
	public author buscar(Long id);
	public author crear(author autor);
	public author actualizar(author autor);
	public void eliminar(Long id);
}
