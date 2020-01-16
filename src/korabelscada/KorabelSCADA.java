package korabelscada;

import JDBCmodule.mySQLsend;
import static JDBCmodule.mySQLsend.SSHSession;
import ScadaGUI.FXMLDocumentController;
import databaseUnit.SSHDB;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Properties;
import de.re.easymodbus.server.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.application.Platform;

public class KorabelSCADA extends Thread implements Runnable {

//    Выполнить соединение с сокетом.
//    Дать запрос. EX: 10 03 00 16 00 02 26 8E
//    10 03 00 16 00 04 A6 8C
//    Получить ответ.
//    Проверить ответ и преобразовать данные.
//    Записать в БД
    public static String addressFiorenza = "192.168.9.201";  //Fiorenza
    public static String addressFriulmac = "192.168.10.191";  //Friulmac
    public static int port = 9761;
    public static Socket s;
    public static String workTime = "";

    public static String getWorkTime() {
        return workTime;
    }

    public static void main(String[] args) throws Exception {
        SSHSession = SSHDB.connectSSH();

        Thread friulmac = new threadForMachine(addressFriulmac, port, "Friulmac");
        friulmac.start();

        Thread fiorenza = new threadForMachine(addressFiorenza, port, "Fiorenza");
        fiorenza.start();

        System.out.println("GO");

//        byte [] comm = {(byte)0x10, (byte)0x03, (byte)0x00, (byte)0x16, (byte)0x00, (byte)0x02, (byte)0x26, (byte)0x8E};
//        [10] [03] [0017] [0001] [374F]
//        [10] [03] [0016] [0001] [668F]
//        [10] [03] [001A] [0001] [A68C] status
//        [10] [03] [0017] [0002] [774E] два регистра
//        10 03 00 16 00 02 26 8E  - два регистра из скады
//        10 03 00 18 00 02 47 4D - число включений
//        10 03 04 00 BF 31 28 DF 58 - ответ из опенскады
//        10 03 00 1A 00 01 A6 8C 	
//        10 03 00 16 00 02 26 8E 	OVEN скада запрос времени
//        10 03 00 1A 00 01 A6 8C 	OVEN статус запрос
//        Опрос регистров времени  10 3 4 0 1f 1e 31 2 80 ответ от оборудования
        byte[] requestForFirstRegTime = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x01, (byte) 0x37, (byte) 0x4F};
        byte[] requestForSecondRegTime = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x01, (byte) 0x66, (byte) 0x8F};
        byte[] requestSomething = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x02, (byte) 0x26, (byte) 0x8E};
        byte[] requestBothRegTime = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x02, (byte) 0x26, (byte) 0x8E};
        byte[] requestForStatus = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x1A, (byte) 0x00, (byte) 0x01, (byte) 0xA6, (byte) 0x8C};
        byte[] requestForOnTimes = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x18, (byte) 0x00, (byte) 0x02, (byte) 0x47, (byte) 0x4D};
        byte[] allRegisterRequest = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x03, (byte) 0xE7, (byte) 0x4E};//10 03 00 16 00 03 E7 4E
        byte[] parseHEX = {(byte) 0xc0, 0x56};
        byte[] answer = {(byte) 0x10, (byte) 0x3, (byte) 0x4, (byte) 0x0, (byte) 0x1f, (byte) 0x1e, (byte) 0x31, (byte) 0x2, (byte) 0x80};

        int slaveID = 16;
        byte funcCode = 0x03;
        int regOffset = 22;
        int numberOfRegisters = 2;

//        System.out.println("ads" + threadForMachine.workFlow(requestForStatus, address, port));
//        toHex(regOffset);
//        workFlow(requestForFirstRegTime);
//        workFlow(requestForSecondRegTime);
//        do{
////            System.out.print("Опрос регистров времени ");
////            String worktime = workFlow(requestBothRegTime);
////            System.out.println(worktime);
//            System.out.print("Опрос статуса "); System.out.println(workFlow(requestForStatus));
////            System.out.print("Опрос колва включ ");System.out.println(workFlow(requestForOnTimes));
////            System.out.print("Опрос колва включ ");workFlow(allRegisterRequest);
//        }while(true);
    }

    public static String getTimeCounter() throws IOException {
        int length = 7;
        byte[] requestBothRegTime = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x02, (byte) 0x26, (byte) 0x8E};
        String timeCounter = "";
        timeCounter = workFlow(requestBothRegTime, length);
        return timeCounter;
    }

    public static String workFlow(byte[] comm, int length) throws IOException {
        String dataBlock = "";
        byte[] response;
        try {
            byte inp[] = new byte[10];
            s = createSocket(addressFiorenza, port);
            s.getOutputStream().write(comm);
            System.out.println("Command sent");
            s.setSoTimeout(500);
            Thread.sleep(250);
            s.getInputStream().read(inp);

//            for (int i = 0; i < inp.length; i++) {
//                System.out.print(" " + Integer.toHexString(Byte.toUnsignedInt(inp[i])));
//            }
            System.out.println("");
            dataBlock = Integer.toString(fromHex(getTime(inp, length)));
            getTime(inp, length);
            s.close();
        } catch (Exception e) {
            System.out.println("init error: " + e.getMessage());
            if (e.getMessage().contains("Read timed out")) {
                System.out.println("Connection lost. Attempting to reconnect");
                s.close();
            }
        }
        return dataBlock;
    }

    public static String getTime(byte[] inp, int length) {
        String time = "";
        for (int i = 3; i < length; i++) {
            String temp = (Integer.toHexString(Byte.toUnsignedInt(inp[i])));
            if (temp.length() < 2) {
                temp = "0" + temp;
            }
            time = time + temp;
//            System.out.println(time);
//            System.out.println(inp[i]);

        }

//        System.out.println(time + " " + (Integer.parseUnsignedInt(time, 16)));
        return time;
    }

    public static String response(byte[] inp, int length) {
        String time = "";
        for (int i = 0; i < length; i++) {
            time = time + (Integer.toHexString(Byte.toUnsignedInt(inp[i])));
            System.out.println(inp[i]);
        }

//        System.out.println(time + " " + (Integer.parseUnsignedInt(time, 16)));
        return time;
    }

    public static Socket createSocket(String address, int port) throws Exception {
        Socket socket = new Socket(address, port);
        return socket;
    }

//    public static void commandGenerator(int slaveID, byte funcCode, int regOffset, int numberOfRegisters){
//        Request req = new Request();
//        req.setSlaveID((byte)toHex(slaveID));
//        req.setFuncCode(funcCode);
////        req.setRegOffset((byte)toHex(regOffset));
////        req.
//        
//    }
    public static byte toHex(int toHex) {
        byte hexed = Byte.parseByte(Integer.toHexString(toHex));
        return hexed;
    }

    public static int fromHex(String inp) {
        int decimal = Integer.parseInt(inp, 16);
//        System.out.println(decimal);  
        return decimal;
    }

    public static byte[] CRC(byte[] inp) {
        byte CRC = 0;
        for (int i = 0; i < inp.length; i++) {
            CRC ^= inp[i];
        }
        byte[] command = new byte[(inp.length + 1)];
        for (int i = 0; i < command.length; i++) {
            if (i < inp.length) {
                command[i] = inp[i];
            }
            if (i == inp.length) {
                command[i] = CRC;
            }
        }
        System.out.println("0x" + Integer.toHexString(CRC)); // для проверки преобразования в ХЕКС

        return command;
    }

}
