package com.programacion.distribuida;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.CheckOptions;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.util.List;

@ApplicationScoped
public class LifeCycle[X] { // <-- Cambiar el nombre de la clase

@Inject
@ConfigProperty(name = "quarkus.http.port")
Integer puertoApp;

@Inject
@ConfigProperty(name = "consul.host")
String consulHost;

@Inject
@ConfigProperty(name = "consul.port")
Integer consulPort;

String servicioId;

public void registro(@Observes StartupEvent event, Vertx vertx) {
    try {
        ConsulClientOptions opciones = new ConsulClientOptions()
                .setHost(consulHost)
                .setPort(consulPort);

        ConsulClient cliente = ConsulClient.create(vertx, opciones);

        String direccionIp = InetAddress.getLocalHost().getHostAddress();

        // 1. Crear un ID único
        servicioId = "[X]-%s:%d".formatted(direccionIp, puertoApp);

        // 2. Ruta de Salud (Asegúrate de tener instalada la librería quarkus-smallrye-health)
        var urlCheck = "http://%s:%d/q/health/live".formatted(direccionIp, puertoApp);

        CheckOptions checkOptions = new CheckOptions()
                .setHttp(urlCheck)
                .setInterval("10s")
                .setDeregisterAfter("10s");

        // 3. Tus etiquetas de Traefik
        var tags = List.of(
                "traefik.enable=true",
                "traefik.http.routers.router-[X].rule=PathPrefix(`/[X]`)",
                "traefik.http.routers.router-[X].middlewares=middlewares-[X]",
                "traefik.http.middlewares.middlewares-[X].stripprefix.prefixes=/[X]"
        );

        // 4. Armar el paquete
        ServiceOptions serviceOptions = new ServiceOptions()
                .setName("[X]")
                .setId(servicioId)
                .setAddress(direccionIp)
                .setPort(puertoApp)
                .setCheckOptions(checkOptions)
                .setTags(tags);

        // 5. Registrar en Consul
        cliente.registerService(serviceOptions)
                .onSuccess(it -> System.out.println("Registro exitoso: [X]"))
                .onFailure(it -> System.out.println("Fallo al registrar: [X]"));

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void deregistro(@Observes ShutdownEvent event, Vertx vertx) {
    ConsulClientOptions options = new ConsulClientOptions()
            .setHost(consulHost)
            .setPort(consulPort);

    ConsulClient client = ConsulClient.create(vertx, options);

    client.deregisterService(servicioId)
            .onSuccess(it -> System.out.println("Se quitó correctamente: [X]"))
            .onFailure(it -> System.out.println("Fallo al quitar: [X]"));
}
}