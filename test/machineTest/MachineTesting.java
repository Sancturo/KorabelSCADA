/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machineTest;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static korabelscada.KorabelSCADA.fromHex;
import static korabelscada.KorabelSCADA.getTime;
import korabelscada.Request;
import korabelscada.threadForMachine;
import static korabelscada.threadForMachine.createSocket;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author korabel245
 */
public class MachineTesting {

    public static String address = "192.168.8.180";  //Holzma
    public static int port = 9761;
    byte[] comm = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x06, (byte) 0x8A};
//    byte[] comm = {(byte) 0x10, (byte) 0x03, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x02, (byte) 0x26, (byte) 0x8E};

    private Socket s;
    private String machineName = "Holzma";

    @Test
    public void testingConnection() throws IOException {

//        String dataBlock = "0";
//        comm = new Request().request(16, 2, 8, 2);
//        System.out.println(toHex(16) + " " + toHex(3) + " " + toHex(850) + " " + toHex(25));
        System.out.println("Bytes " + GetBytesU16(16).length);
//        try {
//            byte inp[] = new byte[9];
//            s = createSocket(address, port);
//            s.getOutputStream().write(comm);
//            s.setSoTimeout(1000);
//            System.out.println("socket " + s);
//            Thread.sleep(500);
//            s.getInputStream().read(inp);
////            System.out.println("Command sent");
//            dataBlock = Integer.toString(fromHex(getTime(inp, 7)));
//
////            getTime(inp, length);
//            for (int i = 0; i < inp.length; i++) {
//                System.out.print(" " + Integer.toHexString(Byte.toUnsignedInt(inp[i])));
//            }
//            System.out.println("");
//            s.close();
//
//        } catch (Exception e) {
//            System.out.println(machineName + " init error: " + e.getMessage());
//            if (e.getMessage().contains("Read timed out")) {
//                System.out.println("Connection lost. Attempting to reconnect");
//                s.close();
//            }
//        }
//        System.out.println("Data is " + dataBlock);
    }

    public static byte[] GetBytesU16(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());
        buffer.putLong(value);
        return buffer.array();
    }

    private static Byte CRCHigh;//Переменная контрольной суммы
    private static Byte CRCLow;//Переменная контрольной суммы

    public static byte toHex(int toHex) {
        byte hexed = Byte.parseByte(Integer.toHexString(toHex));
        return hexed;
    }

//        public static byte[] ModbusCommand8(byte id, byte command, byte startAddress, byte length)
//    {
//        byte[] data = new byte[8];
//        //Адрес устройства
//        data[0] = id;
//        //Код функции Modbus
//        data[1] = command;
//        //Начальный адрес регистра
//        byte[] _adr = BitConverter.getBytes(startAddress);
//        data[2]=_adr[1] ;//[1] младший
//        data[3] =_adr[0];//[0] старший
//        //Колличество регистров которое необходимо считать
//        byte[] _length = BitConverter.getBytes(length);
//        data[4]=_length[1];//[1]
//        data[5] =_length[0];//[0]
//        //Контрольная сумма CRC
//        myCRC(data, 6);
//        data[6] = CRCLow;//[1]
//        data[7] = CRCHigh;//[0]
//        try {Thread.sleep( 200 );}
//        catch (InterruptedException e){}
//        return data;
//    }
//    //Метод расчёта контрольной суммы CRC-16
//    public static void myCRC(byte[] message, int length)
//    {
//        char CRCFull = (char)0xFFFF;
//        for (int i = 0; i < length; i++)
//        {
//            CRCFull = (char)(CRCFull ^ message[i]);
//            for (int j = 0; j < 8; j++)
//            {
//                if ((CRCFull & 0x0001) == 0)
//                    CRCFull = (char)(CRCFull >> 1);
//                else
//                {
//                    CRCFull = (char)((CRCFull >> 1) ^ 0xA001);
//                }
//            }
//        }
//        CRCHigh = (byte)((CRCFull >> 8) & 0xFFFF);
//        CRCLow = (byte)(CRCFull & 0xFFFF);
//    }
}
