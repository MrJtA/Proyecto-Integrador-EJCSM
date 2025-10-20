public class Controlador {

    private final Vista vista = new Vista();
    private Funcionalidades funcionalidades;

    public static void main(String[] args) {
        Controlador app = new Controlador();
        app.menu();
    }

    public void menu() {
        boolean seguir = true;
        while (seguir) {
            vista.menu();
            int opcion = vista.opcion();
            switch (opcion) {
                case 1 -> {
                    funcionalidades = new Texto(vista.pedirFichero());
                    subMenu();
                }
                case 2 -> {
                    funcionalidades = new Binario(vista.pedirFichero());
                    subMenu();
                }
                case 3 -> {
                    funcionalidades = new XML(vista.pedirFichero());
                    subMenu();
                }
                case 4 -> seguir = false;
                default -> seguir = true;
            }
        }
    }

    public void subMenu() {
        boolean seguir = true;
        while (seguir) {
            vista.subMenu();
            int opcion = vista.opcion();
            switch (opcion) {
                case 1 -> funcionalidades.buscar(vista.pedirLibro());
                case 2 -> funcionalidades.insertar(vista.crearLibro());
                case 3 -> funcionalidades.borrar(vista.pedirLibro());
                case 4 -> funcionalidades.modificar(vista.pedirLibro(), vista.crearLibro());
                case 5 -> funcionalidades.mostrar();
                case 6 -> funcionalidades.traspasarDatos(vista.pedirFichero());
                case 7 -> seguir = false;
                default -> seguir = true;
            }
        }
    }

}