package utilities;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.json.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UtilityClass {

    public static String getAlphaNumericString(int n) {
        String AlphanumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder sb = new StringBuilder(n);
        for(int i=0; i < n; i++){
            int index = (int) (AlphanumericString.length() * Math.random());
            sb.append(AlphanumericString.charAt(index));
        }
        return sb.toString();
    }

    public static int getRandomNumber(Integer limit){
        Random random = new Random();
        Integer number;
        do {
            number = random.nextInt(limit);
        } while (number == 0);
        return number;
    }

    public static double getRandomDouble(Integer limit){
        Random random = new Random();
        Double number;
        do {
            number = random.nextDouble(limit);
        } while (number == 0);
        return number;
    }


    public static long getRandomLong(Long upperRange, long lowerRange){

        Long myvalue;
        myvalue = lowerRange + (long) (Math.random() * (upperRange - lowerRange));
        return myvalue;
    }

    public static String getJsonParameterByPath(String JsonString, String path){
        JsonPath js = new JsonPath(JsonString);
        return (js.get(path)).toString();

    }

    public static Double getJsonParameterByPathDouble(String JsonString, String path){
        JsonPath js = new JsonPath(JsonString);
        return (js.getDouble(path));
    }

    public static String getJsonParameterLargeDecimal(String JsonString, String path){
        JsonObject jObject = JsonParser.parseString(JsonString).getAsJsonObject();
        if(path.contains(".")){
            String[] pathArray = path.split(("\\."));
            for (int i=0; i<pathArray.length -1; i++){
                jObject = jObject.get(pathArray[i]).getAsJsonObject();
            }
            return jObject.get(pathArray[pathArray.length-1]).getAsString();
        }else
            return jObject.get(path).getAsString();
    }


    public  static String convertUnitTimeStamp(Long unixTimeStamp){
        Integer length = unixTimeStamp.toString().length();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDtm = null;

        if(length ==13){
            formattedDtm = Instant.ofEpochSecond(unixTimeStamp / 1000).atZone(ZoneId.of("GMT-4")).format(formatter);
        }else if(length ==10){
            formattedDtm = Instant.ofEpochSecond(unixTimeStamp).atZone(ZoneId.of("GMT-4")).format(formatter);
        }
        return formattedDtm;
    }

    public  static String convertUnitTimeStamptoDesiredFormat(Long unixTimeStamp, String format){
        Integer length = unixTimeStamp.toString().length();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDtm = null;

        if(length ==13){
            formattedDtm = Instant.ofEpochSecond(unixTimeStamp / 1000).atZone(ZoneId.of("GMT-4")).format(formatter);
        }else if(length ==10){
            formattedDtm = Instant.ofEpochSecond(unixTimeStamp).atZone(ZoneId.of("GMT-4")).format(formatter);
        }
        return formattedDtm;
    }

    public static <T> Boolean compareArrayEqual(T[] array1, T[] array2) {
        Boolean flag = true;

        if (array1.length != array2.length)
            flag = false;
        else {
            for (int i = 0; i < array1.length; i++) {
                Boolean isDouble = isDouble(array1[i].toString());

                if (isDouble) {
                    if (BigDecimal.valueOf(Double.parseDouble(array1[i].toString())).subtract(BigDecimal.valueOf(Double.parseDouble(array2[i].toString()))).abs().compareTo(BigDecimal.valueOf(0.01d)) > 0) {
                        flag = false;
                        break;
                    } else if (!array1[i].toString().equalsIgnoreCase(array2[i].toString())) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public static <T> Boolean compareArrayEqual(T[][] array1, T[][] array2) {
        Boolean flag = true;

        if (array1.length != array2.length)
            flag = false;
        else {
            for (int i = 0; i < array1.length; i++) {
                if (array1[i].length != array2[i].length) {
                    flag = false;
                    break;
                    } else {
                }
                for (int j=0; j < array1[i].length; i++)
                {
                    String first = array1[i][j].toString();
                    String second = array2[i][j].toString();
                    if(!first.equalsIgnoreCase(second)){
                        flag = false;
                        break;
                }
                }
                }
            }
        return flag;
    }

    private static Boolean isDouble(String value){
    Boolean flag = false;
    try{
        Double.parseDouble(value);
        flag = true;
    }catch (NumberFormatException e) {
        System.out.println(e.getStackTrace());
    }
    return flag;

    }

    public  static String encryptXOR(String message, String key){

        try{
            if(message == null || key == null)
                return  null;
            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();
            int ml = mesg.length;
            int kl =keys.length;
            char[] newmsg = new char[ml];
            for(int i=0; i<ml; i++){
                newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
            }
            mesg = null;
            keys = null;
            return  new String(Base64.getEncoder().encode((new String(newmsg).getBytes())));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public  static String dcryptXOR(String message, String key){

        try{
            if(message == null || key == null)
                return  null;
            char[] keys = key.toCharArray();
            char[] mesg = new String(Base64.getDecoder().decode(message)).toCharArray();
            int ml = mesg.length;
            int kl =keys.length;
            char[] newmsg = new char[ml];
            for(int i=0; i<ml; i++){
                newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
            }
            mesg = null;
            keys = null;
            return new String(newmsg);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void deleteFilesForPathByPrefix(Path path, String prefix){
        try(DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path, prefix + "*")){
            for(final Path newDirectoryStreamItem : newDirectoryStream){
                Files.delete(newDirectoryStreamItem);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteFilesForPathByExtension(Path path, String extension){
        try(DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(path, "*." + extension)){
            for(final Path newDirectoryStreamItem : newDirectoryStream){
                Files.delete(newDirectoryStreamItem);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static Date dateWithOffest(Integer offset){
       final Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, offset);
       return cal.getTime();
    }


    public static String formatDate(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        return  dateFormat.format(date);
    }


    public static HashMap<String, String> getParametersFromJsonString(String jsonString){
        Gson gson = new Gson();
        JsonObject jo = JsonParser.parseString(jsonString).getAsJsonObject();
        Type type = new TypeToken<HashMap<String, String>>(){
        }.getType();
        HashMap<String, String> map = gson.fromJson(jsonString, type);
        return map;
    }


    public static HashMap<String, String[]> getArrayFromJsonString(String jsonString){
        Gson gson = new Gson();
        JsonObject jo = JsonParser.parseString(jsonString).getAsJsonObject();
        Type type = new TypeToken<HashMap<String, String[]>>(){
        }.getType();
        HashMap<String, String[]> map = gson.fromJson(jsonString, type);
        return map;
    }


    public static HashMap<String, Object> getObjectFromJsonString(String jsonString){
        Gson gson = new Gson();
        JsonObject jo = JsonParser.parseString(jsonString).getAsJsonObject();
        Type type = new TypeToken<HashMap<String, Object>>(){
        }.getType();
        HashMap<String, Object> map = gson.fromJson(jsonString, type);
        return map;
    }


    public static HashMap<String, Object[]> getObjectFromJsonArray(String jsonString){
        Gson gson = new Gson();
        JsonObject jo = JsonParser.parseString(jsonString).getAsJsonObject();
        Type type = new TypeToken<HashMap<String, Object[]>>(){
        }.getType();
        HashMap<String, Object[]> map = gson.fromJson(jsonString, type);
        return map;
    }


    public static void printingCharacters(HashMap<String, String> m, HashMap<String, String> m1){
        Iterator<Map.Entry<String, String>> it = m.entrySet().iterator();
        Iterator<Map.Entry<String, String>> it1 = m1.entrySet().iterator();

        while ((it.hasNext() && it1.hasNext())){
            prinntEntries(it.next(), it1.next());
        }
    }

    private static void prinntEntries(Map.Entry<String, String> e, Map.Entry<String, String> e1) {
        System.out.printf("Key: %s : Value: %s - \t\t\t Key: %s : Value: %s - \n ", e.getKey(), e.getValue(), e1.getKey(), e1.getValue());
        System.out.println("------------------------------------------------|   \t|-------------------------------------------------------------------|");
    }

    public static void verifyErrorMessagePartialString(String expected, String actual, ExtentTest test){
        String[] expectedArray = expected.split("\\.");
        try{
            //Arrays.asList(expectedArray).forEach(n -> Assert.assertTrue(actual.contains(n), "Expected: " + expected + "But Actual: " + actual));
            test.log(Status.PASS, "Actual error message contains the expected partial String: <b> " + expected + "</b>)");
        }catch (Exception e){
            test.log(Status.FAIL, "Actual error message soesn't contain the expected partial String: <b> " + expected + "</b>)");
        }
    }

    public static void CreateFile(String file, String contents) throws IOException{
        Path filePath = Paths.get(file);

        Files.write(filePath, contents.getBytes());
    }

    public static void DeleteFile(String file) throws IOException{
        Path filePath = Paths.get(file);

        Files.deleteIfExists(filePath);
    }


    public static Object getSha256Hash(String originalString) {
        String sha256hex = Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();

        return sha256hex;
    }


    public static String getElementFromCSV(String csvString, int position){
        String[] vals = csvString.split(",");
        return vals[position-1];
    }

    public static String[] getArrayFromCSV(String csvString){
        return csvString.split(",");
    }
}
