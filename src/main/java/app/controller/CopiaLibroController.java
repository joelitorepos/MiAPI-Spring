package app.controller;

import app.dao.CopiaLibroDAO;
import app.model.CopiaLibro;
import app.model.CopiaLibroConLibro;
import app.model.CopiaLibroConDetalles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/copialibro")
public class CopiaLibroController {

    private final CopiaLibroDAO copiaLibroDAO;

    @Autowired
    public CopiaLibroController(CopiaLibroDAO copiaLibroDAO) {
        this.copiaLibroDAO = copiaLibroDAO;
    }

    @GetMapping("/porlibro")
    public ResponseEntity<List<CopiaLibro>> listarPorLibro(@RequestParam int idLibro) {
        try {
            List<CopiaLibro> copias = copiaLibroDAO.listarPorLibro(idLibro);
            return ResponseEntity.ok(copias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/disponiblesporlibro")
    public ResponseEntity<List<CopiaLibro>> listarDisponiblesPorLibro(@RequestParam int idLibro) {
        try {
            List<CopiaLibro> copias = copiaLibroDAO.listarDisponiblesPorLibro(idLibro);
            return ResponseEntity.ok(copias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/disponible/{idCopiaLibro}")
    public ResponseEntity<Boolean> isDisponible(@PathVariable int idCopiaLibro) {
        try {
            boolean disponible = copiaLibroDAO.isDisponible(idCopiaLibro);
            return ResponseEntity.ok(disponible);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody CopiaLibro c) {
        try {
            int id = copiaLibroDAO.insertar(c);
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
    public ResponseEntity<List<CopiaLibro>> listar() {
        try {
            List<CopiaLibro> copias = copiaLibroDAO.listar();
            return ResponseEntity.ok(copias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopiaLibro> buscarPorId(@PathVariable int id) {
        try {
            CopiaLibro copia = copiaLibroDAO.buscarPorId(id);
            if (copia != null) {
                return ResponseEntity.ok(copia);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody CopiaLibro c) {
        try {
            c.setId(id); // Asegurar que el ID coincida
            boolean actualizado = copiaLibroDAO.actualizar(c);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/conlibro")
    public ResponseEntity<List<CopiaLibroConLibro>> listarConLibro() {
        try {
            List<CopiaLibroConLibro> copias = copiaLibroDAO.listarConLibro();
            return ResponseEntity.ok(copias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inventariocondetalles")
    public ResponseEntity<List<CopiaLibroConDetalles>> listarInventarioConDetalles() {
        try {
            List<CopiaLibroConDetalles> copias = copiaLibroDAO.listarInventarioConDetalles();
            return ResponseEntity.ok(copias);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}