package com.FoodSpringApp.FoodSpringApp.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import com.FoodSpringApp.FoodSpringApp.Component.CustomAuthSuccessHandler;
import com.FoodSpringApp.FoodSpringApp.model.Alquiler;
import com.FoodSpringApp.FoodSpringApp.model.Usuario;
//import com.FoodSpringApp.FoodSpringApp.repository.VehiculoRepository;
import com.FoodSpringApp.FoodSpringApp.service.AlquilerService;
import com.FoodSpringApp.FoodSpringApp.service.UsuarioService;

@Controller
@RequestMapping("/api/alquileres")
public class AlquilerController {
    private Map<String, String> response = new HashMap<>();

    @Autowired
    private AlquilerService alquilerService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/obtener-alquileres")
    public ResponseEntity<?> obtenerTodosAlquileress() {
        try {
            List<Alquiler> alquileres = alquilerService.obtenerTodosAlquileres();
            return ResponseEntity.ok(alquileres);
        } catch (Exception e) {
            System.err.println("Error al obtener los alquileres: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al obtener la lista de alquileres. Por favor, intente más tarde.");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/crear-alquiler")
    public ResponseEntity<Map<String, String>> crearAlquiler(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
            String token = (String) tokenDataMap.get("token");
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
            Map<String, Object> alquilerData = (Map<String, Object>) data.get("alquilerData");
            Alquiler alquiler = new Alquiler();
            alquiler.setClienteId(Integer.parseInt((String) alquilerData.get("clienteId")));
            alquiler.setVehiculoId(Integer.parseInt((String) alquilerData.get("vehiculoId")));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaInicio = LocalDate.parse((String) alquilerData.get("fechaInicio"), formatter);
            LocalDate fechaFin = LocalDate.parse((String) alquilerData.get("fechaFin"), formatter);
            Date fechaInicioDate = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fechaFinDate = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
            alquiler.setFechaInicio(fechaInicioDate);
            alquiler.setFechaFin(fechaFinDate);
            alquiler.setPrecio(Double.parseDouble((String) alquilerData.get("precio")));
            alquilerService.create(alquiler);
            response.put("message", "Alquiler creado con éxito.");
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            response.put("message", "Hubo un error al registrar el usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/mis-alquileres")
    public ResponseEntity<List<Alquiler>> obtenerAlquileresDelUsuario() {
        // Obtener el usuario autenticado
        String dni = SecurityContextHolder.getContext().getAuthentication().getName();
        // Obtener información del usuario
        Usuario usuario = usuarioService.obtenerUsuarioPorDni(dni); // Asume que este método está en UsuarioService
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }
        // Obtener los alquileres asociados al usuario
        List<Alquiler> alquileres = alquilerService.obtenerAlquileresPorCliente(usuario.getId());
        return ResponseEntity.ok(alquileres);
    }

    @PutMapping("/actualizar-alquiler")
    public ResponseEntity<Map<String, String>> actualizarAlquiler(@RequestBody(required = false) Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validar que 'data' no sea null
            if (data == null) {
                response.put("message", "Los datos de la solicitud son nulos.");
                return ResponseEntity.status(400).body(response); // 400 Bad Request
            }
    
            // Validar que 'tokenData' exista
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
            if (tokenDataMap == null || !tokenDataMap.containsKey("token")) {
                response.put("message", "Falta la información del token.");
                return ResponseEntity.status(400).body(response); // 400 Bad Request
            }
    
            String token = (String) tokenDataMap.get("token");
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response); // 401 Unauthorized
            }
    
            // Validar que 'alquilerData' exista
            Map<String, Object> alquilerData = (Map<String, Object>) data.get("alquilerData");
            if (alquilerData == null) {
                response.put("message", "Faltan los datos del alquiler.");
                return ResponseEntity.status(400).body(response); // 400 Bad Request
            }
    
            // Validar que todos los campos necesarios estén presentes
            if (!alquilerData.containsKey("clienteId") ||
                !alquilerData.containsKey("vehiculoId") ||
                !alquilerData.containsKey("fechaInicio") ||
                !alquilerData.containsKey("fechaFin") ||
                !alquilerData.containsKey("precio")) {
                response.put("message", "Faltan campos obligatorios en los datos del alquiler.");
                return ResponseEntity.status(400).body(response); // 400 Bad Request
            }
    
            // Procesar los datos del alquiler
            Alquiler alquiler = new Alquiler();
            alquiler.setId(Integer.parseInt((String) alquilerData.get("id")));
            alquiler.setClienteId(Integer.parseInt((String) alquilerData.get("clienteId")));
            alquiler.setVehiculoId(Integer.parseInt((String) alquilerData.get("vehiculoId")));
    
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaInicio = LocalDate.parse((String) alquilerData.get("fechaInicio"), formatter);
            LocalDate fechaFin = LocalDate.parse((String) alquilerData.get("fechaFin"), formatter);
    
            Date fechaInicioDate = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date fechaFinDate = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
            alquiler.setFechaInicio(fechaInicioDate);
            alquiler.setFechaFin(fechaFinDate);
            alquiler.setPrecio(Double.parseDouble((String) alquilerData.get("precio")));
            
            alquilerService.update(alquiler);
    
            response.put("message", "Alquiler actualizado con éxito.");
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (Exception e) {
            response.put("message", "Hubo un error al actualizar el alquiler: " + e.getMessage());
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }
    }    

    @DeleteMapping("/eliminar-alquiler/{id}")
    public ResponseEntity<Map<String, String>> eliminarAlquiler(@PathVariable int id) {
        try {
            alquilerService.eliminar(id);
            response.put("message", "Alquiler eliminado con éxito.");
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            response.put("message", "Hubo un error al eliminar el alquiler: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}