package app;  // Paquete base

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Habilita auto-configuración, escaneo de componentes y más
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);  // Inicia la app y el servidor
    }
}