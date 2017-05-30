/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cosmin
 */
public class HiloServidor extends Thread {

    DataInputStream fentrada;
    Socket socket = null;

    public HiloServidor(Socket s) {
        socket = s;
        try {
            //CREO FLUJO EDE ENTRADA
            fentrada = new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            System.out.println("ERROR DE E/S");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ServidorChat.mensaje.setText("NUMERO DE CONEXIONES ACTUALES: " + ServidorChat.ACTUALES);
        //NADA MAS CONECTARSE EL CLIENTE LE ENVIO TODOS LOS MENSAJES
        String texto = ServidorChat.textarea.getText();
        EnviarMensajes(texto);
        while (true) {
            String cadena = "";
            try {
                
                cadena = fentrada.readUTF();//lee lo que el cliente escribe
                //cueando un cliente finaliza el envio con un *
                if (cadena.trim().equals("*")) {
                    ServidorChat.ACTUALES--;
                    ServidorChat.tabla.remove(socket);
                    ServidorChat.mensaje.setText("NUMERO DE CONEXIONES ACTUALES: " + ServidorChat.ACTUALES);
                    break;//salir del while
                }
                ServidorChat.textarea.append(cadena + "\n");
                texto = ServidorChat.textarea.getText();
                EnviarMensajes(texto); //envio texto a todos los clientes
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }//fin while
    }//run
    
    //ENVIA LOS MENSAJES DEL TEXTAREA A LOS CLIENTES DEL CHAT
    private void EnviarMensajes(String texto){
        int i;
        //recorremos talba de sockets para enviarles los mensajes
        for (i = 0; i < ServidorChat.ACTUALES; i++){
            Socket s1 = ServidorChat.tabla.get(i);//((obtener socket));
            try{
                DataOutputStream fsalida = new DataOutputStream(s1.getOutputStream());
                fsalida.writeUTF(texto);//escribir en el socket el texto
            } catch (SocketException se) {
                //esta excepcion ocurre cuando escribimos en un socket
                //de un cliente que ha finalizado
            }catch (IOException e) {
                e.printStackTrace();
            }
            
        }//for
    }//enviarMensajes

}
