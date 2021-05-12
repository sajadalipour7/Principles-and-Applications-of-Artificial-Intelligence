import java.io.*;
import java.sql.Array;
import java.util.*;

class Node {
    private int x;
    private int y;
    private boolean butterFixed = false;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isButterFixed() {
        return butterFixed;
    }

    public void setButterFixed(boolean butterFixed) {
        this.butterFixed = butterFixed;
    }
}

class Block {
    private String type;
    private int cost;

    public Block(String type, int cost) {
        this.cost = cost;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }
}

class State {
    static int producedStates;
    static int expandedStates;
    static ArrayList<Node> goals;
    static Block[][] map;
    static int n, m;
    private State parentState;
    private ArrayList<String> butters;
    private Node robot;
    private int f = 0;
    private int h = 0;
    private int g = 0;

    public State() {

    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public void setF(int f) {
        this.f = f;
    }

    public void calculateH() {
        this.h = 0;
        for (String butter : butters) {
            String[] tmp = butter.split(",");
            int xButter = Integer.parseInt(tmp[0]);
            int yButter = Integer.parseInt(tmp[1]);
            int minimumDist = Integer.MAX_VALUE;
            for (Node goal : goals) {
                int dist = Math.abs(goal.getX() - xButter) + Math.abs(goal.getY() - yButter);
                if (dist < minimumDist) {
                    minimumDist = dist;
                }
            }
            this.h += minimumDist;
        }

    }

    public void setButters(ArrayList<String> butters) {
        this.butters = butters;
    }

    public void setRobot(Node robot) {
        this.robot = robot;
    }


    public void printDetails() {
        System.out.println("Robot: " + robot.getX() + "," + robot.getY());
        for (String butter : butters) {
            System.out.println(butter);
        }
        System.out.println("G : " + g + " , H : " + h + " , F : " + f);
        System.out.println("--------------------------------------------");

    }

    public ArrayList<State> getSuccessorsForIDS() {
//        System.out.println("I am successing . My state is :");
//        this.printDetails();
//        if(parentState!=null){
//            System.out.println("My father is : ");
//            parentState.printDetails();
//        }


        ArrayList<State> successors = new ArrayList<>();
        int x = robot.getX();
        int y = robot.getY();


        //left
        if (y - 1 >= 0) {
            String type = map[x][y - 1].getType();
            int cost = map[x][y - 1].getCost();
            boolean isButter = false;
            String chosenButter = "";
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y - 1 && xButter == x) {
                    isButter = true;
                    chosenButter = butter;
                }
            }
            if (!isButter && !(type.equals("x"))) {
                State successor = new State();
                successor.setRobot(new Node(x, y - 1));
                successor.setButters(butters);
                successor.setParentState(this);
                successors.add(successor);
            } else if (isButter) {
                String[] tmp = chosenButter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                String isFixed = tmp[2];
                if (isFixed.equals("not")) {
                    String fixOrNot = "not";
                    if (y - 2 >= 0) {
                        boolean isEmpty = true;
                        for (String bt : butters) {
                            String[] tp = bt.split(",");
                            int xN = Integer.parseInt(tp[0]);
                            int yN = Integer.parseInt(tp[1]);
                            if (yN == y - 2 && xN == x) {
                                isEmpty = false;
                                break;
                            }
                        }
                        if (isEmpty && !map[x][y - 2].getType().equals("x")) {
                            if (map[x][y - 2].getType().equals("p")) {
                                fixOrNot = "fix";
                            }
                            ArrayList<String> successorButters = new ArrayList<>();
                            for (String butter : butters) {
                                if (!butter.equals(chosenButter)) {
                                    successorButters.add(butter);
                                } else {
                                    successorButters.add(String.valueOf(xButter) + "," + String.valueOf(yButter - 1) + "," + fixOrNot);
                                }
                            }
                            State successor = new State();
                            successor.setParentState(this);
                            successor.setRobot(new Node(x, y - 1));
                            successor.setButters(successorButters);
                            successors.add(successor);
                        }
                    }
                }
            }
        }
        //right

