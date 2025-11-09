import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface Funcionalidades {

    public Map<Integer, Libro> leerFichero() throws IOException, SQLException;
    public void insertar(Libro libro) throws IOException, SQLException;
    public void modificar(int isbn, Libro libro) throws IOException, SQLException;
    public void borrar(int isbn) throws IOException, SQLException;
    public void traspasarDatosFichero(File file) throws IOException, SQLException;
    public void traspasarDatosDatabase(String nombreDatabase) throws IOException, SQLException;

}