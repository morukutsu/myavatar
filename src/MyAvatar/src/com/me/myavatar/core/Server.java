package com.me.myavatar.core;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Pixmap;

public class Server
{  
	// Server info
    private final static int port = 8000; 
    private String hostname = "";
    private String hostIP ="";
    private ServerSocket serverSocket=null;
    
    // Commands received
    public LinkedList<String> commands = new LinkedList<String>();
    
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
                w = new ClientWorker(serverSocket.accept(), this);
                Thread t = new Thread(w);
                t.start();
                
                System.out.println("Client connected!");
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
    Server server;
    
    public ClientWorker(Socket incoming, Server serv)
    {
        this.incoming = incoming;
        server = serv;
    }
    public void run()
    {
    	System.out.println("Client worker thread running");
    	
    	while(true)
    	{
	        String request = null;
	        BufferedReader in = null;
	        try {
	            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	        	System.out.println("Waiting for command...");
	            request = in.readLine();
	            server.commands.add(request);
	            
	            System.out.println("Received: " + request);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    }
}
