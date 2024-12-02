package com.FoodSpringApp.FoodSpringApp.controller;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.FoodSpringApp.FoodSpringApp.model.Usuario;
import com.FoodSpringApp.FoodSpringApp.service.UsuarioService;
@Controller
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    @Autowired
    private UsuarioService usuarioService;
    @PostMapping("/public/save-client")
    public ResponseEntity<Map<String, String>> guardarCliente(@RequestBody Usuario usuario) {
        Map<String, String> response = new HashMap<>();
        try {
            if (usuario.getRole() != null && !usuario.getRole().isEmpty()) {
                usuario.setRole(usuario.getRole());
            }else{
                usuario.setRole("USER");
            }
            Usuario nuevoUsuario = usuarioService.save(usuario);
            response.put("message", "Usuario registrado con éxito.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Hubo un error al registrar el usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    @PutMapping("/private/update-client")
    public ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario usuarioData) {
        Usuario usuarioActualizado = usuarioService.update(usuarioData.getId(), usuarioData);
        System.out.println(usuarioActualizado);
        if (usuarioActualizado != null) {
            return ResponseEntity.ok(usuarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/private/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        if (usuarioService.findById(id) == null) {
            response.put("message", "Usuario no encontrado.");
            return ResponseEntity.status(404).body(response);
        }
        usuarioService.deleteById(id);
        response.put("message", "Usuario eliminado exitosamente.");
        return ResponseEntity.status(204).body(response);
    }
}