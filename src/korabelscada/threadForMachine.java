/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package korabelscada;

import JDBCmodule.mySQLsend;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static korabelscada.KorabelSCADA.fromHex;
import static korabelscada.KorabelSCADA.getTime;

/**
 *
 * @author korabel245
 */
public class threadForMachine extends Thread implements Runnable {

    private Socket s;
    public String address;
    public int port;
    
    public String machineName;
    private String timeStart;
    private String timeStop;
    private String dateStart;
    private String dateStop;
    private int upTimeStart;
    private int upTimeStop;

    byte[] requestBothRegTime = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x02, (byte) 0x26, (byte) 0x8E};
    byte[] requestForStatus = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x1A, (byte) 0x00, (byte) 0x01, (byte) 0xA6, (byte) 0x8C};
    byte[] requestForOnTimes = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x02, (byte) 0x47, (byte) 0x4D};

    String inProgress = "3425680";
    String inIdle = "1328200";
    boolean isStarted;

    public threadForMachine(String IPAddress, int port, String machineName) {
        this.address = IPAddress;
        this.port = port;
        this.machineName = machineName;
    }

    public void run() {

        System.out.print("Current status of" + address + " is: ");
        try {
            if (status() == true) {
                System.out.println("ONLINE");
            } else {
                System.out.println("OFFLINE");
            }
        } catch (IOException ex) {
            Logger.getLogger(threadForMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
        do {
            try {
                getRequest(address, "Fiorenza");
            } catch (IOException ex) {
                Logger.getLogger(threadForMachine.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);

    }

    private void getRequest(String IP, String machineID) throws IOException {
//        System.out.println("get uptime" + getUptime());
        if (status() == true && isStarted == false) {
            isStarted = true;
            timeStart = getCurrentTime();
            dateStart = getCurrentDate();
            upTimeStart = getUptime();
//            System.out.println("Time is " + getUptime());
//            System.out.println("Times on " + getOnTimes());
            System.out.println(machineName + " IS ALIVE!!!");
        }
        if (status() == false && isStarted == true) {
            timeStop = getCurrentTime();
            dateStop = getCurrentDate();
            upTimeStop = getUptime();
            int workTime = upTimeStop - upTimeStart;
            System.out.println(machineName + " Worked " + workTime + " secs From " + timeStart + " till " + timeStop + " at the date " + dateStart);
            Thread mySQLThread = new mySQLsend(machineName, timeStart, timeStop, dateStart, dateStop, upTimeStart, upTimeStop, workTime);
            mySQLThread.start();
            isStarted = false;
            System.out.println(machineName + " IS DEAD!!!!");
        }
    }

    public boolean status() throws IOException {
        String status = workFlow(requestForStatus, address, port, 7);
        if (status.equals(inProgress)) {
            return true;
        }
        return false;
    }

    public int getUptime() throws IOException {
        int upTime = Integer.parseInt(workFlow(requestBothRegTime, address, port, 7));
        return upTime;
    }

    public int getOnTimes() throws IOException {
        int onTimes = Integer.parseInt(workFlow(requestForOnTimes, address, port, 7));
        return onTimes;
    }

    public String workFlow(byte[] comm, String address, int port, int length) throws IOException {
        String dataBlock = "0";

        try {
            byte inp[] = new byte[9];
            s = createSocket(address, port);
            s.getOutputStream().write(comm);
            s.setSoTimeout(1000);
            Thread.sleep(500);
            s.getInputStream().read(inp);
//            System.out.println("Command sent");
            dataBlock = Integer.toString(fromHex(getTime(inp, length)));
            
            getTime(inp, length);

//            for (int i = 0; i < inp.length; i++) {
//                System.out.print(" " + Integer.toHexString(Byte.toUnsignedInt(inp[i])));
//            }
//            System.out.println("");
            
            s.close();

        } catch (Exception e) {
            System.out.println(machineName + " init error: " + e.getMessage());
            if (e.getMessage().contains("Read timed out")) {
                System.out.println("Connection lost. Attempting to reconnect");
                s.close();
                System.out.println("Current status is " + status());
            }
        }
        return dataBlock;
    }

    public static Socket createSocket(String address, int port) throws Exception {
        Socket socket = new Socket(address, port);
        return socket;
    }
    
    // метод возвращающий текущую дату 
    private static String getCurrentDate() {
        DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd");
        Date date = new Date();
        String currentDate = timeFormat.format(date);
        return currentDate;
    }

    //Метод возвращающий текущее время
    private static String getCurrentTime() {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String currentTime = timeFormat.format(date);
        return currentTime;
    }
    
    	public int countCRC16(byte [] buf) {
		int i, j;
		int c, crc = 0xFFFF;
		for (i = 0; i < buf.length; i++) {
			c = buf[i] & 0x00FF;
			crc ^= c;
			for (j = 0; j < 8; j++) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0xA001;
				} else
					crc >>= 1;
			}
		}
                System.out.println(crc);
		return (crc);
	}

}
