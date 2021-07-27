package com.programacion.configurations;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

 
//el estado livness nos sirve para la Verificaciones de vivacida
@Liveness
@ApplicationScoped
public class Mycheck implements HealthCheck {


@Override
public HealthCheckResponse call() {
	//para conocer los pings que le hace cada 5s definido en el InicializacionCdi
	System.out.println("...->Ping1");
// Los métodos estáticos HealthCheckResponse para recuperar un HealthCheckResponseBuilder para construir una respuesta
	//cuando el status de la aplicacion este correcto en el name presentara "OK"
return HealthCheckResponse.up("ok");
}
	

}
