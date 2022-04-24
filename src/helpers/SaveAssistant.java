package helpers;

import com.google.gson.Gson;
import users.Person;
import users.Personenbeschreibung;

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


/**
 * Interaktionen mit der Datenbank (also den Speicherdateien)
 */
public class SaveAssistant {
    private static String saveFileValidationRegEx = "\\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+, \\b([A-ZÀ-ÿ][-,a-z. 'äöüß]+[ ]*)+#\\d+\\.json";
    public static String savePath = ".\\resources\\save_files\\";


    public static void speicherePerson(Person p) {
        String jsonString = new Gson().toJson(p);
        String path = savePath + constructFileName(p.getVorname(), p.getNachname(), p.getID());
        schreibe(jsonString, path);
    }


    public static Person ladePerson(Personenbeschreibung pb){
        return ladePerson(pb.vorname, pb.nachname, pb.id);
    }

    /** Gibt null zurück wenn File nicht gefunden. */
    public static Person ladePerson(String vorname, String nachname, int id){
        String filename = constructFileName(vorname, nachname, id); 
        if(!Arrays.asList(listSaveDirectory()).contains(filename))
            return null;
        Path path = Paths.get(savePath + filename);
        return leseObjekt(path, Person.class);
    }


    public static void loeschePerson(Personenbeschreibung pb) {
        String filename = constructFileName(pb.vorname, pb.nachname, pb.id);
        new File(savePath + filename).delete();
    }


    public static void bennenePersonUm(Personenbeschreibung neu, Personenbeschreibung alt) {
        String filename = constructFileName(alt.vorname, alt.nachname, alt.id);
        String newName = constructFileName(neu.vorname, neu.nachname, neu.id);
        Path source = Paths.get(savePath + filename);
        try {
            Files.move(source, source.resolveSibling(newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** Läd nur die Restschulden einer Person. Ist empfohlen, um Speicherverbrauch zu vermeiden. */
    public static int greifeSchuldenBetrag(Personenbeschreibung p){
        Person person = ladePerson(p);
        if(person == null)
            throw new IllegalArgumentException("File not Found: "+constructFileName(p.vorname, p.nachname, p.id));
        return person.getRestSchulden();
    }


    /** Return: ArrayListe aus Personenbeschreibungen von allen gefunden Personendateien */
    public static ArrayList<Personenbeschreibung> getPersonenbeschreibungen() {
        ArrayList<Personenbeschreibung> liste = new ArrayList<>();
        for(String s: listSaveDirectory()){
            if(istSaveFile(s)){
                liste.add(getBeschreibungFromFile(s));
            }
        }
        return liste;
    }


    /** Speichern anderer Objekte ermoeglicht. Anwendung: Behalten von Metadaten. */
    public static void speichereObjekt(Object o, String filename) {
        String jsonString = new Gson().toJson(o);
        String path = savePath + filename;
        schreibe(jsonString, path);
    }

    public static <T> T ladeObjekt(String filename, Class<T> klasse) {
        Path path = Paths.get(savePath + filename);
        return leseObjekt(path, klasse);
    }


    public static boolean istRichtigFormatiert(Personenbeschreibung pb) {
        String filename = constructFileName(pb.vorname, pb.nachname, pb.id);
        return filename.matches(saveFileValidationRegEx);
    }


    // ====================== Helfer Methoden ========================

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


    private static <T> T leseObjekt(Path path, Class<T> klasse) {
        try {
            String jsonObj = Files.readString(path, StandardCharsets.UTF_8);
            return new Gson().fromJson(jsonObj, klasse);
        } catch (IOException e) {
            // pass for now
        }
        return null;
    }


    private static Personenbeschreibung getBeschreibungFromFile(String filename) {
        int comma = filename.indexOf(',');
        int hashtag = filename.indexOf('#');
        int json = filename.indexOf(".json");
        String nachname = filename.substring(0, comma);
        String vorname = filename.substring(comma+2, hashtag);
        int id = Integer.parseInt(filename.substring(hashtag+1, json));
        return new Personenbeschreibung(vorname, nachname, id);
    }

    private static boolean istSaveFile(String filename) {
        if(filename.indexOf(',') != filename.lastIndexOf(',')) return false;
        if(filename.indexOf('#') != filename.lastIndexOf('#')) return false;
        return filename.matches(saveFileValidationRegEx);
    }
}
