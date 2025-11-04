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

    public void escribirListaBinario() {
        try (FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Libro libro : this.biblioteca.values()) {
                oos.writeObject(libro);
            }
        } catch (IOException e) {
            System.err.println("Error al leer los libros del fichero: " + e.getMessage());
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
                String valorAtributo = String.valueOf(libro.getISBN());
                elemento.setAttribute("ISBN", valorAtributo);
                Element titulo = documento.createElement("titulo");
                Text textoTitulo = documento.createTextNode(libro.getTitulo());
                titulo.appendChild(textoTitulo);
                elemento.appendChild(titulo);
                Element autor = documento.createElement("autor");
                Text textoAutor = documento.createTextNode(libro.getAutor());
                autor.appendChild(textoAutor);
                elemento.appendChild(autor);
                Element editorial = documento.createElement("editorial");
                Text textoEditorial = documento.createTextNode(libro.getEditorial());
                editorial.appendChild(textoEditorial);
                elemento.appendChild(editorial);
                Element genero = documento.createElement("genero");
                Text textoGenero = documento.createTextNode(libro.getGenero());
                genero.appendChild(textoGenero);
                elemento.appendChild(genero);
                documento.getDocumentElement().appendChild(elemento);
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
    public void traspasarDatosFichero(File file) {
        String nombreFichero = file.getName().toLowerCase();
        if (nombreFichero.endsWith(".txt") || nombreFichero.endsWith("")) {
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
        try (Connection conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas","root", "root")) {
            Boolean existeDatabase = false;
            String query = "show databases like ?";
            try (PreparedStatement ps=conexion.prepareStatement(query)){
                ps.setString(1, nombreDatabase);
                ResultSet rs=ps.executeQuery();
                if (rs.next()){
                    existeDatabase = true;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            if (!existeDatabase) {
                String query2 = "create database " + nombreDatabase;
                try (PreparedStatement ps = conexion.prepareStatement(query2)) {
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
            try (Connection caux = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + nombreDatabase, "root", "root")) {
                String query3 = "create table if not exists libro (isbn int primary key, titulo varchar (100), autor varchar (100), editorial varchar (100), genero varchar (100))";
                PreparedStatement ps1 = caux.prepareStatement(query3);
                ps1.executeUpdate();
                ps1.close();
                String query4 = "TRUNCATE TABLE libro";
                PreparedStatement ps2 = caux.prepareStatement(query4);
                ps2.executeUpdate();
                ps2.close();
                for (Libro libro : this.biblioteca.values()) {
                    String query5="insert into pelicula (isbn,titulo,autor,editorial,genero) values (?,?,?,?,?)";
                    try (PreparedStatement ps3=caux.prepareStatement(query5)) {
                        ps3.setInt(1, libro.getISBN());
                        ps3.setString(2, libro.getTitulo());
                        ps3.setString(3, libro.getAutor());
                        ps3.setString(4, libro.getEditorial());
                        ps3.setString(5, libro.getGenero());
                        ps3.executeUpdate();
                        ps3.close();
                    } catch (SQLException e) {
                        System.err.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
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

}