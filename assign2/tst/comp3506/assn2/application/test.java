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
        searchApplication.phraseOccurrence("means may fit");
    }


}
