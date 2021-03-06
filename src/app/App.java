package app;

import Controllers.ItineraryController;
import Controllers.OfertaController;
import Controllers.UserController;
import helper.Helper;
import models.Oferta;
import models.User;

import java.util.LinkedList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        LinkedList<User> users = new LinkedList<>();
        Oferta oferta = null;
        UserController uc = new UserController();
        OfertaController oc = new OfertaController();
        ItineraryController itc = new ItineraryController();
        users = uc.load();
        String decision = "Y";
        Scanner sc = new Scanner(System.in); // creamos un scanner para leer la entrada de consola

        for (User usuario : users) {
            System.out.println("\nBienvenido " + usuario.getNombre() + ". Prepara tu itinerario");
            System.out.println("Presta atencion a las siguientes ofertas:\n ");

            while (!oc.generarOferta(usuario).equals(Oferta.vacia())) {
                oferta = oc.generarOferta(usuario);
                System.out.println("Monedas restantes: " + usuario.getPresupuesto() + " Tiempo restante: " + usuario.getTiempo_disponible());
                System.out.println(oferta);
                System.out.println("Desea comprarla?");
                System.out.println("Y - Si");
                System.out.println("N - No");
                decision = sc.next();
                if (decision.equalsIgnoreCase("Y")) {
                    uc.comprar(oferta,usuario);
                    oc.actualizarCupos(oferta);
                    itc.actualizarItinerario(usuario, oferta);
                    System.out.println("Producto adquirido!");
                } else {
                    oc.getRechazados().add(oferta);
                }
            }
            System.out.println("No hay mas ofertas disponibles. Este es tu itinerario para el dia de hoy.");
            System.out.println("---------------------------------------------- \n");


            /*TODO: Mostrar el itinerario del usuario*/
            System.out.println(Helper.mostrarItinerario(usuario));
            int costoTotal=0;
            double tiempoTotal=0;
            for (Oferta ofer : usuario.getCompras()) {
                costoTotal+=ofer.getPrecio();
                tiempoTotal+=ofer.getTiempo();
            }
            System.out.println("Costo total: "+costoTotal+" Monedas  Tiempo de recorrido: "+tiempoTotal+" horas\n");
            System.out.println("Monedas restantes: " + usuario.getPresupuesto() + " Monedas  Tiempo restante: " + usuario.getTiempo_disponible()+ " horas.\n");


            System.out.println("---------------------------------------------- \n");
            oc.getRechazados().clear();
        }
        System.out.println("Programa Finalizado con exito. Itinerarios cargados");
    }
}

