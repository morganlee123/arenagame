package com.nexide.main.net;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectToServer implements Callable<String> {
    //Each tick, client should... send next movement to server
                                //get other positions/health from server
                                //get bullets from server
    
    private static final int TPS = 30; //ticks per second
    
    private static final int HEALTH_DROP = 4;
    private static final int TIMEOUT1=1; //How long the client checks for a server before creating its own, in seconds.
    private static final byte MAX_PARTY_SIZE = 8; //Max party size
    private static final int MAX_PT_YCOORD = 35; //"barrier" for points, max y-value to earn points
    private static final int MIN_PT_YCOORD = 20; //"barrier" for points, min y-value to earn points
    
    
    private static ArrayList<String> DNSListings = new ArrayList<String>(); //Stored as [IP]@[channel] ex.: "192.168.1.1@4444"
    
    private static Socket mSocket = null;    //mSocket is the main socket
    private static BufferedReader in = null; //receives server messages
    private static PrintWriter out = null;   //sends messages to server
    private static Thread serverThread = null;

    private static int startPosX[] = {-1344 ,-1536,-1344,-1536,-1344,-1536,-1344,-1536};
    private static int startPosY[] = { 144  ,  144,   16,   16,-2736,-2736,-2864,-2864};
    public static int ID = -1;
    
    public static void initialize() {
        
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
        
        try {send(System.getProperty("user.name"));} catch(Exception e) {e.printStackTrace();}
        try {ID = Integer.parseInt(receive());} catch (Exception e) { e.printStackTrace(); }
        //STEP 4: Get original packet of information from the server.
        
        
    }
    
    public static void send(String message) throws IOException{
        out.println(message);
    }
    
    public static String receive() throws IOException{
        return in.readLine();
    }
    
    public static int getTick() {
    	try {
			send("getTick");
			return Integer.parseInt(receive());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 0;
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
        try (DatagramSocket c = new DatagramSocket()){
            //Open a random port to send the package
            
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
                
                //Check if the message is correct
                String message = new String(receivePacket.getData()).trim();
                boolean AlreadySent = false;
                for (int i = 0; i < DNSListings.size(); i++)
                    if (message.equals(DNSListings.get(i))) AlreadySent = true;
                if (message.startsWith("AVAIL_SERVER_") && !AlreadySent) {
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
    
    
    public static class Server implements Runnable {
        private int port = 4444; //accessed by gsp
        private int tick = -2; //signifies waiting for players
        private static boolean isRunning = false;
        
        private static Server s = new Server(); //accessed by getInstance
        
        public ReentrantLock ServerLock = new ReentrantLock(true);
        
        private static Rectangle enemy1, enemy2, enemy3, enemy4;
        private static boolean s1 = false;
        
        private static ArrayList<Thread> clients = new ArrayList<Thread>();
        private static ArrayList<String> unames = new ArrayList<String>(); //Requires Lock
        private static ArrayList<Integer> xCoords = new ArrayList<Integer>(); //Requires Lock
        private static ArrayList<Integer> yCoords = new ArrayList<Integer>(); //Requires Lock
        private static ArrayList<Integer> health = new ArrayList<Integer>();
        private static ArrayList<Integer> rotation = new ArrayList<Integer>();
        private static ArrayList<Integer> shootStatus = new ArrayList<Integer>();
        private static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
        private static ArrayList<Boolean> shooting = new ArrayList<Boolean>();
        private static ArrayList<Integer> shotTick = new ArrayList<Integer>();

        private int BlueTeamScore = 0;
        private int RedTeamScore = 0;
        
        public static Server getInstance() {return s;}
        
        public int getServerPort() {return port;}
        
        public static ArrayList<String> getUsernames(){return unames;}
        
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
                    Thread temp = new ServerThread(serverSocket.accept(),clients.size());
                    unames.add("<awaiting connection>");
                    xCoords.add(startPosX[clients.size()]);
                    yCoords.add(startPosY[clients.size()]);
                    health.add(100);
                    rotation.add(0);
                    shooting.add(false);
                    shootStatus.add(0);
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
            try{Thread.sleep(100);}catch(Exception e){}
            System.out.println(Arrays.toString(unames.toArray()));
            tick = -1; //signifies syncing
            try {Thread.sleep(100);} catch (Exception e) {} //Small delay to sync
            tick++;
            double mspt = (1.0/((double)TPS))*1000.0;
            System.out.println("Expected MSPT: " + mspt);
            while (isRunning) {
                long START = System.currentTimeMillis();
                
     
                Rectangle leftzone, rightzone, midzone;
                
                leftzone = new Rectangle((5*64), (24*64), (10*64), (8*64));
                midzone = new Rectangle((30*64), (22*64), (6*64), (12*64));
                rightzone = new Rectangle((51*64),(24*64), (10*64), (8*64));
                
                // 0. update where the player hitboxes are
                
                if (ID > 3) { /*TODO: PROBLEM WITH THIS CODE IS THAT IT ONLY MAKES ONE TEAM ATTACKABLE AND THE OTHER CANT BE KILLED*/
                	enemy1 = new Rectangle(0-(xCoords.get(0)-640),0-(yCoords.get(0)-400),64,64);
                	enemy2 = new Rectangle(0-(xCoords.get(1)-640),0-(yCoords.get(1)-400),64,64);
                	enemy3 = new Rectangle(0-(xCoords.get(2)-640),0-(yCoords.get(2)-400),64,64);
                	enemy4 = new Rectangle(0-(xCoords.get(3)-640),0-(yCoords.get(3)-400),64,64);
                } else {
                	enemy1 = new Rectangle(0-(xCoords.get(4)-640),0-(yCoords.get(4)-400),64,64);
                	enemy2 = new Rectangle(0-(xCoords.get(5)-640),0-(yCoords.get(5)-400),64,64);
                	enemy3 = new Rectangle(0-(xCoords.get(6)-640),0-(yCoords.get(6)-400),64,64);
                	enemy4 = new Rectangle(0-(xCoords.get(7)-640),0-(yCoords.get(7)-400),64,64);
                	s1 = false;
                }
                
                //1. update the scores
                
                
                for (int i = 0; i < 4; i++) {
                    if (leftzone.contains(new Point(0-(xCoords.get(i)-640), 0-(yCoords.get(i)-400))))
                        BlueTeamScore++;
                    if (rightzone.contains(new Point(0-(xCoords.get(i)-640), 0-(yCoords.get(i)-400))))
                        BlueTeamScore++;
                    if (midzone.contains(new Point(0-(xCoords.get(i)-640),0-(yCoords.get(i)-400))))
                        BlueTeamScore++;
                }
                
                for (int i = 4; i < 8; i++) {
                	if (leftzone.contains(new Point(0-(xCoords.get(i)-640), 0-(yCoords.get(i)-400))))
                        RedTeamScore++;
                	if (rightzone.contains(new Point(0-(xCoords.get(i)-640), 0-(yCoords.get(i)-400))))
                		RedTeamScore++;
                	if (midzone.contains(new Point(0-(xCoords.get(i)-640), 0-(yCoords.get(i)-400))))
                		RedTeamScore++;
                }

                System.out.println("Red: " + (RedTeamScore/30) + " Blue: " + (BlueTeamScore/30));
                
                //2. update the bullets
                
                //lol I never MADE the bullets
                boolean isShoot = false;
                for (int i = 0 ; i < shooting.size(); i++)
                	if (shooting.get(i)) {
                		bullets.add(new Bullet(xCoords.get(i),yCoords.get(i),rotation.get(i),i));
                		isShoot = true;
                	}
                if (isShoot) {
                	System.out.println("Someone's shooting");
                
                }
                for(int i = bullets.size() - 1; i >= 0; i--) {
                    if (bullets.get(i).shouldDispose()) {
                        bullets.remove(i);
                        System.out.println("DELETED: BULLET");
                    }
                    else {
                        bullets.get(i).updatePosition();
                        System.out.println("UPDATED BULLET");
                    }
                }
                if (bullets.size() > 0) System.out.println("updated " + bullets.size() + " bullets");
                
                //3. update the health
                
                for (Bullet bullet : bullets)
                    bullet.appendHealth();
                    
                //4. update time
                
                double FIN = (double) (System.currentTimeMillis() - START);
                //System.out.println("MSPT = " + mspt + ", FIN = " + FIN + "...");
                if (FIN < mspt)
                    try{Thread.sleep((long)(mspt-FIN));}catch (Exception e) {}
                FIN = System.currentTimeMillis() - START;
                tick++;
                if ((tick != 0) && (tick % 120 == 0))
                    System.out.println("Sample frame's FPS: " + 1/(FIN/1000));
            }
        }
        
        private static class Bullet {
            private static final int BULLETSPEED = 64 * (400 / TPS); //400 Meters per second = ~13 meters per tick @ 30TPS
            
            private double xVelocity;
            private double yVelocity;
            private double currentX;
            private double currentY;
            private double previousX;
            private double previousY;
            private int userID;
            
            public Bullet(double startX, double startY, int degreesRotation, int uid) {
            	System.out.println("CREATED: BULLET");
                currentX=startX; 
                currentY=startY;
                xVelocity = BULLETSPEED*Math.cos(Math.toRadians(degreesRotation));
                yVelocity = BULLETSPEED*Math.sin(Math.toRadians(degreesRotation));
                userID = uid;
            }
            
            public void updatePosition() {
                previousX = currentX;
                previousY = currentY;
                currentX += xVelocity;
                currentY += yVelocity;
            }
            
            public double getX() { return currentX; }
            public double getY() { return currentY; }
            
            public void appendHealth() {
                for (int i = 0; i < Server.getInstance().xCoords.size(); i++) {
                    if (inLine(currentX,currentY,previousY,previousX,i)) {
                        System.out.println("HIT: User = " + Server.getInstance().unames.get(i));
                        Server.getInstance().health.set(i,Server.getInstance().health.get(i) - HEALTH_DROP);
                    }
                }
            }
            
            public boolean shouldDispose() {
            	System.out.println("448-CTS x: " + currentX + "y: " + currentY);
                return (currentX > 64*64 || currentX < 0 || currentY > 54*64 || currentY < 0*64);
            }
            
            private static boolean inLine(double x, double y, double oldX, double oldY,int playerID) { //a and b are bullet path, c is player
               
            	//1. is the player between the endpoints of the bullets, give or take 32 px.
            	double smallStartX = x - 32;
            	double largeStartX = x + 32;
            	double smallStartY = y - 32;
            	double largeStartY = y + 32;
            	double smallEndX = oldX - 32;
            	double largeEndX = oldX + 32;
            	double smallEndY = oldY - 32;
            	double largeEndY = oldY + 32;
            	if (smallStartY > smallEndY)
            		smallStartY = smallEndY;
            	if (smallStartX > smallEndX)
            		smallStartX = smallEndX;
            	if (largeStartY < largeEndY)
            		largeStartY = largeEndY;
            	if (largeStartX < largeEndX)
            		largeStartX = largeEndX;
            	
            	int playerX = xCoords.get(playerID);
            	int playerY = yCoords.get(playerID);
            	
            	System.out.println(smallStartX + "<" + playerX + "<" + largeStartX);
            	System.out.println(smallStartY + "<" + playerX + "<" + largeStartY);
            	
            	if (smallStartX <= playerX && largeStartX >= playerX && smallStartY <= playerY && largeStartY >= playerY)
            		return true;

            	return false;
//            	
//            	//1. is it between A and B?
//               if (!((Ax <= Cx && Cx < Bx) || (Ax >= Cx && Cx > Bx))) {
//                   return false;
//               }
//               if (!((Ay <= Cy && Cy < By) || (Ay >= Cy && Cy > By))) {
//                   return false;
//               }
//               
//               //2. does it have the same slope as A and B?
//               //   a) Vertical?
//               if (Ax == Cx) return Bx == Cx;
//               if (Bx == Cx) return Ax == Cx;
//               //   b) same slope?
//               return (Ay - Cy)/(Ax - Cx) == (Cy - By)/(Cx - Bx);
            }
        }
        
        private static class ServerThread extends Thread {
            private Socket socket = null;
            private final int THREAD_ID;
        
            public ServerThread(Socket socket, int threadID) {
                this.socket = socket;
                THREAD_ID=threadID;
            }
            
            public static int temp = 0;
            
            public synchronized void run() {
            	
            	try {
                    PrintWriter Sout = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader Sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    
                    //SERVER CODE FOR INTERACTIONS GOES HERE
                    Sout.println("Connection was a success!");                  //Success message (confirmation)
                    Server.getInstance().unames.set(THREAD_ID,Sin.readLine());  //Username
                    Sout.println(""+THREAD_ID);
                    
                    while (true) {
                        String input = Sin.readLine();
                        String output = "";
                        //System.out.print("input: " + input);
                        if (input.equalsIgnoreCase("getUserPositions")) {
                            output = Server.getInstance().xCoords.get(0) + "," + Server.getInstance().yCoords.get(0);
                            for(int i = 1; i < Server.getInstance().xCoords.size(); i++) {
                                output = output + "&" + Server.getInstance().xCoords.get(i) + "," + Server.getInstance().yCoords.get(i);
                                int ttemp = Server.getInstance().xCoords.get(i) + Server.getInstance().yCoords.get(i);
                                if (ttemp != temp)
                                	//System.out.println(output);
                            	temp = ttemp; 
                            }
                        } else if (input.equalsIgnoreCase("getUserHealth")) {
                            output = "" + Server.getInstance().health.get(0);
                            for(int i = 1; i < Server.getInstance().xCoords.size(); i++)
                                output = output + "&" + Server.getInstance().health.get(i);
                        } else if (input.equalsIgnoreCase("getHealth")) {
                        	output = "" + health.get(THREAD_ID);
                        } else if (input.equalsIgnoreCase("getDirections")) {
                        	output = "" + Server.getInstance().rotation.get(0);
                        	for (int i = 0; i < Server.getInstance().rotation.size(); i++)
                        		output = output + "&" + Server.getInstance().rotation.get(i);
                        } else if(input.equalsIgnoreCase("getTick")) {
                        
                        	output = "" + Server.getInstance().tick;
                        } else if (input.equalsIgnoreCase("shoot")) {
                            
                        	if (enemy1.contains(new Point(0-(xCoords.get(ID)-640), 0-(yCoords.get(ID)-400))))
                        		if (s1)
                        			health.set(0,health.get(0) - HEALTH_DROP);
                        		else
                        			health.set(4,health.get(4) - HEALTH_DROP);
                        	if (enemy2.contains(new Point(0-(xCoords.get(ID)-640), 0-(yCoords.get(ID)-400))))
                        		if (s1)
                        			health.set(1,health.get(1) - HEALTH_DROP);
                        		else
                        			health.set(5,health.get(5) - HEALTH_DROP);
                        	if (enemy3.contains(new Point(0-(xCoords.get(ID)-640),0-(yCoords.get(ID)-400))))
                        		if (s1)
                        			health.set(2,health.get(2) - HEALTH_DROP);
                        		else
                        			health.set(6,health.get(6) - HEALTH_DROP);
                        	if (enemy4.contains(new Point(0-(xCoords.get(ID)-640),0-(yCoords.get(ID)-400))))
                        		if (s1)
                        			health.set(3,health.get(3) - HEALTH_DROP);
                        		else
                        			health.set(7,health.get(7) - HEALTH_DROP);

                        } else if (input.equalsIgnoreCase("Respawn")) {
                            Server.getInstance().xCoords.set(THREAD_ID,startPosX[THREAD_ID]);
                            Server.getInstance().yCoords.set(THREAD_ID,startPosY[THREAD_ID]);
                            Server.getInstance().health.set(THREAD_ID,100);
                            output = startPosX[THREAD_ID] + "&" + startPosY[THREAD_ID];
                        }else if (input.startsWith("sendInput")) {
                            String portions[] = input.split("/");
                            
                            Server.getInstance().rotation.set(THREAD_ID,Integer.parseInt(portions[1]));
                            Server.getInstance().xCoords.set(THREAD_ID,Integer.parseInt(portions[2]));
                            Server.getInstance().yCoords.set(THREAD_ID,Integer.parseInt(portions[3]));
                            Server.getInstance().shooting.set(THREAD_ID,Boolean.parseBoolean(portions[4]));
                            output = "success";
                        } else if (input.equalsIgnoreCase("getBulletPositions")) {
                            if (Server.getInstance().bullets.size() < 1)
                                output = "no bullets.";
                            else
                                output = Server.getInstance().bullets.get(0).getX() + "," + Server.getInstance().bullets.get(0).getY();
                            for (int i = 1; i < Server.getInstance().bullets.size(); i++)
                                output = output + "&" + Server.getInstance().bullets.get(i).getX() + "," + Server.getInstance().bullets.get(i).getY();
                        } else if (input.equalsIgnoreCase("getScore")) {
                            output = Server.getInstance().BlueTeamScore/30 + "&" + Server.getInstance().RedTeamScore / 30;
                        }
                        Sout.println(output);
                    }
                    
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
                    //Keep a socket open to listen to all the UDP traffic that is destined for this port
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
                          byte[] sendData = ("AVAIL_SERVER_" + fetchIP() + "_" + Server.getInstance().port + "_" + System.getProperty("user.name")).getBytes();
                          
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
