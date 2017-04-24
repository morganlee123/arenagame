package com.nextide.main.net;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.InterfaceAddress;

import java.util.ArrayList;
import java.util.Enumeration;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;

class ConnectToServer implements Callable<String> {
    //TODO: replace synchronizations with ReentrantLocks - they're better, safer.
    
    private static final int TIMEOUT1=3; //How long the client checks for a server before creating its own, in seconds.
    private static final byte MAX_PARTY_SIZE = 12;
    
    private static ArrayList<String> DNSListings = new ArrayList<String>(); //Stored as [IP]@[channel] ex.: "192.168.1.1@4444"
    
    private static Socket mSocket = null;    //mSocket is the main socket
    private static BufferedReader in = null; //receives server messages
    private static PrintWriter out = null;   //sends messaqges to server
    private static Thread serverThread = null;

    public static void main(String[] args) {
        
        // STEP 1: Check for an existing server (or... servers?)
        boolean connected = false; //do until a connection's been made.
        
        while (!connected) {
            sendUserMessage("Looking for an available game...");
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new ConnectToServer());
            
            try {
                System.out.println(future.get(TIMEOUT1, TimeUnit.SECONDS));
                System.out.println("Something's gone wrong! Matchmaking aborted...");
            } catch (TimeoutException e) {
                future.cancel(true);
                sendUserMessage("Finished: found " + DNSListings.size() + " server(s) available.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            executor.shutdownNow();
            // STEP 2: If there was an available server, connect using the provided IP and Channel.
            //         Otherwise, make a server.
            if (DNSListings.size() > 0) {
                sendUserMessage("Establishing a connection...");
                connected = establishConnection(DNSListings.get(0).split("_")[2],Integer.parseInt(DNSListings.get(0).split("_")[3]));
            } else {
                sendUserMessage("Creating a new Server...");
                createServer();
                sendUserMessage("waiting to establish a connection...");
                connected = establishConnection("localhost",Server.getInstance().getServerPort());
            }
        }
        
        //STEP 3: Send original packet of information to the server. Included: username, total kills, total deaths, total score, total time played. 
        //        These things will be used by the server for matchmaking.
        
        
        //STEP 4: Get original packet of information from the server.
        
        
    }
    
    public static void send(String message) throws IOException{
        out.println(message);
    }
    
    public static String receive() throws IOException{
        return in.readLine();
    }
    
    public static String receive(int timeout) throws IOException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new timeoutReceiver());
        String returns = null;
        try {
            returns= future.get(timeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            returns= "Error I transmitting data";
        } catch (InterruptedException e) {
            future.cancel(true);
            returns= "Error II transmitting data";
        } catch (ExecutionException e) {
            future.cancel(true);
            returns= "Error III transmitting data";
        }
        executor.shutdownNow();
        return returns;
    }
    
    private static void createServer() {
        serverThread = new Thread(Server.getInstance());
        serverThread.start();
        try{Thread.sleep(100);} catch(Exception e) {} //allows serverThread to obtain lock.
    }
    
    private static boolean establishConnection(String ip,int channel) {
        System.out.println("Establishing a connection...");
        Server.getInstance().ServerLock.lock();
        try {
            mSocket = new Socket(ip,channel);
            Server.getInstance().ServerLock.unlock();
            in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            out = new PrintWriter(mSocket.getOutputStream(), true);
            String testMessage = in.readLine();
            System.out.println("Success: " + testMessage);
            return true;
        } catch (UnknownHostException e) {e.printStackTrace();  System.out.println("Fail.");
        } catch (IOException e) {e.printStackTrace();           System.out.println("Fail.");
        } finally {
            if (Server.getInstance().ServerLock.isHeldByCurrentThread())
                Server.getInstance().ServerLock.unlock();
        }
        
        return false;
    }

