package comp3506.assn2.application;
import comp3506.assn2.utils.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
    int wordCount;
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
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("files/" + documentFileName)));
            String data;
            while((data = br.readLine()) != null) {
                System.out.println(data);
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream("files/" + indexFileName)));
            while((data = br.readLine()) != null) {
                System.out.println(data);
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream("files/" + stopWordsFileName)));
            while((data = br.readLine()) != null) {
                System.out.println(data);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            throw new FileNotFoundException();
        }

        // TODO Implement constructor to load the data from these files and
		// TODO setup your data structures for the application.
	}

	@Override
    public int wordCount(String word) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Search.wordCount() Not Implemented.");
    }


}
