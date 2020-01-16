/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class ConfigFileManager {

    public static String defaultModuleAddress = "192.168.8.202";
    public static String defaultLEDmoduleAddress = "192.168.8.203";
    public static String defaultUserName = "hb";
    public static String defaultHostName = "hb";
    public static String defaultPassword = "XDWV75SCJEdP";
    private static String[] params = {"SOCKET_ONE_ADDRESS", "LED_MODULE_ADDRESS", "DB_USERNAME", "DB_PASSWORD", "DB_HOST"};

    public static String readConfigFile(String request) throws FileNotFoundException, IOException {
        String param = new String();
        Properties props = new Properties();
        boolean yn = true;
        props.load(new FileInputStream("./config/config.ini"));
        param = props.getProperty(request);
        if(param.length() < 1) {
            System.out.println("Параметр " + request + " отсутствует");
            while (yn){
                System.out.println("Восстановить конфиг со значениями по умолчанию? (Y/N)");
                Scanner input = new Scanner(System.in);
                String choice = input.next();
                if (choice.equalsIgnoreCase("y")) createConfigFile();
                if(choice.equalsIgnoreCase("n")) System.exit(0);
            }
        }
        System.out.println(param);
        return param;
    }

    public static boolean checkConfig() throws FileNotFoundException, IOException {
        File configDir = new File("./config");
        if (!configDir.isDirectory()) {
            System.out.println("Каталог конфигов не найден. Создание каталога конфигураций");
            configDir.mkdir();
        } else {
            System.out.println("ConfDir found");
        }
        File configFile = new File("./config/config.ini");
        if (!configFile.isFile()) {
            createConfigFile();
        } else {
            System.out.println("Проверка конфигурационного файла:");
            for (String str : params) {
                System.out.println(str + " " + readConfigFile(str));
            }
        }
        return true;
    }

    public static void createConfigFile() throws FileNotFoundException, IOException {
        System.out.println("Восстанавливаем конфиг");
        File configDir = new File("./config");
        if (!configDir.isDirectory()) {
            System.out.println("Создание каталога конфигураций");
            configDir.mkdir();
        }
//        File configFile = new File("./config/config.ini");
            FileWriter newConfigFile = new FileWriter("./config/config.ini");
            newConfigFile.write("SOCKET_ONE_ADDRESS = " + defaultModuleAddress + "\nLED_MODULE_ADDRESS = "
                    + defaultLEDmoduleAddress + "\nDB_USERNAME = " + defaultUserName + "\nDB_PASSWORD ="
                    + defaultPassword + "\nDB_HOST = " + defaultHostName);
            System.out.println("Конфиг создан, загружены значения по умолчанию.");
            newConfigFile.close();
        
        System.out.println("Перезапустите приложение");
        System.exit(0);
    }

}
