/*
 * Сервер
 * 
 * 
 */
package ru.chat.core.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/**
 *
 * @author Gerasimov Gerasim
 */
public class MainClassServer {
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ServerSocket serv = null;
        Socket socket = null;
        DataInputStream in;
        DataOutputStream out;
    
        try {
            serv = new ServerSocket ( 8189 );
            System.out.println ( "Сервер запущен, ожидаем подключения..." );
            socket = serv.accept ();
            System.out.println ( "Клиент подключился" );
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            //поток получения сообщений от клиента
            Thread readThread = new Thread(() -> {
                try{
                    while(true){
                        String msg = in.readUTF();
                        System.out.println("\nclient: " + msg);
                        System.out.print(">");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            readThread.start();
            
            //поток отправки сообщений клиенту
            Thread sendThread = new Thread(() -> {
                Scanner read = new Scanner(System.in);
                try{
                    while(true){
                        System.out.print(">");
                        String msg = read.nextLine();
                        if (msg.equals("/end")) break;
                        try {
                            out.writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }finally {
                    try {
                        socket.close();
                        out.close();
                        read.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start(); 
            
        } catch ( IOException e ) {
            System . out . println ( "Ошибка инициализации сервера" );
        } finally {
            try {
                serv . close ();
            } catch ( IOException e ) {
                e . printStackTrace ();
            }
        }
    
    }
        
//    void sendMsg(String msg){
//        try {
//            out.writeUTF(msg);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    /*
    public class Server {
        private ServerSocket serv = null;
        private Socket socket = null;
        private DataInputStream in;
        private DataOutputStream out;
            try {
                serv = new ServerSocket ( 8189 );
                System.out.println ( "Сервер запущен, ожидаем подключения..." );
                while (true) {
                    socket = serv.accept ();
                    System.out.println ( "Клиент подключился" );
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                }
            } catch (IOException e) {
                e . printStackTrace ();
                System . out . println ( "Ошибка инициализации сервера" );
            } finally {
                try {
                    serv . close ();
                } catch ( IOException e ) {
                    e . printStackTrace ();
                }
            }
        }
        */
    
}