    public String call() throws Exception {
        try {
            //Open a random port to send the package
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendData = ("RESPOND_AVAIL_SERV_" + fetchIP()).getBytes();
            //Try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 42317);
                c.send(sendPacket);
                System.out.println(getClass().getName() + "> Request packet sent to: 255.255.255.255 (DEFAULT)");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Broadcast the message over all the network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 9876);
                        c.send(sendPacket);
                    } catch (Exception e) {
                    }
                    System.out.println(getClass().getName() + "> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }
            System.out.println(getClass().getName() + "> Waiting for a reply...");
            //Wait for a response
            int validityCounter = 1;
            DNSListings = new ArrayList<String>();
            while (true) {
                byte[] recvBuf = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                c.receive(receivePacket);
                //We have a response
                //What we used to print: System.out.println(getClass().getName() + ">>> Broadcast response from server: " + receivePacket.getAddress().getHostAddress());
                //Check if the message is correct
                String message = new String(receivePacket.getData()).trim();
                boolean AlreadySent = false;
                for (int i = 0; i < DNSListings.size(); i++)
                    if (message.equals(DNSListings.get(i))) AlreadySent = true;
                if (message.startsWith("AVAIL_SERVER_") && !AlreadySent) {
                    //DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
                    String toPrint = "\t[" + validityCounter + "] " + message.split("_")[2] + " : ";
                    if (message.split("_").length > 4)
                        toPrint = toPrint + message.split("_")[4];
                    else
                        toPrint = toPrint + "<unknown username>";
                    System.out.println(toPrint);
                    validityCounter++;
                    DNSListings.add(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "no available channel"; //Satisfies the IDE, should be unreachable unless something goes wrong.
    }
    
	public static String fetchIP(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            return ip;
        } catch (Exception e) {
            //Should the original method fail, we resort to some underhanded bad code...
            try {
                Runtime rt = Runtime.getRuntime();
                java.lang.Process proc = rt.exec("ipconfig");
                
                BufferedReader stdInput = new BufferedReader(new 
                     InputStreamReader(proc.getInputStream()));
                
                BufferedReader stdError = new BufferedReader(new 
                     InputStreamReader(proc.getErrorStream()));
                
                // read the output from the command
                String s = null;
                boolean lookCloser = false;
                int countToRelease = 0;
                String toReturn = null;
                while ((s = stdInput.readLine()) != null) {
                    if (s.contains("Wireless LAN adapter Wi-Fi:")) {
                        lookCloser = true;
                        countToRelease = 0;
                    }
                    if (countToRelease < 7)
                        if(s.contains("IPv4 Address"))
                            toReturn = s.split(": ")[1];
                }
                return toReturn;
            } catch (Exception f) { e.printStackTrace();
                                    f.printStackTrace(); }
            return null;
        }
    }

    private static void sendUserMessage(String msg) { // TODO: implement this into the GUI, not a simple println :P
        System.out.println(msg);
    }
    
    private static class timeoutReceiver implements Callable<String>{
        public String call() throws IOException{
            return in.readLine();
        }
    }
    
    private static class Server implements Runnable {
        private int port = 4444; //accessed by gsp
        
        private static boolean isRunning = false;
        
        private static Server s = new Server(); //accessed by getInstance
        
        public ReentrantLock ServerLock = new ReentrantLock(true);
        
        private ArrayList<Thread> clients = new ArrayList<Thread>();
        private ArrayList<String> unames = new ArrayList<String>(); //Requires Lock
        private ArrayList<Integer> xCoords = new ArrayList<Integer>(); //Requires Lock
        private ArrayList<Integer> yCoords = new ArrayList<Integer>(); //Requires Lock
        private ArrayList<Integer> OLDxCoords = new ArrayList<Integer>(); //Requires Lock
        private ArrayList<Integer> OLDyCoords = new ArrayList<Integer>(); //Requires Lock
        
        public static Server getInstance() {return s;}
        
        public int getServerPort() {return port;}
        
        public void run() {
            if (isRunning) return;
            ServerLock.lock();
            System.out.println("Server has lock.");
            isRunning = true; //Prevents running of multiple servers from the same computer
            System.out.println("Not currently running a server... Continuing...");
            try { 
                ServerSocket serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();
                System.out.println("Opened port " + serverSocket.getLocalPort() + " of " + serverSocket.getInetAddress());
                ServerLock.unlock();
				Thread discThread = new Thread(DiscoveryThread.getInstance());
        		discThread.start();
                while (clients.size() < MAX_PARTY_SIZE ) {
                    System.out.println("Awaiting Connections...");
                    Thread temp = new ServerThread(serverSocket.accept());
                    temp.start();
                    ServerLock.lock();
                    clients.add(temp);
                    ServerLock.unlock();
                    System.out.println("Clients = " + clients.size());
                }
				discThread.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ServerLock.isHeldByCurrentThread())
                    ServerLock.unlock();
            }
        }
        
        private static class ServerThread extends Thread {
            private Socket socket = null;
        
            public ServerThread(Socket socket) {
                this.socket = socket;
            }
            
            public synchronized void run() {
        
                try {
                    PrintWriter Sout = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader Sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    //SERVER CODE FOR INTERACTIONS GOES HERE
					Sout.println("Connection was a success!");
                    
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private static class DiscoveryThread implements Runnable{
            public static DiscoveryThread getInstance() {
                return DiscoveryThreadHolder.INSTANCE;
            }
            private static class DiscoveryThreadHolder {
                private static final DiscoveryThread INSTANCE = new DiscoveryThread();
            }
            DatagramSocket socket;
            
            public void run() {
                try {
                    //Keep a socket open to listen to all the UDP trafic that is destined for this port
                    socket = new DatagramSocket(42317, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);
					System.out.println("Running DiscoveryThread!");
                    while (true) {
                      //Receive a packet
                      byte[] recvBuf = new byte[15000];
                      DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                      socket.receive(packet);
                      System.out.println("Sniffed a packet");
                      //See if the packet holds the right command (message)
                      String message = new String(packet.getData()).trim();
                      if (message.startsWith("RESPOND_AVAIL_SERV_")) {
                          
                          System.out.println("Sniffed a GOOD packet");
                          byte[] sendData = ("AVAIL_SERVER_" + fetchIP() + "_" + Server.getInstance().port).getBytes();
                          
                          //Send a response
                          
                          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(message.split("_")[3]), packet.getPort());
                          socket.send(sendPacket); 
                          System.out.println(getClass().getName() + ">>>Sent packet to: " + sendPacket.getAddress().getHostAddress() + ":" + packet.getPort());
                      }
                    }
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
