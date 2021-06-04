import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Corrector {
    HashMap<Integer, String> wrongDict = new HashMap<>();

    // I very much like the structure here, although the code is a bit sloppy: spelling suggestions are easily added in
    // misp.txt

    public Corrector(String misspellingsFile) throws FileNotFoundException {
        Scanner file = new Scanner(new File(misspellingsFile));

        while (file.hasNext()) {
            String temp = file.nextLine();
            wrongDict.put((new typo(temp.substring(0, temp.indexOf(' ')))).hashCode(), temp.substring(temp.indexOf(' ')));
        }
    }

    public String getCorrection(String s) {
        return wrongDict.get(new typo(s).hashCode());
    }

    class typo
    {
        String key;
        typo(String key) {
            this.key = key;
        }

        @Override
        public int hashCode()
        {
            int hash = 0;
            for (int i = 0; i < key.length(); i++) {
                hash += key.charAt(i) * 31 ^ (key.length() - i);
            }
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            return key.equals((String)obj);
        }
    }
}
