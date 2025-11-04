import java.io.File;
import java.util.Map;

public interface Funcionalidades {

    public Map<Integer, Libro> leerFichero();
    public void traspasarDatosFichero(File file);
    public void traspasarDatosDatabase(String nombreDatabase);
    public void insertar(Libro libro);
    public void modificar(int isbn, Libro libro);
    public void borrar(int isbn);

}