import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class CSP {
    static int n;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./puzzles/puzzle0.txt");
        Scanner sc = new Scanner(file);
        n = sc.nextInt();
        int dummy = sc.nextInt();
        int[][] a = new int[n][n];
        ArrayList<String> variables = new ArrayList<>();
        HashMap<String, ArrayList<Integer>> domains = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String tmp = sc.next();
                if (tmp.equals("-")) {
                    a[i][j] = -1;
                    String current = i + "," + j;
                    ArrayList<Integer> domain = new ArrayList<>();
                    domain.add(0);
                    domain.add(1);
                    variables.add(current);
                    domains.put(current, domain);
                } else {
                    a[i][j] = Integer.parseInt(tmp);
                }
            }
        }

        cspBacktracking(variables, domains,a);


    }

    static void cspBacktracking(ArrayList<String> variables, HashMap<String, ArrayList<Integer>> domains,int[][] a) {
        int[][] newA=new int[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                newA[i][j]=a[i][j];
            }
        }
        String variableToAssign="";
        //MRV
        int minimumRemaining=Integer.MAX_VALUE;
        for(String key:variables){
            if(domains.get(key).size()<minimumRemaining){
                minimumRemaining= domains.get(key).size();
                variableToAssign=key;
            }
        }
        ArrayList<String> newVariables=new ArrayList<>();
        for(String key:variables){
            if(!key.equals(variableToAssign)){
                newVariables.add(key);
            }
        }
        int x=toX(variableToAssign);
        int y=toY(variableToAssign);
        ArrayList<Integer> domain=domains.get(variableToAssign);
        for(Integer value:domain){
            HashMap<String,ArrayList<Integer>> newDomains=new HashMap<>();
            for(String key:domains.keySet()){
                newDomains.put(key,domains.get(key));
            }
            newA[x][y]=value;
            //////Forward Checking
            ///Constraint 1: equal number of zeros and ones
            boolean constraint1=false;
            //Row check
            for(int i=0;i<n;i++){
                ArrayList<String> emptyCoordinates=new ArrayList<>();
                int zeros=0;
                int ones=0;
                int empties=0;
                for(int j=0;j<n;j++){
                    if(newA[i][j]==1) {
                        ones++;
                    }else if(newA[i][j]==0){
                        zeros++;
                    }else{
                        empties++;
                        emptyCoordinates.add(toStr(i,j));
                    }
                }
                if(zeros!=ones){
                    if(zeros+ones==n){
                        constraint1=true;
                        break;
                    }
                    if(empties+zeros<ones || empties+ones<zeros){
                        constraint1=true;
                        break;
                    }
                    if(empties+zeros==ones){
                        for(String emptyCell:emptyCoordinates){
                            ArrayList<Integer> tmp=new ArrayList<>();
                            tmp.add(0);
                            newDomains.replace(emptyCell,tmp);
                        }
                    }else if(empties+ones==zeros){
                        for(String emptyCell:emptyCoordinates){
                            ArrayList<Integer> tmp=new ArrayList<>();
                            tmp.add(1);
                            newDomains.replace(emptyCell,tmp);
                        }
                    }
                }
            }
            //Column Check
            for(int i=0;i<n;i++){
                ArrayList<String> emptyCoordinates=new ArrayList<>();
                int zeros=0;
                int ones=0;
                int empties=0;
                for(int j=0;j<n;j++){
                    if(newA[j][i]==1) {
                        ones++;
                    }else if(newA[j][i]==0){
                        zeros++;
                    }else{
                        empties++;
                        emptyCoordinates.add(toStr(j,i));
                    }
                }
                if(zeros!=ones){
                    if(zeros+ones==n){
                        constraint1=true;
                        break;
                    }
                    if(empties+zeros<ones || empties+ones<zeros){
                        constraint1=true;
                        break;
                    }
                    if(empties+zeros==ones){
                        for(String emptyCell:emptyCoordinates){
                            ArrayList<Integer> tmp=new ArrayList<>();
                            tmp.add(0);
                            newDomains.replace(emptyCell,tmp);
                        }
                    }else if(empties+ones==zeros){
                        for(String emptyCell:emptyCoordinates){
                            ArrayList<Integer> tmp=new ArrayList<>();
                            tmp.add(1);
                            newDomains.replace(emptyCell,tmp);
                        }
                    }
                }
            }
            if(constraint1){
                continue;
            }
            ///Constraint 2: unique string in every column and row
            boolean constraint2=false;
            //Row check
            for(int i=0;i<n-1;i++){
                for(int k=i+1;k<n;k++) {
                    boolean equalRow=true;
                    for (int j = 0; j < n; j++) {
                        if (a[i][j] != a[k][j]) {
                            equalRow = false;
                            break;
                        }
                        if(a[i][j]==a[k][j] && a[i][j]==-1){
                            equalRow = false;
                        }
                    }
                    if(equalRow){
                        constraint2=true;
                        break;
                    }
                }
                if(constraint2){
                    break;
                }
            }
            //Column check
            for(int i=0;i<n-1;i++){
                for(int k=i+1;k<n;k++) {
                    boolean equalRow=true;
                    for (int j = 0; j < n; j++) {
                        if (a[j][i] != a[j][k]) {
                            equalRow = false;
                            break;
                        }
                        if(a[j][i]==a[j][k] && a[j][k]==-1){
                            equalRow = false;
                        }
                    }
                    if(equalRow){
                        constraint2=true;
                        break;
                    }
                }
                if(constraint2){
                    break;
                }
            }
            if(constraint2){
                continue;
            }
            ///Constraint 3: more than two zeros or ones can't be adjacent
            
        }
    }

    static String toStr(int i, int j) {
        return i + "," + j;
    }

    static int toX(String s) {
        String[] tmp = s.split(",");
        return Integer.parseInt(tmp[0]);
    }

    static int toY(String s) {
        String[] tmp = s.split(",");
        return Integer.parseInt(tmp[1]);
    }

    static void printArr(int[][] a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }
}
