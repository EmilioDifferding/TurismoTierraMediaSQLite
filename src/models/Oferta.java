package models;

public class Oferta {
    private int id;
    private String nombre;
    private boolean paquete;
    private String descripcion;
    private double tiempo;
    private int precio;



    public Oferta(int id, String nombre, boolean paquete, String descripcion, double tiempo, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.paquete = paquete;
        this.descripcion = descripcion;
        this.tiempo = tiempo;
        this.precio = precio;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return ((Oferta) obj).getNombre().equals(this.nombre);
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Producto: "+this.nombre+ " Descripcion: "+this.descripcion+" Precio: "+this.precio+" Monedas Tiempo de recorrido: "+this.tiempo;
    }

    public static Oferta vacia() {
        return new Oferta(0,"vacia",false,"",0.0,0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPaquete() {
        return paquete;
    }

    public void setPaquete(boolean paquete) {
        this.paquete = paquete;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
