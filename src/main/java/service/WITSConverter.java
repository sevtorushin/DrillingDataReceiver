package service;

import entity.WITSPackage;
import annotations.WITSPackageCode;
import annotations.WITSRecordCode;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class WITSConverter implements Convertible<WITSPackage>{

    public WITSPackage convert(byte[] data, Class<? extends WITSPackage> clazz) {
        String WITSData = new String(data, StandardCharsets.UTF_8);
        WITSPackage witsPackage = null;
        try {
            witsPackage = buildObject(WITSData, clazz);
        } catch (IllegalAccessException | InstantiationException e) {
            System.err.println("WITSObject creation error");
            e.printStackTrace();
        }
        return witsPackage;
    }

    private Object[] buildWITSArrayValues(String WITSData, Class<? extends WITSPackage> clazz){
        Object[] fieldValues = new Object[99];
        String witsRecordCode = clazz.getAnnotation(WITSPackageCode.class).code();
        Arrays.stream(WITSData.split("\r\n"))
                .filter(s -> s.matches(witsRecordCode + "\\d{2}-?\\d*.?\\d*?"))
                .forEach(s -> fieldValues[Integer.parseInt(s.substring(2, 4))] = s.substring(4));
        fieldValues[5] = LocalDate.parse((String) fieldValues[5], DateTimeFormatter.ofPattern("yyMMdd"));
        fieldValues[6] = LocalTime.parse((String) fieldValues[6], DateTimeFormatter.ofPattern("HHmmss"));
        for (int i = 7; i < 99; i++) {
            if (fieldValues[i] != null && !String.valueOf(fieldValues[i]).isEmpty())
                fieldValues[i] = Double.parseDouble((String) fieldValues[i]);
        }
        return fieldValues;
    }

    private WITSPackage buildObject(String WITSData, Class<? extends WITSPackage> clazz) throws IllegalAccessException, InstantiationException {
        Object[] fieldValues = buildWITSArrayValues(WITSData, clazz);
        WITSPackage witsPackage = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String code = f.getAnnotation(WITSRecordCode.class).code();
            try {
                f.set(witsPackage, fieldValues[Integer.parseInt(code)]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return witsPackage;
    }
}
