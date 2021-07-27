package com.programacion.configurations;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;


@ApplicationScoped
public class InicializacionCdi {

	//Donde esta el register server
	@Inject
	@ConfigProperty(name="configsource.consul.host",defaultValue="127.0.0.1")
	private String consulHost;
	
	@Inject
	@ConfigProperty(name="app.name")
	private String appName;
	
	@Inject
	@ConfigProperty(name="quarkus.http.port",defaultValue = "8080")
	private Integer appPort;
	 
	 
	 
	
	public static final String ID=UUID.randomUUID().toString();
	//Para registrar el servicio
	public void init(@Observes @Initialized(ApplicationScoped.class)Object obt) throws UnknownHostException {
			
	System.out.println("*******init");
	ConsulClient client=new ConsulClient(consulHost);
	NewService newService = new NewService();	
	newService.setId(ID);
	newService.setName(appName);	
	newService.setPort(appPort);
	newService.setAddress("127.0.0.1");
	
	
	NewService.Check serviceCheck = new NewService.Check();
	serviceCheck.setMethod("GET");
	//cada 5 segundos hace una peticon Get para conocer el estado de la aplicacion
	serviceCheck.setHttp("http://"+consulHost+":"+appPort+"/health");
	serviceCheck.setInterval("5s");
	//en caso de que el caso sea estado critical dara de baja al servidor
	serviceCheck.setDeregisterCriticalServiceAfter("5s");
	newService.setCheck(serviceCheck);
	
	//Tags 
		//traefik.http.routers.<router_name>.rule=PathPrefix('/mp-gateway')
		String rule=String.format("traefik.http.routers.%s.rule=PathPrefix(`/%s`)",appName,appName);
		String mids=String.format("traefik.http.routers.%s.middlewares=%s",appName,appName);
		//Midleware
		
		//traefik.http.middlwares.<router_name>.rule=PathPrefix('/mp-gateway')
		String middleware=String.format("traefik.http.middlewares.%s.stripprefix.prefixes=/%s",appName,appName);
		
		newService.setTags(Arrays.asList(rule,middleware,mids));
	
	client.agentServiceRegister(newService);
}
	
	//Para destruir el servicio	
	public void destroy(@Observes @Destroyed(ApplicationScoped.class)Object obt) {
		System.out.println("*******destroy");	
		ConsulClient client =new ConsulClient(consulHost);
		client.agentServiceDeregister(ID);
			
	}
}
