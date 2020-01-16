/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author korabel245
 */
public class SocketIO {
    
    public SocketIO() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    byte [] buf = {(byte)0x10, (byte)0x03, (byte)0x00, (byte)0x16, (byte)0x00, (byte)0x03};
    
    @Test
	public void countCRC16() {
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
//		return (crc);
	}
}
        
