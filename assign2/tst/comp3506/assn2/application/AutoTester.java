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
        String data; //Store one line string from files
        String[] words; //Array of string to be stored words from a line.
        BufferedReader br; //buffered reader
        this.section = null;
        StopWord stopWord = null;
        Section currentSection = null;
        Section newSection;
        Line currentLine;
        Word currentWord;
        StopWord currentStopWord = null;
        StopWord newStopWord;
        try {
            if (documentFileName == null) {
                throw new IllegalArgumentException(); //documentFile cannot be empty.
            }
            if (!(indexFileName == null || indexFileName.isEmpty())) {
                // If indexFile is not NULL or not empty
                br = new BufferedReader(new InputStreamReader(new FileInputStream(indexFileName)));
                while((data = br.readLine()) != null) {
                    // read line until it is empty
                    newSection = new Section(Integer.parseInt(data.split(",")[1]), data.split(",")[0]);
                    if (this.section == null) {
                        this.section = newSection; // Set first section
                        currentSection = this.section;
                    } else if (currentSection != null) {
                        Section lastSection = currentSection;
                        currentSection.setNextSection(newSection); //  Set next section
                        currentSection = currentSection.getNextSection();
                        currentSection.setPreviousSection(lastSection); // Set previous section
                        Section previousSection = currentSection.getPreviousSection();
                        firstLineNo = previousSection.getFirstLine().getLineNo(); // Get first line no of previous section
                        lastLineNo = currentSection.getFirstLine().getLineNo(); // Get first line no of current section
                        currentLine = previousSection.getFirstLine();
                        for (int i = firstLineNo + 1; i < lastLineNo; i++) {
                            // Initialize Line of previous section
                            currentLine.setNextLine(i);
                            currentLine = currentLine.getNextLine();
                        }
                    }
                }
            }
            if (!(stopWordsFileName == null || stopWordsFileName.isEmpty())) {
                // If stopWordsFile is not NULL or not empty
                br = new BufferedReader(new InputStreamReader(new FileInputStream(stopWordsFileName)));
                while((data = br.readLine()) != null) {
                    // Read line from file
                    newStopWord = new StopWord(data);
                    if (stopWord == null) {
                        // Set first stopWord
                        stopWord = newStopWord;
                        currentStopWord = stopWord;
                    } else if (currentStopWord != null) {
                        currentStopWord.setNextStopWord(newStopWord);
                        currentStopWord = currentStopWord.getNextStopWord();
                    }
                }
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(documentFileName)));
            // Read document
            if (this.section != null) {
                // Have indexFile
                currentSection = this.section;
                nextStartLineNo = currentSection.getNextSection().getFirstLine().getLineNo();
            } else {
                // No indexFile provided
                this.section = new Section(1, "");
                currentSection = this.section;
                nextStartLineNo = -1;
            }
            currentLine = currentSection.getFirstLine();
            startLineNo = currentSection.getFirstLine().getLineNo();
            while((data = br.readLine()) != null) {
                // Read line from document
                lineCount++;
                if (lineCount >= startLineNo && (lineCount < nextStartLineNo || nextStartLineNo == -1)) {
                    // Choose lines in current section depends on lineCount
                    columnNo = 1;
                    words = data.toLowerCase().split("\\s");
                    if (words.length == 0) {
                        // whitespace, newline, etc
                        currentWord = new Word(columnNo, "");
                        currentWord.setIsStop();
                    } else {
                        currentWord = new Word(columnNo, words[0]);
                        if (words[0].length() == 0) {
                            //whitespace, newline, etc
                            currentWord.setIsStop();
                        } else {
                            currentStopWord = stopWord;
                            while(currentStopWord != null) {
                                // Detect whether first word on current line is stopWord
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
                        // This is the last section. No next section first line available
                        currentLine.setNextLine(currentLine.getLineNo() + 1);
                    }
                    currentLine.setFirstWord(currentWord);
                    for (int i = 1; i < words.length; i++) {
                        columnNo += (currentWord.getStringSize() + 1); // Store column info of this word
                        currentWord.setNextWord(columnNo, words[i]);
                        currentWord = currentWord.getNextWord();
                        if (words[i].length() == 0) {
                            // whitespace, newline, etc
                            currentWord.setIsStop();
                        } else {
                            currentStopWord = stopWord;
                            while(currentStopWord != null) {
                                if (currentStopWord.getStopWord().equals(words[i].toLowerCase())) {
                                    // current word is stopWord
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
                        // Change to next section
                        currentSection = currentSection.getNextSection();
                        if (currentSection.getNextSection() == null) {
                            // Current section is the last one
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
        int columnNo; // Column no of the first character of this word
        int size; // The original size of this word
        String string;
        Word next;
        boolean isStop;

        Word(int column, String currentWord) {
            columnNo = column;
            string = currentWord.replaceAll("[,.?!@#$%^&*:]", "");
            size = currentWord.length();
            next = null;
            isStop = false;
        }

        /**
         * Get column number.
         *
         * Run-time: O(1)
         *
         * @return The column no of current Word.
         */
        int getColumnNo() {
            return columnNo;
        }

        /**
         * Get string size.
         *
         * Run-time: O(1)
         *
         * @return The original size of current word.
         */
        int getStringSize() {
            return size;
        }

        /**
         * Determines this word is a stopWord.
         *
         * Run-time: O(1)
         *
         * @return True if current word is a stoopWord.
         */
        boolean isStop() {
            return isStop;
        }

        /**
         * Get next word.
         *
         * Run-time: O(1)
         *
         * @return next Word or null.
         */
        Word getNextWord() {
            return next;
        }

        /**
         * Get string of current word.
         *
         * Run-time: O(1)
         *
         * @return String or null.
         */
        String getString() {
            return string;
        }

        /**
         * Set next word.
         *
         * Run-time: O(1)
         *
         * @param column next word column no.
         * @param nextWord next word
         */
        void setNextWord(int column, String nextWord) {
            next = new Word(column, nextWord);
        }

        /**
         * Set current word is a stopWord.
         *
         * Run-time: O(1)
         *
         */
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

        /**
         * Get line no of current line.
         *
         * Run-time: O(1)
         *
         * @return lineNo.
         */
        int getLineNo() {
            return lineNo;
        }

        /**
         * Get first word of current line.
         *
         * Run-time: O(1)
         *
         * @return Word.
         */
        Word getFirstWord() {
            return firstWord;
        }

        /**
         * Get next line.
         *
         * Run-time: O(1)
         *
         * @return Line.
         */
        Line getNextLine() {
            return next;
        }

        /**
         * Get string of current word.
         *
         * Run-time: O(1)
         *
         * @param word next word
         */
        void setFirstWord(Word word) {
            firstWord = word;
        }

        /**
         * Get string of current word.
         *
         * Run-time: O(1)
         *
         * @param line line no of next line
         */
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

        /**
         * Get first line of current section.
         *
         * Run-time: O(1)
         *
         * @return Line.
         */
        Line getFirstLine() {
            return firstLine;
        }

        /**
         * Get next section.
         *
         * Run-time: O(1)
         *
         * @return Section.
         */
        Section getNextSection() {
            return next;
        }

        /**
         * Get previous section.
         *
         * Run-time: O(1)
         *
         * @return Section.
         */
        Section getPreviousSection() {
            return previous;
        }

        /**
         * Get title of current Section.
         *
         * Run-time: O(1)
         *
         * @return String.
         */
        String getTitle() {
            return title;
        }

        /**
         * Set next section.
         *
         * Run-time: O(1)
         *
         * @param newSection next section
         */
        void setNextSection(Section newSection) {
            next = newSection;
        }

        /**
         * Set previous section.
         *
         * Run-time: O(1)
         *
         * @param lastSection previous section
         */
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

        /**
         * Get string of current stopWord.
         *
         * Run-time: O(1)
         *
         * @return String .
         */
        String getStopWord() {
	        return word;
        }

        /**
         * Get next stopWord.
         *
         * Run-time: O(1)
         *
         * @return StopWord.
         */
        StopWord getNextStopWord() {
	        return next;
        }

        /**
         * Set next stopWord.
         *
         * Run-time: O(1)
         *
         * @param nextStopWord next StopWord
         */
        void setNextStopWord(StopWord nextStopWord) {
	        next = nextStopWord;
        }
    }

    /**
     * Search words on current line.
     *
     * Run-time: O(n)
     *
     * @param wordsRequired Array of string need to be searched
     * @param currentLine current Line
     * @return True if words found otherwise false.
     */
    private boolean isWordFound(String[] wordsRequired, Line currentLine) {
        for (String word : wordsRequired) {
            // Searching starts the first word.
            Word currentWord = currentLine.getFirstWord(); // Get current Line
            while (currentWord != null) {
                if (currentWord.isStop()) {
                    currentWord = currentWord.getNextWord();
                    continue;
                }
                if (word.toLowerCase().equals(currentWord.getString())) {
                    // word found
                    return true;
                }
                currentWord = currentWord.getNextWord();
            }
        }
        return false;
    }

    /**
     * Get word count for current line save line no, column no and word into result.
     *
     * Run-time: O(n)
     *
     * @param words Array of string need to be searched
     * @param sectionResult Array list of search result
     * @param wordCount increment when words found
     * @param currentLine current Line
     * @return Number of words were found.
     */
    private int getWordCount(String[] words, List<Triple<Integer, Integer, String>> sectionResult,
                             int wordCount, Line currentLine) {
        while (currentLine != null) {
            for (String word : words) {
                // Stars searching from first word
                Word currentWord = currentLine.getFirstWord();
                while (currentWord != null) {
                    if (currentWord.isStop()) {
                        currentWord = currentWord.getNextWord();
                        continue;
                    }
                    if (currentWord.getString().replaceAll(
                            "[,.?!@#$%^&*:]", "").equals(word.toLowerCase())) {
                        Triple<Integer, Integer, String> triple = new Triple<>(currentLine.getLineNo(),
                                currentWord.getColumnNo(), word.toLowerCase());
                        sectionResult.add(triple); // save result
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

    /**
     * Save line no, column no and word into result. This function be called when the first word was found.
     *
     * Run-time: O(n)
     *
     * @param words Array of string need to be searched
     * @param result Array list of search result
     * @param currentSection current Section
     * @param currentLine current Line
     */
    private void getResult(Word currentWord, Line currentLine, Section currentSection, String[] words,
                                            List<Pair<Integer,Integer>> result) {
        Word nextWord = null;
        int wordsMatch = 1;
        int lineNo = currentLine.getLineNo();
        int columnNo = currentWord.getColumnNo();
        Pair<Integer,Integer> pairResult = new Pair<>(lineNo, columnNo);
        if (words.length == 1) {
            // Found enough words.
            result.add(pairResult);
        } else {
            // Find the rest words.
            // Switch Line and Section if current Line or current Section is the end to continue to search
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
                if (words[wordsMatch].toLowerCase().equals(nextWord.getString())) {
                    wordsMatch++;
                } else {
                    break;
                }
                // Switch Line and Section if current Line or current Section is the end to continue to search
                if (nextWord.getNextWord() != null) {
                    nextWord = nextWord.getNextWord();
                } else if (currentLine.getNextLine() != null) {
                    currentLine = currentLine.getNextLine();
                    nextWord = currentLine.getFirstWord();
                } else if (currentSection.getNextSection() != null) {
                    currentSection = currentSection.getNextSection();
                    currentLine = currentSection.getFirstLine();
                    nextWord = currentLine.getFirstWord();
                } else {
                    // Quit searching if the last line of the last section have been searched.
                    break;
                }
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
	    if (word == null || word.isEmpty()) {
	        throw new IllegalArgumentException();
        }
	    while (currentSection != null) {
	        Line currentLine = currentSection.getFirstLine();
	        while (currentLine != null) {
	            Word currentWord = currentLine.getFirstWord();
	            while (currentWord != null) {
	                if (currentWord.isStop()) {
                        currentWord = currentWord.getNextWord();
	                    continue;
                    }
                    if (word.toLowerCase().trim().equals(currentWord.getString())) {
	                    // Word found
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
        if (phrase == null || phrase.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String[] words = phrase.toLowerCase().trim().replaceAll("[,.?!@#$%^&*:]", "").split("\\s");
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
                        // Found the first word
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
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException();
        }
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
                            // Found word
                            counts++;
                        }
                    }
                    if (counts == prefix.length()) {
                        // Found all words
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
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : words) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
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
                        if (word.toLowerCase().trim().equals(currentWord.getString())) {
                            // Word found
                            wordsCount++;
                            break;
                        }
                        currentWord = currentWord.getNextWord();
                    }
                }
                if (wordsCount == words.length) {
                    // Found all words
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
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : words) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
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
                            // Found word
                            if (!result.contains(currentLine.getLineNo())) {
                                // This line is not added yet
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
        if (wordsRequired == null || wordsRequired.length == 0 || wordsExcluded == null || wordsExcluded.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : wordsRequired) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : wordsExcluded) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        while (currentSection != null) {
            Line currentLine = currentSection.getFirstLine();
            while (currentLine != null) {
                boolean wordFound;
                boolean wordExcludedFound;
                wordFound = isWordFound(wordsRequired, currentLine);
                if (wordFound) {
                    // Word found
                    wordExcludedFound = isWordFound(wordsExcluded, currentLine); // whether contains excluded words
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
        if (titles == null || titles.length == 0 || words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : titles) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : words) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    // Found Section
                    List<Triple<Integer,Integer,String>> sectionResult = new ArrayList<>();
                    int wordCount = 0;
                    Line currentLine = currentSection.getFirstLine();
                    wordCount = getWordCount(words, sectionResult, wordCount, currentLine);
                    if (wordCount == words.length) {
                        // Found all words
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
        if (titles == null || titles.length == 0 || words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : titles) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : words) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
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
                                if (currentWord.getString().equals(word.toLowerCase())) {
                                    // Found word
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
        if (wordsRequired == null || wordsRequired.length == 0 || wordsExcluded == null || wordsExcluded.length == 0 ||
                titles == null || titles.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : wordsRequired) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : wordsExcluded) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : titles) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                boolean excludedFound = false;
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    // Found Section
                    List<Triple<Integer,Integer,String>> sectionResult = new ArrayList<>();
                    int wordCount = 0;
                    Line currentLine = currentSection.getFirstLine();
                    wordCount = getWordCount(wordsRequired, sectionResult, wordCount, currentLine);
                    if (wordCount == wordsRequired.length) {
                        // Found all required words
                        currentLine = currentSection.getFirstLine();
                        while (currentLine != null) {
                            for (String word : wordsExcluded) {
                                // Search excluded words
                                Word currentWord = currentLine.getFirstWord();
                                while (currentWord != null) {
                                    if (currentWord.isStop()) {
                                        currentWord = currentWord.getNextWord();
                                        continue;
                                    }
                                    if (currentWord.getString().equals(word.toLowerCase())) {
                                        // Result not valid
                                        excludedFound = true;
                                        break;
                                    }
                                    currentWord = currentWord.getNextWord();
                                }
                            }
                            currentLine = currentLine.getNextLine();
                        }
                        if (!excludedFound) {
                            // Found required words without words excluded words
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
        if (wordsRequired == null || wordsRequired.length == 0 || orWords == null || orWords.length == 0 ||
                titles == null || titles.length == 0) {
            throw new IllegalArgumentException();
        }
        for (String string : wordsRequired) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : orWords) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String string : titles) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
        for (String name : titles) {
            Section currentSection = this.section;
            while (currentSection != null) {
                boolean orWordFound = false;
                if (currentSection.getTitle().equals(name.toLowerCase())) {
                    // Found Section
                    List<Triple<Integer,Integer,String>> sectionResult = new ArrayList<>();
                    int wordCount = 0;
                    Line currentLine = currentSection.getFirstLine();
                    wordCount = getWordCount(wordsRequired, sectionResult, wordCount, currentLine);
                    if (wordCount == wordsRequired.length) {
                        // Found all required words
                        currentLine = currentSection.getFirstLine();
                        while (currentLine != null) {
                            for (String word : orWords) {
                                Word currentWord = currentLine.getFirstWord();
                                while (currentWord != null) {
                                    if (currentWord.isStop()) {
                                        currentWord = currentWord.getNextWord();
                                        continue;
                                    }
                                    if (currentWord.getString().equals(word.toLowerCase())) {
                                        // Found word
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
