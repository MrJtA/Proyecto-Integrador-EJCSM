import java.io.*;
import java.util.*;

public class Vista {

    final Scanner sc = new Scanner(System.in);

    public void menu() {
        System.out.println("1. Ficheros.");
        System.out.println("2. Bases de datos.");
        System.out.println("3. Salir");
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
                opcion = Integer.parseInt(sc.nextLine()); 
                entradaValida = true;
            } catch (NumberFormatException e) { 
                System.err.println("Error: Opción inválida.");
                System.out.println("Por favor, introduce un opción válida.");
            }
        } while (!entradaValida);
        return opcion;
    }

    /*
    public File pedirFichero() throws IOException {
        File aux = null;
        boolean entradaValida = false;
        while (!entradaValida) {
            System.out.print("Introduce el fichero con el que quieres trabajar (sólo disponibles ficheros de texto, binario o xml): ");
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
                }
            }
        }
        return aux;
    }
    */

    public File pedirFichero() throws IOException {
        File aux = null;
        boolean entradaValida = false;
        while (!entradaValida) {
            System.out.print("Introduce el fichero con el que quieres trabajar (sólo disponibles ficheros de texto, binario o xml): ");
            String rutaFichero = sc.nextLine().trim();
            aux = new File(rutaFichero);
            if (aux.exists()) {
                entradaValida = true;
            } else {
                File directorioPadre = aux.getParentFile();
                if (directorioPadre != null && !directorioPadre.exists()) {
                    if (directorioPadre.mkdirs()) {
                        System.out.println("Se han creado los directorios necesarios: " + directorioPadre.getAbsolutePath());
                    } else {
                        System.err.println("Error: No se pudieron crear los directorios necesarios para la ruta.");
                        continue; 
                    }
                }
                try {
                    if (aux.createNewFile()) {
                        System.out.println("Se ha creado el fichero '" + aux + "'.");
                        entradaValida = true;
                    } else {
                        System.err.println("Error: Fichero inválido o no se pudo crear (problema de permisos/ruta).");
                    }
                } catch (IOException e) {
                    System.err.println("Error: Fichero inválido.");
                    System.out.println("Por favor, introduce una ruta válida.");
                }
            }
        }
        return aux;
    }

    public String comprobarFichero(File file) {
        String nombreFichero = file.getName().toLowerCase();
        if (nombreFichero.endsWith(".txt")) {
            return ".txt";
        } else if (nombreFichero.endsWith(".bin")) {
            return ".bin";
        } else if (nombreFichero.endsWith(".xml")) {
            return ".xml";
        } else {
            System.out.println("Error: Manejo de ficheros de la extensión de " + nombreFichero + " no disponible.");
            System.out.println("Por favor, introduzca un fichero de texto, binario o xml.");
            return "";
        }
    }

    public String pedirDatabase() {
        System.out.println("Introduce el nombre de la base de datos: ");
        String nombre = sc.nextLine();
        return nombre;
    }

    public Libro crearLibro() {
        Libro libro = null;
        boolean entradaValida = false;
        do {
            try {
                System.out.println("Introduce a continuación los datos del nuevo libro.");
                System.out.print("ISBN: ");
                int isbn = Integer.parseInt(sc.nextLine());
                System.out.print("Titulo: ");
                String titulo = sc.nextLine();
                System.out.print("Autor: ");
                String autor = sc.nextLine();
                System.out.print("Editorial: ");
                String editorial = sc.nextLine();
                System.out.print("Género: ");
                String genero = sc.nextLine();
                libro = new Libro(isbn, titulo, autor, editorial, genero);
                entradaValida = true;

            } catch (NumberFormatException | InputMismatchException e) {
                System.err.println("Error: ISBN inválido. Debe ser un número entero.");
                System.out.println("Por favor, introduce un ISBN válido.");
            }
        } while (!entradaValida);
        return libro;
    }

    public int pedirLibro(Map<Integer, Libro> biblioteca) {
        String entrada;
        int isbn = -1;
        System.out.print("Introduce el ISBN o título del libro para la búsqueda: ");
        entrada = sc.nextLine();
        try {
            isbn = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Se buscará el libro por título.");
            for (Libro libro : biblioteca.values()) {
                if (entrada.equalsIgnoreCase(libro.getTitulo())) {
                    isbn = libro.getISBN();
                    break;
                }
            }
        }
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