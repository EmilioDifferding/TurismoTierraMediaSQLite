package Controllers;

import database.DbConnection;
import models.Oferta;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

public class ItineraryController {
    Connection con = null;

    //devuelve el valor del id del itinerario, si no existe itinerario devuelve 0
    private int obtenerItinerario(User usuario){
        con= DbConnection.connect();
        PreparedStatement ps=null;
        ResultSet rs = null;
        String query = "SELECT itinerario.itinerario_id \r\n" + "FROM itinerario \r\n"
                + "JOIN usuario ON itinerario.usuario_id =" + usuario.getId()+"";
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
                rs.close();
                ps.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    // creamos un nuevo itinerario con referencia al usuario y a la fecha
    private void insertarItinerario(int user_id){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-3:00"));
        String fecha = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO itinerario(fecha, usuario_id) VALUES(?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, fecha);
            ps.setInt(2,user_id );
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void insertarAtraccion(int atraccionId,int ofertaId, int itinerarioId){
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        try {
            String query = ("INSERT INTO detalle_itinerario (itinerario_id, atraccion_id,promo_id)\r\n"
                    + "VALUES (?,?,?)");

            ps = con.prepareStatement(query);
            ps.setInt(1, itinerarioId);
            ps.setInt(2, atraccionId);
            ps.setInt(3,ofertaId);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void actualizarItinerario(User usuario, Oferta oferta){

         /*
         * el usuario, en caso contrario debe ser creado
         */
        int idItinerario = obtenerItinerario( usuario );
        if(idItinerario == 0){
            insertarItinerario(usuario.getId());
        }

        if (oferta.isPaquete()) {
            for(int atraccion : OfertaController.getAtraccionesDePaquete(oferta.getId())) {
                insertarAtraccion(atraccion,oferta.getId(), obtenerItinerario( usuario ) );
            }


        } else {
           insertarAtraccion(oferta.getId(),0, obtenerItinerario( usuario ));
        }

    }

    public void listAll(){

    }

}
