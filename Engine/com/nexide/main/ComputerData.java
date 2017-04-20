package com.nexide.main;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 * Quick static methods to get user info such as local ip, username, etc. Will grow this file as needed.
 *
 * @version 4/19
 */
public class ComputerData
{
    // instance variables - replace the example below with your own
    private int x;
    
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
}
