import java.io.*;
import java.util.*;

public final class Binario extends Fichero implements Serializable {

    public Binario(File file) {
        super(file);
        this.biblioteca = leerFichero();
    }

    @Override
    public Map<Integer, Libro> leerFichero() {
        Map<Integer, Libro> aux = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(this.file);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
            try {
                Libro libro = (Libro) ois.readObject();
                aux.put(libro.getISBN(), libro);
            } catch (EOFException e) {
                break;
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
        super.escribirListaBinario();
    }

}