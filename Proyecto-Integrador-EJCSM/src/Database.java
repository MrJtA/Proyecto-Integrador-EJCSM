import java.io.*;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import static javax.xml.transform.TransformerFactory.newInstance;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public final class Database implements Funcionalidades {
    
    private Connection conexion;
    private String nombreDatabase;
    private String url;
    private String usuario;
    private String contraseña;
    private final Map<Integer, Libro> biblioteca;
    
    public Database(String nombreDatabase) throws SQLException {
        this.url = "jdbc:mysql://localhost:3306/";
        this.nombreDatabase = nombreDatabase;
        this.usuario = "root";
        this.contraseña = "root";
        try {
            crearDatabase(this.nombreDatabase);
            this.conexion = DriverManager.getConnection(this.url + this.nombreDatabase, this.usuario, this.contraseña);
            System.out.println("Conexión principal a la BD '" + nombreDatabase + "' establecida.");
        } catch (SQLException e) {
            System.err.println("Error al establecer conexión inicial con la base de datos: " + e.getMessage());
            throw e;
        }
        this.biblioteca = leerFichero();
    }

    private void crearDatabase(String nombreDatabase) throws SQLException {
        try (Connection caux = DriverManager.getConnection(this.url, this.usuario, this.contraseña)) {
            boolean existeDatabase = false;
            String queryVerificar = "SHOW SCHEMAS LIKE ?"; 
            try (PreparedStatement ps = caux.prepareStatement(queryVerificar)) {
                ps.setString(1, nombreDatabase);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existeDatabase = true;
                    }
                }
            }
            if (!existeDatabase) {
                String queryCrearBD = "CREATE DATABASE " + nombreDatabase;
                try (Statement stmt = caux.createStatement()) {
                    stmt.executeUpdate(queryCrearBD);
                    System.out.println("Base de datos '" + nombreDatabase + "' creada con éxito.");
                }
            }
            String queryCrearTabla = "CREATE TABLE IF NOT EXISTS " + nombreDatabase + ".libro(isbn int primary key, titulo varchar (100), autor varchar (100), editorial varchar (100), genero varchar (100))";
            try (Statement stmt = caux.createStatement()) {
                stmt.executeUpdate(queryCrearTabla);
                System.out.println("Tabla 'libro' verificada/creada en '" + nombreDatabase + "'.");
            }
        }
    }
    
    @Override
    public Map<Integer, Libro> leerFichero() {
        Map<Integer, Libro> aux = new HashMap<>();
        String query = "SELECT * FROM libro";
        try (Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                int isbn = rs.getInt("isbn");
                String nombre = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                String genero = rs.getString("genero");
                Libro libro = new Libro(isbn, nombre, autor, editorial, genero);
                aux.put(isbn, libro);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return aux;
    }

    @Override
    public void insertar(Libro libro) {
        String query = "INSERT INTO libro (isbn, titulo, autor, editorial, genero) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = this.conexion.prepareStatement(query)) {
            ps.setInt(1, libro.getISBN());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getEditorial());
            ps.setString(5, libro.getGenero());
            int filasInsertadas = ps.executeUpdate();
            System.out.println("Filas insertadas: " + filasInsertadas);
            this.biblioteca.put(libro.getISBN(), libro);
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
        }
    }
    
    @Override
    public void borrar(int isbn) {
        String query = "DELETE FROM libro WHERE isbn = ?";
        try (PreparedStatement ps = this.conexion.prepareStatement(query)) {
            ps.setInt(1, isbn);
            int filasEliminadas = ps.executeUpdate();
            System.out.println("Filas eliminadas: " + filasEliminadas);
            this.biblioteca.remove(isbn);
        } catch (SQLException e) {
            System.err.println("Error al borrar libro: " + e.getMessage());
        }
    }

    @Override
    public void modificar(int isbnAntiguo, Libro libroNuevo) {
        String query = "UPDATE libro SET isbn = ?, titulo = ?, autor = ?, editorial = ?, genero = ? WHERE isbn = ?";
        try (PreparedStatement ps = this.conexion.prepareStatement(query)) {
            ps.setInt(1, libroNuevo.getISBN());
            ps.setString(2, libroNuevo.getTitulo());
            ps.setString(3, libroNuevo.getAutor());
            ps.setString(4, libroNuevo.getEditorial());
            ps.setString(5, libroNuevo.getGenero());
            ps.setInt(6, isbnAntiguo); 
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                this.biblioteca.remove(isbnAntiguo);
                this.biblioteca.put(libroNuevo.getISBN(), libroNuevo);
            }
            System.out.println("Filas modificadas: " + filasAfectadas);
        } catch (SQLException e){
            System.err.println("Error al modificar libro: " + e.getMessage());
        }
    }
    
    /*

    @Override
    public void escribirLista() {
        try {
            String query = "UPDATE libro SET isbn = ?, titulo = ?, autor = ?, editorial = ?, genero = ?";
            PreparedStatement ps = this.conexion.prepareStatement(query);
            for (Libro libro : this.biblioteca.values()) {
                ps.setInt(1, libro.getISBN());
                ps.setString(2, libro.getTitulo());
                ps.setString(3, libro.getAutor());
                ps.setString(4, libro.getEditorial());
                ps.setString(5, libro.getGenero());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void buscar(int isbn) {
        try {
            String query = "SELECT FROM libro WHERE isbn = " + isbn;
            System.out.println(query);
            Statement st = conexion.createStatement();
            int isOk = st.executeUpdate(query);
            System.out.println(isOk);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void mostrar() {
        String query = "SELECT * FROM libro";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            System.out.println(query);
            System.out.println(ps.executeUpdate());
        } catch (SQLException e) {
            System.err.println(e.getMessage());

        }
    }

    */

    @Override
    public void traspasarDatosFichero(File file) {
        String nombreFichero = file.getName().toLowerCase();
        if (nombreFichero.endsWith(".txt") || nombreFichero.endsWith("")) {
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
                System.err.println("Error al escribir los libros del fichero: " + e.getMessage());
            }
        } else if (nombreFichero.endsWith(".bin")) {
            try (FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                for (Libro libro : this.biblioteca.values()) {
                    oos.writeObject(libro);
                }
            } catch (IOException e) {
                System.err.println("Error al escribir los libros del fichero: " + e.getMessage());
            }
        } else if (nombreFichero.endsWith(".xml")) {
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
                Result result = new StreamResult(file);
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
        } else {
            System.out.println("Error: Traspaso de datos a ficheros de la extensión de " + nombreFichero + " no disponible.");
            System.out.println("Por favor, introduzca un fichero de texto, binario o xml.");
            return;
        }
        System.out.println("Se han traspasado los datos y creado una copia de seguridad en el fichero '" + file.getName() + "' correctamente.");
    }

    @Override
    public void traspasarDatosDatabase(String nombreDatabase) {
        try {
            crearDatabase(nombreDatabase);
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos" + e.getMessage());
            return;
        }
        try (Connection caux = DriverManager.getConnection(this.url + nombreDatabase, this.usuario, this.contraseña)) {
            String queryTruncar = "TRUNCATE TABLE libro";
            try (Statement st = caux.createStatement()) { 
                st.executeUpdate(queryTruncar);
                System.out.println("Tabla 'libro' truncada (datos anteriores eliminados).");
            } catch (SQLException e) {
                System.err.println("Error al truncar la tabla 'libro': " + e.getMessage());
                return; 
            }
            for (Libro libro : this.biblioteca.values()) {
                String queryInsertar = "INSERT INTO libro (isbn,titulo,autor,editorial,genero) values (?,?,?,?,?)";
                try (PreparedStatement ps = caux.prepareStatement(queryInsertar)) {
                    ps.setInt(1, libro.getISBN());
                    ps.setString(2, libro.getTitulo());
                    ps.setString(3, libro.getAutor());
                    ps.setString(4, libro.getEditorial());
                    ps.setString(5, libro.getGenero());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

}