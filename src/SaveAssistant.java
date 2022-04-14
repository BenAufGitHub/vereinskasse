import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SaveAssistant {

    public static String savePath = ".\\resources\\save_files\\";

    public static void speicherePerson(Person p) {
        String jsonString = new Gson().toJson(p);
        String path = savePath + constructFileName(p.getVorname(), p.getNachname());

        try (FileWriter fw = new FileWriter(new File(path));
             BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(jsonString);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }


    public static Person ladePerson(String vorname, String nachname){
        String match = findSaveMatch(vorname, nachname, listSaveDirectory());
        if(match == null) return null;
        Path path = Paths.get(savePath + match);
        try {
            String jsonObj = Files.readString(path, StandardCharsets.UTF_8);
            return new Gson().fromJson(jsonObj, Person.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *  Momentan konstruiert sie nur den Namen des Files, falls es das gibt.
     *  TODO ausweiten: kontrolle zweier Personen mit gleichem Namen
     */
    public static String findSaveMatch(String vor, String nach, String[] liste) {
        String fileName = constructFileName(vor, nach);
        for(String s : liste) {
            if(fileName.equals(s))
                return fileName;
        }
        return null;
    }

    // ====================== Helfer Methoden ===========================

    private static String[] listSaveDirectory() {
        File dir = new File(savePath);
        File[] fileList = dir.listFiles();
        String[] saveDir = new String[(int) fileList.length];
        for(int i=0; i< fileList.length; i++){
            saveDir[i] = fileList[i].getName();
        }
        return saveDir;
    }

    private static String constructFileName(String vorname, String nachname) {
        return nachname + ", " + vorname + ".json";
    }
}
