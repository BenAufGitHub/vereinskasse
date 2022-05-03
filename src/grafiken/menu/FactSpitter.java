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
            "Fast daneben ist auch vorbei",
            "\"Every 60 seconds, a minute passes\"",
            "Die Wahrscheinlichkeit, in einem Haus mit ungerader Hausnummer zu leben, liegt bei circa 50,2%.",
            "\"Nein\" ~Johann Wolfang von Goethe, Faust I: V.63",
            "Anhänger der Shopping-Card-Theory besagen, man könne den Charakter eines Menschen durch die Frage bestimmen, " +
                    "ob dieser seinen Einkaufwagen zurückstellen würde, wenn keiner hinschauen würde.",
            "Mischt man ein Blackjack-Deck, kann man sich sicher sein, dass die resultierende Kartenfolge in der Geschichte noch nie vorgekommen ist.",
            "Clinophobie bezeichnet die Angst, ins Bett zu gehen.",
            "Etwa 99% aller Kurven sind Rechtskurven.",
            "Viele Star Wars Fans sind sich einig: Jar-Jar Binks ist ein Sith-Lord.",
            "Verlasse das Gebäude nicht, es sei denn, du willst raus!"
    };

    public String spit() {
        int rand = new Random().nextInt(facts.length);
        return facts[rand];
    }
}
