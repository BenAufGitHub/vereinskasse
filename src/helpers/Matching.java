package helpers;

import org.apache.commons.lang3.StringUtils;
import users.Personenbeschreibung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Matching {

    public static List<Personenbeschreibung> getBestMatching(String comparison, ArrayList<Personenbeschreibung> pbListe) {
        String[] words = comparison.toLowerCase().strip().split("[ ]+");
        HashMap<Integer, Double> scores = new HashMap<>();
        HashMap<Integer, Personenbeschreibung> refs = new HashMap<>();
        for(Personenbeschreibung pb : pbListe){
            String name = (pb.name).toLowerCase();
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
                refs.put(pb.id, pb);
        }
        return sortMap(scores, refs);
    }


    private static List<Personenbeschreibung> sortMap(HashMap<Integer, Double> scores, HashMap<Integer, Personenbeschreibung> pbRefs) {
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
            pbs.add(pbRefs.get(i));
        return pbs;
    }


    private static double getFullMatchScore(String word, String name) {
        int index = name.indexOf(word);
        if(index == -1 || word.length() == 0) return 0.0;
        if(index == 0 || name.charAt(index-1) == ' ') return 1.0;
        if(index != name.length()-1 && name.charAt(index+1) == ' ') return 0.75;
        return 0.5;
    }

}
