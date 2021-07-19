import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class NLP {
    static ArrayList<String> testDataPos = new ArrayList<>();
    static ArrayList<String> testDataNeg = new ArrayList<>();
    static HashMap<String, Integer> posDict;
    static HashMap<String, Integer> posBigramDict;
    static int numOfWordsInPos;
    static HashMap<String, Integer> negDict;
    static HashMap<String, Integer> negBigramDict;
    static int numOfWordsInNeg;

    public static void main(String[] args) throws FileNotFoundException {
        //Load data
        File pos = new File("./rt-polarity.pos");
        File neg = new File("./rt-polarity.neg");
        //Preprocess data
        preprocess(pos, "Positive");
        preprocess(neg, "Negative");
        //Set Parameters
        double[] landa1 = {0.1};
        double[] landa2 = {0.25};
        double[] landa3 = {0.65};
        double[] epsilon = {0.4};
        // Menu
        System.out.println("1 : Your custom sentence");
        System.out.println("2 : From positive test data ");
        System.out.println("3 : From negative test data ");
        System.out.println("4 : From both positive and negative test data");
        System.out.println("5 : unigram");
        System.out.println("!q : Exit");
        Scanner sc = new Scanner(System.in);
        String method = sc.nextLine();
        while (!method.equals("!q")) {
            if (method.equals("1")) {
                //Get input
                String tmp = sc.nextLine();
                while (!tmp.equals("!q")) {
                    boolean shouldFilter = calculateProbability(tmp, landa1[0], landa2[0], landa3[0], epsilon[0]);
                    if (shouldFilter) {
                        System.out.println("filter this");
                    } else {
                        System.out.println("not filter this");
                    }
                    tmp = sc.nextLine();
                }
            } else if (method.equals("2")) {

                int correct = 0;
                int all = 0;
                for (String test : testDataPos) {
                    if (!calculateProbability(test, landa1[0], landa2[0], landa3[0], epsilon[0])) {
                        correct++;
                    }
                    all++;
                }
                System.out.println("Precision : " + (((double) correct) / ((double) all)) * 100);

            } else if (method.equals("3")) {

                int correct = 0;
                int all = 0;
                for (String test : testDataNeg) {
                    if (calculateProbability(test, landa1[0], landa2[0], landa3[0], epsilon[0])) {
                        correct++;
                    }
                    all++;
                }
                System.out.println("Precision : " + (((double) correct) / ((double) all)) * 100);

            } else if (method.equals("4")) {

                int correct = 0;
                int all = 0;
                for (String test : testDataPos) {
                    if (!calculateProbability(test, landa1[0], landa2[0], landa3[0], epsilon[0])) {
                        correct++;
                    }
                    all++;
                }
                for (String test : testDataNeg) {
                    if (calculateProbability(test, landa1[0], landa2[0], landa3[0], epsilon[0])) {
                        correct++;
                    }
                    all++;
                }
                System.out.println("Precision : " + (((double) correct) / ((double) all)) * 100);

            } else if (method.equals("5")) {
                {
                    int correct = 0;
                    int all = 0;
                    for (String test : testDataPos) {
                        if (!calculateUnigram(test, landa1[0], landa2[0], epsilon[0])) {
                            correct++;
                        }
                        all++;
                    }
                    System.out.println("Pos Precision : " + (((double) correct) / ((double) all)) * 100);
                }
                {
                    int correct = 0;
                    int all = 0;
                    for (String test : testDataNeg) {
                        if (calculateUnigram(test, landa1[0], landa2[0], epsilon[0])) {
                            correct++;
                        }
                        all++;
                    }
                    System.out.println("Neg Precision : " + (((double) correct) / ((double) all)) * 100);
                }
            }
            if (method.equals("1")) {
                method = "!q";
            } else {
                method = sc.nextLine();
            }
        }


    }

    /**
     * Model with only unigram probability calculating
     *
     * @param sentence
     * @param landa1
     * @param landa2
     * @param epsilon
     * @return
     */
    static boolean calculateUnigram(String sentence, double landa1, double landa2, double epsilon) {
//        String[] words = sentence.split(" ");
        String[] tmpWords=sentence.split(" ");
        ArrayList<String> tmp=new ArrayList<>();
        for(int i=0;i<tmpWords.length;i++){
            String word=tmpWords[i];
            if(!word.equals(".") && !word.equals(",") && !word.equals("this") && !word.equals("the") && !word.equals("*") && !word.equals("?") && !word.equals("!") && !word.equals("a")){
                tmp.add(word);
            }
        }
        String[] words=new String[tmp.size()];
        for(int i=0;i<tmp.size();i++){
            words[i]=tmp.get(i);
        }
        double posProb = 0;
        double negProb = 0;
        for (String word : words) {
            posProb += specialLog(landa2 * p(word, "Positive") + landa1 * epsilon);
            negProb += specialLog(landa2 * p(word, "Negative") + landa1 * epsilon);
        }
        if (negProb > posProb) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Lograithm with care for zero errors
     *
     * @param x
     * @return
     */
    static double specialLog(double x) {
        if (x < 0) {
            System.out.println("bad");
        }
        if (x == 0) {
            return 0;
        } else {
            return Math.log(x);
        }
    }

    /**
     * Calculates probability of a sentence belongs to pos or neg dataset
     * and the bigger one will be chosen for predict
     *
     * @param sentence
     * @param landa1
     * @param landa2
     * @param landa3
     * @param epsilon
     * @return
     */
    static boolean calculateProbability(String sentence, double landa1, double landa2, double landa3, double epsilon) {
//        String[] words = sentence.split(" ");
        String[] tmpWords=sentence.split(" ");
        ArrayList<String> tmp=new ArrayList<>();
        for(int i=0;i<tmpWords.length;i++){
            String word=tmpWords[i];
            if(!word.equals(".") && !word.equals(",") && !word.equals("this") && !word.equals("the") && !word.equals("*") && !word.equals("?") && !word.equals("!") && !word.equals("a")){
                tmp.add(word);
            }
        }
        String[] words=new String[tmp.size()];
        for(int i=0;i<tmp.size();i++){
            words[i]=tmp.get(i);
        }
        double posProb = specialLog(p(words[0], "Positive"));
        posProb += bigramP(words, landa1, landa2, landa3, epsilon, "Positive");
        double negProb = specialLog(p(words[0], "Negative"));
        negProb += bigramP(words, landa1, landa2, landa3, epsilon, "Negative");
//        System.out.println("Pos " + posProb);
//        System.out.println("Neg " + negProb);
        if (negProb > posProb) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates bigram probability of a sentence
     *
     * @param words
     * @param landa1
     * @param landa2
     * @param landa3
     * @param epsilon
     * @param kind
     * @return
     */
    static double bigramP(String[] words, double landa1, double landa2, double landa3, double epsilon, String kind) {
        double ans = 0;
        if (kind.equals("Positive")) {
            for (int i = 1; i < words.length; i++) {
                String biWord = words[i - 1] + " " + words[i];
                double tmp;
                try {
                    tmp = ((double) posBigramDict.get(biWord)) / ((double) posDict.get(words[i - 1]));
                } catch (Exception e) {
                    tmp = 0;
                }
                ans += specialLog(landa3 * tmp + landa2 * p(words[i], "Positive") + landa1 * epsilon);
            }
        } else {
            for (int i = 1; i < words.length; i++) {
                String biWord = words[i - 1] + " " + words[i];
                double tmp;
                try {
                    tmp = ((double) negBigramDict.get(biWord)) / ((double) negDict.get(words[i - 1]));
                } catch (Exception e) {
                    tmp = 0;
                }
                ans += specialLog(landa3 * tmp + landa2 * p(words[i], "Negative") + landa1 * epsilon);
            }
        }
        return ans;
    }

    /**
     * Caculates probability of word 'w' being in 'kind' set
     *
     * @param w
     * @param kind
     * @return
     */
    static double p(String w, String kind) {
        double ans;
        if (kind.equals("Positive")) {
            try {
                ans = ((double) posDict.get(w)) / ((double) numOfWordsInPos);
            } catch (Exception e) {
                ans = 0;
            }
        } else {
            try {
                ans = ((double) negDict.get(w)) / ((double) numOfWordsInNeg);
            } catch (Exception e) {
                ans = 0;
            }
        }
        return ans;
    }

    /**
     * Preprocessing data (delete some words and make bigram and unigram dictionary)
     *
     * @param file
     * @param kind
     * @throws FileNotFoundException
     */
    static void preprocess(File file, String kind) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        ArrayList<String> tmpSentences = new ArrayList<>();
        String tmp = sc.nextLine();
        while (tmp != null) {
            tmpSentences.add(tmp);
            try {
                tmp = sc.nextLine();
            } catch (Exception e) {
                tmp = null;
            }
        }
        ArrayList<String> sentences = new ArrayList<>();
        int tenPercent = (int) (0.1 * tmpSentences.size());
        while (tenPercent > 0) {
            Random rand = new Random();
            int index = rand.nextInt(tmpSentences.size());
            String x = tmpSentences.get(index);
            if (kind.equals("Positive") && !testDataPos.contains(x)) {
                testDataPos.add(x);
                tenPercent--;
            } else if (kind.equals("Negative") && !testDataNeg.contains(x)) {
                testDataNeg.add(x);
                tenPercent--;
            }
        }
        for (String s : tmpSentences) {
            if (kind.equals("Positive")) {
                if (!testDataPos.contains(s)) {
                    sentences.add(s);
                }
            } else {
                if (!testDataNeg.contains(s)) {
                    sentences.add(s);
                }

            }
        }
        ArrayList<String> words = new ArrayList<>();
        for (String s : sentences) {
            String[] tmpW = s.split(" ");
            for (String word : tmpW) {
                if (!word.equals(".") && !word.equals(",") && !word.equals("this") && !word.equals("the") && !word.equals("*") && !word.equals("?") && !word.equals("!") && !word.equals("a")) {
                    words.add(word);
                }
            }
        }
        HashMap<String, Integer> dict = new HashMap<>();
        HashMap<String, Integer> bigramDict = new HashMap<>();
        for (String s : words) {

            if (dict.get(s) != null) {
                dict.replace(s, dict.get(s) + 1);
            } else {
                dict.put(s, 1);
            }

        }
        for (int i = 0; i < words.size() - 1; i++) {
            if (true) {
                String currentPair = words.get(i) + " " + words.get(i + 1);
                if (bigramDict.get(currentPair) != null) {
                    bigramDict.put(currentPair, bigramDict.get(currentPair) + 1);
                } else {
                    bigramDict.put(currentPair, 1);
                }
            }
        }
        int num = 0;
        for (String key : dict.keySet()) {
            num += dict.get(key);
        }
        if (kind.equals("Positive")) {
            posDict = dict;
            posBigramDict = bigramDict;
            numOfWordsInPos = num;
        } else if (kind.equals("Negative")) {
            negDict = dict;
            negBigramDict = bigramDict;
            numOfWordsInNeg = num;
        }
    }
}
