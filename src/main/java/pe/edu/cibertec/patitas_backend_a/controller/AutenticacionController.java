package pe.edu.cibertec.patitas_backend_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.patitas_backend_a.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_a.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_backend_a.service.AutenticacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@RestController
@RequestMapping("/autenticacion")
public class AutenticacionController {

    private static final Logger logger = LoggerFactory.getLogger(AutenticacionController.class);
    @Autowired
    AutenticacionService autenticacionService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        logger.info("Iniciando sesión para usuario: {}", loginRequestDTO.numeroDocumento());
        try {
            String[] datosUsuario = autenticacionService.validarUsuario(loginRequestDTO);
            if (datosUsuario == null){
                logger.warn("Usuario no encontrado: {}", loginRequestDTO.numeroDocumento());
                return new LoginResponseDTO("01", "Error: Usuario no encontrado", "", "");
            }
            return new LoginResponseDTO("00", "", datosUsuario[0], datosUsuario[1]);
        } catch (IOException e) {
            logger.error("Error al validar usuario", e);
            return new LoginResponseDTO("99", "Error: Ocurrio un problema", "", "");
        }


    }
}
