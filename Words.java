//This class includes both the original, positive & negative review words
//This does not need to be done this way - it is just a suggestion
public class Words {
    String origWord;
    String editWord;
    String charWord;
    String posWord;
    String negWord;
    double sentOrigValue;
    double sentPosValue;
    double sentNegValue;
    static boolean posFlag = false;
    static boolean negFlag = false;

    public Words(String o, String e, String c, String p, String n, double s, double sp, double sn){
        origWord = o;
        editWord = e; 
        charWord = c;
        posWord = p;
        negWord = n;
        sentOrigValue = s;
        sentPosValue = sp;
        sentNegValue = sn;
    }
    public static void setPosFlag(boolean s){
        posFlag = s;
    }
    public static void setNegFlag(boolean s){
        negFlag = s;
    }

    public static boolean getPosFlag(){
        return posFlag;
    }
    public static boolean getNegFlag(){
        return negFlag;
    }
    /*@Override
    public String toString() {
        return "Words{" +
                "origWord='" + origWord + '\'' +
                ", editWord='" + editWord + '\'' +
                ", charWord='" + charWord + '\'' +
                ", posWord='" + posWord + '\'' +
                ", negWord='" + negWord + '\'' +
                ", sentOrigValue=" + sentOrigValue +
                ", sentPosValue=" + sentPosValue +
                ", sentNegValue=" + sentNegValue +
                '}';
    }*/
}
