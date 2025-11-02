package app.controller;

import app.dao.ParametroDAO;
import app.model.Parametro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/parametro")
public class ParametroController {

    private final ParametroDAO parametroDAO;

    @Autowired
    public ParametroController(ParametroDAO parametroDAO) {
        this.parametroDAO = parametroDAO;
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Parametro p) {
        try {
            int id = parametroDAO.insertar(p);
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
    public ResponseEntity<List<Parametro>> listar() {
        try {
            List<Parametro> parametros = parametroDAO.listar();
            return ResponseEntity.ok(parametros);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parametro> buscarPorId(@PathVariable int id) {
        try {
            Parametro parametro = parametroDAO.buscarPorId(id);
            if (parametro != null) {
                return ResponseEntity.ok(parametro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/porclave")
    public ResponseEntity<Parametro> buscarPorClave(@RequestParam String clave) {
        try {
            Parametro parametro = parametroDAO.buscarPorClave(clave);
            if (parametro != null) {
                return ResponseEntity.ok(parametro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/valor")
    public ResponseEntity<String> getValor(@RequestParam String clave) {
        try {
            String valor = parametroDAO.getValor(clave);
            if (valor != null) {
                return ResponseEntity.ok(valor);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Parametro p) {
        try {
            p.setId(id); // Asegurar que el ID coincida
            boolean actualizado = parametroDAO.actualizar(p);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}