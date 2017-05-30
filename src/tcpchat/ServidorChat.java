/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpchat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Cosmin
 */
public class ServidorChat extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    static ServerSocket servidor;
    static final int PUERTO = 44444;//Puerto por el que escucha
    //static int CONEXIONES = 0; //cuenta las conexiones
    static int ACTUALES = 0;//nº de conexiones actuales activas
    static int MAXIMO = 7;//maximo de conexiones permitidas

    static JTextField mensaje = new JTextField("");
    static JTextField mensaje2 = new JTextField("");
    static JScrollPane scrollpane1;
    static JTextArea textarea;
    JButton salir = new JButton("Salir");
    static ArrayList<Socket> tabla = new ArrayList();//almacena sckets de clientes

    public ServidorChat() {
        super("VENTANA DEL SERVIDOR DE CHAT");
        setLayout(null);
        mensaje.setBounds(10, 10, 400, 30);
        add(mensaje);
        mensaje.setEditable(false);

        mensaje2.setBounds(10, 348, 400, 30);
        mensaje2.setEditable(false);

        textarea = new JTextArea();
        scrollpane1 = new JScrollPane(textarea);

        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);
        salir.setBounds(420, 10, 100, 30);
        add(salir);

        textarea.setEditable(false);
        salir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == salir) {//se pulsa salir
            try {
                servidor.close(); //cieroo
                
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);//fin
        }
    }

    public static void main(String[] args) throws IOException {
        servidor = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado...");
        ServidorChat pantalla = new ServidorChat();
        pantalla.setBounds(0, 0, 540, 400);
        pantalla.setVisible(true);
        mensaje.setText("NUMERO DE CONEXIONES ACTUALES: " + 0);

        //SE ADMITEN HASTA 10 CONEXIONES
        while (ACTUALES < MAXIMO) {
            Socket s = new Socket();
            try {
                s = servidor.accept();//esperando cliente

                tabla.add(s); //almacenar socket
                //CONEXIONES++;
                ACTUALES++;
                HiloServidor hilo = new HiloServidor(s);
                hilo.start(); //lanzar hilo
            } catch (SocketException ns) {
                //sale por aqui si pulsamos boton Salir y
                //no se ejecuta todo el bucle
                break; //salir del bucle
            }

        }//fin while

        if (!servidor.isClosed()) {
            try {
                //sale cuando se llega al maximo de conexiones
                mensaje2.setForeground(Color.red);
                mensaje2.setText("MAXIMO Nº DE CONEXIONES ESTBLECIDAS: " + ACTUALES);
                servidor.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Servidor finalizado...");
        }//main
    }//..Fin servidorChat

}
