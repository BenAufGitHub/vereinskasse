package grafiken.menu;

import java.util.Random;

public class FactSpitter {

    private String[] facts = {
        "Diesen Satz verfasste ich um 00:58 Uhr :>",
            "Ciceros Text 'Lorem ipsum...' wurde schon im 16.Jahrhundert als Mustertext zur Vorlage verwendet. " +
                    "Auch dieser Text wurde damit formatiert.",
            "Wieso ist die Banane krumm?\n" +
                    " ... ich weiß es auch nicht :c",
            "Der erste Wingsuit-Flug dauerte ca. 4 Sekunden...",
            "Eine Enklave ist ein Landesabschnitt, der komplett von einem anderem Land umgrenzt wird. Indiens einzige Enklave " +
                    "3.Ordnung liegt in Bangladesh, bzw. in Indien, bzw. in Bangladesh...",
            "Was hat vier Beine und kann fliegen? Zwei Vögel!",
            "Die größte ethnische Gruppe in Bayern sind die Deutschen",
            "Insekten sind gegen Fallschaden immun, da sieht keine großen Geschwindigkeiten und Aufprallkräfte erreichen.",
            "Denk dran die Mülltonne rauszustellen.",
            "Fast daneben ist auch vorbei"
    };

    public String spit() {
        int rand = new Random().nextInt(facts.length);
        return facts[rand];
    }
}
