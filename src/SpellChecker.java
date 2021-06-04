import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class SpellChecker {

    public static void main(String[] args) throws FileNotFoundException {
        SpellChecker spel = new SpellChecker("./resources/dict.txt", "./resources/misp.txt");
        spellCheck("./resources/test.txt");
    }

    private static HashSet<String> dict = new HashSet<>();
    private static Corrector corrector;

    public SpellChecker(String dictionaryFile, String misspellingsFile) throws FileNotFoundException {
        Scanner file = new Scanner(new File(dictionaryFile));

        while (file.hasNext()) {
            dict.add(file.next().trim());
        }

        corrector = new Corrector(misspellingsFile);
    }

    // Cool little idea I had to keep things nice and allow for future changes. Format doesn't work for all rules though.

    // There has gotta be a ruleset for finding roots, right? Because the one provided doesn't address all cases, not
    // close. Maybe english really is just this messy.
    static private String[] rules = {
            "ing",
            "ies",
            "ied",
            "ed",
            "es",
            // All before here are special cases
            "s",
            "ly",
            "ed",
            "ning"
    };

    static private ArrayList<String> root(String[] word) {
        // returns all possible variations of the root
        ArrayList<String> book = new ArrayList<>();
        String temp = word[0];
        book.add(temp);

        for (int i = 0; i < rules.length; i++) {
            if (temp.length() < rules[i].length()) {
                break;
            }
            if (temp.substring(temp.length() - rules[i].length()).equals(rules[i])) {
                if (i == 0) {
                    book.add(temp.substring(0, temp.length() - rules[i].length()) + "e");
                } else if (i == 1 || i == 2) {
                    book.add(temp.substring(0, temp.length() - rules[i].length()) + "y");
                    break;
                } else if (i == 3 || i == 4) {
                    book.add(temp.substring(0, temp.length() - 1));
                }
                book.add(temp.substring(0, temp.length() - rules[i].length()));
            }
        }

        return book;
    }

    // Ironically, the dictionary is missing the word "mistake," as well as some others that I added.

    static void spellCheck(String documentFile) throws FileNotFoundException {
        Scanner file = new Scanner(new File(documentFile));

        while (file.hasNext()) {
            String[] word = file.next().trim().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");;
            // One of the more beautiful lines of code I've seen. Stolen from
            // https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java.
            ArrayList<String> tries = root(word);
            boolean exists = false;
            for (String aTry : tries) {
                if (dict.contains(aTry)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                System.out.print("Misspelling at " + word[0]);
                String correction = corrector.getCorrection(word[0]);
                if (correction != null) {
                    System.out.print(", suggestion: " + correction + ".\n");
                } else {
                    System.out.print(", no corrections found.\n");
                }
            }
        }
    }
}
