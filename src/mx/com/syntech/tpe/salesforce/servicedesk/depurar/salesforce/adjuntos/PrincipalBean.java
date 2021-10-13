package mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.conectbdd.ConnectorBDDConsultasBean;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.core.CoreResultadoVO;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.keys.CodeKeys;
import mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.props.AppPropsBean;
import org.apache.log4j.Category;
import org.omg.CORBA.StringHolder;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class PrincipalBean {

    // Propiedades de la clase
    private URL url = null;
    private int sid = 0;

    // Constantes estáticas de la clase
    private static Category log = Category.getInstance(PrincipalBean.class);
    private static final String uNoLock = "  WITH (NOLOCK) ";

    public PrincipalResultadoVO eliminarAdjuntosPorTicket(String folioTicket, String usuario, String password) throws PrincipalException {
        PrincipalResultadoVO retorno = null;
        if (folioTicket != null && !folioTicket.isEmpty()
                && usuario != null && !usuario.isEmpty()
                && password != null && !password.isEmpty()) {
            // Inicializar variables del proceso
            retorno = new PrincipalResultadoVO();
            List<AdjuntoInfoVO> listAdjuntoVO = new ArrayList<>();

            // Declaramos el conector a la base de datos
            Connection conn = null;
            try {

                // Inicializamos la URL del web service al cual se va a conectar
                this.url = new URL(AppPropsBean.getPropsVO().getUrlServicedeskWs());

                // Conectar a la base de datos
                conn = ConnectorBDDConsultasBean.getConectionServiceDesk();

                // Constrimos la consulta
                log.info(" ::: Realizamos la consulta de la información de los adjuntos del ticket: " + folioTicket);
                String sqlBuscarAdjuntosPorTicket
                        = "   SELECT TOP 1000 TICKET.ID AS CLAVE_TICKET, TICKET.PERSID AS PERSID_TICKET, \n"
                        + "          TICKET.REF_NUM AS NUMERO_TICKET, RELACION.ID AS CLAVE_RELACION,  \n"
                        + "          ADJUNTO.ID AS CLAVE_ADJUNTO, ADJUNTO.REL_FILE_PATH AS CARPETA_REPOSITORIO,  \n"
                        + "	     ADJUNTO.DEL AS BANDERA_BORRAR_ADJUNTO, ADJUNTO.ORIG_FILE_NAME AS NOMBRE_ARCHIVO_ORIGINAL,  \n"
                        + "	     ADJUNTO.FILE_NAME AS NOMBRE_ARCHIVO_REPOSITORIO, ADJUNTO.ZIP_FLAG AS BANDERA_COMPRIMIDO,  \n"
                        + "	     ADJUNTO.STATUS AS ESTATUS_ARCHIVO, ADJUNTO.DESCRIPTION AS DESCRIPCION_ADJUNTO,  \n"
                        + "          ADJUNTO.CREATED_BY AS CREADO_POR, ADJUNTO.PERSID AS PERSID_ADJUNTO "
                        + "     FROM ATTMNT AS ADJUNTO " + uNoLock + ", USP_LREL_ATTACHMENTS_REQUESTS AS RELACION " + uNoLock + ", CALL_REQ AS TICKET " + uNoLock + " \n"
                        + "    WHERE ADJUNTO.ID = RELACION.ATTMNT  \n"
                        + "      AND TICKET.PERSID = RELACION.CR  \n"
                        + "      AND TICKET.REF_NUM = ?  \n"
                        + "    ORDER BY ADJUNTO.FILE_DATE DESC";
                PreparedStatement psBuscarAdjuntosPorTicket = conn.prepareCall(sqlBuscarAdjuntosPorTicket);
                psBuscarAdjuntosPorTicket.setString(1, folioTicket);
                log.debug(" ::: query a ejecutar");
                log.debug(sqlBuscarAdjuntosPorTicket);
                ResultSet rsBuscarAdjuntosPorTicket = psBuscarAdjuntosPorTicket.executeQuery();

                // escaneamos el resultado
                AdjuntoInfoVO elemVO = null;
                while (rsBuscarAdjuntosPorTicket.next()) {
                    elemVO = new AdjuntoInfoVO();
                    elemVO.setBanderaBorrarAdjunto(rsBuscarAdjuntosPorTicket.getString("BANDERA_BORRAR_ADJUNTO"));
                    elemVO.setBanderaComprimido(rsBuscarAdjuntosPorTicket.getString("BANDERA_COMPRIMIDO"));
                    elemVO.setCarpetaRepositorio(rsBuscarAdjuntosPorTicket.getString("CARPETA_REPOSITORIO"));
                    elemVO.setClaveAdjunto(rsBuscarAdjuntosPorTicket.getInt("CLAVE_ADJUNTO"));
                    elemVO.setClaveRelacion(rsBuscarAdjuntosPorTicket.getInt("CLAVE_RELACION"));
                    elemVO.setClaveTicket(rsBuscarAdjuntosPorTicket.getInt("CLAVE_TICKET"));
                    elemVO.setEstatusArchivo(rsBuscarAdjuntosPorTicket.getString("ESTATUS_ARCHIVO"));
                    elemVO.setNombreArchivoOriginal(rsBuscarAdjuntosPorTicket.getString("NOMBRE_ARCHIVO_ORIGINAL"));
                    elemVO.setNombreArchivoRepositorio(rsBuscarAdjuntosPorTicket.getString("NOMBRE_ARCHIVO_REPOSITORIO"));
                    elemVO.setNumeroTicket(rsBuscarAdjuntosPorTicket.getString("NUMERO_TICKET"));
                    elemVO.setPersidTicket(rsBuscarAdjuntosPorTicket.getString("PERSID_TICKET"));
                    elemVO.setPersIdAdjunto(rsBuscarAdjuntosPorTicket.getString("PERSID_ADJUNTO"));
                    elemVO.setDescripcionAdjunto(rsBuscarAdjuntosPorTicket.getString("DESCRIPCION_ADJUNTO"));
                    elemVO.setCreadoPor(rsBuscarAdjuntosPorTicket.getString("CREADO_POR"));
                    listAdjuntoVO.add(elemVO);
                }

                // Validar si resolvió al menos un registro
                if (listAdjuntoVO.size() > 0) {
                    log.info(" ::: Obtener credenciales para la depuracion de servicios");

                    log.info(" ::: Se encontraron " + listAdjuntoVO.size() + " adjuntos para el ticket: " + folioTicket);
                    log.info(" ::: Escaneamos los registros....");
                    for (int i = 0; i < listAdjuntoVO.size(); i++) {

                        log.info(" ::: Procesando registro " + (i + 1) + " de " + listAdjuntoVO.size() + ", original:" + listAdjuntoVO.get(i).getNombreArchivoOriginal() + ", en repo ...\\" + listAdjuntoVO.get(i).getCarpetaRepositorio() + "\\" + listAdjuntoVO.get(i).getNombreArchivoRepositorio());
                        log.info(" ::: Descripción: " + listAdjuntoVO.get(i).getDescripcionAdjunto());

                        // Validar si la descripción tiene la marca
                        log.info(" ::: nO SE  '" + PrincipalKeys.KEY_ARCHIVO_REPLICADO_EN_SALESFORCE + "'");
                        if (Boolean.TRUE || (listAdjuntoVO.get(i).getDescripcionAdjunto()!=null
                                && !listAdjuntoVO.get(i).getDescripcionAdjunto().isEmpty()
                                && listAdjuntoVO.get(i).getDescripcionAdjunto().contains(PrincipalKeys.KEY_ARCHIVO_REPLICADO_EN_SALESFORCE))) {
                            // log.info(" ::: El adjunto se encuentra marcado, por lo tanto lo eliminamos");
                            log.info(" ::: Se elimina el adjunto");

                            log.info(" ::: COMMAND --> Eliminar adjunto por WS eliminarAdjuntoPorWS()");
                            this.eliminarAdjuntoPorWS("" + listAdjuntoVO.get(i).getPersIdAdjunto(), usuario, password);

                            log.info(" ::: COMMAND --> Eliminar adjunto fisicamente ");
                            this.eliminarArchivo(listAdjuntoVO.get(i).getCarpetaRepositorio(), listAdjuntoVO.get(i).getNombreArchivoRepositorio());

                            log.info(" ::: COMMAND --> Eliminar referencias en la Base de Datos ");
                            this.eliminarAdjuntoDBMS(listAdjuntoVO.get(i).getNumeroTicket(), listAdjuntoVO.get(i).getClaveAdjunto(), listAdjuntoVO.get(i).getClaveRelacion());

                            log.info(" ::: COMMAND --> Agregar comentario en el log de actividades");
                            try{
                            this.agregarLogActivity(usuario, password, listAdjuntoVO.get(i).getNumeroTicket(), 
                                    listAdjuntoVO.get(i).getPersidTicket(), "" + listAdjuntoVO.get(i).getClaveAdjunto().intValue(), 
                                    listAdjuntoVO.get(i).getNombreArchivoOriginal(), listAdjuntoVO.get(i).getNombreArchivoRepositorio());
                            }catch(Exception ex){
                                log.error("   ::: No se logró agregar el registro en el log activity.");
                                log.error("Se recibe el siguiente mensaje: ");
                                log.error(ex.getMessage());
                                log.error("Traza del error", ex);
                            }

                            log.info(" ::: La ejecución del Log Activity se completo");

                        } else {
                            //log.info(" ::: Adjunto sin marca. NO se elimina y continuamos.");
                        }
                    }

                } else {
                    log.info(" ::: No se encontraron adjuntos para el ticket: " + folioTicket);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al intentar eliminar adjuntos del ticket: " + folioTicket);
                log.error(ex.getMessage(), ex);

            } catch (Throwable th) {
                th.printStackTrace();
                log.error("Error al intentar eliminar adjuntos del ticket: " + folioTicket);
                log.error(th.getMessage(), th);

            } finally {
                try {
                    // Cerramos la conexión a la base de datos
                    log.debug("Intentando cerrar la conexión a la base de datos...");
                    conn.close();
                    log.debug("Ok, la conexión se cerro correctamente");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("Error al intentar eliminar adjuntos del ticket: " + folioTicket);
                    log.error(ex.getMessage(), ex);

                } catch (Throwable th) {
                    th.printStackTrace();
                    log.error("Error al intentar eliminar adjuntos del ticket: " + folioTicket);
                    log.error(th.getMessage(), th);

                }
            }
        } else {
            throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception("No se está recibiendo el número de ticket, cuenta de usuario y/o contraseña a procesar !!"));
        }
        return retorno;
    }

    private String agregarLogActivity(String usuario, String password, String creator, String persidTicket, String adjuntoId, String nombreOriginal, String nombreFisico) throws PrincipalException {

        // Validamos la información
        if (usuario != null && !usuario.isEmpty() && password != null && !password.isEmpty()
                && creator != null && !creator.isEmpty() && persidTicket != null && !persidTicket.isEmpty()) {

            log.info(" ::: Generamos el comentario del LogActivity");
            log.info(" ::: Obtenemos el sid con el usuario " + usuario + "....");
            int sid = this.login(usuario, password);
            log.info(" ::: SID: " + sid);

            log.info(" ::: Preparar comentario...");
            

            String comentario = "Se elimino el elemento adjunto con identificador: " + adjuntoId
                    + " correspondiente al ticket " + persidTicket + "\n"
                    + " Nombre del Adjunto (nombre original): " + nombreOriginal + "\n"
                    + " Nombre del Archivo (nombre en repositorio): " + nombreFisico;
            log.info(" ::: Comentario: " + comentario);

            return createActivityLog(
                    sid,
                    "cnt:" + creator.substring(2),
                    persidTicket,
                    comentario,
                    "LOG", 0, Boolean.FALSE);
            

        }else{
            throw new PrincipalException(CodeKeys.CODE_970_DATABASE_ERROR_NC, new Exception("Parámetros incorrectos !!"));
        }

    }

    private void eliminarAdjuntoPorWS(String adjunto, String usuario, String password) throws PrincipalException {
        if (adjunto != null && !adjunto.isEmpty()
                && usuario != null && !usuario.isEmpty()
                && password != null && !password.isEmpty()) {
            int sid = 0;

            try {
                log.info(" ::: ELIMINANDO ADJUNTO " + adjunto + " DESDE EL CONTEXTO DEL SERVICIO");
                log.info(" ::: Validando credenciales para el servico...");
                sid = this.login(usuario, password);
                if (sid > 0) {
                    log.info(" ::: Credenciales correctas. SID: " + sid);

                    log.info(" ::: Eliminar el adjunto: " + adjunto);

                    // Eliminar el archivo
                    StringHolder shAdjunto = new StringHolder(adjunto);
                    removeAttachment(sid, shAdjunto.value);

                    log.info(" ::: El archivo se elimino desde el conexto del Servicio Web: " + adjunto);
                    log.info(" ::: Agregar esta actividad a los comentarios de log");
                    log.info(" ::: Terminamos la sesión sid: ");
                    this.logout(sid);
                    log.info(" ::: Eliminación del documento por WS se ejecutó correctamente.");
                } else {
                    log.info(" ::: Credenciales incorrectas: " + sid);
                    throw new PrincipalException(CodeKeys.CODE_950_SERVICE_DESK_WSERROR_CREDS, new Exception("Las credenciales proporcionadas son incorrectas !!"));
                }
            } catch (Exception ex) {
                // Cerramos la sesión
                this.logout(sid);
                ex.printStackTrace();
                String error = "Se produjo un error al intentar eliminar desde el contexto del servicio de Service Desk el adjunto: " + adjunto;
                log.error(" ::: " + error);
                throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC + ":" + error, ex);

            } catch (Throwable th) {
                // Cerramos la sesión
                this.logout(sid);
                th.printStackTrace();
                String error = "Se produjo un error al intentar eliminar desde el contexto del servicio de Service Desk el adjunto: " + adjunto;
                log.error(" ::: " + error);
                throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC + ":" + error, th);

            } finally {
                log.info(" ::: Cerramos la sesión " + sid);
                this.logout(sid);
                log.info(" ::: La sesión se cerró satisfactoriamente");
            }
        } else {
            log.error(" ::: Información incompleta !! ");
            throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception("No se está recibiendo el número de ticket, cuenta de usuario y/o contraseña a procesar !!"));
        }
    }

    private void eliminarArchivo(String rutaRepositorio, String nombreArchivo) throws PrincipalException, InterruptedException {
        if (rutaRepositorio != null && !rutaRepositorio.isEmpty()
                && nombreArchivo != null && !nombreArchivo.isEmpty()) {
            String rutaArchivoEliminar
                    = AppPropsBean.getPropsVO().getPathRepositorioAdjunto()
                    + File.separatorChar
                    + rutaRepositorio
                    + File.separatorChar
                    + nombreArchivo;
            log.info("Eliminatr el archivo: " + rutaArchivoEliminar + "...");
            File file = new File(rutaArchivoEliminar);
            // Validamos si el archivo existe
            if (file.exists()) {
                file.delete();
                Thread.sleep(2600);  // Esperamos a que lo termine de procesar el servidor
                File fileConfirmar = new File(rutaArchivoEliminar);
                if (!fileConfirmar.exists()) {
                    log.info("El archivo: " + rutaArchivoEliminar + " se eliminó del repositorio satisfactoriamente");

                    // Agregamos en el log de actividades
                } else {
                    String error = "El archivo: " + rutaArchivoEliminar + " no se pudo borrar !!. Intente borrarlo manualmente !!";
                    log.error(error);
                    throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));
                }
            } else {
                String error = "El archivo: " + rutaArchivoEliminar + " no existe en la ruta especificada. por lo tanmto no hay nada para borrar";
                log.warn(error);

            }
        } else {
            String error = "El algun adjunto a eliminar no se tiene especificado el nombre de la carpeta de repositorio y/o el nombre del archivo físico en el repositorio de Service Desk.";
            log.error(error);
            throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));

        }
    }

    public void eliminarAdjuntoDBMS(String folioTicket, Integer claveAdjunto, Integer claveRelacion) throws PrincipalException {
        if (claveAdjunto != null && claveRelacion != null
                && folioTicket != null && !folioTicket.isEmpty()) {

            // Definir el elemento de conexión a la base de datos
            Connection conn = null;
            try {
                conn = ConnectorBDDConsultasBean.getConectionServiceDesk();

                // Eliminar la relación entre el adjunto y el ticket por medio del ticket
                String sqlEliminarRelacion
                        = "DELETE FROM USP_LREL_ATTACHMENTS_REQUESTS \n"
                        + " WHERE ID = ? \n"
                        + "   AND CR = ? \n"
                        + "   AND ATTMNT = ?";
                PreparedStatement psEliminarRelacion = conn.prepareCall(sqlEliminarRelacion);
                psEliminarRelacion.setInt(1, claveRelacion);
                psEliminarRelacion.setString(2, folioTicket);
                psEliminarRelacion.setInt(3, claveAdjunto);
                log.info(" ::: Eliminando relación adjunto-ticket");
                psEliminarRelacion.executeUpdate();

                // Eliminar el registro del adjunto
                String sqlEliminarAdjunto
                        = "DELETE FROM ATTMNT \n"
                        + " WHERE ID = ? ";
                PreparedStatement psEliminarAjunto = conn.prepareCall(sqlEliminarAdjunto);
                psEliminarAjunto.setInt(1, claveAdjunto);
                log.info(" ::: Eliminando relación adjunto-ticket");
                psEliminarAjunto.executeUpdate();

                // Commiteamos los cambios
                conn.commit();
                log.info(" ::: La eliminación de los registros en la base de datos se ejecutarón correctamente");

            } catch (Exception ex) {
                String error = "Se produjo un error al intentar eliminar los registros en la base de datos: " + ex.getMessage();
                log.error(error);
                throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);

            } catch (Throwable th) {
                String error = "Se produjo un error al intentar eliminar los registros en la base de datos: " + th.getMessage();
                log.error(error);
                throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, th);

            } finally {
                try {
                } catch (Exception ex) {
                    String error = "Se produjo un error al intentar eliminar los registros en la base de datos: " + ex.getMessage();
                    log.error(error);
                    throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));

                }
            }
        } else {
            String error = "No se está recibiendo la información del adjunto que se requiere eliminar en los registros del sistema.";
            log.error(error);
            throw new PrincipalException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));
        }
    }
    
    public static void asignarCharSet(String charset) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        log.info("   ::: Charset actual: " + Charset.defaultCharset().name());
        log.info("   ::: File encoding actual: " + System.getProperty("file.encoding"));
        log.info("   ::: Asignando el charset: " + charset);
        System.setProperty("file.encoding", charset);

        log.info("   ::: File encoding actual: " + System.getProperty("file.encoding"));

        Field fieldCharset = Charset.class.getDeclaredField("defaultCharset");
        fieldCharset.setAccessible(true);
        fieldCharset.set(null, null);
        log.info("   ::: Charset actual: " + Charset.defaultCharset().name());

    }
    
    

    private int login(java.lang.String username, java.lang.String password) {
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService(this.url);
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.login(username, password);
    }

    private void logout(int sid) {
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService(this.url);
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        port.logout(sid);
    }

    private int removeAttachment(int sid, java.lang.String attHandle) {
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService(this.url);
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.removeAttachment(sid, attHandle);
    }

    private String createActivityLog(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, java.lang.String logType, int timeSpent, boolean internal) {
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService(this.url);
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.createActivityLog(sid, creator, objectHandle, description, logType, timeSpent, internal);
    }

    private String getHandleForUserid(int sid, java.lang.String userID) {
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebService(this.url);
        mx.com.syntech.tpe.salesforce.servicedesk.depurar.salesforce.adjuntos.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.getHandleForUserid(sid, userID);
    }

}
