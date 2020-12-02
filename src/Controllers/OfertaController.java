package Controllers;

import database.DbConnection;
import models.Oferta;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class OfertaController {
    private LinkedList<Oferta> rechazados = new LinkedList<Oferta>();

    // Retornamos un LinkdList con los id de las atracciones que incluye el paquete.
    public static LinkedList<Integer> getAtraccionesDePaquete(int ofertaId){
        LinkedList<Integer> atracciones = new LinkedList<Integer>();
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select atraccion.atraccion_id\r\n"
                + "from promocion join promocion_atraccion \r\n"
                + "on promocion.promocion_id = promocion_atraccion.promocion_id\r\n"
                + "join atraccion on atraccion.atraccion_id = promocion_atraccion.atraccion_id \r\n"
                + "where promocion.promocion_id ="+ofertaId;
        try{
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                atracciones.add(rs.getInt(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                con.close();
                rs.close();
                ps.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return atracciones;
    }

    public LinkedList<Oferta> getRechazados() {
        return rechazados;
    }

    public Oferta generarOferta(User usuario){
        if (buscar(usuario, true).equals(Oferta.vacia())) {
            return buscar(usuario, false);
        } else {
            return buscar(usuario, true);
        }
    }
    /*
     * Buscamos promos que contanga al menos una atracción de la preferencia del
     * usuario. Ninguna de las atracciones en la promo esta en el itinerario del
     * usuario
     */
    private String conPref(boolean conpref, User usuario) {
        if (conpref) {
            return "where tipo_atraccion.nombre ='"+usuario.getFav()+"' AND atraccion.cupo > 1";
        }
        return "where atraccion.cupo > 1";
    }

    public Oferta buscar(User usuario, boolean conPref){
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        ResultSet rsPromo = null;
        ResultSet rsTiempoPrecioPromo = null;
        ResultSet rsAtraccionGratis = null;
        ResultSet rsAtraccion = null;

        Oferta oferta = Oferta.vacia();
        String query = "select promocion.nombre, promocion.descripcion, atraccion.atraccion_id, promocion.tipo_promocion, promocion.descuento, promocion.promocion_id\r\n"
                + "from promocion join promocion_atraccion on promocion.promocion_id = promocion_atraccion.promocion_id\r\n"
                + "join atraccion on atraccion.atraccion_id = promocion_atraccion.atraccion_id \r\n"
                + "join tipo_atraccion on tipo_atraccion.tipo_atraccion_id = atraccion.tipo_id\r\n"
                + this.conPref(conPref, usuario)+"\r\n"
                + "group by promocion.nombre\r\n"
                + "\r\n"
                + "EXCEPT\r\n"
                + "\r\n"
                + "select promocion.nombre, promocion.descripcion, atraccion.atraccion_id, promocion.tipo_promocion, promocion.descuento,  promocion.promocion_id\r\n"
                + "from promocion join promocion_atraccion on promocion.promocion_id = promocion_atraccion.promocion_id\r\n"
                + "join atraccion on atraccion.atraccion_id = promocion_atraccion.atraccion_id \r\n"
                + "join tipo_atraccion on tipo_atraccion.tipo_atraccion_id = atraccion.tipo_id\r\n"
                + "where atraccion.atraccion_id in(select detalle_itinerario.atraccion_id\r\n"
                + "								from detalle_itinerario join itinerario on itinerario.itinerario_id = detalle_itinerario.itinerario_id\r\n"
                + "								join usuario on usuario.usuario_id = itinerario.usuario_id\r\n"
                + "								where usuario.usuario_id ='"+usuario.getId()+"')";
        try{
            ps = con.prepareStatement(query);
            rsPromo = ps.executeQuery();
            while (rsPromo.next()){
                oferta.setNombre(rsPromo.getString(1));
                oferta.setId(rsPromo.getInt(6));
                oferta.setDescripcion(rsPromo.getString(2));
                oferta.setPaquete(true);
                ps2 = con.prepareStatement("select sum(atraccion.costo) as costo, sum(atraccion.tiempo) as tiempo\r\n"
                        + "from promocion join promocion_atraccion on promocion.promocion_id = promocion_atraccion.promocion_id\r\n"
                        + "join atraccion on atraccion.atraccion_id = promocion_atraccion.atraccion_id \r\n"
                        + "where promocion.promocion_id = " + oferta.getId()+"");
                rsTiempoPrecioPromo = ps2.executeQuery();
                oferta.setPrecio(rsTiempoPrecioPromo.getInt(1));
                oferta.setTiempo(rsTiempoPrecioPromo.getDouble(2));

                switch (rsPromo.getInt(4)) {
                    /* Descuento porcentual */
                    case 1: {
                        oferta.setPrecio(oferta.getPrecio() * Integer.parseInt(rsPromo.getString(5)) / 100);
                        break;
                    }
                    /* Atraccion Gratuita */
                    case 2: {
                        String q = "select atraccion.costo from atraccion\r\n"
                                + "where atraccion.nombre ='" + rsPromo.getString(5)+"'";

                        ps3 = con.prepareStatement(q);
                        rsAtraccionGratis = ps3.executeQuery();
                        oferta.setPrecio(oferta.getPrecio() - rsAtraccionGratis.getInt(1));
                        break;
                    }
                    /*Precio Absoluto*/
                    case 3: {
                        oferta.setPrecio(Integer.parseInt(rsPromo.getString(5)));
                        break;
                    }
                }
                if (usuario.getPresupuesto() < oferta.getPrecio() || usuario.getTiempo_disponible() < oferta.getTiempo()) {
                    oferta = Oferta.vacia();
                }
                if (rechazados.contains(oferta)) {
                    oferta = Oferta.vacia();
                }
                if (!oferta.equals(Oferta.vacia())) {
                    return oferta;
                }

            }//ENDWHILE

            /*
             * No hay ninguna oferta tipo promo que cumpla los requisitos. Buscamos una
             * atracción de la preferencia del usuario. Debe ser la mas cara y en caso de
             * haber empate debe ser la que mas tiempo consuma y no debe estar en el
             * itinerario.
             */
            String queryAtracciones = "select atraccion.nombre, atraccion.descripcion, atraccion.costo, atraccion.tiempo, atraccion.atraccion_id\r\n"
                    + "from atraccion join tipo_atraccion on atraccion.tipo_id = tipo_atraccion.tipo_atraccion_id\r\n"
                    + this.conPref(conPref, usuario) + "\r\n"
                    + "\r\n"
                    + "EXCEPT\r\n"
                    + "\r\n"
                    + "select atraccion.nombre, atraccion.descripcion, atraccion.costo, atraccion.tiempo, atraccion.atraccion_id\r\n"
                    + "from atraccion join tipo_atraccion on atraccion.tipo_id = tipo_atraccion.tipo_atraccion_id\r\n"
                    + "where atraccion.atraccion_id in(select detalle_itinerario.atraccion_id\r\n"
                    + "								from detalle_itinerario join itinerario on itinerario.itinerario_id = detalle_itinerario.itinerario_id\r\n"
                    + "	join usuario on usuario.usuario_id = itinerario.usuario_id\r\n"
                    + "								where usuario.usuario_id = " + usuario.getId() + ")\r\n"
                    + "\r\n" + "order by costo desc, tiempo desc";
            ps4 = con.prepareStatement(queryAtracciones);
            rsAtraccion = ps4.executeQuery();
            while (rsAtraccion.next()) {
                oferta = new Oferta(rsAtraccion.getInt(5),rsAtraccion.getString(1), false, rsAtraccion.getString(2),rsAtraccion.getDouble(4), rsAtraccion.getInt(3));

                if (usuario.getPresupuesto() < oferta.getPrecio() || usuario.getTiempo_disponible() < oferta.getTiempo()) {
                    oferta = Oferta.vacia();
                }
                if (!rechazados.contains(oferta) && !oferta.equals(Oferta.vacia())) {
                    return oferta;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
                if (ps != null){
                    ps.close();
                    rsPromo.close();}
                if (ps2 != null){
                    rsTiempoPrecioPromo.close();
                    ps2.close();
                }
                if (ps3 != null){
                    ps3.close();
                    rsAtraccionGratis.close();
                }
                if (ps4 != null){
                    ps4.close();
                    rsAtraccion.close();
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return Oferta.vacia();
    }

    public void actualizarCupos(Oferta oferta){
        PreparedStatement ps = null;
        Connection con = DbConnection.connect();
        if(oferta.isPaquete()){
            for (int atraccion: getAtraccionesDePaquete(oferta.getId())) {
                String query = "UPDATE atraccion SET cupo=cupo-? Where atraccion.atraccion_id=?";
                try{
                    ps = con.prepareStatement(query);
                    ps.setInt(1,1);
                    ps.setInt(2,atraccion);
                    ps.execute();


                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        else{
            String query = "UPDATE atraccion SET cupo=cupo-? Where atraccion.atraccion_id=?";
            try{
                ps = con.prepareStatement(query);
                ps.setInt(1,1);
                ps.setInt(2,oferta.getId());
                ps.execute();


            }catch (SQLException e){
                e.printStackTrace();
            }
        }







    }



}
