package comp3506.assn2.application;

import java.io.FileNotFoundException;

public class test {
    public static void main(String[] argv) throws FileNotFoundException {
        //System.out.println(System.getProperty("user.dir"));
        AutoTester tester = new AutoTester("files/shakespeare.txt", "files/shakespeare-index.txt",
                "files/stop-words.txt");
        System.out.println(tester.wordCount("obscure"));
        String [] searchTerm = {"riper", "decease"};
        tester.wordsOnLine(searchTerm);
        tester.someWordsOnLine(searchTerm);
    }
}
