//NAME: Ryan Montgomery
//ASSIGNMENT: Program 3 Sentiment Analysis
//LECTURE SECTION: Tu/Thurs

//Import needed files
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Main{
    public static void main(String[] args) throws FileNotFoundException {
        //Declare ArrayLists
        ArrayList<SentList> sentList = new ArrayList<SentList>();
        ArrayList<SentList> posList = new ArrayList<SentList>();
        ArrayList<SentList> negList = new ArrayList<SentList>();
        ArrayList<Words> wordList = new ArrayList<Words>();

        //load sentiment, positive words and negative words arraylists
        readSentimentList(sentList, posList, negList);

        //read review
        //load ArrayList wordList that will contain original review & pos & neg
        String inFileName;
        String outFileName = "reviews.txt";
        PrintWriter outFile = new PrintWriter(outFileName);

        // open input file adding review + number + ".txt" to review
        // if not able to open, print a message and continue
        // else process the file
        // if the file can be read properly, print the results
        for (int i = 1; i <= 8; i++){
 
        }

        outFile.close();
    }


    public static void readSentimentList(ArrayList<SentList> sentList,
                                         ArrayList<SentList> posList,
                                         ArrayList<SentList> negList){
    //PRE:  accept the empty ArrayLists created in main
    //POST: the arrays are loaded with the proper words and information
        String csvFilePath = "sentiment.txt";
        String line;
        double tempValue;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
                 // Split the line by commas into an array of strings
 
                 
                 //Create object SentList & add to sentList arraylist
 
                 //if word values are pos add to posList
                 //if word values are neg, add to neglist
            }
        } 
        catch (IOException e) {
            System.out.println("Error reading the file.");
            e.printStackTrace();
        }
    }

    public static boolean readReview(ArrayList<SentList> sentList,
                                  ArrayList<SentList> posList,
                                  ArrayList<SentList> negList,
                                  ArrayList<Words> wordList,
                                  String fileName){

        //PRE: accept the word lists and file name to open
        //POST: read the file while the line is not null
        //      each word is edited (to lower case without punctuation)
        //      the sentiment value is accessed
        //      if the word is positive - update to a random word in the negative list and update the word value
        //      if the word is negative - update to a random positive word in the positive list & update the word value

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                 // Split the line by commas into an array of strings
                 String[] values = line.split(" ");
                 
                 //for each word
                 //  strip off punctuation at the end of word set charWord to null or value of punctuation
                 //  save origWord, posWord, negWord
                 //  change to lower case  (set editWord)
                 //  look up word in sentList (set origValue, posVlue, negValue)
                 //  if positive enough, find new negword in negList & reset negWord & NegValue, reset negFlag
                 //  if negative enough, find new posword in posList & reset posWord & posValue, reset posFlag


            }
            br.close();
            return true;
        } 
        catch (IOException e) {
            System.out.println("Error reading the file: " + fileName);
            return false;
        }
    }

    public static void printReview(ArrayList<Words> wordList, String inFile, 
                                  PrintWriter outFile){
        //PRE:  accept the updated wordlist
        //POST: loop through word list, create a string that will be the original, positive & negative reviews
        //      print each review
 
    }

    static boolean isPunctuation(char ch) {
        //PRE:  accept a character
        //POST: return true if this character is punctuation; false otherwise 
        if (ch == '!' || ch == '\"' || ch == '#' || ch == '$' || ch == '%' || ch == '&' || ch == '\'' || ch == '(' || ch == ')' || ch == '*' || ch == '+' || ch == ',' || ch == '-' || ch == '.' || ch == '/' || ch == ':' || ch == ';' || ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '@' || ch == '[' || ch == '\\' || ch == ']' || ch == '^' || ch == '`' || ch == '{' || ch == '|' || ch == '}')
          return true;
        return false;
    }

    static double getSentiment (ArrayList<SentList> sentList, String eWord) {
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