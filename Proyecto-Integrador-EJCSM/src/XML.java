import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public final class XML extends Fichero {

    public XML(File file) {
        super(file);
        this.biblioteca = leerFichero();
    }

    @Override
    public Map<Integer, Libro> leerFichero() {
        Map<Integer, Libro> aux = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.parse("ficheros/File.xml");
            NodeList padres = documento.getElementsByTagName("libro");
            for (int i=0; i<padres.getLength(); i++) {
                Node nodo = padres.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element padre = (Element) nodo;
                    int isbn = Integer.parseInt(padre.getAttribute("ISBN"));
                    NodeList hijos = padre.getChildNodes();
                    List<String> listaHijos = new ArrayList<>();
                    for (int j=0; j<hijos.getLength(); j++) {
                        Node hijo = hijos.item(j);
                        if (hijo.getNodeType() == Node.ELEMENT_NODE) {
                            Element eHijo = (Element) hijo;
                            listaHijos.add(eHijo.getTextContent());
                        }
                    }
                    Libro libro = new Libro(isbn, listaHijos.get(0), listaHijos.get(1), listaHijos.get(2), listaHijos.get(3));
                    aux.put(isbn, libro);
                }
            }
        } catch (ParserConfigurationException | DOMException e) {
            System.err.println("Error al leer los libros del fichero: " + e.getMessage());
        } catch (SAXException | IOException ex) {
            System.err.println("Error al procesar el documento XML o de E/S: " + ex.getMessage());
        }
        return aux;
    }

    @Override
    public void escribirLista() {
        super.escribirListaXML();
    }

}