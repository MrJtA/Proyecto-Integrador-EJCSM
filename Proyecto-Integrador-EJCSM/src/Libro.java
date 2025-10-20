import java.io.Serializable;

public class Libro implements Serializable {
    
    private final int isbn;
    private final String nombre;
    private final String autor;
    private final String editorial;
    private final String genero;

    public Libro(int isbn, String nombre, String autor, String editorial, String genero) {
        this.isbn = isbn;
        this.nombre = nombre;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
    }

    public int getISBN() {
        return this.isbn;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getAutor() {
        return this.autor;
    }

    public String getEditorial() {
        return this.editorial;
    }

    public String getGenero() {
        return this.genero;
    }

    @Override
    public String toString() {
        return this.isbn + ", " + this.nombre + ", " + this.autor + ", " + this.editorial + ", " + this.genero + ";";
    }

}