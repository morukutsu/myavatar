package com.me.myavatar.core;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server
{  
	// Server info
    private final static int port = 8000; 
    private String hostname = "";
    private String hostIP ="";
    private ServerSocket serverSocket=null;
    
    // Status
    public boolean isClientConnected = false;
    
    public Server()
    {     
        try {
            // Get host information
            hostname = InetAddress.getLocalHost().getHostName();
            hostIP = InetAddress.getLocalHost().getHostAddress();
            
            // Display server information
            System.out.println("Server started on "+hostname+" with IP: "+hostIP + " on the port number: " + port);
            serverSocket = new ServerSocket(port);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void acceptClient()
    {
    	while(true)
        {   
            ClientWorker w;
            try
            {   
                w = new ClientWorker(serverSocket.accept());
                Thread t = new Thread(w);
                t.start();
                
                isClientConnected = true;
                break;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }   
        }
    }
}

class ClientWorker implements Runnable
{
    Socket incoming;
    public ClientWorker(Socket incoming)
    {
        this.incoming = incoming;
    }
    public void run()
    {
        String request = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            request = in.readLine();
            System.out.println("request =" + request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(incoming.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("The command is not readable");

    }
}
