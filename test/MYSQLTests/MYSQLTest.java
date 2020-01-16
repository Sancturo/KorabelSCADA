/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MYSQLTests;

import JDBCmodule.mySQLsend;
import static JDBCmodule.mySQLsend.con;
import static JDBCmodule.mySQLsend.password;
import static JDBCmodule.mySQLsend.rs;
import static JDBCmodule.mySQLsend.stmt;
import static JDBCmodule.mySQLsend.url;
import static JDBCmodule.mySQLsend.user;
import ScadaGUI.FXMLDocumentController;
import ScadaGUI.MachineSessions;
import databaseUnit.SSHDB;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author korabel245
 */
public class MYSQLTest {

    public MYSQLTest() {
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
//2019-11-05 23:52:57 UXTS - 1546293177
//  Correct is 1572997977  

    @Test
    public void baseTest() throws Exception {

        String myDate = "2019-11-05 23:52:57";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(myDate);
        long millis = date.getTime();
        System.out.println(millis);
    }

}
