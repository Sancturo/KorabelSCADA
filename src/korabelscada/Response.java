/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package korabelscada;


public class Response {
    byte slaveID;
    byte funcCode;
    byte byteCount;
    byte [] data;

    public byte getSlaveID() {
        return slaveID;
    }

    public void setSlaveID(byte slaveID) {
        this.slaveID = slaveID;
    }

    public byte getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(byte funcCode) {
        this.funcCode = funcCode;
    }

    public byte getByteCount() {
        return byteCount;
    }

    public void setByteCount(byte byteCount) {
        this.byteCount = byteCount;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getCrc() {
        return crc;
    }

    public void setCrc(byte[] crc) {
        this.crc = crc;
    }
    byte [] crc;
}
