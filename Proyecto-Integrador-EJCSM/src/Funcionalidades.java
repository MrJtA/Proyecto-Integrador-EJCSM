import java.io.File;
import java.util.Map;

public interface Funcionalidades {
    
    public Map<Integer, Libro> leerFichero();
    public void escribirLista();
    public void escribirListaTexto(File file);
    public void escribirListaBinario(File file);
    public void escribirListaXML(File file);
    public void traspasarDatos(File file);
    public void buscar(int isbn);
    public void insertar(Libro libro);
    public void borrar(int isbn);
    public void modificar(int isbn, Libro libro);
    public void mostrar();

}