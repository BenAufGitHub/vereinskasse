package helpers;

import org.apache.commons.lang3.StringUtils;
import users.Personenbeschreibung;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Profilliste {

    public enum Sortierung {
        ABC, ID
    }

    private ArrayList<Personenbeschreibung> pbListe = new ArrayList<>();
    private HashMap<Integer, Personenbeschreibung> idMap = new HashMap<>();
    private ArrayList<Integer> idListe = new ArrayList<>();


    protected Profilliste(List<Personenbeschreibung> array) {
        for(Personenbeschreibung pb : array) {
            insert(pb);
        }
    }

    protected void delete(Personenbeschreibung pb) {
        int index = findePositionNach(pb, Sortierung.ABC);
        if(index < 0) return;
        idMap.remove(pb.id);
        idListe.remove((Integer) pb.id);
        pbListe.remove(index);
    }

    protected void insert(Personenbeschreibung pb) {
        registerID(pb);
        insert(pb, pbListe);
    }


    /**
     * Intern werden Listen nach angegebenem Sortierformat sortiert und die Position einer Beschreibung in der Liste ausgegeben.
     * Return: Position, wenn es nicht gefunden wurde, gibt die Methode -1 zurueck. */
    public int findePositionNach(Personenbeschreibung pb, Sortierung sort) {
        if (sort == Sortierung.ABC)
            return findeInListe(pb, pbListe);
        if(!idMap.containsKey(pb.id)) return -1;
        return findeInListe(pb.id, idListe);
    }


    /** Gibt Personenbeschreibung von der Position nach angegebenem Sortierverfahren zurueck.*/
    public Personenbeschreibung getPB(int position, Sortierung sort) {
        if(sort == Sortierung.ABC)
            return pbListe.get(position);
        int id = idListe.get(position);
        return idMap.get(id);
    }


    /** Gibt Menge der Personenbeschreibungen zurueck. */
    public int size(){
        return idListe.size();
    }



    private void registerID(Personenbeschreibung pb) {
        insert(pb.id, idListe);
        idMap.put(pb.id, pb);
    }



    // ======================= Fummelarbeit =========================


    private int findeInListe(Comparable comp, ArrayList list) {
        if(list.isEmpty()) return -1;
        int anfang = 0;
        int ende = list.size() - 1;
        while(anfang <= ende){
            int pos = anfang + ((ende - anfang) / 2);
            int diff = comp.compareTo(list.get(pos));
            if(diff == 0) return pos;
            if(diff <= -1)
                ende = pos -1;
            else
                anfang = pos+1;
        }
        return -1;
    }

    private void insert(Comparable comp, ArrayList list){
        if(list.isEmpty()){
            list.add(comp);
            return;
        }
        int anfang = 0;
        int ende = list.size() - 1;
        while(true){
            int pos = anfang + ((ende - anfang) / 2);
            if(comp.compareTo(list.get(pos)) <= -1) {
                if(pos == anfang){
                    list.add(pos, comp);
                    return;
                }
                ende = pos -1;
                continue;
            }
            if(pos == ende){
                list.add(ende+1, comp);
                return;
            }
            anfang = pos+1;
        }
    }

    public List<Personenbeschreibung> getBestMatching(String comparison) {
        String[] words = comparison.toLowerCase().strip().split("[ ]+");
        HashMap<Integer, Double> scores = new HashMap<>();
        for(Personenbeschreibung pb : pbListe){
            String name = (pb.vorname + pb.nachname).toLowerCase();
            double score = 0.0;
            for(String word : words) {
                score += getFullMatchScore(word, name);
            }
            double changes = StringUtils.getLevenshteinDistance(name, comparison.strip());
            int difference = Math.max(0, name.length() - comparison.strip().length());
            changes  = Math.max(changes/1.65, changes-difference);
            score += 3.0 / (0.3 +((double) changes));
            if(score >= 0.5)
                scores.put(pb.id, score);
        }
        return sortMap(scores);
    }

    private List<Personenbeschreibung> sortMap(HashMap<Integer, Double> scores) {
        Set<Integer> ids = scores.keySet();
        List<Integer> list = new ArrayList<>(ids);
        list.sort((element1, element2) -> {
            double comp = scores.get(element1) - scores.get(element2);
            if(comp < 0) return 1;
            if(comp == 0) return 0;
            return -1;
        });
        List<Personenbeschreibung> pbs = new ArrayList<>();
        for(int i : list)
            pbs.add(idMap.get(i));
        return pbs;
    }

    private double getFullMatchScore(String word, String name) {
        int index = name.indexOf(word);
        if(index == -1 || word.length() == 0) return 0.0;
        if(index == 0 || name.charAt(index-1) == ' ') return 1.0;
        if(index != name.length()-1 || name.charAt(index+1) == ' ') return 0.75;
        return 0.5;
    }
}