        if (y + 1 < m) {
            String type = map[x][y + 1].getType();
            int cost = map[x][y + 1].getCost();
            boolean isButter = false;
            String chosenButter = "";
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y + 1 && xButter == x) {
                    isButter = true;
                    chosenButter = butter;
                }
            }
            if (!isButter && (!type.equals("x"))) {
                State successor = new State();
                successor.setRobot(new Node(x, y + 1));
                successor.setButters(butters);
                successor.setParentState(this);
                successors.add(successor);
            } else if (isButter) {


                String[] tmp = chosenButter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                String isFixed = tmp[2];

                if (isFixed.equals("not")) {

                    String fixOrNot = "not";
                    if (y + 2 < m) {

                        boolean isEmpty = true;
                        for (String bt : butters) {
                            String[] tp = bt.split(",");
                            int xN = Integer.parseInt(tp[0]);
                            int yN = Integer.parseInt(tp[1]);
                            if (yN == y + 2 && xN == x) {
                                isEmpty = false;
                                break;
                            }
                        }
                        if (isEmpty && !map[x][y + 2].getType().equals("x")) {
                            if (map[x][y + 2].getType().equals("p")) {
                                fixOrNot = "fix";
                            }
                            ArrayList<String> successorButters = new ArrayList<>();
                            for (String butter : butters) {
                                if (!butter.equals(chosenButter)) {
                                    successorButters.add(butter);
                                } else {
                                    successorButters.add(String.valueOf(xButter) + "," + String.valueOf(yButter + 1) + "," + fixOrNot);
                                }
                            }
                            State successor = new State();
                            successor.setParentState(this);
                            successor.setRobot(new Node(x, y + 1));
                            successor.setButters(successorButters);
                            successors.add(successor);
                        }
                    }
                }
            }
        }
        //up
        if (x - 1 >= 0) {
            String type = map[x - 1][y].getType();
            int cost = map[x - 1][y].getCost();
            boolean isButter = false;
            String chosenButter = "";
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y && xButter == x - 1) {
                    isButter = true;
                    chosenButter = butter;
                }
            }
            if (!isButter && (!type.equals("x"))) {
                State successor = new State();
                successor.setRobot(new Node(x - 1, y));
                successor.setButters(butters);
                successor.setParentState(this);
                successors.add(successor);
            } else if (isButter) {
                String[] tmp = chosenButter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                String isFixed = tmp[2];
                if (isFixed.equals("not")) {
                    String fixOrNot = "not";
                    if (x - 2 >= 0) {
                        boolean isEmpty = true;
                        for (String bt : butters) {
                            String[] tp = bt.split(",");
                            int xN = Integer.parseInt(tp[0]);
                            int yN = Integer.parseInt(tp[1]);
                            if (yN == y && xN == x - 2) {
                                isEmpty = false;
                                break;
                            }
                        }
                        if (isEmpty && !map[x - 2][y].getType().equals("x")) {
                            if (map[x - 2][y].getType().equals("p")) {
                                fixOrNot = "fix";
                            }
                            ArrayList<String> successorButters = new ArrayList<>();
                            for (String butter : butters) {
                                if (!butter.equals(chosenButter)) {
                                    successorButters.add(butter);
                                } else {
                                    successorButters.add(String.valueOf(xButter - 1) + "," + String.valueOf(yButter) + "," + fixOrNot);
                                }
                            }
                            State successor = new State();
                            successor.setParentState(this);
                            successor.setRobot(new Node(x - 1, y));
                            successor.setButters(successorButters);
                            successors.add(successor);
                        }
                    }
                }
            }
        }
        //down
        if (x + 1 < n) {
            String type = map[x + 1][y].getType();
            int cost = map[x + 1][y].getCost();
            boolean isButter = false;
            String chosenButter = "";
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y && xButter == x + 1) {
                    isButter = true;
                    chosenButter = butter;
                }
            }
            if (!isButter && (!type.equals("x"))) {
                State successor = new State();
                successor.setRobot(new Node(x + 1, y));
                successor.setButters(butters);
                successor.setParentState(this);
                successors.add(successor);
            } else if (isButter) {
                String[] tmp = chosenButter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                String isFixed = tmp[2];
                if (isFixed.equals("not")) {
                    String fixOrNot = "not";
                    if (x + 2 < n) {
                        boolean isEmpty = true;
                        for (String bt : butters) {
                            String[] tp = bt.split(",");
                            int xN = Integer.parseInt(tp[0]);
                            int yN = Integer.parseInt(tp[1]);
                            if (yN == y && xN == x + 2) {
                                isEmpty = false;
                                break;
                            }
                        }
                        if (isEmpty && !map[x + 2][y].getType().equals("x")) {
                            if (map[x + 2][y].getType().equals("p")) {
                                fixOrNot = "fix";
                            }
                            ArrayList<String> successorButters = new ArrayList<>();
                            for (String butter : butters) {
                                if (!butter.equals(chosenButter)) {
                                    successorButters.add(butter);
                                } else {
                                    successorButters.add(String.valueOf(xButter + 1) + "," + String.valueOf(yButter) + "," + fixOrNot);
                                }
                            }
                            State successor = new State();
                            successor.setParentState(this);
                            successor.setRobot(new Node(x + 1, y));
                            successor.setButters(successorButters);
                            successors.add(successor);
                        }
                    }
                }
            }
        }

        if (parentState != null) {
            Iterator<State> iterator = successors.iterator();
            while (iterator.hasNext()) {
                State tmpState = iterator.next();

                if (tmpState.equals(parentState)) {
                    iterator.remove();

                }

            }
        }
