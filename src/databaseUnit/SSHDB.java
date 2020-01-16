/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseUnit;

import com.jcraft.jsch.*;
import databaseUnit.ConfigFileManager;
import java.io.IOException;
import java.util.logging.Level;

public class SSHDB {

    static int lport;
    static String rhost;
    static int rport;

    public static Session connectSSH() throws IOException{
        String user = "hb";
        String password = "XDWV75SCJEdP";
        String host = "hb";
//        String user = ConfigFileManager.readConfigFile("DB_USERNAME");
//        String password = ConfigFileManager.readConfigFile("DB_PASSWORD");
//        String host = ConfigFileManager.readConfigFile("DB_HOST");
//        try {
//            user = ConfigFileManager.readConfigFile("DB_USERNAME");
//            password = ConfigFileManager.readConfigFile("DB_PASSWORD");
//            host = ConfigFileManager.readConfigFile("DB_HOST");
//        int port = 3306;
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host);
                lport = 3306;
                rhost = "db3";
                rport = 3306;
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                System.out.println("Establishing Connection...");
                session.connect();
                System.out.println("Connection established: " + session.getServerVersion());
                int assinged_port = session.setPortForwardingL(lport, rhost, rport);
                System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
                return session;

            } catch (Exception e) {
                System.err.print(e);
            }
            return null;
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(SSHDB.class.getName()).log(Level.SEVERE, null, ex);
//        }return null;
    }
}
