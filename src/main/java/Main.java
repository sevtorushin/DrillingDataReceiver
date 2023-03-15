import connection.SIBReceiverServer;
import connection.TransferClient;
import connection.WITSReceivingClient;
import entity.SIBParameter;
import exceptions.DisconnectedException;
import service.SIBConverter;
import service.SIBStreamEmulator;

import java.io.*;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        //Трансляция клиентом дампа SR из бинарного файла----------------------------------------
//        try(TransferClient client = new TransferClient("localhost", 5000)) {
//            SIBStreamEmulator emulator = new SIBStreamEmulator(new File("E:\\Documents\\Java_Projects\\DrillingDataReceiver\\src\\main\\resources\\dumps\\sibDump.bin"));
//            OutputStream os = client.getOutputStreamToServer();
//            byte[] b;
//            for (int i = 0; i < 5; i++) {
//                b = emulator.buildBinaryObject();
//                os.write(b);
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//----------------------------------------------------------------------------------------------
//        Сборка объекта пакета №1 WITS
//        WITSReceivingClient client = new WITSReceivingClient("192.168.0.100", 5050);
//        WITSConverter converter = new WITSConverter();
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("e:\\witsObjectData.bin"));
////        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("e:\\witsBinaryData.bin"));
//        byte[] bytes;
//        while (true) {
//            bytes = client.receiveBytes();
//            WITSPackageTimeBased packageTimeBased = (WITSPackageTimeBased) converter.convert(bytes, WITSPackageTimeBased.class);
//            System.out.println(Arrays.toString(bytes));
//            System.out.println(packageTimeBased);
//            oos.writeObject(packageTimeBased);
//            oos.flush();
////            bos.write(bytes);
////            bos.flush();
//        }
//----------------------------------------------------------------------------------------------
        //Чтение объектов WITS из файла
//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("e:\\witsObjectData.bin"));
//        try {
//            while (true) {
//                System.out.println(ois.readObject());
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//----------------------------------------------------------------------------------------------
        //Прием данных из SR, конвертация в объект и печать в консоль
        try(SIBReceiverServer srv = new SIBReceiverServer(5000)) {
            SIBConverter converter = new SIBConverter();
            while (true) {
                byte[] data = srv.receiveBytes();
                    SIBParameter parameter = converter.convert(data, SIBParameter.class);
                    System.out.println(parameter);
            }
        } catch (DisconnectedException e){
            System.out.println(e.getMessage());
        }
        catch (IOException e){
            e.printStackTrace();
        }
//---------------------------------------------------------------------------------------------
        //Прием данных из SR и запись байтов в бинарный файл
//        SIBReceiverServer srv = new SIBReceiverServer(5111);
//        TransferClient.transferFromTo(srv.getInpStrm(), new FileOutputStream(
//                "E:\\Documents\\Java_Projects\\WITSServer\\src\\main\\resources\\dumps\\slbDump.bin"));
//---------------------------------------------------------------------------------------------
        //Прием данных из SR, конвертация в объект и запись в бинарный файл
//        SIBReceiverServer srv = new SIBReceiverServer(5111);
//        SIBConverter converter = new SIBConverter();
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("e:\\sibData.bin"));
//        while (true) {
//            byte[] b = srv.receiveBytes();
//            SIBParameter parameter = converter.convert(b);
//            oos.writeObject(parameter);
//            oos.flush();
//    }
//---------------------------------------------------------------------------------------------
        //Чтение объектов SR из файла
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("e:\\sibDump.bin"))){
//            while (true) {
//                System.out.println(ois.readObject());
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

//----------------------------------------------------------------------------------------------

        //Эмуляция приема данных от SR. Чтение из файла и печать объектов в консоль
//        SIBStreamEmulator emulator = new SIBStreamEmulator(new File(
//                "E:\\Documents\\Java_Projects\\WITSServer\\src\\main\\resources\\dumps\\sibDump.bin"));
//        SIBConverter converter = new SIBConverter();
//        byte[] data;
//        for (int i = 0; i < 89; i++) {
//            System.out.println(converter.convert(emulator.buildBinaryObject(), SIBParameter.class));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//--------------------------------------------------------------------------------------------

        //Прием данных из SR по протоколу GeoServer и печать байтов в консоль
//        SIBReceiverServer srv = new SIBReceiverServer(1011);
//        FileOutputStream fos = new FileOutputStream("e:\\geoSrv.bin");
//        while (true) {
//            byte[] b = srv.receiveBytes();
////            if (b[1] == 23)
//            fos.write(b);
//            System.out.println(Arrays.toString(b));
//
//        }
//--------------------------------------------------------------------------------------------
    }
}