//        System.out.println("My Children are : ");
//        for (State s : successors) {
//            s.printDetails();
//        }

        return successors;
    }

    public void setParentState(State parentState) {
        this.parentState = parentState;
    }

    public ArrayList<String> getButters() {
        return butters;
    }

    public Node getRobot() {
        return robot;
    }

    public State getParentState() {
        return parentState;
    }


    public boolean equals(State state) {
        boolean isButtersEqual = true;
        ArrayList<String> secondButters = state.getButters();
        for (int i = 0; i < secondButters.size(); i++) {
            if (!secondButters.get(i).equals(butters.get(i))) {
                isButtersEqual = false;
                break;
            }
        }
        boolean isRobotEqual = false;
        if (state.getRobot().getX() == robot.getX() && state.getRobot().getY() == robot.getY()) {
            isRobotEqual = true;
        }
        return isButtersEqual && isRobotEqual;
    }


}

public class AstarApproach {
    static Block[][] map;
    static int n, m;
    static ArrayList<Node> goals;

    public static void main(String[] args) throws IOException {
        File file = new File("./input/test1.txt");
        //Scanner sc=new Scanner(System.in);
        Scanner sc = new Scanner(file);
        n = sc.nextInt();
        m = sc.nextInt();
        map = new Block[n][m];
        goals = new ArrayList<>();
        Node robot = new Node(0, 0);
        ArrayList<String> butters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                String type = sc.next();
                int cost = -1;
                if (type.contains("r")) {
                    robot.setX(i);
                    robot.setY(j);
                    cost = Integer.parseInt(type.replace("r", ""));
                    type = "r";
                    map[i][j] = new Block(type, cost);
                } else if (type.contains("x")) {
                    type = "x";
                    cost = -1;
                    map[i][j] = new Block(type, cost);
                } else if (type.contains("b")) {
                    cost = Integer.parseInt(type.replace("b", ""));
                    type = "b";
                    butters.add(String.valueOf(i) + "," + String.valueOf(j) + "," + "not");
                    map[i][j] = new Block(type, cost);
                } else if (type.contains("p")) {
                    cost = Integer.parseInt(type.replace("p", ""));
                    type = "p";
                    goals.add(new Node(i, j));
                    map[i][j] = new Block(type, cost);
                } else {
                    cost = Integer.parseInt(type);
                    type = "normal";
                    map[i][j] = new Block(type, cost);
                }

            }

        }
        State.map = map;
        State.n = n;
        State.m = m;
        State.goals = goals;
        State.producedStates = 0;
        State.expandedStates = 0;


        //long start=new Date().getTime();
        State finalState = Astar(robot, butters);
        //long finish=new Date().getTime();
        //System.out.println(finish-start);
        if (finalState != null) {
            State tmpc = finalState;
            Stack<String> moves = new Stack<>();
            String path = "";
            int cost = 0;
            int depth = 0;
            while (tmpc.getParentState() != null) {
                State parent = tmpc.getParentState();
                int xR = tmpc.getRobot().getX();
                int yR = tmpc.getRobot().getY();
                int xRParent = parent.getRobot().getX();
                int yRParent = parent.getRobot().getY();
                cost += map[xR][yR].getCost();

                if (xR - xRParent == -1) {
                    //up
                    moves.add("U");
                    depth += 1;
                } else if (xR - xRParent == 1) {
                    //down
                    moves.add("D");
                    depth += 1;
                } else if (yR - yRParent == -1) {
                    //left
                    moves.add("L");
                    depth += 1;
                } else if (yR - yRParent == 1) {
                    //right
                    moves.add("R");
                    depth += 1;
                } else {
                    moves.add("Error");
                }
                tmpc = tmpc.getParentState();
                //tmpc.printDetails();
            }

            while (!moves.isEmpty()) {

                path += moves.pop() + " ";
            }

            FileWriter f = new FileWriter("./output/result1_Astar.txt");
            f.write(path + "\n");
            f.write(cost + "\n");
            f.write(depth + "\n");
            f.close();


            System.out.println(path);
            System.out.println(cost);
            System.out.println(depth);


        } else {
            FileWriter f = new FileWriter("./output/result4_Astar.txt");
            f.write("can't pass the butter");
            f.close();
            System.out.println("can't pass the butter");
        }

