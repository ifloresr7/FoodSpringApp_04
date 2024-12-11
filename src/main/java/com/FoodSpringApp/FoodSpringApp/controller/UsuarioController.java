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
    public ResponseEntity<Map<String, String>> crearCliente(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
            String token = (String) tokenDataMap.get("token");
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
            Map<String, Object> usuarioDataMap = (Map<String, Object>) data.get("usuarioData");
            Usuario usuario = new Usuario();
            usuario.setNombre((String) usuarioDataMap.get("nombre"));
            usuario.setApellidos((String) usuarioDataMap.get("apellidos"));
            usuario.setDni((String) usuarioDataMap.get("dni"));
            usuario.setEmail((String) usuarioDataMap.get("email"));
            usuario.setTelefono((String) usuarioDataMap.get("telefono"));
            usuario.setDireccion((String) usuarioDataMap.get("direccion"));
            usuario.setPassword((String) usuarioDataMap.get("password"));
            usuario.setRole((String) usuarioDataMap.get("role"));
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
    public ResponseEntity<Map<String, String>> actualizarUsuario(@RequestBody Map<String, Object> data) {
        try {
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
            String token = (String) tokenDataMap.get("token");
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
            Map<String, Object> usuarioDataMap = (Map<String, Object>) data.get("usuarioData");
            Usuario usuario = new Usuario();
            usuario.setId(Integer.parseInt((String) usuarioDataMap.get("id")));
            usuario.setNombre((String) usuarioDataMap.get("nombre"));
            usuario.setApellidos((String) usuarioDataMap.get("apellidos"));
            usuario.setEmail((String) usuarioDataMap.get("email"));
            usuario.setTelefono((String) usuarioDataMap.get("telefono"));
            usuario.setDireccion((String) usuarioDataMap.get("direccion"));
            usuario.setPassword((String) usuarioDataMap.get("password"));
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
            return ResponseEntity.status(400).body(response);
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
    public ResponseEntity<Map<String, String>> eliminarUsuario(@RequestBody Map<String, Object> data, @PathVariable int id) {
        try {
            Map<String, Object> tokenDataMap = (Map<String, Object>) data.get("tokenData");
            String token = (String) tokenDataMap.get("token");
            if (!CustomAuthSuccessHandler.isSessionIdValid(token)) {
                response.put("message", "Consulta no autorizada, token inválido.");
                return ResponseEntity.status(401).body(response);
            }
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                response.put("message", "Usuario no encontrado.");
                return ResponseEntity.status(404).body(response);
            }
            usuarioService.deleteById(id);
            response.put("message", "Usuario " + usuario.getNombre() + " eliminado exitosamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error al eliminar el usuario: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
