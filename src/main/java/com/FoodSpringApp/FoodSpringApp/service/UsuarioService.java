package com.FoodSpringApp.FoodSpringApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.FoodSpringApp.FoodSpringApp.model.Usuario;
import com.FoodSpringApp.FoodSpringApp.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByDni(dni);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con DNI: " + dni);
        }
        return org.springframework.security.core.userdetails.User.builder()
            .username(usuario.getDni())
            .password(usuario.getPassword())
            .roles(usuario.getRole())
            .build();
    }

    public Usuario save(Usuario usuario) {
        String encryptedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encryptedPassword);
        System.out.println("Guardando el usuario: " + usuario);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario update(int id, Usuario usuarioData) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);
        if (usuarioExistente == null) {
            return null;
        }
        if (usuarioData.getPassword() != null && !usuarioData.getPassword().isEmpty()) {
            String nuevaContrasena = usuarioData.getPassword();
            usuarioExistente.setPassword(passwordEncoder.encode(nuevaContrasena));
        }
        if (usuarioData.getDni() != null && !usuarioData.getDni().isEmpty()) {
            usuarioExistente.setDni(usuarioData.getDni());
        }
        if (usuarioData.getRole() != null && !usuarioData.getRole().isEmpty()) {
            usuarioExistente.setRole(usuarioData.getRole());
        }
        usuarioExistente.setNombre(usuarioData.getNombre());
        usuarioExistente.setApellidos(usuarioData.getApellidos());
        usuarioExistente.setEmail(usuarioData.getEmail());
        usuarioExistente.setTelefono(usuarioData.getTelefono());
        usuarioExistente.setDireccion(usuarioData.getDireccion());
        return usuarioRepository.save(usuarioExistente);
    }

    public void deleteById(int id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario findById(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario obtenerUsuarioPorDni(String dni) {
        return usuarioRepository.findByDni(dni);
    }
}
