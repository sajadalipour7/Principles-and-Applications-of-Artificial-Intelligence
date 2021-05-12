import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    static int expandedNodes;
    static Block[][] map;
    static int n, m;
    private State parentState;
    private ArrayList<String> butters;
    private Node robot;

    public State() {

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
        producedStates += successors.size();
        expandedNodes += 1;
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

public class IDSApproach {
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
        State.producedStates = 0;
        State.expandedNodes = 0;

        IDS(robot, butters);

//        System.out.println("Produced States : "+State.producedStates);
//        System.out.println("Expanded States : "+State.expandedNodes);


    }

    public static void IDS(Node robot, ArrayList<String> butters) throws IOException {

        //long start=new Date().getTime();

        boolean hasFound = false;
        for (int depth = 0; depth < 20; depth++) {
            State initialState = new State();
            initialState.setButters(butters);
            initialState.setRobot(robot);


            //System.out.println("Depth limit : " + depth + "************************************************");
            State result = DLS(initialState, depth);

            if (result != null) {
                //long finish=new Date().getTime();
                //System.out.println(finish-start);

                hasFound = true;

                //System.out.println("found at depth:" + depth);
                //result.printDetails();
                State tmpc = result;
                Stack<String> moves = new Stack<>();
                String path = "";
                int cost = 0;
                while (tmpc.getParentState() != null) {
                    State parent = tmpc.getParentState();
                    int xR = tmpc.getRobot().getX();
                    int yR = tmpc.getRobot().getY();
                    int xRParent = parent.getRobot().getX();
                    int yRParent = parent.getRobot().getY();
                    cost +=1;
                    if (xR - xRParent == -1) {
                        //up
                        moves.add("U");
                    } else if (xR - xRParent == 1) {
                        //down
                        moves.add("D");
                    } else if (yR - yRParent == -1) {
                        //left
                        moves.add("L");
                    } else if (yR - yRParent == 1) {
                        //right
                        moves.add("R");
                    } else {
                        moves.add("Error");
                    }
                    tmpc = tmpc.getParentState();
                    //tmpc.printDetails();
                }

                while (!moves.isEmpty()) {

                    path += moves.pop() + " ";
                }


                FileWriter f = new FileWriter("./output/result1_IDS.txt");
                f.write(path + "\n");
                f.write(cost + "\n");
                f.write(depth + "\n");
                f.close();

                System.out.println(path);
                System.out.println(cost);
                System.out.println(depth);
                break;
            }
        }

        if (!hasFound) {
            FileWriter f = new FileWriter("./output/result4_IDS.txt");
            f.write("can't pass the butter");
            f.close();
            System.out.println("can't pass the butter");

        }

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

    public static State DLS(State current, int depth) {
        if (depth == 0) {
            if (isFinished(current)) {
                return current;
            } else {
                return null;
            }
        } else {
            ArrayList<State> successors = current.getSuccessorsForIDS();
            for (State successor : successors) {
                State result = DLS(successor, depth - 1);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }


    }


}
