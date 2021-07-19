import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NLP {
    static HashMap<String,Integer> posDict;
    static HashMap<String,Integer> posBigramDict;
    static HashMap<String,Integer> negDict;
    static HashMap<String,Integer> negBigramDict;

    public static void main(String[] args) throws FileNotFoundException {
        //Load data
        File pos=new File("./rt-polarity.pos");
        File neg=new File("./rt-polarity.neg");
        //Preprocess data
        preprocess(pos,"Positive");
        preprocess(neg,"Negative");
        //Get input
        Scanner sc=new Scanner(System.in);
        String tmp=sc.nextLine();
        while (!tmp.equals("!q")){
            tmp=sc.nextLine();
            boolean shouldFilter=calculateProbability(tmp);
            if(shouldFilter){
                System.out.println("filter this");
            }else{
                System.out.println("not filter this");
            }
        }



    }

    static boolean calculateProbability(String sentence){
        return true;
    }
    static void preprocess(File file,String kind) throws FileNotFoundException {
        Scanner sc=new Scanner(file);
        ArrayList<String> words=new ArrayList<>();
        String tmp=sc.next();
        while(tmp!=null){
            words.add(tmp);
            try {
                tmp = sc.next();
            }catch (Exception e){
                tmp=null;
            }
        }
        HashMap<String,Integer> dict=new HashMap<>();
        HashMap<String,Integer> bigramDict=new HashMap<>();
        for(String s:words){
            if(!s.equals("*")) {
                if (dict.get(s) != null) {
                    dict.replace(s, dict.get(s) + 1);
                } else {
                    dict.put(s, 1);
                }
            }
        }
        for(int i=0;i<words.size()-1;i++){
            if(!words.get(i).equals(".")){
                String currentPair=words.get(i)+" "+words.get(i+1);
                if(bigramDict.get(currentPair)!=null) {
                    bigramDict.put(currentPair, bigramDict.get(currentPair)+1);
                }else{
                    bigramDict.put(currentPair,1);
                }
            }
        }
        if(kind.equals("Positive")){
            posDict=dict;
            posBigramDict=bigramDict;
        }else if(kind.equals("Negative")){
            negDict=dict;
            negBigramDict=bigramDict;
        }
    }
}
