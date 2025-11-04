import java.io.*;
import java.util.*;

public final class Texto extends Fichero {

    public Texto(File file) {
        super(file);
        this.biblioteca = leerFichero();
    }

    @Override
    public Map<Integer, Libro> leerFichero() {
        Map<Integer, Libro> aux = new HashMap<>();
        try (FileReader fr = new FileReader(this.file);
             BufferedReader br = new BufferedReader(fr)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] s = linea.split(", "); 
                int isbn = Integer.parseInt(s[0]);
                Libro libro = new Libro(isbn, s[1], s[2], s[3], s[4]);
                if (!aux.containsKey(isbn)) {
                    aux.put(isbn, libro);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer/parsear los libros del fichero: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al leer los libros del fichero: " + e.getMessage());
        }
        return aux;
    }

    @Override
    public void escribirLista() {
        super.escribirListaTexto();
    }

}