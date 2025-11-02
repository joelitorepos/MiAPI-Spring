package app.controller;

import app.dao.AutorDAO;
import app.model.Autor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/autor")
public class AutorController {

    private final AutorDAO autorDAO;

    @Autowired
    public AutorController(AutorDAO autorDAO) {
        this.autorDAO = autorDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Autor a) {
        try {
            int id = autorDAO.insertar(a);
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

    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        try {
            List<Autor> autores = autorDAO.listar();
            return ResponseEntity.ok(autores);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable int id) {
        try {
            Autor autor = autorDAO.buscarPorId(id);
            if (autor != null) {
                return ResponseEntity.ok(autor);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Autor a) {
        try {
            a.setId(id); // Asegurar que el ID coincida
            boolean actualizado = autorDAO.actualizar(a);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}