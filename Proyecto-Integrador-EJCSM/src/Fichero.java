import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import static javax.xml.transform.TransformerFactory.newInstance;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public abstract class Fichero implements Funcionalidades {
    
    protected final File file;
    protected Map<Integer, Libro> biblioteca;

    public Fichero(File file) {
        this.file = file;
        this.biblioteca = new HashMap<>();
    }

    public Fichero() {
        this.file = null;
    }

    @Override
    public abstract Map<Integer, Libro> leerFichero();

    @Override
    public abstract void escribirLista();
    
    @Override
    public void escribirListaTexto(File file) {
        file = this.file;
        try (FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw)) {
            boolean comienza = true;
            for (Libro libro : this.biblioteca.values()) {
                if (!comienza) {
                    bw.newLine();
                }
                bw.write(libro.toString());
                comienza = false;
            }
        } catch (IOException e) {
            System.err.println("Error al leer los libros del fichero: " + e.getMessage());
        }
    }

    @Override
    public void escribirListaBinario(File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Libro libro : this.biblioteca.values()) {
                oos.writeObject(libro);
            }
        } catch (IOException e) {
            System.err.println("Error al leer los libros del fichero: " + e.getMessage());
        }
    }

    @Override
    public void escribirListaXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document documento = implementation.createDocument(null, "biblioteca", null);
            documento.setXmlVersion("1.0");
            for (int isbn : this.biblioteca.keySet()) {
                Element libro = documento.createElement("libro");
                String valorAtributo = String.valueOf(isbn);
                libro.setAttribute("ISBN", valorAtributo);
                Libro libroenCuestion = this.biblioteca.get(isbn);
                Element titulo = documento.createElement("titulo");
                Text textoTitulo = documento.createTextNode(libroenCuestion.getNombre());
                titulo.appendChild(textoTitulo);
                libro.appendChild(titulo);
                Element autor = documento.createElement("autor");
                Text textoAutor = documento.createTextNode(libroenCuestion.getAutor());
                autor.appendChild(textoAutor);
                libro.appendChild(autor);
                Element editorial = documento.createElement("editorial");
                Text textoEditorial = documento.createTextNode(libroenCuestion.getEditorial());
                editorial.appendChild(textoEditorial);
                libro.appendChild(editorial);
                Element genero = documento.createElement("genero");
                Text textoGenero = documento.createTextNode(libroenCuestion.getGenero());
                genero.appendChild(textoGenero);
                libro.appendChild(genero);
                documento.getDocumentElement().appendChild(libro);
            }
            Source source = new DOMSource(documento);
            Result result = new StreamResult(file);
            Transformer transformer = newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); 
            transformer.transform(source, result);
        } catch (ParserConfigurationException | DOMException e) {
            System.err.println("Error al leer los libros del fichero: " + e.getMessage());
        } catch (TransformerConfigurationException ex) {
            System.err.println("Error de configuración del transformador XML: " + ex.getMessage());
        } catch (TransformerException ex) {
            System.err.println("Error durante la transformación o escritura del XML: " + ex.getMessage());
        }
    }

    @Override
    public void traspasarDatos(File file) {
        String nombreFichero = file.getName().toLowerCase();
        if (nombreFichero.endsWith(".txt") || nombreFichero.endsWith("")) {
            escribirListaTexto(file);
        } else if (nombreFichero.endsWith(".bin")) {
            escribirListaBinario(file);
        } else if (nombreFichero.endsWith(".xml")) {
            escribirListaXML(file);
        } else {
            System.out.println("Error: Traspaso de datos a ficheros de la extensión de " + nombreFichero + " no disponible.");
            System.out.println("Por favor, introduzca un fichero de texto, binario o xml.");
            return;
        }
        System.out.println("Se ha creado una copia de seguridad en el fichero '" + file.getAbsolutePath() + "' correctamente.");
    }

    @Override
    public void buscar(int isbn) {
        if (this.biblioteca.isEmpty()) {
            System.out.println("Error: No hay ningún libro registrado.");
        } else {
            if (this.biblioteca.containsKey(isbn)) {
                System.out.println("Se ha encontrado el libro: " + this.biblioteca.get(isbn));
            } else {
                System.out.println("Error: No se ha encontrado el libro.");
            }
        }
    }

    @Override
    public void insertar(Libro libro) {
        if (this.biblioteca.containsKey(libro.getISBN())) {
            System.out.println("Error: Ya existe un libro con el mismo isbn: " + libro.getISBN());
        } else {
            this.biblioteca.put(libro.getISBN(), libro);
            System.out.println("El libro con el isbn: " + libro.getISBN() + ", se ha registrado correctamente.");
            escribirLista();
        }
    }

    @Override
    public void borrar(int isbn) {
        if (this.biblioteca.isEmpty() || !this.biblioteca.containsKey(isbn)) {
            System.out.println("Error: No existe ningún libro con el isbn: " + isbn);
        } else {
            this.biblioteca.remove(isbn);
            System.out.println("El libro con el isbn: " + isbn + ", se ha borrado correctamente.");
            escribirLista();
        }
    }

    @Override
    public void modificar(int isbn, Libro libro) {
        if (!this.biblioteca.containsKey(isbn)) {
            System.out.println("Error: No existe ningún libro para borrar con el isbn: " + isbn);
        } else if (this.biblioteca.containsKey(libro.getISBN())) {
            System.out.println("Error: Ya existe un libro con el mismo isbn: " + libro.getISBN());
        } else {
            this.biblioteca.replace(isbn, libro);
            System.out.println("El libro con el isbn: " + isbn + ", se ha sustituido correctamente por el libro con el isbn: " + libro.getISBN());
            escribirLista();
        }
    }

    @Override
    public void mostrar() {
        if (!this.biblioteca.isEmpty()) {
            System.out.println("Error: No hay ningún libro registrado.");
        } else {
            System.out.println("Libros registrados en " + this.file + " : ");
            for (Libro libro : this.biblioteca.values()) {
                System.out.println(libro.toString());
            }
        }
    }

}