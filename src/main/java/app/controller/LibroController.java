package app.controller;

import app.dao.LibroDAO;
import app.model.Libro;
import app.model.LibroConAutor;
import app.model.LibroConAutorYCategoria;
import app.model.TopLibro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/libro")
public class LibroController {

    private final LibroDAO libroDAO;

    @Autowired
    public LibroController(LibroDAO libroDAO) {
        this.libroDAO = libroDAO;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        try {
            List<Libro> libros = libroDAO.listar();
            return ResponseEntity.ok(libros);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Integer> insertar(@RequestBody Libro l) {
        try {
            int id = libroDAO.insertar(l);
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

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> actualizar(@PathVariable int id, @RequestBody Libro l) {
        try {
            l.setId(id); // Asegurar que el ID coincida
            boolean actualizado = libroDAO.actualizar(l);
            return ResponseEntity.ok(actualizado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable int id) {
        try {
            Libro libro = libroDAO.buscarPorId(id);
            if (libro != null) {
                return ResponseEntity.ok(libro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/conautor")
    public ResponseEntity<List<LibroConAutor>> listarConAutor() {
        try {
            List<LibroConAutor> libros = libroDAO.listarConAutor();
            return ResponseEntity.ok(libros);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/confiltros")
    public ResponseEntity<List<LibroConAutorYCategoria>> buscarConFiltros(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false, defaultValue = "0") int anioInicio,
            @RequestParam(required = false, defaultValue = "0") int anioFin,
            @RequestParam(required = false, defaultValue = "false") boolean soloDisponibles) {
        try {
            List<LibroConAutorYCategoria> libros = libroDAO.buscarConFiltros(busqueda, filtro, anioInicio, anioFin, soloDisponibles);
            return ResponseEntity.ok(libros);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/topprestados")
    public ResponseEntity<List<TopLibro>> getTopLibrosPrestados(@RequestParam int limite) {
        try {
            List<TopLibro> topLibros = libroDAO.getTopLibrosPrestados(limite);
            return ResponseEntity.ok(topLibros);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}