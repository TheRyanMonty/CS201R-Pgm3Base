import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Main{

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<SentList> sentList = new ArrayList<SentList>();
        ArrayList<SentList> posList = new ArrayList<SentList>();
        ArrayList<SentList> negList = new ArrayList<SentList>();
        ArrayList<Words> wordList = new ArrayList<Words>();

        //load sentiment, positive words and negative words
        readSentimentList(sentList, posList, negList);

        //read review
        //load ArrayList wordList that will contain original review & pos & neg
        String inFileName;
        String outFileName = "reviews.txt";
        PrintWriter outFile = new PrintWriter(outFileName);

        for (int i = 1; i <= 8; i++){
             String number = Integer.toString(i);
             inFileName = "review" + number + ".txt";
             wordList.clear();
             if (readReview(sentList, posList, negList, wordList, inFileName)) 
                printReview(wordList, inFileName, outFile); 
        }

        outFile.close();
    }

    public static void readSentimentList(ArrayList<SentList> sentList,
                                         ArrayList<SentList> posList,
                                         ArrayList<SentList> negList){
        String csvFilePath = "sentiment.txt";
        String line;
        double tempValue;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
                 // Split the line by commas into an array of strings
                 String[] values = line.split(",");
                 
                 //Create object SentList & add to sentList arraylist
                 tempValue = Double.parseDouble(values[1]);
                 SentList tempList = new SentList(values[0], tempValue);
                 sentList.add(tempList);
                 if (tempValue > 1.25)
                    posList.add(tempList);
                 if (tempValue < -1.25)
                    negList.add(tempList);
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

        String line, oWord, eWord, cWord, pWord, nWord;
        double oValue;
        double tempValue, pValue, nValue;
        int idx, arraySize;
        boolean pFlag = false, nFlag = false;
        Words tempWord;
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


                 for (int i = 0; i < values.length; i++){
                    oWord = pWord = nWord = values[i];

                    if (isPunctuation(oWord.charAt(oWord.length() - 1))){
                        eWord = oWord.substring(0,oWord.length() - 1);
                    }
                    else{
                        eWord = oWord;
                    }
                    eWord = eWord.toLowerCase();
                    oValue =  getSentiment(sentList, eWord);
                    nValue = pValue = oValue;

                    if (oValue < -1){
                        //get a random positive word
                        idx = (int)(Math.random()*(posList.size()));
                        pWord = posList.get(idx).word;
                        pValue = posList.get(idx).value;
                        pFlag = true;
                    }
                    if (oValue > 1){
                        //get a random negative word
                        idx = (int)(Math.random()*(negList.size()));
                        nWord = negList.get(idx).word;
                        nValue = negList.get(idx).value;
                        nFlag = true;
                    }

                    tempWord = new Words(oWord, eWord, pWord, nWord, oValue, pValue, nValue);
                    Words.setPosFlag(pFlag);
                    Words.setNegFlag(nFlag);
                    wordList.add(tempWord);
                }
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

        double tempTotalOrig = 0, tempTotalPos = 0, tempTotalNeg = 0;
        double tempReplacedOrigP = 0, tempReplacedOrigN = 0, tempReplacedPos = 0, tempReplacedNeg = 0;
        int origCharCount = 0, posCharCount = 0, negCharCount = 0, charCount = 0;
        String origReview = new String("\n\nOriginal Review for File: " + inFile + "\n\n");
        String posReview = new String("\n\nPositive Review for File: " + inFile);
        String negReview = new String("\n\nNegative Review for File: " + inFile);
        String posChart = new String();
        String negChart = new String();

        if (!(Words.getPosFlag()))
            posReview += ". Review cannot be made more positive";     
        else
            posReview += "\n";

        if (!(Words.getNegFlag()))
            negReview += ". Review cannot be made more negative";     
        else
            negReview += "\n";

        for (int i = 0; i < wordList.size(); i++){
            
            tempTotalOrig += wordList.get(i).sentOrigValue;
            tempTotalPos += wordList.get(i).sentPosValue;
            tempTotalNeg += wordList.get(i).sentNegValue;

            //create original review
            if (origCharCount + wordList.get(i).origWord.length() > 80){
                origReview += "\n";
                origCharCount = wordList.get(i).origWord.length() + 1;
            }
            else
                origCharCount += wordList.get(i).origWord.length() + 1;
            origReview += wordList.get(i).origWord + " ";


            //check for posReview 
            if (Words.getPosFlag()){
                if (posCharCount + wordList.get(i).posWord.length()  > 80){
                    posReview += "\n";
                    posCharCount = wordList.get(i).posWord.length()  + 1;
                }
                else
                    posCharCount += wordList.get(i).posWord.length() +  + 1;
                posReview += wordList.get(i).posWord + " ";
                if (!(wordList.get(i).origWord.equals(wordList.get(i).posWord))){
                    posChart += String.format("%20s%8.2f%20s%8.2f\n",wordList.get(i).editWord, wordList.get(i).sentOrigValue,
                                                wordList.get(i).posWord, wordList.get(i).sentPosValue);
                    tempReplacedOrigP += wordList.get(i).sentOrigValue;
                    tempReplacedPos += wordList.get(i).sentPosValue;
                }
            }

            //check for negative Review 
            if (Words.getNegFlag()){
                if (negCharCount + wordList.get(i).negWord.length()  > 80){
                    negReview += "\n";
                    negCharCount = wordList.get(i).negWord.length()  + 1;
                }
                else
                    negCharCount += wordList.get(i).negWord.length() + 1;
                negReview += wordList.get(i).negWord + " ";
                if (!(wordList.get(i).origWord.equals(wordList.get(i).negWord))){
                    negChart += String.format("%20s%8.2f%20s%8.2f\n",wordList.get(i).editWord, wordList.get(i).sentOrigValue,
                                                wordList.get(i).negWord, wordList.get(i).sentNegValue);
                    tempReplacedOrigN += wordList.get(i).sentOrigValue;
                    tempReplacedNeg += wordList.get(i).sentNegValue;
                }
            }
        }  

        outFile.println(origReview + "\n");    
        outFile.printf("\nOriginal Sentiment: %5.2f\n",tempTotalOrig);

        outFile.println(posReview); 
        if (Words.getPosFlag()){
            posChart += String.format("Total: %13s%8.2f%20s%8.2f\n"," ",tempReplacedOrigP, " ",tempReplacedPos); 
            outFile.print(posChart);    
            outFile.printf("\nPositive Sentiment: %5.2f\n",tempTotalPos);   
        }

        outFile.println(negReview);
        if (Words.getNegFlag()){
            negChart += String.format("Total: %13s%8.2f%20s%8.2f\n"," ",tempReplacedOrigN, " ",tempReplacedNeg); 
            outFile.print(negChart);     
            outFile.printf("\nNegative Sentiment: %5.2f\n",tempTotalNeg);  
        }
    }

    static boolean isPunctuation(char ch) {
        if (ch == '!' || ch == '\"' || ch == '#' || ch == '$' || ch == '%' || ch == '&' || ch == '\'' || ch == '(' || ch == ')' || ch == '*' || ch == '+' || ch == ',' || ch == '-' || ch == '.' || ch == '/' || ch == ':' || ch == ';' || ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '@' || ch == '[' || ch == '\\' || ch == ']' || ch == '^' || ch == '`' || ch == '{' || ch == '|' || ch == '}')
          return true;
        return false;
    }

    static double getSentiment (ArrayList<SentList> sentList, String eWord) {
        for (int w = 0; w < sentList.size(); w++){
            if (eWord.equals(sentList.get(w).word)){
                return sentList.get(w).value;
            }
        }
        return 0.0;
    }
}