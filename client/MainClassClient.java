/*
 * Клиент
 * 
 * 
 */
package ru.chat.core.client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Gerasimov Gerasim
 */
public class MainClassClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Client client = new Client();
        Thread t = Thread.currentThread();
        while (true) try {
            Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } 
    }
    
    
public static class Client {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 8189;

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    Client() {
        System.out.println("client created");
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            //поток чтения
            Thread readThread = new Thread(() -> {
                    try {
                        while (true) {
                            String s = in.readUTF();
                            System.out.println("\nserver:"+s);
                            System.out.print(">");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


            });
            readThread.start();
            
            //поток отправки сообщений на сервер
            Thread sendThread = new Thread(() -> {
                    Scanner read = new Scanner(System.in);
                    try {
                        while (true) {
                            System.out.print(">");
                            String s = read.nextLine();
                            sendMsg(s);
                        }
                    } finally {
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
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


}



}