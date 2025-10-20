import java.io.*;
import java.util.*;

public class Vista {

    final Scanner sc = new Scanner(System.in);

    public void menu() {
        System.out.println("1. Ficheros de texto.");
        System.out.println("2. Ficheros binarios.");
        System.out.println("3. Ficheros XML.");
        System.out.println("4. Salir.");
    }

    public void subMenu() {
        System.out.println("1. Buscar un libro.");
        System.out.println("2. Añadir un libro.");
        System.out.println("3. Borrar un libro.");
        System.out.println("4. Modificar un libro.");
        System.out.println("5. Listar libros.");
        System.out.println("6. Traspasar datos.");
        System.out.println("7. Volver al menú principal.");
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

    public Libro crearLibro() {
        Libro libro = null;
        boolean entradaValida = false;
        do { 
            try {
            System.out.println("Introduce a continuación los datos del nuevo libro.");
            System.out.print("ISBN: ");
            int isbn = sc.nextInt();
            sc.nextLine();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Autor: ");
            String autor = sc.nextLine();
            System.out.print("Editorial: ");
            String editorial = sc.nextLine();
            System.out.print("Género: ");
            String genero = sc.nextLine();
            libro = new Libro(isbn, nombre, autor, editorial, genero);
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

}