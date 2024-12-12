/**
 * Controlador para la gestión de usuarios.
 * Proporciona endpoints para registrar, crear, actualizar y eliminar usuarios.
 */
package com.FoodSpringApp.FoodSpringApp.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FoodSpringApp.FoodSpringApp.Component.CustomAuthSuccessHandler;
import com.FoodSpringApp.FoodSpringApp.model.Usuario;
import com.FoodSpringApp.FoodSpringApp.service.UsuarioService;

@Controller
@RequestMapping("/api/usuarios")
public class UsuarioController {

    /**
     * Mapa de respuesta para enviar mensajes al cliente.
     */
    private Map<String, String> response = new HashMap<>();

    /**
     * Servicio de usuarios que contiene la lógica de negocio relacionada con los usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para registrar un nuevo usuario cliente.
     *
     * @param usuario Objeto {@link Usuario} con la información del cliente a registrar.
     * @return Respuesta HTTP indicando el éxito o fallo del registro.
     */
    @PostMapping("/register-client")
    public ResponseEntity<Map<String, String>> formularioRegistroCliente(@RequestBody Usuario usuario) {
        try {
            usuario.setRole("USER");
            Usuario nuevoUsuario = usuarioService.save(usuario);
            response.put("message", "Usuario registrado con éxito.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Hubo un error al registrar el usuario! " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint para crear un usuario.
     *
     * @param data Mapa que contiene los datos del usuario y un token de sesión.
     * @return Respuesta HTTP indicando el éxito o fallo de la creación.
     */
    @PostMapping("/create-user")
    public ResponseEntity<Map<String, String>> crearCliente(@RequestBody(required = false) Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validar si el cuerpo es nulo o vacío
            if (data == null || data.isEmpty()) {
                response.put("message", "El cuerpo de la solicitud está vacío o no es válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            // Validar que 'tokenData' exista y sea un mapa
            if (!data.containsKey("tokenData") || !(data.get("tokenData") instanceof Map)) {
                response.put("message", "El campo 'tokenData' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
    
            // Validar que 'token' exista y sea una cadena
            if (!tokenDataMap.containsKey("token") || !(tokenDataMap.get("token") instanceof String)) {
                response.put("message", "El token es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            String token = (String) tokenDataMap.get("token");
    
            // Validar si el token es válido
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
    
            // Validar que 'usuarioData' exista y sea un mapa
            if (!data.containsKey("usuarioData") || !(data.get("usuarioData") instanceof Map)) {
                response.put("message", "El campo 'usuarioData' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            Map<String, Object> usuarioDataMap = (Map<String, Object>) data.get("usuarioData");
    
            // Crear usuario con validaciones básicas de campos obligatorios
            Usuario usuario = new Usuario();
            if (usuarioDataMap.get("nombre") == null || !(usuarioDataMap.get("nombre") instanceof String)) {
                response.put("message", "El campo 'nombre' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
            usuario.setNombre((String) usuarioDataMap.get("nombre"));
    
            // Repetir validaciones similares para otros campos
            usuario.setApellidos((String) usuarioDataMap.getOrDefault("apellidos", ""));
            usuario.setDni((String) usuarioDataMap.getOrDefault("dni", ""));
            usuario.setEmail((String) usuarioDataMap.getOrDefault("email", ""));
            usuario.setTelefono((String) usuarioDataMap.getOrDefault("telefono", ""));
            usuario.setDireccion((String) usuarioDataMap.getOrDefault("direccion", ""));
            usuario.setPassword((String) usuarioDataMap.getOrDefault("password", ""));
            usuario.setRole((String) usuarioDataMap.getOrDefault("role", ""));
    
            // Guardar el nuevo usuario
            Usuario nuevoUsuario = usuarioService.save(usuario);
            response.put("message", "Usuario " + nuevoUsuario.getNombre() + " creado con éxito.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Hubo un error al registrar el usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Endpoint para actualizar la información de un usuario existente.
     *
     * @param data Mapa que contiene los datos del usuario a actualizar y un token de sesión.
     * @return Respuesta HTTP indicando el éxito o fallo de la actualización.
     */
    @PutMapping("/update-client")
    public ResponseEntity<Map<String, String>> actualizarUsuario(@RequestBody(required = false) Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validar si el cuerpo es nulo o vacío
            if (data == null || data.isEmpty()) {
                response.put("message", "El cuerpo de la solicitud está vacío o no es válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            // Validar que 'tokenData' exista y sea un mapa
            if (!data.containsKey("tokenData") || !(data.get("tokenData") instanceof Map)) {
                response.put("message", "El campo 'tokenData' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
    
            // Validar que 'token' exista y sea una cadena
            if (!tokenDataMap.containsKey("token") || !(tokenDataMap.get("token") instanceof String)) {
                response.put("message", "El token es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            String token = (String) tokenDataMap.get("token");
    
            // Validar si el token es válido
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
    
            // Validar que 'usuarioData' exista y sea un mapa
            if (!data.containsKey("usuarioData") || !(data.get("usuarioData") instanceof Map)) {
                response.put("message", "El campo 'usuarioData' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            Map<String, Object> usuarioDataMap = (Map<String, Object>) data.get("usuarioData");
    
            // Validar que 'id' exista y sea un número válido
            if (!usuarioDataMap.containsKey("id") || !(usuarioDataMap.get("id") instanceof String)) {
                response.put("message", "El campo 'id' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            int userId;
            try {
                userId = Integer.parseInt((String) usuarioDataMap.get("id"));
            } catch (NumberFormatException e) {
                response.put("message", "El 'id' debe ser un número válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            // Crear usuario con datos actualizados
            Usuario usuario = new Usuario();
            usuario.setId(userId);
    
            usuario.setNombre((String) usuarioDataMap.getOrDefault("nombre", ""));
            usuario.setApellidos((String) usuarioDataMap.getOrDefault("apellidos", ""));
            usuario.setEmail((String) usuarioDataMap.getOrDefault("email", ""));
            usuario.setTelefono((String) usuarioDataMap.getOrDefault("telefono", ""));
            usuario.setDireccion((String) usuarioDataMap.getOrDefault("direccion", ""));
            usuario.setPassword((String) usuarioDataMap.getOrDefault("password", ""));
    
            // Actualizar usuario
            Usuario usuarioActualizado = usuarioService.update(usuario.getId(), usuario);
            if (usuarioActualizado != null) {
                response.put("message", "Usuario " + usuarioActualizado.getNombre() + " actualizado con éxito.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Usuario no encontrado.");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    

    /**
     * Endpoint para eliminar un usuario.
     *
     * @param data Mapa que contiene un token de sesión para validar la autenticación.
     * @param id   ID del usuario a eliminar.
     * @return Respuesta HTTP indicando el éxito o fallo de la eliminación.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@RequestBody(required = false) Map<String, Object> data, @PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validar si el cuerpo de la solicitud está vacío o nulo
            if (data == null || data.isEmpty()) {
                response.put("message", "El cuerpo de la solicitud está vacío o no es válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            // Validar que 'tokenData' exista y sea un mapa
            if (!data.containsKey("tokenData") || !(data.get("tokenData") instanceof Map)) {
                response.put("message", "El campo 'tokenData' es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
    
            // Validar que 'token' exista y sea una cadena
            if (!tokenDataMap.containsKey("token") || !(tokenDataMap.get("token") instanceof String)) {
                response.put("message", "El token es requerido y debe ser válido.");
                return ResponseEntity.badRequest().body(response);
            }
    
            String token = (String) tokenDataMap.get("token");
    
            // Validar si el token es válido
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
    
            // Buscar al usuario por ID
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                response.put("message", "Usuario no encontrado.");
                return ResponseEntity.status(404).body(response);
            }
    
            // Eliminar usuario
            usuarioService.deleteById(id);
            response.put("message", "Usuario " + usuario.getNombre() + " eliminado exitosamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error al eliminar el usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
}
