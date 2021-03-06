package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.pruebas;

import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.utils.LeerArchivoTextoTOStringArrayBean;

/**
 *
 * @author Jorge Zavala Navarro
 */











































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































public class PruebasMasivasMain {

    // Propiedades de la clase
    private static final int CANTIDAD_EJECUCIONES = 10;
    private static final String[] folio = null;
    private static final String FILE_FOLIOS = "C:\\APIs\\APIEliminarSFDAdjuntos\\Temp\\FoliosTicketSDM.txt";

    public static void main(String... param) {

        try {
            // Cargamos los folios
            String[] folios = LeerArchivoTextoTOStringArrayBean.leerArchivoTOArray(FILE_FOLIOS);

            PruebaMasivaThread[] thread = new PruebaMasivaThread[CANTIDAD_EJECUCIONES];

            for (int i = 0; i < CANTIDAD_EJECUCIONES; i++) {
                thread[i] = PruebaMasivaThread.iniciarProceso(folios[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
