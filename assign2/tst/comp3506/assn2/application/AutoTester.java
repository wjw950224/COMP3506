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
        int firstLineNo, lastLineNo, startLineNo, nextStartLineNo, columnNo;
        int lineCount = 0;
        String data;
        String[] words;
        BufferedReader br;
        this.section = null;
        Section currentSection = null;
        Section newSection;
        Line currentLine;
        Word currentWord;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFileName)));
            while((data = br.readLine()) != null) {
                newSection = new Section(Integer.parseInt(data.split(",")[1]), data.split(",")[0]);
                if (section == null) {
                    this.section = newSection;
                    currentSection = this.section;
                } else if (currentSection != null) {
                    Section lastSection = currentSection;
                    currentSection.setNextSection(newSection);
                    currentSection = currentSection.getNextSection();
                    currentSection.setPreviousSection(lastSection);
                    Section previousSection = currentSection.getPreviousSection();
                    firstLineNo = previousSection.getFirstLine().getLineNo();
                    lastLineNo = currentSection.getFirstLine().getLineNo();
                    currentLine = previousSection.getFirstLine();
                    for (int i = firstLineNo + 1; i < lastLineNo; i++) {
                        currentLine.setNextLine(i);
                        currentLine = currentLine.getNextLine();
                    }
                }

            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(documentFileName)));
            currentSection = this.section;
            currentLine = currentSection.getFirstLine();
            startLineNo = currentSection.getFirstLine().getLineNo();
            nextStartLineNo = currentSection.getNextSection().getFirstLine().getLineNo();
            while((data = br.readLine()) != null) {
                lineCount++;
                if (lineCount >= startLineNo && (lineCount < nextStartLineNo || nextStartLineNo == -1)) {
                    columnNo = 1;
                    words = data.toLowerCase().split("\\s+");
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
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFileName)));
            //sb = new StringBuilder((int) new File(stopWordsFileName).length());
            //while((data = br.readLine()) != null) {
            //    sb.append(data).append(System.getProperty("line.separator"));
            //}
            //this.stopWordsLines = sb.toString().split(System.getProperty("line.separator"));
            br.close();
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        // TODO Implement constructor to load the data from these files and
		// TODO setup your data structures for the application.
	}

    private class Word<T> {
        int columnNo;
        String string;
        Word next;
        boolean isStop;

        Word(int column, String currentWord) {
            columnNo = column;
            string = currentWord;
            next = null;
            isStop = false;
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

        void setIsStop() {
            isStop = true;
        }
    }

    private class Line<T> {
        int lineNo;
        Word firstWord;
        Line next;

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
        Line firstLine;
        String title;
        Section next;
        Section previous;

        Section(int lineNo, String words) {
            firstLine = new Line(lineNo);
            title = words;
            next = null;
            previous = null;
        }

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
        return wordsCount;
    }

    @Override
    public List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        List<Pair<Integer,Integer>> result = new LinkedList<>();
        String[] words = phrase.split("\\s+");

        return result;
        //throw new UnsupportedOperationException("Search.phraseOccurrence() Not Implemented.");
    }

    @Override
    public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        List<Integer> result = new LinkedList<>();
        int count = 0;

        return result;
        //throw new UnsupportedOperationException("Search.wordsOnLine() Not Implemented.");
    }
}
