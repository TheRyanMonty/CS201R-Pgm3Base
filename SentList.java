public class SentList {
    String word;
    double value;


    SentList (String w, double v){
      word = w;
      value = v;
    }
    @Override
    public String toString() {
        return "Word: " + this.word + ", Value: " + this.value;
        // Or any format you prefer, e.g., return this.word + "(" + this.sentimentValue + ")";
    }
}
