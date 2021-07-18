import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class CSP {
    static int n;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./puzzles/puzzle6.txt");
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

        //cspBacktrackingWithForwardChecking(variables, domains, a);
        cspBacktrackingWithMac(variables, domains, a);


    }

    static void cspBacktrackingWithMac(ArrayList<String> variables, HashMap<String, ArrayList<Integer>> domains, int[][] newA) {
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = newA[i][j];
            }
        }
        String variableToAssign = "";
        //MRV
        int minimumRemaining = Integer.MAX_VALUE;
        for (String key : variables) {
            if (domains.get(key).size() < minimumRemaining) {
                minimumRemaining = domains.get(key).size();
                variableToAssign = key;
            }
        }
        ArrayList<String> newVariables = new ArrayList<>();
        for (String key : variables) {
            if (!key.equals(variableToAssign)) {
                newVariables.add(key);
            }
        }
        int x = toX(variableToAssign);
        int y = toY(variableToAssign);
        ArrayList<Integer> domain = domains.get(variableToAssign);
        for (Integer value : domain) {
            HashMap<String, ArrayList<Integer>> newDomains = new HashMap<>();
            for (String key : domains.keySet()) {
                if (!key.equals(variableToAssign)) {
                    newDomains.put(key, domains.get(key));
                }
            }
            a[x][y] = value;
            //////Mac
            ///Constraint 1: equal number of zeros and ones
            {
                Queue<ArrayList<String>> queue = new LinkedList<>();
                for (String vari : newVariables) {
                    ArrayList<String> tmpArr = new ArrayList<>();
                    tmpArr.add(vari);
                    tmpArr.add(variableToAssign);
                    queue.add(tmpArr);
                }
                while (!queue.isEmpty()) {
                    ArrayList<String> current = queue.remove();
                    //Revise
                    String xi = current.get(0);
                    String xj = current.get(1);
                    Integer Xi = toX(xi);
                    Integer Yi = toY(xi);
                    Integer Xj = toX(xj);
                    Integer Yj = toY(xj);
                    ArrayList<Integer> checkedDomain = new ArrayList<>();
                    for (Integer val : newDomains.get(xi)) {
                        int sumOfZeros = 0;
                        int sumOfOnes = 0;
                        if (val == 1) {
                            sumOfOnes = 1;
                        } else {
                            sumOfZeros = 1;
                        }
                        boolean flag = true;

                        if (Xi == Xj) {
                            for (int u = 0; u < n; u++) {
                                if (a[Xi][u] == 1) {
                                    sumOfOnes++;
                                } else if (a[Xi][u] == 0) {
                                    sumOfZeros++;
                                }
                            }
                            if (sumOfOnes + sumOfZeros == n) {
                                if (sumOfOnes != sumOfZeros) {
                                    flag = false;
                                }
                            }
                        } else if (Yi == Yj) {
                            for (int u = 0; u < n; u++) {
                                if (a[u][Yi] == 1) {
                                    sumOfOnes++;
                                } else if (a[u][Yi] == 0) {
                                    sumOfZeros++;
                                }
                            }
                            if (sumOfOnes + sumOfZeros == n) {
                                if (sumOfOnes != sumOfZeros) {
                                    flag = false;
                                }
                            }
                        }
                        if (flag) {
                            if(newDomains.get(xi).contains(val)){
                                checkedDomain.add(val);
                            }
                        }
                    }
                    newDomains.replace(xi, checkedDomain);

                }
            }
            ///Constraint 2: unique string in every column and row
            {
                Queue<ArrayList<String>> queue = new LinkedList<>();
                for (String vari : newVariables) {
                    ArrayList<String> tmpArr = new ArrayList<>();
                    tmpArr.add(vari);
                    tmpArr.add(variableToAssign);
                    queue.add(tmpArr);
                }
                while (!queue.isEmpty()) {
                    ArrayList<String> current = queue.remove();
                    //Revise
                    String xi = current.get(0);
                    String xj = current.get(1);
                    Integer Xi = toX(xi);
                    Integer Yi = toY(xi);
                    Integer Xj = toX(xj);
                    Integer Yj = toY(xj);
                    if (Xi != Xj && Yi != Yj) {
                        ArrayList<Integer> checkedDomain = new ArrayList<>();
                        for (Integer val : newDomains.get(xi)) {
                            //row check
                            int eq = 0;
                            boolean flag = true;
                            for (int u = 0; u < n; u++) {
                                if (Yi != u) {
                                    if (a[Xi][u] != a[Xj][u] || a[Xi][u] == -1 || a[Xj][u] == -1) {
                                        break;
                                    } else if (a[Xi][u] == a[Xj][u] && a[Xi][u] != -1) {
                                        eq++;
                                    }
                                } else {
                                    if (a[Xj][u] == val) {
                                        eq++;
                                    }
                                }
                            }
                            if (eq == n) {
                                flag = false;
                            }
                            if (flag) {
                                //Column check
                                int eq2 = 0;
                                for (int u = 0; u < n; u++) {
                                    if (Xi != u) {
                                        if (a[u][Yi] != a[u][Yj] || a[u][Yi] == -1 || a[u][Yj] == -1) {
                                            break;
                                        } else if (a[u][Yi] == a[u][Yj] && a[u][Yi] != -1) {
                                            eq2++;
                                        }
                                    } else {
                                        if (a[u][Yj] == val) {
                                            eq2++;
                                        }
                                    }
                                }
                                if (eq2 == n) {
                                    flag = false;
                                }
                            }
                            if (flag) {
                                if(newDomains.get(xi).contains(val)){
                                    checkedDomain.add(val);
                                }
                            }
                        }
                        newDomains.replace(xi, checkedDomain);
                    }
                }
            }
            ///Constraint 3: more than two zeros or ones can't be adjacent
            {
                Queue<ArrayList<String>> queue = new LinkedList<>();
                for (String vari : newVariables) {
                    ArrayList<String> tmpArr = new ArrayList<>();
                    tmpArr.add(vari);
                    tmpArr.add(variableToAssign);
                    queue.add(tmpArr);
                }
                while (!queue.isEmpty()) {
                    ArrayList<String> current = queue.remove();
                    //Revise
                    String xi = current.get(0);
                    String xj = current.get(1);
                    Integer Xi = toX(xi);
                    Integer Yi = toY(xi);
                    Integer Xj = toX(xj);
                    Integer Yj = toY(xj);
                    if(Xi==Xj && Math.abs(Yi-Yj)<=2){
                        ArrayList<Integer> checkedDomain = new ArrayList<>();
                        for(Integer val:newDomains.get(xi)){
                            boolean flag=true;
                            if(a[Xj][Yj]==val){
                                if(Yj>Yi){
                                    if(Yj+1<n && Yj-Yi==1){
                                        if(a[Xj][Yj+1]==val){
                                            flag=false;
                                        }
                                    }else if(Yj-Yi==2){
                                        if(a[Xi][Yi+1]==val){
                                            flag=false;
                                        }
                                    }
                                }else{
                                    if(Yj-1>=0 && Yi-Yj==1){
                                        if(a[Xj][Yj-1]==val){
                                            flag=false;
                                        }
                                    }else if(Yi-Yj==2){
                                        if(a[Xi][Yi-1]==val){
                                            flag=false;
                                        }
                                    }

                                }
                            }
                            if(flag){
                                if(newDomains.get(xi).contains(val)){
                                    checkedDomain.add(val);
                                }
                            }
                        }
                        newDomains.replace(xi, checkedDomain);
                    }else if(Yi==Yj && Math.abs(Xi-Xj)<=2){
                        ArrayList<Integer> checkedDomain = new ArrayList<>();
                        for(Integer val:newDomains.get(xi)){
                            boolean flag=true;
                            if(a[Xj][Yj]==val){
                                if(Xj>Xi){
                                    if(Xj+1<n && Xj-Xi==1){
                                        if(a[Xj+1][Yj]==val){
                                            flag=false;
                                        }
                                    }else if(Xj-Xi==2){
                                        if(a[Xi+1][Yi]==val){
                                            flag=false;
                                        }
                                    }
                                }else{
                                    if(Xj-1>=0 && Xi-Xj==1){
                                        if(a[Xj-1][Yj]==val){
                                            flag=false;
                                        }
                                    }else if(Xi-Xj==2){
                                        if(a[Xi-1][Yi]==val){
                                            flag=false;
                                        }
                                    }

                                }
                            }
                            if(flag){
                                if(newDomains.get(xi).contains(val)){
                                    checkedDomain.add(val);
                                }
                            }
                        }
                        newDomains.replace(xi, checkedDomain);
                    }
                }
            }
            ///A little constraint satisfaction
            boolean constraint3 = false;
            //Row check
            if (y - 2 >= 0) {
                if (a[x][y - 2] == a[x][y - 1] && a[x][y - 2] != -1) {
                    if (a[x][y] == a[x][y - 1]) {
                        constraint3 = true;
                    }
                }
            }
            if (y - 1 >= 0 && y + 1 < n) {
                if (a[x][y - 1] == a[x][y] && a[x][y] == a[x][y + 1]) {
                    constraint3 = true;
                }
            }
            if (y + 2 < n) {
                if (a[x][y + 2] == a[x][y + 1] && a[x][y + 2] != -1) {
                    if (a[x][y] == a[x][y + 1]) {
                        constraint3 = true;
                    }
                }
            }
            //Column Check
            if (x - 2 >= 0) {
                if (a[x - 2][y] == a[x - 1][y] && a[x - 2][y] != -1) {
                    if (a[x][y] == a[x - 1][y]) {
                        constraint3 = true;
                    }
                }
            }
            if (x - 1 >= 0 && x + 1 < n) {
                if (a[x - 1][y] == a[x][y] && a[x][y] == a[x + 1][y]) {
                    constraint3 = true;
                }
            }
            if (x + 2 < n) {
                if (a[x + 2][y] == a[x + 1][y] && a[x + 2][y] != -1) {
                    if (a[x][y] == a[x + 1][y]) {
                        constraint3 = true;
                    }
                }
            }
            if(constraint3){
                continue;
            }
            ///Mac Finished
            if (newVariables.size() == 0) {
                if(isFinished(a)) {
                    for (int row = 0; row < n; row++) {
                        for (int column = 0; column < n; column++) {
                            System.out.print(a[row][column] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("--------------------------------------------");
                }
            } else {
                cspBacktrackingWithMac(newVariables, newDomains, a);
            }

        }
    }

    static boolean isFinished(int[][] a){
        boolean flag=true;
        //Constraint 1
        for(int i=0;i<n;i++){
            int zeros=0;
            int ones=0;
            for(int j=0;j<n;j++){
                if(a[i][j]==1){
                    ones++;
                }else if(a[i][j]==0){
                    zeros++;
                }
            }
            if(zeros!=ones){
                flag=false;
                break;
            }
        }
        if(flag) {
            for (int i = 0; i < n; i++) {
                int zeros = 0;
                int ones = 0;
                for (int j = 0; j < n; j++) {
                    if (a[j][i] == 1) {
                        ones++;
                    } else if (a[j][i] == 0) {
                        zeros++;
                    }
                }
                if (zeros != ones) {
                    flag = false;
                    break;
                }
            }
        }
        //Constraint 2
        //Row check
        for (int i = 0; i < n - 1; i++) {
            for (int k = i + 1; k < n; k++) {
                boolean equalRow = true;
                for (int j = 0; j < n; j++) {
                    if (a[i][j] != a[k][j]) {
                        equalRow = false;
                        break;
                    }
                }
                if (equalRow) {
                    flag=false;
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }
        //column check
        if(flag){
            for (int i = 0; i < n - 1; i++) {
                for (int k = i + 1; k < n; k++) {
                    boolean equalRow = true;
                    for (int j = 0; j < n; j++) {
                        if (a[j][i] != a[j][k]) {
                            equalRow = false;
                            break;
                        }
                    }
                    if (equalRow) {
                        flag=false;
                        break;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        }

        return flag;

    }
    static void cspBacktrackingWithForwardChecking(ArrayList<String> variables, HashMap<String, ArrayList<Integer>> domains, int[][] newA) {
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = newA[i][j];
            }
        }
        String variableToAssign = "";
        //MRV
        int minimumRemaining = Integer.MAX_VALUE;
        for (String key : variables) {
            if (domains.get(key).size() < minimumRemaining) {
                minimumRemaining = domains.get(key).size();
                variableToAssign = key;
            }
        }
        ArrayList<String> newVariables = new ArrayList<>();
        for (String key : variables) {
            if (!key.equals(variableToAssign)) {
                newVariables.add(key);
            }
        }
        int x = toX(variableToAssign);
        int y = toY(variableToAssign);
        ArrayList<Integer> domain = domains.get(variableToAssign);
        for (Integer value : domain) {
            HashMap<String, ArrayList<Integer>> newDomains = new HashMap<>();
            for (String key : domains.keySet()) {
                if (!key.equals(variableToAssign)) {
                    newDomains.put(key, domains.get(key));
                }
            }
            a[x][y] = value;
            //////Forward Checking
            ///Constraint 1: equal number of zeros and ones
            boolean constraint1 = false;
            //Row check
            for (int i = 0; i < n; i++) {
                ArrayList<String> emptyCoordinates = new ArrayList<>();
                int zeros = 0;
                int ones = 0;
                int empties = 0;
                for (int j = 0; j < n; j++) {
                    if (a[i][j] == 1) {
                        ones++;
                    } else if (a[i][j] == 0) {
                        zeros++;
                    } else {
                        empties++;
                        emptyCoordinates.add(toStr(i, j));
                    }
                }
                if (zeros != ones) {
                    if (zeros + ones == n) {
                        constraint1 = true;
                        break;
                    }
                    if (empties + zeros < ones || empties + ones < zeros) {
                        constraint1 = true;
                        break;
                    }
                    if (empties + zeros == ones) {
                        for (String emptyCell : emptyCoordinates) {
                            ArrayList<Integer> tmp = new ArrayList<>();
                            tmp.add(0);
                            newDomains.replace(emptyCell, tmp);
                        }
                    } else if (empties + ones == zeros) {
                        for (String emptyCell : emptyCoordinates) {
                            ArrayList<Integer> tmp = new ArrayList<>();
                            tmp.add(1);
                            newDomains.replace(emptyCell, tmp);
                        }
                    }
                }
            }
            //Column Check
            for (int i = 0; i < n; i++) {
                ArrayList<String> emptyCoordinates = new ArrayList<>();
                int zeros = 0;
                int ones = 0;
                int empties = 0;
                for (int j = 0; j < n; j++) {
                    if (a[j][i] == 1) {
                        ones++;
                    } else if (a[j][i] == 0) {
                        zeros++;
                    } else {
                        empties++;
                        emptyCoordinates.add(toStr(j, i));
                    }
                }
                if (zeros != ones) {
                    if (zeros + ones == n) {
                        constraint1 = true;
                        break;
                    }
                    if (empties + zeros < ones || empties + ones < zeros) {
                        constraint1 = true;
                        break;
                    }
                    if (empties + zeros == ones) {
                        for (String emptyCell : emptyCoordinates) {
                            ArrayList<Integer> tmp = new ArrayList<>();
                            tmp.add(0);
                            newDomains.replace(emptyCell, tmp);
                        }
                    } else if (empties + ones == zeros) {
                        for (String emptyCell : emptyCoordinates) {
                            ArrayList<Integer> tmp = new ArrayList<>();
                            tmp.add(1);
                            newDomains.replace(emptyCell, tmp);
                        }
                    }
                }
            }
            if (constraint1) {
                continue;
            }
            ///Constraint 2: unique string in every column and row
            boolean constraint2 = false;
            //Row check
            for (int i = 0; i < n - 1; i++) {
                for (int k = i + 1; k < n; k++) {
                    boolean equalRow = true;
                    for (int j = 0; j < n; j++) {
                        if (a[i][j] != a[k][j]) {
                            equalRow = false;
                            break;
                        }
                        if (a[i][j] == a[k][j] && a[i][j] == -1) {
                            equalRow = false;
                        }
                    }
                    if (equalRow) {
                        constraint2 = true;
                        break;
                    }
                }
                if (constraint2) {
                    break;
                }
            }
            //Column check
            for (int i = 0; i < n - 1; i++) {
                for (int k = i + 1; k < n; k++) {
                    boolean equalRow = true;
                    for (int j = 0; j < n; j++) {
                        if (a[j][i] != a[j][k]) {
                            equalRow = false;
                            break;
                        }
                        if (a[j][i] == a[j][k] && a[j][k] == -1) {
                            equalRow = false;
                        }
                    }
                    if (equalRow) {
                        constraint2 = true;
                        break;
                    }
                }
                if (constraint2) {
                    break;
                }
            }
            if (constraint2) {
                continue;
            }
            ///Constraint 3: more than two zeros or ones can't be adjacent
            boolean constraint3 = false;
            //Row check
            if (y - 2 >= 0) {
                if (a[x][y - 2] == a[x][y - 1] && a[x][y - 2] != -1) {
                    if (a[x][y] == a[x][y - 1]) {
                        constraint3 = true;
                    }
                }
            }
            if (y - 1 >= 0 && y + 1 < n) {
                if (a[x][y - 1] == a[x][y] && a[x][y] == a[x][y + 1]) {
                    constraint3 = true;
                }
            }
            if (y + 2 < n) {
                if (a[x][y + 2] == a[x][y + 1] && a[x][y + 2] != -1) {
                    if (a[x][y] == a[x][y + 1]) {
                        constraint3 = true;
                    }
                }
            }
            //Column Check
            if (x - 2 >= 0) {
                if (a[x - 2][y] == a[x - 1][y] && a[x - 2][y] != -1) {
                    if (a[x][y] == a[x - 1][y]) {
                        constraint3 = true;
                    }
                }
            }
            if (x - 1 >= 0 && x + 1 < n) {
                if (a[x - 1][y] == a[x][y] && a[x][y] == a[x + 1][y]) {
                    constraint3 = true;
                }
            }
            if (x + 2 < n) {
                if (a[x + 2][y] == a[x + 1][y] && a[x + 2][y] != -1) {
                    if (a[x][y] == a[x + 1][y]) {
                        constraint3 = true;
                    }
                }
            }
            if (constraint3) {
                continue;
            }
            //Now Forward checking has been finished.
            if (newVariables.size() == 0) {
                for (int row = 0; row < n; row++) {
                    for (int column = 0; column < n; column++) {
                        System.out.print(a[row][column] + " ");
                    }
                    System.out.println();
                }
            } else {
                cspBacktrackingWithForwardChecking(newVariables, newDomains, a);
            }

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
