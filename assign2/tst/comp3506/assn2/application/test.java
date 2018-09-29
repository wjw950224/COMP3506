package comp3506.assn2.application;

import java.io.FileNotFoundException;

public class test {
    public static void main(String[] argv) throws FileNotFoundException {
        AutoTester tester = new AutoTester("shakespeare.txt", "shakespeare-index.txt",
                "stop-words.txt");
    }
}