//        System.out.println("Produced States : "+State.producedStates);
//        System.out.println("Expanded States : "+State.expandedStates);

    }

    public static State Astar(Node robot, ArrayList<String> butters) throws IOException {
        ArrayList<State> fringe = new ArrayList<>();
        ArrayList<State> explored = new ArrayList<>();

        State initialState = new State();
        initialState.setButters(butters);
        initialState.setRobot(robot);


        fringe.add(initialState);

        while (!fringe.isEmpty()) {
            //Finding Minimum State
            State minState = fringe.get(0);
            int index = 0;
            for (int i = 0; i < fringe.size(); i++) {
                if (fringe.get(i).getF() < minState.getF()) {
                    minState = fringe.get(i);
                    index = i;
                }
            }
            //System.out.println("Min state :");
            //minState.printDetails();
            fringe.remove(index);

            ArrayList<State> temporarySuccessors = minState.getSuccessorsForIDS();
            State.producedStates += temporarySuccessors.size();
            State.expandedStates += 1;

            for (State successor : temporarySuccessors) {
                if (isFinished(successor)) {
                    return successor;
                }
                successor.setG(minState.getG() + map[successor.getRobot().getX()][successor.getRobot().getY()].getCost());
                successor.calculateH();
                successor.setF(successor.getG() + successor.getH());

            }
            for (State tmpState : temporarySuccessors) {
                boolean isThereFinFringeLessThan = false;
                for (State stateInFringe : fringe) {
                    if (tmpState.equals(stateInFringe)) {
                        if (stateInFringe.getF() < tmpState.getF()) {
                            isThereFinFringeLessThan = true;
                        }
                    }
                }
                if (isThereFinFringeLessThan) {
                    State.producedStates--;
                    continue;
                }
                boolean isThereFinExploredLessThan = false;
                for (State stateInExplored : explored) {
                    if (tmpState.equals(stateInExplored)) {
                        if (stateInExplored.getF() < tmpState.getF()) {
                            isThereFinExploredLessThan = true;
                        }
                    }
                }
                if (isThereFinExploredLessThan) {
                    State.producedStates--;
                    continue;
                }
                fringe.add(tmpState);

            }

            explored.add(minState);


//            System.out.println("Fringe:");
//            for(State s1:fringe){
//                s1.printDetails();
//            }
//            System.out.println("Explored:");
//            for(State s2:explored){
//                s2.printDetails();
//            }
//            System.out.println("**************************************************");
        }
        return null;


    }

    public static boolean isFinished(State state) {
        ArrayList<String> butters = state.getButters();
        for (String butter : butters) {
            String[] tmp = butter.split(",");
            int x = Integer.parseInt(tmp[0]);
            int y = Integer.parseInt(tmp[1]);
            if (!map[x][y].getType().equals("p")) {
                return false;
            }
        }
        return true;
    }


}
