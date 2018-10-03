package comp3506.assn2.application;
import comp3506.assn2.utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 *
 * Memory usage: O(n) where n is the number of words of input files.
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
        StopWord stopWord = null;
        Section currentSection = null;
        Section newSection;
        Line currentLine;
        Word currentWord;
        StopWord currentStopWord = null;
        StopWord newStopWord;
        try {
            if (!(indexFileName == null || indexFileName.equals(""))) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFileName)));
                while((data = br.readLine()) != null) {
                    newSection = new Section(Integer.parseInt(data.split(",")[1]), data.split(",")[0]);
                    if (this.section == null) {
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
            }
            if (!(stopWordsFileName == null || stopWordsFileName.equals(""))) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFileName)));
                while((data = br.readLine()) != null) {
                    newStopWord = new StopWord(data);
                    if (stopWord == null) {
                        stopWord = newStopWord;
                        currentStopWord = stopWord;
                    } else if (currentStopWord != null) {
                        currentStopWord.setNextStopWord(newStopWord);
                        currentStopWord = currentStopWord.getNextStopWord();
                    }
                }
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(documentFileName)));
            if (this.section != null) {
                currentSection = this.section;
                nextStartLineNo = currentSection.getNextSection().getFirstLine().getLineNo();
            } else {
                this.section = new Section(1, "");
                currentSection = this.section;
                nextStartLineNo = -1;
            }
            currentLine = currentSection.getFirstLine();
            startLineNo = currentSection.getFirstLine().getLineNo();
            while((data = br.readLine()) != null) {
                lineCount++;
                if (lineCount >= startLineNo && (lineCount < nextStartLineNo || nextStartLineNo == -1)) {
                    columnNo = 1;
                    words = data.toLowerCase().split("\\s");
                    if (words.length == 0) {
                        currentWord = new Word(columnNo, "");
                        currentWord.setIsStop();
                    } else {
                        currentWord = new Word(columnNo, words[0]);
                        if (words[0].length() == 0) {
                            currentWord.setIsStop();
                        } else {
                            currentStopWord = stopWord;
                            while(currentStopWord != null) {
                                if (currentStopWord.getStopWord().equals(words[0].toLowerCase())) {
                                    currentWord.setIsStop();
                                    break;
                                } else {
                                    currentStopWord = currentStopWord.getNextStopWord();
                                }
                            }
                        }
                    }
                    if (nextStartLineNo == -1) {
                        currentLine.setNextLine(currentLine.getLineNo() + 1);
                    }
                    currentLine.setFirstWord(currentWord);
                    for (int i = 1; i < words.length; i++) {
                        columnNo += (currentWord.getStringSize() + 1);
                        currentWord.setNextWord(columnNo, words[i]);
                        currentWord = currentWord.getNextWord();
                        if (words[i].length() == 0) {
                            currentWord.setIsStop();
                        } else {
                            currentStopWord = stopWord;
                            while(currentStopWord != null) {
                                if (currentStopWord.getStopWord().equals(words[i].toLowerCase())) {
                                    currentWord.setIsStop();
                                    break;
                                } else {
                                    currentStopWord = currentStopWord.getNextStopWord();
                                }
                            }
                        }
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
            br.close();
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
	}

    private class Word {
        int columnNo;
        int size;
        String string;
        Word next;
        boolean isStop;

        Word(int column, String currentWord) {
            columnNo = column;
            string = currentWord.replaceAll(",", "");
            size = currentWord.length();
            next = null;
            isStop = false;
        }

        int getColumnNo() {
            return columnNo;
        }

        int getStringSize() {
            return size;
        }

        boolean isStop() {
            return isStop;
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

    private class Line {
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

    private class Section {
        Line firstLine;
        String title;
        Section next;
        Section previous;

        Section(int lineNo, String words) {
            firstLine = new Line(lineNo);
            title = words.toLowerCase();
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

        String getTitle() {
            return title;
        }

        void setNextSection(Section newSection) {
            next = newSection;
        }

        void setPreviousSection(Section lastSection) {
            previous = lastSection;
        }
    }

    private class StopWord {
	    String word;
	    StopWord next;

	    StopWord(String stopWord) {
	        word = stopWord.toLowerCase();
	        next = null;
        }

        String getStopWord() {
	        return word;
        }

        StopWord getNextStopWord() {
	        return next;
        }

        void setNextStopWord(StopWord nextStopWord) {
	        next = nextStopWord;
        }
    }

    private boolean isWordFound(String[] wordsRequired, Line currentLine) {
        for (String word : wordsRequired) {
            Word currentWord = currentLine.getFirstWord();
            while (currentWord != null) {
                if (currentWord.isStop()) {
                    currentWord = currentWord.getNextWord();
                    continue;
                }
                if (word.toLowerCase().equals(currentWord.getString())) {
                    return true;
                }
                currentWord = currentWord.getNextWord();
            }
        }
        return false;
    }

    private int getWordCount(String[] words, List<Triple<Integer, Integer, String>> sectionResult,
                             int wordCount, Line currentLine) {
        while (currentLine != null) {
            for (String word : words) {
                Word currentWord = currentLine.getFirstWord();
                while (currentWord != null) {
                    if (currentWord.isStop()) {
                        currentWord = currentWord.getNextWord();
                        continue;
                    }
                    if (currentWord.getString().replaceAll(
                            "\\pP", "").equals(word.toLowerCase())) {
                        Triple<Integer, Integer, String> triple = new Triple<>(currentLine.getLineNo(),
                                currentWord.getColumnNo(), word.toLowerCase());
                        sectionResult.add(triple);
                        wordCount++;
                        break;
                    }
                    currentWord = currentWord.getNextWord();
                }
            }
            currentLine = currentLine.getNextLine();
        }
        return wordCount;
    }

    private void getResult(Word currentWord, Line currentLine, Section currentSection, String[] words,
                                            List<Pair<Integer,Integer>> result) {
        Word nextWord = null;
        int wordsMatch = 1;
        int lineNo = currentLine.getLineNo();
        int columnNo = currentWord.getColumnNo();
        Pair<Integer,Integer> pairResult = new Pair<>(lineNo, columnNo);
        if (words.length == 1) {
            result.add(pairResult);
        } else {
            if (lineNo == 28267) {
                System.out.println("28267");
            }
            if (currentWord.getNextWord() != null) {
                nextWord = currentWord.getNextWord();
            } else if (currentLine.getNextLine() != null) {
                currentLine = currentLine.getNextLine();
                nextWord = currentLine.getFirstWord();
            } else if (currentSection.getNextSection() != null) {
                currentSection = currentSection.getNextSection();
                currentLine = currentSection.getFirstLine();
                nextWord = currentLine.getFirstWord();
            }
            while (nextWord != null) {
                if (wordsMatch == words.length){
                    break;
                }
                if (currentWord.isStop()) {
                    currentWord = currentWord.getNextWord();
                    continue;
                }
                if (words[wordsMatch].toLowerCase().equals(nextWord.getString())) {
                    wordsMatch++;
                } else {
                    break;
                }
                nextWord = nextWord.getNextWord();
            }
            if (wordsMatch == words.length) {
                result.add(pairResult);
            }
        }
	}

    /**
     * Determines the number of times the word appears in the document.
     *
     * Run-time: O(n)
     *
     * @param word The word to be counted in the document.
     * @return The number of occurrences of the word in the document.
     * @throws IllegalArgumentException if word is null or an empty String.
     */
    @Override
    public int wordCount(String word) throws IllegalArgumentException {
	    int wordsCount = 0;
	    Section currentSection = this.section;
	    while (currentSection != null) {
	        Line currentLine = currentSection.getFirstLine();
	        while (currentLine != null) {
	            Word currentWord = currentLine.getFirstWord();
	            while (currentWord != null) {
	                if (currentWord.isStop()) {
                        currentWord = currentWord.getNextWord();
	                    continue;
                    }
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

    /**
     * Finds all occurrences of the phrase in the document.
     * A phrase may be a single word or a sequence of words.
     *
     * Run-time: O(n)
     *
     * @param phrase The phrase to be found in the document.
     * @return List of pairs, where each pair indicates the line and column number of each occurrence of the phrase.
     *         Returns an empty list if the phrase is not found in the document.
     * @throws IllegalArgumentException if phrase is null or an empty String.
     */
    @Override
    public List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        List<Pair<Integer,Integer>> result = new ArrayList<>();
        String[] words = phrase.split("\\s");
        Section currentSection = this.section;
        while (currentSection != null) {
            Line currentLine = currentSection.getFirstLine();
            while (currentLine != null) {
                Word currentWord = currentLine.getFirstWord();
                while (currentWord != null) {
                    if (currentWord.getString().equals("")) {
                        currentWord = currentWord.getNextWord();
                        continue;
                    }
                    if (currentWord.getString().equals(words[0])) {
                        getResult(currentWord, currentLine, currentSection, words, result);
                    }
                    currentWord = currentWord.getNextWord();
                }
                currentLine = currentLine.getNextLine();
            }
            currentSection = currentSection.getNextSection();
        }

        return result;
    }

    /**
     * Finds all occurrences of the prefix in the document.
     * A prefix is the start of a word. It can also be the complete word.
     * For example, "obscure" would be a prefix for "obscure", "obscured", "obscures" and "obscurely".
     *
     * Run-time: O(n)
     *
     * @param prefix The prefix of a word that is to be found in the document.
     * @return List of pairs, where each pair indicates the line and column number of each occurrence of the prefix.
     *         Returns an empty list if the prefix is not found in the document.
     * @throws IllegalArgumentException if prefix is null or an empty String.
     */
    @Override
    public List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        List<Pair<Integer,Integer>> result = new ArrayList<>();
        Section currentSection = this.section;
        while (currentSection != null) {
            Line currentLine = currentSection.getFirstLine();
            while (currentLine != null) {
                Word currentWord = currentLine.getFirstWord();
                while (currentWord != null) {
                    if (currentWord.isStop()) {
                        currentWord = currentWord.getNextWord();
                        continue;
                    }
                    int counts = 0;
                    if (currentWord.getString().length() < prefix.length()) {
                        currentWord = currentWord.getNextWord();
                        continue;
                    }
                    String word = currentWord.getString();
                    for (int i = 0; i < prefix.length(); i++) {
                        if (prefix.charAt(i) == word.charAt(i)) {
                            counts++;
                        }
                    }
                    if (counts == prefix.length()) {
                        Pair<Integer,Integer> pair = new Pair<>(currentLine.getLineNo(), currentWord.getColumnNo());
                        result.add(pair);
                    }
                    currentWord = currentWord.getNextWord();
                }
                currentLine = currentLine.getNextLine();
            }
            currentSection = currentSection.getNextSection();
        }
        return result;
    }

    /**
     * Searches the document for lines that contain all the words in the 'words' parameter.
     * Implements simple "and" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * Run-time: O(n)
     *
     * @param words Array of words to find on a single line in the document.
     * @return List of line numbers on which all the words appear in the document.
     *         Returns an empty list if the words do not appear in any line in the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    @Override
    public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        List<Integer> result = new ArrayList<>();
        Section currentSection = this.section;
        while (currentSection != null) {
            Line currentLine = currentSection.getFirstLine();
            while (currentLine != null) {
                int wordsCount = 0;
                for (String word : words) {
                    Word currentWord = currentLine.getFirstWord();
                    while (currentWord != null) {
                        if (currentWord.isStop()) {
                            currentWord = currentWord.getNextWord();
                            continue;
                        }
                        if (word.toLowerCase().equals(currentWord.getString())) {
                            wordsCount++;
                            break;
                        }
                        currentWord = currentWord.getNextWord();
                    }
                }
                if (wordsCount == words.length) {
                    result.add(currentLine.getLineNo());
                }
                currentLine = currentLine.getNextLine();
            }
            currentSection = currentSection.getNextSection();
        }
        return result;
    }

    /**
     * Searches the document for lines that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * Run-time: O(n)
     *
     * @param words Array of words to find on a single line in the document.
     * @return List of line numbers on which any of the words appear in the document.
     *         Returns an empty list if none of the words appear in any line in the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    @Override
    public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
        List<Integer> result = new ArrayList<>();
        Section currentSection = this.section;
        while (currentSection != null) {
            Line currentLine = currentSection.getFirstLine();
            while (currentLine != null) {
                for (String word : words) {
                    Word currentWord = currentLine.getFirstWord();
                    while (currentWord != null) {
                        if (currentWord.isStop()) {
                            currentWord = currentWord.getNextWord();
                            continue;
                        }
                        if (word.toLowerCase().equals(currentWord.getString())) {
                            if (!result.contains(currentLine.getLineNo())) {
                                result.add(currentLine.getLineNo());
                            }
                            break;
                        }
                        currentWord = currentWord.getNextWord();
                    }
                }
                currentLine = currentLine.getNextLine();
            }
            currentSection = currentSection.getNextSection();
        }
        return result;
    }

    /**
     * Searches the document for lines that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * Run-time: O(n)
     *
     * @param wordsRequired Array of words to find on a single line in the document.
     * @param wordsExcluded Array of words that must not be on the same line as 'wordsRequired'.
     * @return List of line numbers on which all the wordsRequired appear
     *         and none of the wordsExcluded appear in the document.
     *         Returns an empty list if no lines meet the search criteria.
     * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded are null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    @Override
    public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded)
            throws IllegalArgumentException {
        List<Integer> result = new ArrayList<>();
        Section currentSection = this.section;
        while (currentSection != null) {
            Line currentLine = currentSection.getFirstLine();
            while (currentLine != null) {
                boolean wordFound;
                boolean wordExcludedFound;
                wordFound = isWordFound(wordsRequired, currentLine);
                if (wordFound) {
                    wordExcludedFound = isWordFound(wordsExcluded, currentLine);
                    if (!result.contains(currentLine.getLineNo()) && !wordExcludedFound) {
                        result.add(currentLine.getLineNo());
                    }
                }
                currentLine = currentLine.getNextLine();
            }
            currentSection = currentSection.getNextSection();
        }
        return result;
    }

    /**
     * Searches the document for sections that contain all the words in the 'words' parameter.
     * Implements simple "and" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * Run-time: O(n)
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param words Array of words to find within a defined section in the document.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> result = new ArrayList<>();
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    List<Triple<Integer,Integer,String>> sectionResult = new ArrayList<>();
                    int wordCount = 0;
                    Line currentLine = currentSection.getFirstLine();
                    wordCount = getWordCount(words, sectionResult, wordCount, currentLine);
                    if (wordCount == words.length) {
                        result.addAll(sectionResult);
                    }
                }
                currentSection = currentSection.getNextSection();
            }
        }
        return result;
    }

    /**
     * Searches the document for sections that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * Run-time: O(n)
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param words Array of words to find within a defined section in the document.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer,Integer,String>> simpleOrSearch(String[] titles, String[] words)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> result = new ArrayList<>();
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    Line currentLine = currentSection.getFirstLine();
                    while (currentLine != null) {
                        for (String word : words) {
                            Word currentWord = currentLine.getFirstWord();
                            while (currentWord != null) {
                                if (currentWord.isStop()) {
                                    currentWord = currentWord.getNextWord();
                                    continue;
                                }
                                if (currentWord.getString().replaceAll(
                                        "\\pP", "").equals(word.toLowerCase())) {
                                    Triple<Integer, Integer, String> triple = new Triple<>(currentLine.getLineNo(),
                                            currentWord.getColumnNo(), word.toLowerCase());
                                    result.add(triple);
                                    break;
                                }
                                currentWord = currentWord.getNextWord();
                            }
                        }
                        currentLine = currentLine.getNextLine();
                    }
                }
                currentSection = currentSection.getNextSection();
            }
        }
        return result;
    }

    /**
     * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * Run-time: O(n)
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param wordsRequired Array of words to find within a defined section in the document.
     * @param wordsExcluded Array of words that must not be in the same section as 'wordsRequired'.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the required words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer,Integer,String>> simpleNotSearch(String[] titles, String[] wordsRequired,
                                                                 String[] wordsExcluded)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> result = new ArrayList<>();
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                boolean excludedFound = false;
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    List<Triple<Integer,Integer,String>> sectionResult = new ArrayList<>();
                    int wordCount = 0;
                    Line currentLine = currentSection.getFirstLine();
                    wordCount = getWordCount(wordsRequired, sectionResult, wordCount, currentLine);
                    if (wordCount == wordsRequired.length) {
                        currentLine = currentSection.getFirstLine();
                        while (currentLine != null) {
                            for (String word : wordsExcluded) {
                                Word currentWord = currentLine.getFirstWord();
                                while (currentWord != null) {
                                    if (currentWord.isStop()) {
                                        currentWord = currentWord.getNextWord();
                                        continue;
                                    }
                                    if (currentWord.getString().replaceAll(
                                            "\\pP", "").equals(word.toLowerCase())) {
                                        excludedFound = true;
                                        break;
                                    }
                                    currentWord = currentWord.getNextWord();
                                }
                            }
                            currentLine = currentLine.getNextLine();
                        }
                        if (!excludedFound) {
                            result.addAll(sectionResult);
                        }
                    }

                }
                currentSection = currentSection.getNextSection();
            }
        }
        return result;
    }

    /**
     * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
     * and at least one of the words in the 'orWords' parameter.
     * Implements simple compound "and/or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * Run-time: O(n)
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param wordsRequired Array of words to find within a defined section in the document.
     * @param orWords Array of words, of which at least one, must be in the same section as 'wordsRequired'.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer,Integer,String>> compoundAndOrSearch(String[] titles, String[] wordsRequired,
                                                                    String[] orWords)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> result = new ArrayList<>();
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                boolean orWordFound = false;
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    List<Triple<Integer,Integer,String>> sectionResult = new ArrayList<>();
                    int wordCount = 0;
                    Line currentLine = currentSection.getFirstLine();
                    wordCount = getWordCount(wordsRequired, sectionResult, wordCount, currentLine);
                    if (wordCount == wordsRequired.length) {
                        currentLine = currentSection.getFirstLine();
                        while (currentLine != null) {
                            for (String word : orWords) {
                                Word currentWord = currentLine.getFirstWord();
                                while (currentWord != null) {
                                    if (currentWord.isStop()) {
                                        currentWord = currentWord.getNextWord();
                                        continue;
                                    }
                                    if (currentWord.getString().replaceAll(
                                            "\\pP", "").equals(word.toLowerCase())) {
                                        orWordFound = true;
                                        Triple<Integer, Integer, String> triple = new Triple<>(currentLine.getLineNo(),
                                                currentWord.getColumnNo(), word.toLowerCase());
                                        sectionResult.add(triple);
                                        break;
                                    }
                                    currentWord = currentWord.getNextWord();
                                }
                            }
                            currentLine = currentLine.getNextLine();
                        }
                        if (orWordFound) {
                            result.addAll(sectionResult);
                        }
                    }
                }
                currentSection = currentSection.getNextSection();
            }
        }
        return result;
    }
}
