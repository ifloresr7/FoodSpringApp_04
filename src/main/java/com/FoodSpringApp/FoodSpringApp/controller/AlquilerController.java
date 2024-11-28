package com.FoodSpringApp.FoodSpringApp.controller;

import java.util.List;

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

import com.FoodSpringApp.FoodSpringApp.model.Alquiler;
import com.FoodSpringApp.FoodSpringApp.model.Usuario;
//import com.FoodSpringApp.FoodSpringApp.repository.VehiculoRepository;
import com.FoodSpringApp.FoodSpringApp.service.AlquilerService;
import com.FoodSpringApp.FoodSpringApp.service.UsuarioService;


@Controller
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Alquiler>> obtenerTodosAlquileres() {
    List<Alquiler> alquileres = alquilerService.obtenerTodosAlquileres();
    return ResponseEntity.ok(alquileres);
    }

    @GetMapping("/friendly")
    public ResponseEntity<List<Alquiler>> obtenerTodosAlquileress() {
        List<Alquiler> alquileres = alquilerService.obtenerTodosAlquileres();
        return ResponseEntity.ok(alquileres);
    }

    @PostMapping("/crear-alquiler")
    public ResponseEntity<?> crearAlquiler(@RequestBody Alquiler alquilerData) {
        try {
            // Validar campos obligatorios
            if (alquilerData.getClienteId() == 0) {
                return ResponseEntity.badRequest().body("El campo clienteId es obligatorio.");
            }
            if (alquilerData.getVehiculoId() == 0) {
                return ResponseEntity.badRequest().body("El campo vehiculoId es obligatorio.");
            }

            // Crear el alquiler
            Alquiler nuevoAlquiler = alquilerService.create(alquilerData);
            if (nuevoAlquiler != null) {
                return ResponseEntity.ok(nuevoAlquiler);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Hubo un problema al guardar el alquiler.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error inesperado: " + e.getMessage());
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
    public ResponseEntity<?> actualizarAlquiler(@RequestBody Alquiler alquilerData) {
        try {
            Alquiler actualizado = alquilerService.update(alquilerData);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error al actualizar el alquiler: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminar-alquiler/{id}")
    public ResponseEntity<?> eliminarAlquiler(@PathVariable int id) {
        try {
            alquilerService.eliminar(id);
            return ResponseEntity.ok("Alquiler eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error al eliminar el alquiler: " + e.getMessage());
        }
    }


}