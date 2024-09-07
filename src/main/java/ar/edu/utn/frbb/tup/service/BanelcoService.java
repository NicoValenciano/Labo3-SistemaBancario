package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Component;

@Component
public class BanelcoService {
     public boolean aprobarTransaccion(double monto)
     {
         return monto % 3 != 0;
     }
}
