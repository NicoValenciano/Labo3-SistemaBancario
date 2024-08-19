package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Component;

@Component
public class BanelcoService {
     public static boolean aprobarTransaccion(double monto)
     {
         return monto % 3 != 0;
     }
}
