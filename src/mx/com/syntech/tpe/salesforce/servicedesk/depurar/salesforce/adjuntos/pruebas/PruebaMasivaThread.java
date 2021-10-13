package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.pruebas;

import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.utils.SerialClaveBean;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class PruebaMasivaThread implements Runnable{
    
    // Constantes de la clase
    private static final String COMMANDO="java -jar \"C:\\SYNTECH\\SERVICIOS\\TotalPlay\\Desarrollos\\APIEliminarSFDAdjuntos\\dist\\APIEliminarSFDAdjuntos.jar\"";
    
    // Propiedades de la clase
    Thread hilo = null;
    private static String folioTicket = null;
    
    /** Constructor del hilo **/
    PruebaMasivaThread(){
        this.hilo = new Thread(this, "ELIMINAR");
    }
    
    /** Singleton para gestrionar el hilo **/
     public static PruebaMasivaThread iniciarProceso (String folio){
         PruebaMasivaThread miHilo=new PruebaMasivaThread();
         folioTicket = folio;
         miHilo.hilo.start(); //Inicia el hilo
         return miHilo;
     }

     //Punto de entrada de hilo.
     public void run(){
         String idEjecucion = (new  SerialClaveBean()).getSerial(5);
         System.out.println("Ejecutamos el nuevo hilo con ID: " + idEjecucion);
         /**try { **/
             for (int count=0; count<10;count++){
                 // Arrancamos el comando
                 PruebaMasivaCommand command = new PruebaMasivaCommand();
                 String comandoEjecutar = COMMANDO + " " + folioTicket + " servicedesk DeskService01";
                 command.ejecutarComando(comandoEjecutar, idEjecucion);
             }
         /**}catch (InterruptedException exc){
             System.out.println(hilo.getName()+ " interrumpudo.");
         }
         System.out.println(hilo.getName()+" terminado.");**/
     }
    
    
    

}
