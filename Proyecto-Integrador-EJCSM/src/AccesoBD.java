import java.sql.*;
import java.util.*;

public class AccesoBD {
    
    Connection conn;
    private Map<Integer, Libro> biblioteca;

    public AccesoBD() {
        String driver = "com.mysql.cj.jdbc.Driver";
        String database = "bdpruebas";
        String hostname = "localhost";
        String port = "3306";
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
        String username = "root";
        String password = "root";
        try {
            Class.forName(driver);
            System.out.println(url);
            conn = DriverManager.getConnection(url, username, password);
            this.biblioteca = leerFichero();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public Map<Integer, Libro> leerFichero() {
        Map<Integer, Libro> aux = new HashMap<>();
        String query = "SELECT * FROM ELEMENTOS";
        try (Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                int isbn = rs.getInt("ISBN");
                String nombre = rs.getString("Nombre");
                String autor = rs.getString("Autor");
                String editorial = rs.getString("Editorial");
                String genero = rs.getString("Género");
                Libro libro = new Libro(isbn, nombre, autor, editorial, genero);
                aux.put(isbn, libro);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return aux;
    }

    /*
    public void escribirLista() {
        final String updateQuery = 
        "UPDATE ELEMENTOS SET ISBN = ?, Nombre = ?, Autor = ?, Editorial = ?, Género = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)){
            for (Libro l : this.biblioteca.values()) {
                ps.setInt(1, l.getISBN());
                ps.setString(2, l.getNombre());
                ps.setString(3, l.getAutor());
                ps.setString(4, l.getEditorial());
                ps.setString(5, l.getGenero());
            }
		} catch(SQLException e){
			System.err.println(e.getMessage());
		}
    }
    */

    public void insertar(Libro libro) {
		try{
			String query = "INSERT INTO elementos (isbn,nombre,autor,editorial,genero) VALUES (";
			query+= "isbn = '" + libro.getISBN() + "',";
            query+= "nombre = '" + libro.getNombre() + ",";
            query+= "editorial = '" + libro.getEditorial() + ",";
            query+= "genero = " + libro.getGenero() + "')";
			System.out.println(query);
			Statement st = conn.createStatement();
			int isOk = st.executeUpdate(query);
			System.out.println(isOk);
            this.biblioteca.put(libro.getISBN(), libro);
		} catch(Exception e){
		    System.err.println(e.getMessage());
		}
	}

	public void actualizar(Libro libro) {
		try{
            this.biblioteca.remove(libro.getISBN());
			String query = "UPDATE elementos SET ";
			query+= "isbn = '" + libro.getISBN() + "',";
            query+= "nombre = '" + libro.getNombre() + ",";
            query+= "autor = '" + libro.getAutor() + "',";
            query+= "editorial = '" + libro.getEditorial() + ",";
            query+= "genero = " + libro.getGenero() + "'";
			System.out.println(query);
			Statement st = conn.createStatement();
			int isOk = st.executeUpdate(query);
			System.out.println(isOk);
            this.biblioteca.put(libro.getISBN(), libro);
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	public void borrar(int isbn) {
		try {
			String query = "DELETE FROM elementos WHERE id = " + isbn; 
			System.out.println(query);
			Statement st = conn.createStatement();
			int isOk = st.executeUpdate(query);
			System.out.println(isOk);
            this.biblioteca.remove(isbn);
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

}