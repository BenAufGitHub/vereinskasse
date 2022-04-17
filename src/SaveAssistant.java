import com.google.gson.Gson;
import users.Person;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


public class SaveAssistant {
    private static String saveFileValidationRegEx = "\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+, \\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+#\\d+\\.json";
    public static String savePath = ".\\resources\\save_files\\";

    public static void speicherePerson(Person p) {
        String jsonString = new Gson().toJson(p);
        String path = savePath + constructFileName(p.getVorname(), p.getNachname(), p.getID());
        schreibe(jsonString, path);
    }


    public static Person ladePerson(Person.Personenbeschreibung pb){
        return ladePerson(pb.vorname, pb.nachname, pb.id);
    }


    /** Gibt null zurück wenn File nicht gefunden. */
    public static Person ladePerson(String vorname, String nachname, int id){
        String filename = constructFileName(vorname, nachname, id); 
        if(!Arrays.asList(listSaveDirectory()).contains(filename))
            return null;
        Path path = Paths.get(savePath + filename);
        return lesePerson(path);
    }

    public static int greifeSchuldenBetrag(Person.Personenbeschreibung p){
        Person person = ladePerson(p);
        if(person == null)
            throw new IllegalArgumentException("File not Found: "+constructFileName(p.vorname, p.nachname, p.id));
        return person.getRestSchulden();
    }

    public static ArrayList<Person.Personenbeschreibung> getPersonenNamen() {
        ArrayList<Person.Personenbeschreibung> liste = new ArrayList<>();
        for(String s: listSaveDirectory()){
            if(istSaveFile(s)){
                liste.add(getBeschreibungFromFile(s));
            }
        }
        return liste;
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

    private static String constructFileName(String vorname, String nachname, int id) {
        return nachname + ", " + vorname +"#"+id+".json";
    }

    private static void schreibe(String text, String pfad) {
        try (FileWriter fw = new FileWriter(new File(pfad));
             BufferedWriter writer = new BufferedWriter(fw)) {
             writer.write(text);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static Person lesePerson(Path path) {
        try {
            String jsonObj = Files.readString(path, StandardCharsets.UTF_8);
            return new Gson().fromJson(jsonObj, Person.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Person.Personenbeschreibung getBeschreibungFromFile(String filename) {
        int comma = filename.indexOf(',');
        int hashtag = filename.indexOf('#');
        int json = filename.indexOf(".json");
        String nachname = filename.substring(0, comma);
        String vorname = filename.substring(comma+2, hashtag);
        int id = Integer.parseInt(filename.substring(hashtag+1, json));
        return new Person.Personenbeschreibung(vorname, nachname, id);
    }

    private static boolean istSaveFile(String filename) {
        if(filename.indexOf(',') != filename.lastIndexOf(',')) return false;
        if(filename.indexOf('#') != filename.lastIndexOf('#')) return false;
        return filename.matches(saveFileValidationRegEx);
    }
}
