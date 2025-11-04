import java.io.*;
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

    public File pedirFichero() {
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
                if (aux.isDirectory()) {
                    System.out.println("Error: La ruta introducida es un directorio, no un fichero.");
                    System.out.println("Por favor, introduce un nombre de fichero.");
                    continue;
                }
                entradaValida = true;
            } else {
                try {
                    aux.createNewFile();
                    System.out.println("El fichero no existe, se va a crear.");
                    entradaValida = true;
                } catch (IOException e) {
                    System.err.println("Error: Fichero inválido.");
                    System.out.println("Por favor, introduce una ruta válida.");
                }
            }
        }
        return aux;
    }

    public String pedirDatabase(){
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
            System.out.println("Error: No hay ningún libro registrado.");
        } else {
            if (biblioteca.containsKey(isbn)) {
                System.out.println("Se ha encontrado el libro: " + biblioteca.get(isbn));
            } else {
                System.out.println("Error: No se ha encontrado el libro.");
            }
        }
    }

    public void mostrar(Map<Integer, Libro> biblioteca){
        if (!biblioteca.isEmpty()) {
            System.out.println("Error: No hay ningún libro registrado.");
        } else {
            System.out.println("Libros registrados: ");
            for (Libro libro : biblioteca.values()) {
                System.out.println(libro.toString());
            }
        }
    }



}