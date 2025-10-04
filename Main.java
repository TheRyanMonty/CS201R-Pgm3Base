//NAME: Ryan Montgomery
//DATE: 10/3/25
//ASSIGNMENT: Program 3 Sentiment Analysis
//LECTURE SECTION: Tu/Thurs

//Import needed files
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Main{
    public static void main(String[] args) throws FileNotFoundException {
        //Declare Vectors
        Vector<SentList> sentList = new Vector<SentList>();
        Vector<SentList> posList = new Vector<SentList>();
        Vector<SentList> negList = new Vector<SentList>();
        ArrayList<Words> reviewWordList = new ArrayList<Words>();
        String inFileName, wordType;
        String outFileName = "reviews.txt";
        PrintWriter outFile = new PrintWriter(outFileName);
        double sentimentValue = 0;

        //Read in the sentiment file to the vectors and populate the appropriate lists
        readSentimentList(sentList, posList, negList);

        //Open the review files assuming they start with review followed by a number starting at 1
        //and ending in .txt
        for (int i = 1; i <= 8; i++) {
            //Establish inFileName variable to pass into functions
            inFileName = "review"+i+".txt";
            //Check if file exists, if so process for output, otherwise generate an error
            File fileExists = new File(inFileName);
            if (fileExists.exists()) {
                //Read in review file, populating the reviewWordList as part of the Words class
                readReviewFile(inFileName, reviewWordList, sentList, negList, posList);
                //Assign current label and wordType
                String currentLabel = "Original";
                wordType = "edit";
                //Begin processing original review file and format
                formatPrintReview(reviewWordList, outFile, currentLabel, inFileName, wordType, sentList);
                //Calculate and pass the sentiment value
                sentimentValue = calcSentiment(sentList, reviewWordList, wordType);
                //Output the original sentiment
                printSentiment(sentimentValue, outFile, currentLabel);
                //Update label to positive and wordType
                currentLabel = "Positive";
                wordType = "pos";
                //Output a more positive version of the review
                formatPrintReview(reviewWordList, outFile, currentLabel, inFileName, wordType, sentList);
                //Update label to negative and wordType
                currentLabel = "Negative";
                wordType = "neg";
                //Output a more negative version of the review
                formatPrintReview(reviewWordList, outFile, currentLabel, inFileName, wordType, sentList);
            }
            else {
                System.out.println("Error: File "+inFileName+" does not exist!");
            }
        }
        outFile.close();
    }

    public static void readSentimentList(Vector<SentList> sentList,
                                         Vector<SentList> posList,
                                         Vector<SentList> negList){
    //PRE:  accept the empty ArrayLists created in main
    //POST: the arrays are loaded with the proper words and information
        String csvFilePath = "sentiment.txt";
        String line;
        double tempValue;
        String tempString;
        //Open the sentiment file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            //Read the file line by line until the end of the file
            while ((line = br.readLine()) != null) {
                 // Split the line by commas into a string array called parts
                 // 0 is the sentiment string, 1 is the number value
                 String[] parts = line.split(",");
                 
                 //Assign the number to the double tempValue, and the string to a tempString
                 tempValue = Double.parseDouble(parts[1].trim());
                 tempString = parts[0];

                 //Create object SentList & add to sentList arraylist
                 SentList newEntry = new SentList(tempString, tempValue);
                 sentList.add(newEntry);

                 //if word values are pos add to posList
                 if (tempValue>1.25) {
                    posList.add(newEntry);
                 }
                 //if word values are neg, add to neglist
                 else if (tempValue<-1.25) {
                    negList.add(newEntry);
                 }

            }
        } 
        catch (IOException e) {
            System.out.println("Error reading the file.");
            e.printStackTrace();
        }
    }

    public static void readReviewFile(String fileName, ArrayList<Words> reviewWordList, Vector<SentList> sentList,
                                        Vector<SentList> negList, Vector<SentList> posList) {
        //PRE: Obtain file name, words list, sentiment lists (pos/neg/total)
        //POST: Load review into word list along with all sentiment values and pos/neg words as needed
        //Variable Declaration
        String line, origWord, editWord, charWord, posWord, negWord, testCharWord;
        double sentOrigValue, sentPosValue, sentNegValue;
        Random rand = new Random();
        int upperBounds = 0;
        //Clear the reviewWordList
        reviewWordList.clear();
        //
        //Need to assign the following attributes in the Words class in order:
        //String origWord, String editWord, String charWord; String posWord; 
        //String negWord; double sentOrigValue;  double sentPosValue; double sentNegValue;
        //
        //Open the review file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                //Continue if line is empty
                if (line == null || line.isEmpty()) {
                    continue;
                }
                //Variable Declaration
                 // Split the line by regex space into an array of strings
                 String[] pieces = line.split("\\s+");
                 //Isolate each word
                for ( String word : pieces) {
                    //Assign origWord
                    origWord = word;
                    //Assign editWord
                    //Use regex to isolate and clean the the punctuation from the word and make lowercase
                    editWord = word.replaceAll("\\p{Punct}", "").toLowerCase();
                    //Assign charWord
                    //use regex to isolate punctuation
                    testCharWord = word.replaceAll("\\P{Punct}", "");
                    if (testCharWord.length() > 0) {
                        // If punctuation is found, assign it to charWord
                        charWord = testCharWord;
                    } else {
                        // If no punctuation is found, set the object reference to null
                        charWord = ""; 
                    }
                    //Assign sentOrigValue
                    sentOrigValue = getSentiment(sentList, editWord);
                    //Assign posWord if the original word is less than negative one
                    if (sentOrigValue<-1) {
                        //set upperbounds by determining the size of positive word list
                        upperBounds = posList.size();
                        //Generate a random number and pull from the vector
                        int randomIndex = rand.nextInt(upperBounds);
                        posWord = posList.get(randomIndex).word;
                    }
                    else {
                        posWord = "";
                    }
                    //Assign negWord if original sentiment is greater than 1
                    if (sentOrigValue>1) {
                        //set upperbounds by determining the size of positive word list
                        upperBounds = negList.size();
                        //Generate a random number and pull from the vector
                        int randomIndex = rand.nextInt(upperBounds);
                        negWord = negList.get(randomIndex).word;
                    }
                    else {
                        negWord = "";
                    }                    
                    //Assign sentPosValue
                    sentPosValue = getSentiment(sentList, posWord);
                    //Assign sentNegValue
                    sentNegValue = getSentiment(sentList, negWord);
                    //Create new word entry to add to the list
                    Words newWordEntry = new Words(origWord, editWord, charWord, posWord, negWord, 
                        sentOrigValue, sentPosValue, sentNegValue);
                    //System.out.println("New Entry Created: " + newWordEntry); 
                    //Add the new entry to the word list
                    reviewWordList.add(newWordEntry);
                    //System.out.println("Current word list: "+reviewWordList);
                }
            }  
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading the file: " + fileName);
        }
    }

    public static void formatPrintReview(ArrayList<Words> reviewWordList, PrintWriter outFile, String currentLabel, 
                                        String fileName, String wordType, Vector<SentList> sentList) {

        //PRE: Accept the word list of the review and the post to the outfile for writing
        //POST: Loop through to count 80 characters and after 80 characters insert a newline to outfile
        //
        //Output the formatted review
        outFile.print(currentLabel+" review for file: "+fileName);
        //Initialize the text string
        String text = "";
        //Temporary Vectors
        Vector<SentList> wordsReplaced = new Vector<SentList>();
        Vector<SentList> newWords = new Vector<SentList>();
        String tempWord = "";
        double tempValue = 0;
        double newWordTotal = 0;
        double wordsReplacedTotal = 0;

        //Populate the large string based on wordType
        switch (wordType) {
            case "edit":
                //for loop to make large string based on editWord
                for (Words currentWord : reviewWordList) text += currentWord.origWord + " "; 
                //Create space for next section
                outFile.print("\n\n");
                break;
            case "pos":
                for (Words currentWord : reviewWordList) {
                    //Test if posword is blank, if so reassign to Orig word
                    if (currentWord.posWord == "") text += currentWord.origWord + " ";
                    else {
                        if (currentWord.charWord == "") {
                            //Add the positive word to the text string
                            text += currentWord.posWord + " "; 
                        }
                        //If charWord isn't blank, append charword to the end of the positive word for text output
                        else {
                            text +=currentWord.posWord+currentWord.charWord+" ";
                        }
                        //Add the original word and it's value to a words replaced sent vector
                        tempWord  = currentWord.editWord;
                        tempValue = currentWord.sentOrigValue;
                        wordsReplacedTotal = wordsReplacedTotal+tempValue;
                        wordsReplaced.add(new SentList(tempWord, tempValue));
                        //Add the positive word and it's value to a new words sent vector
                        tempWord = currentWord.posWord;
                        tempValue = currentWord.sentPosValue;
                        newWordTotal = newWordTotal+tempValue;
                        newWords.add(new SentList(tempWord, tempValue));
                    }
                } 
                //If new word total is = 0, print nothing to be done and return
                if (newWordTotal == 0) {
                    outFile.print("  Review cannot be made more positive!\n\n\n");
                    return;
                }
                else outFile.print("\n\n");
                break;
            case "neg":
                for (Words currentWord : reviewWordList) {
                    //Test if negword is blank, if so reassign to Orig word
                    if (currentWord.negWord == "") text += currentWord.origWord + " ";
                    else {
                        if (currentWord.charWord == "") {
                            //Add the negative word to the text string
                            text += currentWord.negWord + " "; 
                        }
                        //If charWord isn't blank, append charword to the end of the negative word for text output
                        else {
                            text +=currentWord.negWord+currentWord.charWord+" ";
                        }
                        //Add the original word and it's value to a words replaced sent vector
                        tempWord  = currentWord.editWord;
                        tempValue = currentWord.sentOrigValue;
                        wordsReplacedTotal = wordsReplacedTotal+tempValue;
                        wordsReplaced.add(new SentList(tempWord, tempValue));
                        //Add the positive word and it's value to a new words sent vector
                        tempWord = currentWord.negWord;
                        tempValue = currentWord.sentNegValue;
                        newWordTotal = newWordTotal+tempValue;
                        newWords.add(new SentList(tempWord, tempValue));
                    }
                } 
                //If new word total is = 0, return
                if (newWordTotal == 0) {
                    outFile.print("  Review cannot be made more negative!\n\n");
                    return;
                }
                else outFile.print("\n\n");
                break;
            default:
                System.out.println("Unknown wordType specified!");
                break;
        }
        //Declare variables
        int startIndex = 0;
        int length = text.length();
        //Main loop to separate based on 80 character limit, used google for structure
        while (startIndex < length) {
            // Determine the maximum position to cut (80 characters away)
            int hardBreakIndex = Math.min(startIndex + 80, length);
            int actualBreakIndex;

            if (hardBreakIndex == length) {
                // Last segment: cut at the end of the text
                actualBreakIndex = length;
            } else {
                // Find the last space (' ') before or at the hard break point
                String segment = text.substring(startIndex, hardBreakIndex);
                int lastSpace = segment.lastIndexOf(' ');
                
                if (lastSpace == -1) {
                    // Fallback: If no space is found, the word is > 80 chars; hard break.
                    actualBreakIndex = hardBreakIndex; 
                } else {
                    // Set break point to the position of the last space
                    actualBreakIndex = startIndex + lastSpace;
                }
            }
            
            // Extract, trim, and output the line segment with a newline from println()
            String line = text.substring(startIndex, actualBreakIndex);
            outFile.println(line.trim()); 

            // Advance the startIndex, skipping the space character we broke on (+1)
            startIndex = actualBreakIndex + 1; 
            
            // Ensure clean loop exit on the last line
            if (actualBreakIndex == length) {
                break;
            }
        } 
        //If this is pos or neg print out the replaced words and new words with their values
        int tempIndex = 0;
        if (wordType == "pos" || wordType == "neg") {
            //Provide heading for section
            outFile.print("\nWords updated to be more ");
            if (wordType == "pos") {
                outFile.print("positive:\n");
            }
            else if (wordType == "neg") {
                outFile.print("negative:\n");
            }
            //for each entry output
            for (SentList eachWord : newWords) {
                //Temporary variables
                SentList tempNewWords = newWords.get(tempIndex);
                SentList tempWordsReplaced = wordsReplaced.get(tempIndex);
                //Print the output
                //Formatting syntax came from google
                outFile.printf(
                    "\t%-20s %-10.2f %-20s %-10.2f%n", 
                    tempWordsReplaced.word,
                    tempWordsReplaced.value,
                    tempNewWords.word,
                    tempNewWords.value
                );
                //Iterate the index counter
                tempIndex++;
            }
            //Formatting syntax came from google
            outFile.printf("\t%-20s %-10s %-20s %-10s%n", " ", "-------", "","------");
            outFile.printf("\t%-20s %-10.2f %-20s %-10.2f%n", "Totals: ", wordsReplacedTotal, " ",newWordTotal);
        }

        //Add new lines to outfile
        outFile.print("\n\n");
        //If newWordTotal is not equal to 0, print the sentiment
        if (wordsReplacedTotal != 0) {
            //Calculate new positive sentiment and output
            double sentimentValue;
            sentimentValue = calcSentiment(sentList, reviewWordList, wordType);
            printSentiment(sentimentValue, outFile, currentLabel);
        }
    }

    public static double calcSentiment(Vector<SentList> sentList, ArrayList<Words> reviewWordList, String wordType) {
        //PRE: Accept sentiment list to pass to getSentiment and reviewWordList for the review words
        //Post: Return the calculated sentiment value
        //
        //Declare Variables
        double sentValue = 0;
        double wordValue = 0;
        String heading, suffix;
        String word ="";
        //Open the file and process
        for (Words currentWord : reviewWordList) {
            //System.out.println("Word type is "+wordType);
            //Get the lowercase word to compare
            switch (wordType) {
                case "edit":
                    word = currentWord.editWord;
                    break;
                case "pos":
                    word = currentWord.posWord;
                    //If posword is null, revert to edit word
                    if (word == "") word = currentWord.editWord;
                    break;
                case "neg":
                    word = currentWord.negWord;
                    //if negword is null, revert to edit word
                    if (word == "") word = currentWord.editWord;
                    break;
                default:
                    System.out.println("Unknown wordType specified!");
                    break;
            }
            //System.out.println("Current word is: "+word);
            //Ensure word value is 0 before lookup
            wordValue = 0;
            //Get the sentiment
            wordValue = getSentiment(sentList, word);
            //Add the sentiment value for later return
            sentValue = sentValue+wordValue;
        }
        //Pass the total value back
        return sentValue;
    }

    public static void printSentiment(double runningTotal, PrintWriter outFile, String currentLabel) {
        //PRE: Accept a total, outfile and label
        //POST: Output Sentiment, flex based on current label from main function
        outFile.printf(currentLabel+" Sentiment: %.2f\n\n\n\n", runningTotal);
    }

    static double getSentiment (Vector<SentList> sentList, String eWord) {
        //PRE:  accept the sentiment words list and a word to find
        //POST: return the value of the sentiment if found, 0 otherwise
        for (int w = 0; w < sentList.size(); w++){
            if (eWord.equals(sentList.get(w).word)){
                return sentList.get(w).value;
            }
        }
        return 0.0;
    }

}