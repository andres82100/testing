package com.programacion.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.microprofile.auth.LoginConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import com.programacion.object.author;
import com.programacion.object.books;
import com.programacion.servicio.servicioBooks;
import io.opentracing.Tracer;
@LoginConfig(authMethod = "MP-JWT", realmName = "public")
@RolesAllowed("mysimplerole")
@Path("/booksRest")
@Produces(value = MediaType.APPLICATION_JSON)
public class booksRest {

	@Inject
	@ConfigProperty(name = "configsource.consul.host", defaultValue = "127.0.0.1")
	private String consulHost;

	@Inject
	@ConfigProperty(name = "quarkus.http.port", defaultValue = "8080")
	private Integer appPort;

	@Inject
	Tracer tracer;

	@Inject
	servicioBooks services;

	@GET
	@Counted(name = "LLamadasListarLibro")
	@Timed(name = "TiempoEjecucionListarLibro", unit = MetricUnits.MILLISECONDS)
	// Retry intenta 3 veces,si despues de 3 veces sigue fallando el metodo se nos
	// redirecciona a fallback
	@Retry(maxRetries = 3)
	// Si el servicio se demora en responder mas de 5s,se redirecciona al metodo
	// falback
	@Timeout(value = 5000L)
	// Si entra mas de 5 peticiones al servicio al mismo tiempo ,se redirecciona a
	// Fallback
	@Bulkhead(value = 5)
	// Si despues de 10 llamadas el 50% de las llamadas es un fallo se abre el
	// circuito y nos redirecciona a Fallback
	// despues de un reardo de 5 s y 3 llamadas sin fallo se cierra el circuito y el
	// servicio continua funcionando normalmente.
	@CircuitBreaker(successThreshold = 3, requestVolumeThreshold = 10, failureRatio = 0.5, delay = 5000L)
	@Fallback(fallbackMethod = "getAuthor")

	public List<books> customers() {
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		return services.listar();
	}

	@GET
	@Path("/author")
	@Produces(value = MediaType.APPLICATION_JSON)
	@Counted(name = "LLamadasListarautor")
	@Timed(name = "TiempoEjecucionListarautor", unit = MetricUnits.MILLISECONDS)
	public List<author> autores() {
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		return services.listarAuthor();
	}

	@GET
	@Path("/{id}")
	@Counted(name = "LLamadasListarLibroId")
	@Timed(name = "TiempoEjecucionLibroId", unit = MetricUnits.MILLISECONDS)
	@Produces(value = MediaType.APPLICATION_JSON)
	public books buscar(@PathParam("id") Long id) {
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		books book = services.buscar(id);
		return book;
	}

	@GET
	@Path("/author/{id}")
	@Counted(name = "LLamadasListarAuthord")
	@Timed(name = "TiempoEjecucionAuthorId", unit = MetricUnits.MILLISECONDS)
	@Produces(value = MediaType.APPLICATION_JSON)
	public List<books> buscarbyAuthor(@PathParam("id") Long id) {
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		return services.listarbyAuthor(id);
	}

	@POST
	@Transactional
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Counted(name = "LLamadasCrearLibro")
	@Timed(name = "TiempoEjecucionCrearLibro", unit = MetricUnits.MILLISECONDS)
	public Response save(books data) {
		services.crear(data);
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		return Response.created(UriBuilder.fromResource(booksRest.class).path(String.valueOf(data.getId())).build())
				.build();
	}

	@DELETE
	@Path("/remove/{id}")
	@Transactional
	@Counted(name = "LLamadasEliminarAutor")
	@Metered(name = "TiempoEjecucionEliminarAutor", unit = MetricUnits.MILLISECONDS, description = "metricas para eliminar author", absolute = true)
	public void remove(@PathParam("id") Long id) {
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		services.eliminar(id);
	}

	@PUT
	@Counted(name = "LLamadasActualizarLibro")
	@Timed(name = "TiempoEjecucionActualizarLibro", unit = MetricUnits.MILLISECONDS)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.APPLICATION_JSON)
	@Transactional
	public Response update(books data) {
		tracer.activeSpan().setTag("puerto", appPort);
		tracer.activeSpan().setTag("ip", consulHost);
		services.actualizar(data);
		return Response.noContent().build();
	}
	// Metodos en caso de falla
	public List<books> getAuthor() {
		List<books> lista = new ArrayList<>();
		books au = new books();
		au.setId(Long.valueOf(2));
		au.setIsbn("454");
		au.setIdauthor(Long.valueOf(4));
		au.setTitle("estamos trabajando en la falla");
		lista.add(au);
		return lista;
	}

	// METODO QUE SIMULLA FALLO
	public void fallo() {
		var random = new Random();
		if (random.nextBoolean()) {
			throw new RuntimeException("falla");
		}
	}

	// METODO QUE SIMULA UNA DEMORA
	public void falloTiempo() {
		var random = new Random();
		try {
			Thread.sleep((random.nextInt(10) + 2) * 1000L);
		} catch (Exception ex) {

		}
	}
}
