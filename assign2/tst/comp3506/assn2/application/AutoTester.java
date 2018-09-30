package comp3506.assn2.application;
import comp3506.assn2.utils.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author Jingwei WANG
 */
public class AutoTester implements Search {
    private String[] indexLines;
    private String[] documentLines;
    private String[] stopWordsLines;
	/**
	 * Create an object that performs search operations on a document.
	 * If indexFileName or stopWordsFileName are null or an empty string the document should be loaded
	 * and all searches will be across the entire document with no stop words.
	 * All files are expected to be in the files sub-directory and 
	 * file names are to include the relative path to the files (e.g. "files\\shakespeare.txt").
	 * 
	 * @param documentFileName  Name of the file containing the text of the document to be searched.
	 * @param indexFileName     Name of the file containing the index of sections in the document.
	 * @param stopWordsFileName Name of the file containing the stop words ignored by most searches.
	 * @throws FileNotFoundException if any of the files cannot be loaded. 
	 *                               The name of the file(s) that could not be loaded should be passed 
	 *                               to the FileNotFoundException's constructor.
	 * @throws IllegalArgumentException if documentFileName is null or an empty string.
	 */
	public AutoTester(String documentFileName, String indexFileName, String stopWordsFileName)
			throws FileNotFoundException, IllegalArgumentException {
        //FileInputStream fis = new FileInputStream(documentFileName);
        String data;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(documentFileName)));
            StringBuilder sb = new StringBuilder((int) new File(documentFileName).length());
            while((data = br.readLine()) != null) {
                //System.out.println(data);
                sb.append(data);
            }
            this.documentLines = sb.toString().split(System.getProperty("line.separator"));

            br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFileName)));
            sb = new StringBuilder((int) new File(indexFileName).length());
            //data = "";
            while((data = br.readLine()) != null) {
                //System.out.println(data);
                sb.append(data);
            }
            this.indexLines = sb.toString().split(System.getProperty("line.separator"));
            br = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFileName)));
            sb = new StringBuilder((int) new File(stopWordsFileName).length());
            while((data = br.readLine()) != null) {
                //System.out.println(data);
                sb.append(data);
            }
            this.stopWordsLines = sb.toString().split(System.getProperty("line.separator"));
            br.close();
        } catch (IOException e) {
            //e.printStackTrace();
            throw new FileNotFoundException();
        }
        //System.out.println(Arrays.toString(indexLines));
        // TODO Implement constructor to load the data from these files and
		// TODO setup your data structures for the application.
	}

	@Override
    public int wordCount(String word) throws IllegalArgumentException {
	    int wordsCount = 0;
        for (String documentLine : this.documentLines) {
            for (String words : documentLine.split("\\s+")) {
                if (word.toLowerCase().equals(words.toLowerCase())) {
                    wordsCount++;
                }
            }
        }
        return wordsCount;
        //throw new UnsupportedOperationException("Search.wordCount() Not Implemented.");
    }


}
