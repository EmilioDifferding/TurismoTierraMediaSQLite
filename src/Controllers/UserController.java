package Controllers;
import database.DbConnection;
import models.Oferta;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class UserController {

    public LinkedList<User> load(){
        LinkedList<User> usuarios = new LinkedList<User>();
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM usuario";
        try{
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                usuarios.add(new User(
                        rs.getInt("usuario_id"),
                        rs.getInt("atraccion_favorita"),
                        rs.getInt("presupuesto"),
                        rs.getDouble("tiempo_disponible"),
                        rs.getString("nombre")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                con.close();
                ps.close();
                rs.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    public void getOne(int id){

    }

    public void addNew(){
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO usuario(nombre, atraccion_favorita,tiempo_disponible) VALUES(?,?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, "PedroJr");
            ps.setInt(2,1 );
            ps.setDouble(3, 3.7);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void comprar(Oferta oferta, User usuario){
        usuario.setPresupuesto( usuario.getPresupuesto()-oferta.getPrecio() );
        usuario.setTiempo_disponible( usuario.getTiempo_disponible() - oferta.getTiempo() );

        PreparedStatement ps = null;
        Connection con = DbConnection.connect();
        String query = "UPDATE usuario SET presupuesto=?, tiempo_disponible=? Where usuario_id=?";
        try{
            ps = con.prepareStatement(query);
            ps.setInt(1,usuario.getPresupuesto() );
            ps.setDouble(2, usuario.getTiempo_disponible());
            ps.setInt(3, usuario.getId());
            ps.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
