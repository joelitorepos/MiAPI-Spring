package app.controller;

import app.dao.UsuarioDAO;
import app.model.Usuario;
import app.model.UsuarioConRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    public UsuarioController(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @GetMapping("/byusername")
    public ResponseEntity<Usuario> findByUsername(@RequestParam String username) {
        try {
            Usuario usuario = usuarioDAO.findByUsername(username);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Integer> crearUsuario(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String plainPassword = (String) request.get("plainPassword");
            String nombre = (String) request.get("nombre");
            int idRol = (int) request.get("idRol");
            int id = usuarioDAO.crearUsuario(username, plainPassword, nombre, idRol);
            if (id != -1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(id);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-2);
        }
    }

    @GetMapping("/conrol")
    public ResponseEntity<List<UsuarioConRol>> listarConRol() {
        try {
            List<UsuarioConRol> usuarios = usuarioDAO.listarConRol();
            return ResponseEntity.ok(usuarios);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable int id) {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Map<String, Object> request) {
        try {
            Usuario u = (Usuario) request.get("usuario");
            u.setId(id); // Asegurar que el ID coincida
            String newPlainPassword = (String) request.get("newPlainPassword");
            boolean actualizado = usuarioDAO.actualizar(u, newPlainPassword);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> validarLogin(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            Usuario usuario = usuarioDAO.validarLogin(username, password);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Integer> eliminarUsuario(@RequestParam String username) {
        try {
            int rowsAffected = usuarioDAO.eliminarUsuario(username);
            return ResponseEntity.ok(rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        try {
            List<Usuario> usuarios = usuarioDAO.listar();
            return ResponseEntity.ok(usuarios);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}