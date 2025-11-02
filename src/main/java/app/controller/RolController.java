package app.controller;

import app.dao.RolDAO;
import app.model.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/rol")
public class RolController {

    private final RolDAO rolDAO;

    @Autowired
    public RolController(RolDAO rolDAO) {
        this.rolDAO = rolDAO;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> listar() {
        try {
            List<Rol> roles = rolDAO.listar();
            return ResponseEntity.ok(roles);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Rol rol) {
        try {
            int id = rolDAO.insertar(rol);
            if (id != -1) {
                return ResponseEntity.status(HttpStatus.CREATED).body(id);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}