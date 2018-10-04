package comp3506.assn2.application;

import java.io.FileNotFoundException;

public class  test {
    private static Search searchApplication;
    public static void main(String[] argv) {
        try {
            searchApplication = new AutoTester("files/shakespeare.txt", "", "");
        } catch (FileNotFoundException | IllegalArgumentException e) {
            System.out.println("Opening files failed!");
            e.printStackTrace();
        }
        searchApplication.phraseOccurrence("the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against");
        System.out.println(searchApplication.wordCount("fortune"));
        String[] words = new String[3];
        words[0] = "Devoutly";
        words[1] = "wish'd";
        words[2] = "sleep";
        searchApplication.wordsOnLine(words);
        searchApplication.someWordsOnLine(words);
    }


}
