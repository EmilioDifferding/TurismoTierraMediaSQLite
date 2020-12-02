package helper;

import database.DbConnection;
import models.Oferta;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Helper {
    public static String mostrarItinerario(User usuario){
        String compra="";
        for (Oferta oferta: usuario.getCompras()) {
            compra+=oferta+"\n";
        }

        return compra;
    }

    /*TODO: Para un futuro donde haya mas tiempo*/
    /*private static String getPromos(User usuario){
        String promos="";

        Connection con= DbConnection.connect();
        ResultSet rs =null;
        PreparedStatement ps=null;


        String query="select promocion.nombre, promocion.descripcion,\n" +
                "promocion.tiempo, promocion.costo\n" +
                "from promocion join detalle_itinerario on\n" +
                "promocion.promocion_id = detalle_itinerario.promo_id\n" +
                "join itinerario on itinerario.itinerario_id = detalle_itinerario.itinerario_id\n" +
                "join usuario on usuario.usuario_id = itinerario.usuario_id\n" +
                "where usuario.usuario_id ="+usuario.getId();
        try {
            ps= con.prepareStatement(query);
            rs=ps.executeQuery();

            while(rs.next()){
                promos+="Promocion: "+rs.getString("nombre")+" Incluye: "+rs.getString("descripcion")+" Costo: "+rs.getInt("costo")+" Monedas  Tiempo total: "+rs.getDouble("tiempo")+" Horas  \n";
            }


        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
                rs.close();
                ps.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

        return promos;
    }

    private static String getAtracciones(User usuario){
        String atracciones="";

        Connection con= DbConnection.connect();
        ResultSet rs =null;
        PreparedStatement ps=null;


        String query="SELECT";
        try {
            ps= con.prepareStatement(query);
            rs=ps.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
                rs.close();
                ps.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

        return atracciones;
    }*/


}
