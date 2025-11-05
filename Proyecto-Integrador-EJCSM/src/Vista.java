import java.io.*;
import java.sql.*;
import java.util.*;

public class Vista {

    final Scanner sc = new Scanner(System.in);

    public void menu() {
        System.out.println("1. Ficheros de texto.");
        System.out.println("2. Ficheros binarios.");
        System.out.println("3. Ficheros XML.");
        System.out.println("4. Bases de datos.");
        System.out.println("5. Salir.");
    }

    public void subMenu() {
        System.out.println("1. Buscar un libro.");
        System.out.println("2. Mostrar libros.");
        System.out.println("3. Añadir un libro.");
        System.out.println("4. Borrar un libro.");
        System.out.println("5. Modificar un libro.");
        System.out.println("6. Traspasar datos a un fichero.");
        System.out.println("7. Traspasar datos a una base de datos.");
        System.out.println("8. Volver al menú principal.");
    }

    public int opcion() {
        int opcion = -1;
        boolean entradaValida = false;
        do { 
            try {
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.err.println("Error: Opción inválida.");
                System.out.println("Por favor, introduce un opción válida.");
            }
        } while (entradaValida);
        return opcion;
    }

    public File pedirFichero() throws IOException {
        File aux = null;
        boolean entradaValida = false;
        while (!entradaValida) {
            System.out.print("Introduce el fichero con el que quieres trabajar: ");
            String rutaFichero = sc.nextLine().trim();
            if (rutaFichero.isEmpty()) {
                System.out.println("Error: La ruta no puede estar vacía.");
                continue;
            }
            aux = new File(rutaFichero);
            if (aux.exists()) {
                entradaValida = true;
            } else {
                try {
                    aux.createNewFile();
                    System.out.println("Se ha creado el fichero '" + aux + "'.");
                    entradaValida = true;
                } catch (IOException e) {
                    System.err.println("Error: Fichero inválido.");
                    System.out.println("Por favor, introduce una ruta válida.");
                    throw e;
                }
            }
        }
        return aux;
    }

    public String pedirDatabase() throws SQLException {
        System.out.println("Introduce el nombre de la base de datos: ");
        String nombre = sc.nextLine();
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/peliculas", "root", "root")) {
            boolean existeDatabase = false;
            String query = "show databases like ?";
            try (PreparedStatement ps = conexion.prepareStatement(query)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existeDatabase = true;
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al verificar la existencia de la base de datos: " + e.getMessage());
            }
            if (!existeDatabase) {
                String query2 = "CREATE DATABASE " + nombre;
                try (java.sql.Statement stmt = conexion.createStatement()) {
                    stmt.executeUpdate(query2);
                    System.out.println("Se ha creado la base de datos '" + nombre + "'.");
                } catch (SQLException e) {
                    System.err.println("Error al crear la base de datos: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al establecer conexión inicial con la base de datos: " + e.getMessage());
            throw e;
        }
        return nombre;
    }

    public Libro crearLibro() {
        Libro libro = null;
        boolean entradaValida = false;
        do { 
            try {
                System.out.println("Introduce a continuación los datos del nuevo libro.");
                System.out.print("ISBN: ");
                int isbn = sc.nextInt();
                sc.nextLine();
                System.out.print("Titulo: ");
                String titulo = sc.nextLine();
                System.out.print("Autor: ");
                String autor = sc.nextLine();
                System.out.print("Editorial: ");
                String editorial = sc.nextLine();
                System.out.print("Género: ");
                String genero = sc.nextLine();
                libro = new Libro(isbn, titulo, autor, editorial, genero);
            } catch (InputMismatchException e) {
                System.err.println("Error: ISBN inválido.");
                System.out.println("Por favor, introduce un ISBN válido.");
            }
        } while (entradaValida);
        return libro;
    }

    public int pedirLibro() {
        int isbn = -1;
        boolean entradaValida = false;
        do {
            System.out.print("Introduce el ISBN del libro para la búsqueda: ");
            try {
                isbn = sc.nextInt();
                sc.nextLine();
                entradaValida = true;
            } catch (InputMismatchException e) {
                System.err.println("Error: ISBN inválido.");
                System.out.println("Por favor, introduce un ISBN válido.");
                sc.nextLine();
            }
        } while (entradaValida);
        return isbn;
    }

    public void buscar(Map<Integer, Libro> biblioteca, int isbn) {
        if (biblioteca.isEmpty()) {
            System.out.println("No hay ningún libro registrado.");
        } else {
            if (biblioteca.containsKey(isbn)) {
                System.out.println("Se ha encontrado el libro: " + biblioteca.get(isbn));
            } else {
                System.out.println("No se ha encontrado el libro.");
            }
        }
    }

    public void mostrar(Map<Integer, Libro> biblioteca) {
        if (biblioteca.isEmpty()) {
            System.out.println("No hay ningún libro registrado.");
        } else {
            System.out.println("Libros registrados: ");
            for (Libro libro : biblioteca.values()) {
                System.out.println(libro.toString());
            }
        }
    }

}