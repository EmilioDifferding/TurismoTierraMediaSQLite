package models;

public class User {
    private int id;
    private int fav;
    private int presupuesto;
    private double tiempo_disponible;
    private String nombre;

    public User(int id, int fav, int presupuesto, double tiempo_disponible, String nombre) {
        this.id = id;
        this.fav = fav;
        this.presupuesto = presupuesto;
        this.tiempo_disponible = tiempo_disponible;
        this.nombre = nombre;
    }
    public User(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getTiempo_disponible() {
        return tiempo_disponible;
    }

    public void setTiempo_disponible(double tiempo_disponible) {
        this.tiempo_disponible = tiempo_disponible;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "User{" +
                "presupuesto=" + presupuesto +
                ", tiempo_disponible=" + tiempo_disponible +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
