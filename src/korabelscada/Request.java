/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package korabelscada;

/**
 *
 * @author korabel245
 */
public class Request {
    private static Byte CRCHigh;//Переменная контрольной суммы
    private static Byte CRCLow;//Переменная контрольной суммы
    
    public byte[] request(int slaveID, int funcCode, int regOffset, int numberOfReg){
        byte[] generated = {0,0,0,0,0,0,0,0};
        byte[] regisOffset = {0,0};
        generated[0] = KorabelSCADA.toHex(slaveID);
        generated[1] = KorabelSCADA.toHex(funcCode);
        generated[2] = KorabelSCADA.toHex(regOffset);
//        generated[3] = BitConverter
        for (int i = 0; i < 8; i++) {
            System.out.println("generated" + generated[i]);
        }
        return generated;
    }
    
        //Пижженый метод расчёта контрольной суммы CRC-16
    public static void myCRC(byte[] message, int length){
        char CRCFull = (char)0xFFFF;
        for (int i = 0; i < length; i++)
        {
            CRCFull = (char)(CRCFull ^ message[i]);
            for (int j = 0; j < 8; j++)
            {
                if ((CRCFull & 0x0001) == 0)
                    CRCFull = (char)(CRCFull >> 1);
                else
                {
                    CRCFull = (char)((CRCFull >> 1) ^ 0xA001);
                }
            }
        }
        CRCHigh = (byte)((CRCFull >> 8) & 0xFFFF);
        CRCLow = (byte)(CRCFull & 0xFFFF);
    }
    
}
