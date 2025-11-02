package app.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import java.awt.image.BufferedImage;
import java.util.UUID; // Para la generación del código de inventario
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BarCodeManager {

    /**
     * Genera un identificador único (codInventario) basado en UUID y lo formatea.
     * Este valor es el que se guarda en la base de datos (NVARCHAR).
     * @return String alfanumérico de 16 caracteres.
     */
    public static String generateCodInventario() {
        // Genera un UUID y lo limpia para obtener un código alfanumérico único.
        // Se trunca a 16 caracteres para ser legible en un código de barras.
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    /**
     * Convierte una cadena de texto a una imagen de Código de Barras (Code 128).
     * @param data El codInventario (String) a codificar.
     * @param width El ancho deseado de la imagen.
     * @param height La altura deseada de la imagen.
     * @return BufferedImage que se puede mostrar en Swing (JLabel/JPanel).
     * @throws WriterException Si hay un error en la codificación.
     */
    public static BufferedImage generateCode128BarcodeImage(String data, int width, int height) throws WriterException {
        // Code 128 es el formato estándar para inventario y logística.
        Code128Writer barcodeWriter = new Code128Writer();

        // Codifica el texto en una matriz de bits
        BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.CODE_128, width, height);

        // Convierte la matriz de bits a una imagen (BufferedImage)
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}