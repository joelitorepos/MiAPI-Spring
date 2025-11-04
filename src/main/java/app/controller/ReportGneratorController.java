package app.controller;

import app.dao.*;
import app.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportGeneratorController {

    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;
    private final MultaDAO multaDAO;
    private final ClienteDAO clienteDAO;
    private final CopiaLibroDAO copiaLibroDAO;

    @Autowired
    public ReportGeneratorController(LibroDAO libroDAO, PrestamoDAO prestamoDAO, MultaDAO multaDAO,
                                     ClienteDAO clienteDAO, CopiaLibroDAO copiaLibroDAO) {
        this.libroDAO = libroDAO;
        this.prestamoDAO = prestamoDAO;
        this.multaDAO = multaDAO;
        this.clienteDAO = clienteDAO;
        this.copiaLibroDAO = copiaLibroDAO;
    }

    // Método auxiliar para generar PDF en memoria
    private byte[] generatePDF(String title, PdfPTable table) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        document.add(new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    @GetMapping("/catalogo-pdf")
    public ResponseEntity<byte[]> getCatalogoPDF() {
        try {
            List<LibroConAutorYCategoria> libros = libroDAO.listarConAutorYCategoria();
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Autor");
            table.addCell("Categoría");
            table.addCell("Año");
            for (LibroConAutorYCategoria l : libros) {
                table.addCell(String.valueOf(l.getId()));
                table.addCell(l.getLibroNombre());
                table.addCell(l.getAutorNombre());
                table.addCell(l.getCategoriaNombre());
                table.addCell(String.valueOf(l.getAnio()));
            }
            byte[] pdfBytes = generatePDF("Reporte de Catálogo de Libros", table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=catalogo.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/prestamos-pdf")
    public ResponseEntity<byte[]> getPrestamosPDF(@RequestParam String start, @RequestParam String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            List<PrestamoConDetalles> prestamos = prestamoDAO.listarPorPeriodo(startDate, endDate);
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID");
            table.addCell("Cliente");
            table.addCell("Libro");
            table.addCell("Fecha Préstamo");
            table.addCell("Estado");
            for (PrestamoConDetalles p : prestamos) {
                table.addCell(String.valueOf(p.getId()));
                table.addCell(p.getClienteNombre());
                table.addCell(p.getLibroNombre());
                table.addCell(p.getFechaPrestamo().toString());
                table.addCell(p.getEstado());
            }
            byte[] pdfBytes = generatePDF("Reporte de Préstamos por Período", table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prestamos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (SQLException | DocumentException | ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/top-libros-pdf")
    public ResponseEntity<byte[]> getTopLibrosPDF(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<TopLibro> topLibros = libroDAO.getTopLibrosPrestados(limit);
            PdfPTable table = new PdfPTable(3);
            table.addCell("Nombre");
            table.addCell("Autor");
            table.addCell("Préstamos");
            for (TopLibro l : topLibros) {
                table.addCell(l.getNombre());
                table.addCell(l.getAutorNombre());
                table.addCell(String.valueOf(l.getConteoPrestamos()));
            }
            byte[] pdfBytes = generatePDF("Reporte de Top Libros Prestados", table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=top-libros.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/multas-recaudadas-pdf")
    public ResponseEntity<byte[]> getMultasRecaudadasPDF(@RequestParam String start, @RequestParam String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            BigDecimal total = multaDAO.calcularTotalRecaudado(startDate, endDate);
            List<MultaConDetalles> multas = multaDAO.listarPagadasPorPeriodo(startDate, endDate);
            PdfPTable table = new PdfPTable(4);
            table.addCell("ID");
            table.addCell("Cliente");
            table.addCell("Monto");
            table.addCell("Fecha Pago");
            for (MultaConDetalles m : multas) {
                table.addCell(String.valueOf(m.getId()));
                table.addCell(m.getClienteNombre());
                table.addCell(String.valueOf(m.getMonto()));
                table.addCell(m.getFechaPago().toString());
            }
            byte[] pdfBytes = generatePDF("Reporte de Multas Recaudadas (Total: " + total + ")", table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=multas-recaudadas.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (SQLException | DocumentException | ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/clientes-morosos-pdf")
    public ResponseEntity<byte[]> getClientesMorososPDF() {
        try {
            List<ClienteConMultas> morosos = clienteDAO.listarConMultasPendientes();
            PdfPTable table = new PdfPTable(4);
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Multas Pendientes");
            table.addCell("Total Adeudado");
            for (ClienteConMultas c : morosos) {
                table.addCell(String.valueOf(c.getId()));
                table.addCell(c.getNombre());
                table.addCell(String.valueOf(c.getConteoMultasPendientes()));
                table.addCell(String.valueOf(c.getTotalAdeudado()));
            }
            byte[] pdfBytes = generatePDF("Reporte de Clientes Morosos", table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clientes-morosos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inventario-pdf")
    public ResponseEntity<byte[]> getInventarioPDF() {
        try {
            List<CopiaLibroConDetalles> copias = copiaLibroDAO.listarInventarioConDetalles();
            PdfPTable table = new PdfPTable(5);
            table.addCell("ID Copia");
            table.addCell("Libro");
            table.addCell("Sala");
            table.addCell("Estante");
            table.addCell("Estado");
            for (CopiaLibroConDetalles cp : copias) {
                table.addCell(String.valueOf(cp.getId()));
                table.addCell(cp.getLibroNombre());
                table.addCell(cp.getSala());
                table.addCell(cp.getEstante());
                table.addCell(cp.getEstadoCopia());
            }
            byte[] pdfBytes = generatePDF("Reporte de Inventario por Ubicación", table);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventario.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (SQLException | DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}