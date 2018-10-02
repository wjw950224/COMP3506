package comp3506.assn2.application;
import comp3506.assn2.utils.*;

import java.io.*;
import java.util.LinkedList;
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
    private Section section;
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
        BufferedReader br;
        StringBuilder sb;
        this.section = null;
        Section currentSection = null;
        //index =  new Pair<String,Integer>[];
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFileName)));
            //sb = new StringBuilder((int) new File(indexFileName).length());
            //data = "";
            while((data = br.readLine()) != null) {
                //data.split(",");
                //Pair<String,Integer> pair = new Pair<>(data.split(",")[0], Integer.parseInt(data.split(",")[1]));
                //System.out.println(data);
                //index.add(pair);
                //sb.append(data).append(System.getProperty("line.separator"));
                Section newSection = new Section(Integer.parseInt(data.split(",")[1]), data.split(",")[0]);
                if (section == null) {
                    this.section = newSection;
                    currentSection = this.section;
                    //currentSection.setFirstLine(Integer.parseInt(data.split(",")[1]));
                } else if (currentSection != null) {
                    int firstLineNo, lastLineNo;
                    Section lastSection = currentSection;
                    currentSection.setNextSection(newSection);
                    currentSection = currentSection.getNextSection();
                    currentSection.setPreviousSection(lastSection);
                    Section previousSection = currentSection.getPreviousSection();
                    firstLineNo = previousSection.getFirstLine().getLineNo();
                    lastLineNo = currentSection.getFirstLine().getLineNo();
                    //Line firstLine = new Line(firstLineNo);
                    //previousSection.setFirstLine(firstLine);
                    Line currentLine = previousSection.getFirstLine();
                    for (int i = firstLineNo + 1; i < lastLineNo; i++) {
                        currentLine.setNextLine(i);
                        currentLine = currentLine.getNextLine();
                    }
                }

            }

            //this.indexLines = sb.toString().split(System.getProperty("line.separator"));


            br = new BufferedReader(new InputStreamReader(new FileInputStream(documentFileName)));
            //sb = new StringBuilder((int) new File(documentFileName).length());
            int lineCount = 0;
            currentSection = this.section;
            Line currentLine = currentSection.getFirstLine();
            int startLineNo = currentSection.getFirstLine().getLineNo();
            int nextStartLineNo = currentSection.getNextSection().getFirstLine().getLineNo();

            while((data = br.readLine()) != null) {
                lineCount++;
                if (lineCount >= startLineNo && (lineCount < nextStartLineNo || nextStartLineNo == -1)) {
                    int columnNo = 1;
                    Word currentWord;
                    String[] words = data.toLowerCase().split("\\s+");
                    //System.out.println(words[0]);
                    if (words.length == 0) {
                        currentWord = new Word(columnNo, "");
                    } else {
                        currentWord = new Word(columnNo, words[0]);
                    }

                    if (nextStartLineNo == -1) {
                        currentLine.setNextLine(lineCount);
                    }
                    currentLine.setFirstWord(currentWord);
                    for (int i = 1; i < words.length; i++) {
                        //if (words[i].equals("obscure")) {
                        //    System.out.println("words find\n");
                        //}
                        columnNo += (currentWord.getStringSize() + 1);
                        currentWord.setNextWord(columnNo, words[i]);
                        currentWord = currentWord.getNextWord();
                    }
                    currentLine = currentLine.getNextLine();
                    if (lineCount == nextStartLineNo - 1) {
                        currentSection = currentSection.getNextSection();
                        if (currentSection.getNextSection() == null) {
                            nextStartLineNo = -1;
                        } else {
                            nextStartLineNo = currentSection.getNextSection().getFirstLine().getLineNo();
                        }
                        startLineNo = currentSection.getFirstLine().getLineNo();
                        currentLine = currentSection.getFirstLine();
                    }
                }
                //System.out.println(lineCount);
                //if (lineCount == 85424) {
                //    System.out.println(lineCount);
                //}
                //currentLine = currentLine.getNextLine();
                //System.out.println(data);
                //sb.append(data).append(System.getProperty("line.separator"));

            }
            //this.documentLines = sb.toString().toLowerCase().split(System.getProperty("line.separator"));



            br = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFileName)));
            sb = new StringBuilder((int) new File(stopWordsFileName).length());
            while((data = br.readLine()) != null) {
                //System.out.println(data);
                sb.append(data).append(System.getProperty("line.separator"));
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

    private class Word<T> {
        int columnNo;
        String string;
        Word next;
        //IterableQueue<T> cells;

        Word(int column, String currentWord) {
            columnNo = column;
            string = currentWord;
            next = null;
        }

        int getColumnNo() {
            return columnNo;
        }

        int getStringSize() {
            return string.length();
        }

        Word getNextWord() {
            return next;
        }

        String getString() {
            return string;
        }

        void setNextWord(int column, String nextWord) {
            next = new Word(column, nextWord);
        }
    }

    private class Line<T> {
        int lineNo;
        Word firstWord;
        Line next;
        //IterableQueue<T> cells;

        Line(int line) {
            lineNo = line;
            firstWord = null;
            next = null;
        }

        int getLineNo() {
            return lineNo;
        }

        Word getFirstWord() {
            return firstWord;
        }

        Line getNextLine() {
            return next;
        }

        void setFirstWord(Word word) {
            firstWord = word;
        }

        void setNextLine(int line) {
            next = new Line(line);
        }
    }

    private class Section<T> {
        //int firstLineNo;
        Line firstLine;
        String title;
        Section next;
        Section previous;
        //IterableQueue<T> cells;

        Section(int lineNo, String words) {
            firstLine = new Line(lineNo);
            //firstLineNo = lineNo;
            title = words;
            next = null;
            previous = null;
        }

        //int getLineNo() {
        //    return firstLineNo;
        //}

        Line getFirstLine() {
            return firstLine;
        }

        Section getNextSection() {
            return next;
        }

        Section getPreviousSection() {
            return previous;
        }

        void setNextSection(Section newSection) {
            next = newSection;
        }

        void setPreviousSection(Section lastSection) {
            previous = lastSection;
        }

        void setFirstLine(Line line) {
            firstLine = line;
        }
    }


    @Override
    public int wordCount(String word) throws IllegalArgumentException {
	    int wordsCount = 0;
	    Section currentSection = this.section;
	    while (currentSection != null) {
	        Line currentLine = currentSection.getFirstLine();
	        while (currentLine != null) {
	            Word currentWord = currentLine.getFirstWord();
	            while (currentWord != null) {
                    if (word.toLowerCase().equals(currentWord.getString())) {
                        wordsCount++;
                    }
                    currentWord = currentWord.getNextWord();
                }
                currentLine = currentLine.getNextLine();
            }
            currentSection = currentSection.getNextSection();
        }
        /**
        for (String documentLine : this.documentLines) {
            for (String words : documentLine.split("\\s+")) {
                if (word.toLowerCase().equals(words)) {
                    wordsCount++;
                }
            }
        }
         **/
        return wordsCount;
        //throw new UnsupportedOperationException("Search.wordCount() Not Implemented.");
    }

    @Override
    public List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        List<Pair<Integer,Integer>> result = new LinkedList<>();
        String[] words = phrase.split("\\s+");
        for (int i = 0; i < this.documentLines.length; i++) {
           //
        }
        return result;
        //throw new UnsupportedOperationException("Search.phraseOccurrence() Not Implemented.");
    }

    @Override
    public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        List<Integer> result = new LinkedList<>();
        int count = 0;
        for (int i = 0; i < this.documentLines.length; i++) {
            String[] lineWords = documentLines[i].split("\\s+");
            for (String word : words) {
                for (String lineWord : lineWords) {
                    if (word.toLowerCase().equals(lineWord)) {
                        count++;
                        break;
                    }
                }
            }
            if (count == words.length) {
                result.add(i);
            }
        }
        return result;
        //throw new UnsupportedOperationException("Search.wordsOnLine() Not Implemented.");
    }
}
