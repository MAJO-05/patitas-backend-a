package pe.edu.cibertec.patitas_backend_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_backend_a.client.AutenticacionClient;
import pe.edu.cibertec.patitas_backend_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_a.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_backend_a.service.AutenticacionService;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;


@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "http://localhost:8082")
public class AutenticacionController {

    @Autowired
    AutenticacionService autenticacionService;

    @Autowired
    private AutenticacionClient autenticacionClient;

    private String obtenerTipoDocumento(String tipoDocumento) {
        switch (tipoDocumento) {
            case "1":
                return "DNI";
            case "2":
                return "CEX";
            case "3":
                return "PAS";
            default:
                return "Desconocido";
        }
    }


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        try {
            Thread.sleep(Duration.ofSeconds(9));
            String[] datosUsuario = autenticacionService.validarUsuario(loginRequestDTO);
            System.out.println("Resultado: " + Arrays.toString(datosUsuario));
            if (datosUsuario == null) {
                return new LoginResponseDTO("01", "Error: Usuario no encontrado", "", "");
            }
            return new LoginResponseDTO("00", "", datosUsuario[0], datosUsuario[1]);

        } catch (Exception e) {
            return new LoginResponseDTO("99", "Error: Ocurrió un problema", "", "");
        }

    }

    @PostMapping("/autenticar-feign")
    public LoginResponseDTO autenticarFeign(@RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("Iniciando autenticación con Feign Client...");

        try {
            LoginResponseDTO response = autenticacionClient.login(loginRequestDTO);
            System.out.println("Respuesta del cliente Feign: " + response);

            if ("00".equals(response.codigo())) {
                return new LoginResponseDTO("00", "", response.nombreUsuario(), response.correoUsuario());
            } else {
                return new LoginResponseDTO("02", "Error: Autenticación fallida", "", "");
            }
        } catch (Exception e) {
            System.err.println("Error en la autenticación: " + e.getMessage()); // Log de error
            return new LoginResponseDTO("99", "Error: Ocurrió un problema en la autenticación", "", "");
        }
    }






    @PostMapping("/logout")
    public String logout(@RequestBody LoginRequestDTO logoutRequestDTO) {
        String tipoDocumentoTexto = obtenerTipoDocumento(logoutRequestDTO.tipoDocumento());
        String logEntry = String.format("%s - Tipo Documento: %s, Número Documento: %s%n",
                LocalDateTime.now(), tipoDocumentoTexto, logoutRequestDTO.numeroDocumento());

        try (FileWriter writer = new FileWriter("logout.log", true)) {
            writer.write(logEntry);
        } catch (IOException e) {
            return "Error al registrar la salida";
        }

        return "Sesión cerrada correctamente";
    }



}