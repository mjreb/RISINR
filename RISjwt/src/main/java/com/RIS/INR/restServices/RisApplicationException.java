package com.RIS.INR.restServices;
/**
 *
 * @author ibvar
 */
public class RisApplicationException extends RuntimeException{
        public RisApplicationException(/*Tabla msgs, int idMesage*/String mensaje) {
          
          super(mensaje);
        }
}
