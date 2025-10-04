public class SentList {
    String word;
    double value;


    SentList (String w, double v){
      word = w;
      value = v;
    }
    //To test output, syntax from google
    @Override
    public String toString() {
        // This is where you define the readable output format
        return "[" + this.word + ", Value: " + this.value + "]";
    }
}
