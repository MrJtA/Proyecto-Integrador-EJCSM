import java.io.Serializable;

public class Libro implements Serializable {
    
    private final int isbn;
    private final String titulo;
    private final String autor;
    private final String editorial;
    private final String genero;

    public Libro(int isbn, String titulo, String autor, String editorial, String genero) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.genero = genero;
    }

    public int getISBN() {
        return this.isbn;
    }

    public String getTitulo() {
        return this.titulo;
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
        return this.isbn + ", " + this.titulo + ", " + this.autor + ", " + this.editorial + ", " + this.genero;
    }

}