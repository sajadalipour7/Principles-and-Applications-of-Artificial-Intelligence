import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Node{
    public int data;
    public int x,y;
    public ArrayList<Integer> domain;

    public Node(int data,int x,int y){
        domain=new ArrayList<>();
        this.data=data;
        this.x=x;
        this.y=y;
    }
}
public class CSP {
    static int[][] a;
    static  ArrayList<Node> variables;

    public static void main(String[] args) throws FileNotFoundException {
        File file=new File("./puzzles/puzzle0.txt");
        Scanner sc=new Scanner(file);
        int n=sc.nextInt();
        int dummy=sc.nextInt();
        a=new int[n][n];
        variables=new ArrayList<Node>();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                String tmp=sc.next();
                if(tmp.equals("-")){
                    a[i][j]=-1;
                }else{
                    a[i][j]=Integer.parseInt(tmp);
                }
            }
        }



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
