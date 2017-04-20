import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.net.*;
import java.util.*;
import java.io.*;

class DNS implements Callable<String> {
    private static final int TIMEOUT1=3; //How long the client checks for a server before creating its own, in seconds.
    
    private static ArrayList<String> DNSListings = new ArrayList<String>(); //Stored as [IP]@[channel] ex.: "192.168.1.1@4444"
    
    
    
    public void main(String[] args) {
        
        // STEP 1: Check for an existing server (or... servers?)
        
        sendUserMessage("Looking for an available game...");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new DNS());
        
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
    }
    
    @Override
    public String call() throws Exception {
        try {
            //Open a random port to send the package
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendData = ("RESPOND_AVAIL_SERV_" + ComputerData.fetchIP()).getBytes();
            //Try the 255.255.255.255 first
            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 9876);
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
                    String toPrint = "\t[" + validityCounter + "] " + message.split("_")[3] + " : ";
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
    
    public static String getIPbyName(String name) {
        for (int i = 0; i < DNSListings.size(); i++)
            if (DNSListings.get(i).split("_").length > 4)
                if (DNSListings.get(i).split("_")[4].equalsIgnoreCase(name))
                    return DNSListings.get(i).split("_")[3];
        return "DNS failed.";
    }
    
    private void sendUserMessage(String msg) { // TODO: implement this into the GUI, not a simple println :P
        System.out.println(msg);
    }
}