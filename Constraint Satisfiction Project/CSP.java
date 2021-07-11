import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Node{
    public int data;
    public int x,y;

    public Node(int data,int x,int y){

        this.data=data;
        this.x=x;
        this.y=y;
    }
}
public class CSP {
    static int[][] a;

    public static void main(String[] args) throws FileNotFoundException {
        File file=new File("./puzzles/puzzle0.txt");
        Scanner sc=new Scanner(file);
        int n=sc.nextInt();
        int dummy=sc.nextInt();
        a=new int[n][n];
        ArrayList<Node> variables=new ArrayList<Node>();
        HashMap<Node,ArrayList<Integer>> domains=new HashMap<>();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                String tmp=sc.next();
                if(tmp.equals("-")){
                    a[i][j]=-1;
                    Node current=new Node(-1,i,j);
                    ArrayList<Integer> domain=new ArrayList<>();
                    domain.add(0);
                    domain.add(1);
                    variables.add(current);
                    domains.put(current,domain);
                }else{
                    a[i][j]=Integer.parseInt(tmp);
                }
            }
        }

        cspBacktracking(variables,domains);



    }
    static void cspBacktracking(ArrayList<Node> variables,HashMap<Node,ArrayList<Integer>> domains){
        //TODO
    }


    static void printArr(int[][] a,int n){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }
    }
}
