package pe.edu.cibertec.patitas_backend_a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PatitasBackendAApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatitasBackendAApplication.class, args);
	}

}
