/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBCmodule;

import ScadaGUI.FXMLDocumentController;
import ScadaGUI.MachineSessions;
import com.jcraft.jsch.Session;
import databaseUnit.SSHDB;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mySQLsend extends Thread implements Runnable {
//        Локальная БД
//        public static final String url = "jdbc:mysql://localhost:3306/happybuttondb?useUnicode=true&characterEncoding=utf8";
//        public static final String user = "root";
//        public static final String password = "";

//    БД Корабела
    public static final String url = "jdbc:mysql://localhost/hb";
    public static final String user = "hb";
    public static final String password = "XDWV75SCJEdP";

    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;

    public static Session SSHSession;

    String name;
    String timeStart;
    String timeStop;
    String dateStart;
    String dateStop;
    int upTimeStart;
    int upTimeStop;
    int session;

    public mySQLsend(String name, String timeStart, String timeStop, String dateStart, String dateStop, int upTimeStart, int upTimeStop, int session) {
        this.name = name;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        this.dateStart = dateStart;
        this.dateStop = dateStop;
        this.upTimeStart = upTimeStart;
        this.upTimeStop = upTimeStop;
        this.session = session;
    }

    public void run() {
        String command = "INSERT INTO scadaTable(name, timeStart, timeStop, dateStart, dateStop, uptimeStart, uptimeStop, session) VALUES('"
                + name + "','" + timeStart + "','" + timeStop + "','" + dateStart + "','" + dateStop + "','" + upTimeStart + "','" + upTimeStop + "','" + session + "')";
//        System.out.println(command);
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.executeUpdate(command);
        } catch (SQLException sqlX) {
            sqlX.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
            }
        }
    }

    public static void getDataForPeriod(String name, String dateFrom, String dateTo) throws ParseException {
        String command = "SELECT * FROM scadaTable WHERE name = '" + name + "' AND dateStart >= '" + dateFrom + "' AND dateStart <= '" + dateTo + "' ORDER BY id";
        String runtime = "";
        String stopTime = "";
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(command);

            rs.last();
            int size = rs.getRow();
            rs.first();
//            System.out.println(size);
            for (int i = 0; i < size - 1; i++) {
                MachineSessions session = new MachineSessions();
                rs.next();
                runtime = runtime + rs.getString(5) + " " + rs.getString(3);
                stopTime = stopTime + rs.getString(6) + " " + rs.getString(4);
                session.setSession((int) timeConvert(stopTime) - (int) timeConvert(runtime));
                session.setName(rs.getString(2));
                session.setDateStart(rs.getString(5));
                session.setUpTimeStart(rs.getInt(7));
                session.setTimeStart(rs.getString(3));
//                session.setSession(rs.getInt(9));
                FXMLDocumentController.tableFills.add(session);
//                System.out.println("ID " + rs.getString(1));
                runtime = "";
                stopTime = "";
            }
        } catch (SQLException sqlX) {
            sqlX.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
            }
            try {
                stmt.close();
            } catch (SQLException se) {
            }
            try {
                rs.close();
            } catch (SQLException se) {
            }
        }

    }

    public static void getSummaryPerPeriod(String name, String dateFrom, String dateTo) {
        String command = "SELECT * FROM scadaTable WHERE name = '" + name + "' AND dateStart >= '" + dateFrom + "' AND dateStart <= '" + dateTo + "' ORDER BY id";
        int firstSession = 0;
        int lastSession = 0;
        try {
            MachineSessions machSess = new MachineSessions();
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(command);
            rs.last();
            lastSession = rs.getInt(7);
            int size = rs.getRow();
            rs.first();
            firstSession = rs.getInt(8);
            int summaryPerPeriod = lastSession - firstSession;
//            System.out.println(rs.getString(2) + " worked " + lastSession + " " + firstSession + " " + (lastSession - firstSession));
            machSess.setName(name);
            machSess.setSession(summaryPerPeriod);
            machSess.setDateStart(dateFrom + " до " + dateTo);
            machSess.setSessions(size);
            FXMLDocumentController.tableFills.add(machSess);
        } catch (SQLException sqlX) {
            sqlX.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
            }
            try {
                stmt.close();
            } catch (SQLException se) {
            }
            try {
                rs.close();
            } catch (SQLException se) {
            }
        }
    }

    public static String[] getMachines() {
        String command = "SELECT name FROM scadaTable GROUP BY name";
        int size = getMachinelistSize();
//            ArrayList machList = new ArrayList();

        String[] list = new String[size];
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(command);

            for (int i = 0; i < size; i++) {
                rs.next();
//                System.out.println(rs.getString(1));
                list[i] = rs.getString(1);
//                machList.add(list);
            }
        } catch (SQLException sqlX) {
            sqlX.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
            }
            try {
                stmt.close();
            } catch (SQLException se) {
            }
            try {
                rs.close();
            } catch (SQLException se) {
            }
        }
        return list;
    }

    public static int getMachinelistSize() {
        String command = "SELECT name FROM scadaTable GROUP BY name";
        int size = 0;
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(command);
            if (rs != null) {
                rs.last();    // moves cursor to the last row
                size = rs.getRow(); // get row id 
            }
//            System.out.println("Size is: " + size);
        } catch (SQLException sqlX) {
            sqlX.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
            }
            try {
                stmt.close();
            } catch (SQLException se) {
            }
            try {
                rs.close();
            } catch (SQLException se) {
            }
        }
        return size;
    }
    public static long tempTime = 0;

    public static long timeConvert(String timeStiring) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//        Date date = formatter.parse(timeStiring);
////        System.out.println(date);
//        long unixTimestamp = date.getTime() / 1000;
//        System.out.println(timeStiring + " UXTS - " + unixTimestamp + " " + (unixTimestamp - tempTime));
//        tempTime = unixTimestamp;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(timeStiring);
        long unixTimestamp = date.getTime()/1000;

        return unixTimestamp;
    }

}
