import java.io.*;
import java.sql.*;
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

    @Override
    public abstract Map<Integer, Libro> leerFichero();

    public abstract void escribirLista();

    public void escribirListaTexto() {
        try (FileWriter fw = new FileWriter(this.file);
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
            System.err.println("Error al escribir los libros del fichero: " + e.getMessage());
        }
    }

    public void escribirListaBinario() {
        try (FileOutputStream fos = new FileOutputStream(this.file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Libro libro : this.biblioteca.values()) {
                oos.writeObject(libro);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir los libros del fichero: " + e.getMessage());
        }
    }

    public void escribirListaXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document documento = implementation.createDocument(null, "biblioteca", null);
            documento.setXmlVersion("1.0");
            for (Libro libro : this.biblioteca.values()) {
                Element elemento = documento.createElement("libro");
                elemento.setAttribute("ISBN", String.valueOf(libro.getISBN())); 
                elemento.appendChild(documento.createElement("titulo")).setTextContent(libro.getTitulo());
                elemento.appendChild(documento.createElement("autor")).setTextContent(libro.getAutor());
                elemento.appendChild(documento.createElement("editorial")).setTextContent(libro.getEditorial());
                elemento.appendChild(documento.createElement("genero")).setTextContent(libro.getGenero());
                documento.getDocumentElement().appendChild(elemento);
                }
            Source source = new DOMSource(documento);
            Result result = new StreamResult(this.file);
            Transformer transformer = newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); 
            transformer.transform(source, result);
        } catch (ParserConfigurationException | DOMException e) {
            System.err.println("Error al escribir los libros del fichero: " + e.getMessage());
        } catch (TransformerConfigurationException ex) {
            System.err.println("Error de configuración del transformador XML: " + ex.getMessage());
        } catch (TransformerException ex) {
            System.err.println("Error durante la transformación o escritura del XML: " + ex.getMessage());
        }
    }

    /*
    public void escribirListaXML() {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document documento = saxBuilder.build(this.file);
            Element raiz = documento.getRootElement();
            raiz.removeContent();
            for (Libro l : this.biblioteca.values()) {
                Element elemento = new Element("libro");
                elemento.setAttribute("ISBN", String.valueOf(l.getISBN()));
                elemento.addContent(new Element("titulo").setText(d.getTitulo()));
                elemento.addContent(new Element("autor").setText(d.getAutor()));
                elemento.addContent(new Element("editorial").setText(d.getEditorial()));
                elemento.addContent(new Element("genero").setText(d.getGenero()));
                raiz.addContent(elemento);
            }
            XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
            xmlOutput.output(documento, new FileOutputStream(this.file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }
     */

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
    public void traspasarDatosFichero(File file) {
        String nombreFichero = file.getName().toLowerCase();
        if (nombreFichero.endsWith(".txt")) {
            escribirListaTexto();
        } else if (nombreFichero.endsWith(".bin")) {
            escribirListaBinario();
        } else if (nombreFichero.endsWith(".xml")) {
            escribirListaXML();
        } else {
            System.out.println("Error: Traspaso de datos a ficheros de la extensión de " + nombreFichero + " no disponible.");
            System.out.println("Por favor, introduzca un fichero de texto, binario o xml.");
            return;
        }
        System.out.println("Se han traspasado los datos y creado una copia de seguridad en el fichero '" + file.getAbsolutePath() + "' correctamente.");
    }

    @Override
    public void traspasarDatosDatabase(String nombreDatabase) {
        try (Connection caux = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nombreDatabase, "root", "root")) {
            String query3 = "CREATE TABLE IF NOT EXISTS libro (isbn INT PRIMARY KEY, titulo VARCHAR (100), autor VARCHAR (100), editorial VARCHAR (100), genero VARCHAR (100))";
            try (PreparedStatement ps1 = caux.prepareStatement(query3)) {
                ps1.executeUpdate();
            } catch (SQLException e) {
                 System.err.println("Error al crear o verificar la tabla 'libro': " + e.getMessage());
            }
            String query4 = "TRUNCATE TABLE libro";
            try (PreparedStatement ps2 = caux.prepareStatement(query4)) {
                ps2.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error al truncar la tabla 'libro': " + e.getMessage());
            }
            String query5 = "INSERT INTO libro (isbn, titulo, autor, editorial, genero) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps3 = caux.prepareStatement(query5)) {
                for (Libro libro : this.biblioteca.values()) {
                    ps3.setInt(1, libro.getISBN());
                    ps3.setString(2, libro.getTitulo());
                    ps3.setString(3, libro.getAutor());
                    ps3.setString(4, libro.getEditorial());
                    ps3.setString(5, libro.getGenero());
                    ps3.executeUpdate();
                }
                System.out.println("Se han traspasado los datos y creado una copia de seguridad en la base de datos '" + nombreDatabase + "' correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al insertar un libro: " + e.getMessage());
            }
            
        } catch (SQLException e) {
            System.err.println("Error al establecer conexión con la base de datos '" + nombreDatabase + "'" + e.getMessage());
        }
    }

}