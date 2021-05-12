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
    static int expandedStates;
    static Block[][] map;
    static int n, m;
    private State parentState;
    private ArrayList<String> butters;
    private Node robot;
    private boolean isVisited = false;

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


    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public ArrayList<State> getPredecessors() {
//        System.out.println("From Target :");
//        System.out.println("I am predecessoring . My state is :");
//        this.printDetails();
//        if(parentState!=null){
//            System.out.println("My father is : ");
//            parentState.printDetails();
//        }

        ArrayList<State> successors = new ArrayList<>();
        int x = robot.getX();
        int y = robot.getY();

        //down
        if (x + 1 < n) {
            String type = map[x + 1][y].getType();
            int cost = map[x + 1][y].getCost();
            boolean isButter = false;
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y && xButter == x + 1) {
                    isButter = true;
                }
            }
            if (!isButter && (type.equals("normal") || type.equals("b"))) {
                boolean isThere = false;
                String chosenButter = "";
                String isFixed = "";
                for (String butter : butters) {
                    String[] tmp = butter.split(",");
                    int xButter = Integer.parseInt(tmp[0]);
                    int yButter = Integer.parseInt(tmp[1]);
                    if (yButter == y && xButter == x - 1) {
                        chosenButter = butter;
                        isThere = true;
                        isFixed = tmp[2];
                    }
                }
                if (isThere) {
                    if (isFixed.equals("fix")) {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1 + 1) + "," + String.valueOf(yButter1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x + 1, y));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);

                    } else {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1 + 1) + "," + String.valueOf(yButter1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x + 1, y));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);
                        State successor2 = new State();
                        successor2.setRobot(new Node(x + 1, y));
                        successor2.setButters(butters);
                        successor2.setParentState(this);
                        successors.add(successor2);

                    }
                } else {
                    boolean canIPass = true;
                    for (String btt : butters) {
                        String[] tmp1 = btt.split(",");
                        int xBt = Integer.parseInt(tmp1[0]);
                        int yBt = Integer.parseInt(tmp1[1]);
                        String typeO = tmp1[2];
                        if (xBt == x && (yBt == y - 1 || yBt == y + 1)) {
                            if (typeO.equals("fix")) {
                                canIPass = false;
                            }
                        }
                    }
                    if (canIPass) {
                        State successor = new State();
                        successor.setRobot(new Node(x + 1, y));
                        successor.setButters(butters);
                        successor.setParentState(this);
                        successors.add(successor);
                    }
                }

            }
        }

        //up
        if (x - 1 >= 0) {
            String type = map[x - 1][y].getType();
            int cost = map[x - 1][y].getCost();
            boolean isButter = false;
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y && xButter == x - 1) {
                    isButter = true;
                }
            }
            if (!isButter && (type.equals("normal") || type.equals("b"))) {
                boolean isThere = false;
                String chosenButter = "";
                String isFixed = "";
                for (String butter : butters) {
                    String[] tmp = butter.split(",");
                    int xButter = Integer.parseInt(tmp[0]);
                    int yButter = Integer.parseInt(tmp[1]);
                    if (yButter == y && xButter == x + 1) {
                        chosenButter = butter;
                        isThere = true;
                        isFixed = tmp[2];
                    }
                }
                if (isThere) {
                    if (isFixed.equals("fix")) {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1 - 1) + "," + String.valueOf(yButter1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x - 1, y));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);

                    } else {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1 - 1) + "," + String.valueOf(yButter1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x - 1, y));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);
                        State successor2 = new State();
                        successor2.setRobot(new Node(x - 1, y));
                        successor2.setButters(butters);
                        successor2.setParentState(this);
                        successors.add(successor2);

                    }
                } else {
                    boolean canIPass = true;
                    for (String btt : butters) {
                        String[] tmp1 = btt.split(",");
                        int xBt = Integer.parseInt(tmp1[0]);
                        int yBt = Integer.parseInt(tmp1[1]);
                        String typeO = tmp1[2];
                        if (xBt == x && (yBt == y - 1 || yBt == y + 1)) {
                            if (typeO.equals("fix")) {
                                canIPass = false;
                            }
                        }
                    }
                    if (canIPass) {
                        State successor = new State();
                        successor.setRobot(new Node(x - 1, y));
                        successor.setButters(butters);
                        successor.setParentState(this);
                        successors.add(successor);
                    }
                }

            }
        }

        //right
        if (y + 1 < m) {
            String type = map[x][y + 1].getType();
            int cost = map[x][y + 1].getCost();
            boolean isButter = false;
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y + 1 && xButter == x) {
                    isButter = true;
                }
            }
            if (!isButter && (type.equals("normal") || type.equals("b"))) {
                boolean isThere = false;
                String chosenButter = "";
                String isFixed = "";
                for (String butter : butters) {
                    String[] tmp = butter.split(",");
                    int xButter = Integer.parseInt(tmp[0]);
                    int yButter = Integer.parseInt(tmp[1]);
                    if (yButter == y - 1 && xButter == x) {
                        chosenButter = butter;
                        isThere = true;
                        isFixed = tmp[2];
                    }
                }
                if (isThere) {
                    if (isFixed.equals("fix")) {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1) + "," + String.valueOf(yButter1 + 1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x, y + 1));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);

                    } else {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1) + "," + String.valueOf(yButter1 + 1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x, y + 1));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);
                        State successor2 = new State();
                        successor2.setRobot(new Node(x, y + 1));
                        successor2.setButters(butters);
                        successor2.setParentState(this);
                        successors.add(successor2);

                    }
                } else {
                    boolean canIPass = true;
                    for (String btt : butters) {
                        String[] tmp1 = btt.split(",");
                        int xBt = Integer.parseInt(tmp1[0]);
                        int yBt = Integer.parseInt(tmp1[1]);
                        String typeO = tmp1[2];
                        if (yBt == y && (xBt == x - 1 || xBt == x + 1)) {
                            if (typeO.equals("fix")) {
                                canIPass = false;
                            }
                        }
                    }
                    if (canIPass) {
                        State successor = new State();
                        successor.setRobot(new Node(x, y + 1));
                        successor.setButters(butters);
                        successor.setParentState(this);
                        successors.add(successor);
                    }
                }

            }
        }

        //left
        if (y - 1 >= 0) {
            String type = map[x][y - 1].getType();
            int cost = map[x][y - 1].getCost();
            boolean isButter = false;
            for (String butter : butters) {
                String[] tmp = butter.split(",");
                int xButter = Integer.parseInt(tmp[0]);
                int yButter = Integer.parseInt(tmp[1]);
                if (yButter == y - 1 && xButter == x) {
                    isButter = true;
                }
            }
            if (!isButter && (type.equals("normal") || type.equals("b"))) {
                boolean isThere = false;
                String chosenButter = "";
                String isFixed = "";
                for (String butter : butters) {
                    String[] tmp = butter.split(",");
                    int xButter = Integer.parseInt(tmp[0]);
                    int yButter = Integer.parseInt(tmp[1]);
                    if (yButter == y + 1 && xButter == x) {
                        chosenButter = butter;
                        isThere = true;
                        isFixed = tmp[2];
                    }
                }
                if (isThere) {
                    if (isFixed.equals("fix")) {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1) + "," + String.valueOf(yButter1 - 1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x, y - 1));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);

                    } else {
                        ArrayList<String> newButters = new ArrayList<>();
                        for (String butterOld : butters) {
                            if (chosenButter.equals(butterOld)) {
                                String[] tmp1 = chosenButter.split(",");
                                int xButter1 = Integer.parseInt(tmp1[0]);
                                int yButter1 = Integer.parseInt(tmp1[1]);
                                String newTmp1 = String.valueOf(xButter1) + "," + String.valueOf(yButter1 - 1) + "," + "not";
                                newButters.add(newTmp1);
                            } else {
                                newButters.add(butterOld);
                            }
                        }
                        State successor = new State();
                        successor.setRobot(new Node(x, y - 1));
                        successor.setButters(newButters);
                        successor.setParentState(this);
                        successors.add(successor);
                        State successor2 = new State();
                        successor2.setRobot(new Node(x, y - 1));
                        successor2.setButters(butters);
                        successor2.setParentState(this);
                        successors.add(successor2);

                    }
                } else {
                    boolean canIPass = true;
                    for (String btt : butters) {
                        String[] tmp1 = btt.split(",");
                        int xBt = Integer.parseInt(tmp1[0]);
                        int yBt = Integer.parseInt(tmp1[1]);
                        String typeO = tmp1[2];
                        if (yBt == y && (xBt == x - 1 || xBt == x + 1)) {
                            if (typeO.equals("fix")) {
                                canIPass = false;
                            }
                        }
                    }
                    if (canIPass) {
                        State successor = new State();
                        successor.setRobot(new Node(x, y - 1));
                        successor.setButters(butters);
                        successor.setParentState(this);
                        successors.add(successor);
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
        producedStates+=successors.size();
        expandedStates+=1;

//        System.out.println("My Children are : ");
//        for (State s : successors) {
//            s.printDetails();
//        }

        return successors;

    }

    public ArrayList<State> getSuccessorsForIDS() {
//        System.out.println("From start : ");
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

        producedStates+=successors.size();
        expandedStates+=1;

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

public class BidirectionalBFSApproach {
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
        State.producedStates=0;
        State.expandedStates=0;
//        long start=new Date().getTime();
        boolean hasAnswer = BidirectionalBFS(robot, butters);
//        long finish=new Date().getTime();
//        System.out.println(finish-start);
        if(!hasAnswer){

            FileWriter f = new FileWriter("./output/result4_BBFS.txt");
            f.write("can't pass the butter");
            f.close();
            System.out.println("can't pass the butter");
        }

        System.out.println("Produced states : "+State.producedStates);
        System.out.println("Expanded states : "+State.expandedStates);


    }

    public static boolean BidirectionalBFS(Node robot, ArrayList<String> butters) throws IOException {
        State start = new State();
        start.setRobot(robot);
        start.setButters(butters);

        ArrayList<String> finishButters = new ArrayList<>();
        for (Node goal : goals) {
            String tmp = goal.getX() + "," + goal.getY() + "," + "fix";
            finishButters.add(tmp);
        }
        for (Node goal : goals) {
            int x = goal.getX();
            int y = goal.getY();
            //down
            if (x + 1 < n) {
                String type = map[x + 1][y].getType();
                if (type.equals("normal")) {
                    State tmpState = new State();
                    tmpState.setButters(finishButters);
                    tmpState.setRobot(new Node(x + 1, y));

                    if (BBFS(start, tmpState) != null) {

                        return true;
                    }
                    //System.out.println(BBFS(start, tmpState));
                }
            }
            //up
            if (x - 1 >= 0) {
                String type = map[x - 1][y].getType();
                if (type.equals("normal")) {
                    State tmpState = new State();
                    tmpState.setButters(finishButters);
                    tmpState.setRobot(new Node(x - 1, y));

                    if (BBFS(start, tmpState) != null) {
                        return true;
                    }
                }
            }
            //right
            if (y + 1 < m) {
                String type = map[x][y + 1].getType();
                if (type.equals("normal")) {
                    State tmpState = new State();
                    tmpState.setButters(finishButters);
                    tmpState.setRobot(new Node(x, y + 1));

                    if (BBFS(start, tmpState) != null) {
                        return true;
                    }
                }
            }
            //left
            if (y - 1 >= 0) {
                String type = map[x][y - 1].getType();
                if (type.equals("normal")) {
                    State tmpState = new State();
                    tmpState.setButters(finishButters);
                    tmpState.setRobot(new Node(x, y - 1));

                    if (BBFS(start, tmpState) != null) {
                        return true;
                    }
                }
            }
        }
        return false;


    }

    public static State BBFS(State start, State target) throws IOException {
        Queue<State> queueStart = new LinkedList<>();
        Queue<State> queueTarget = new LinkedList<>();
        queueStart.add(start);
        queueTarget.add(target);

        ArrayList<State> visitedStart = new ArrayList<>();
        ArrayList<State> visitedTarget = new ArrayList<>();
        visitedStart.add(start);
        visitedTarget.add(target);


        while (!queueStart.isEmpty() || !queueTarget.isEmpty()) {
            State first = BFS(queueStart, visitedStart, visitedTarget, 1);
            if (first != null) {
                return first;
            }
            State second = BFS(queueTarget, visitedTarget, visitedStart, 2);
            if (second != null) {
                return second;
            }
        }
        return null;
    }

    public static State BFS(Queue<State> queue, ArrayList<State> start, ArrayList<State> target, int type) throws IOException {
        if (!queue.isEmpty()) {
            State current = queue.remove();
            ArrayList<State> children = null;
            if (type == 1) {
                children = current.getSuccessorsForIDS();
            } else if (type == 2) {
                children = current.getPredecessors();
            }
            for (State child : children) {
                if (hasItInThisArray(child, target) || isFinished(child)) {
                    String path = "";
                    State backward, forward;
                    if (type == 1) {
                        backward = child;
                        forward = fromArray(child, target);
                    } else {
                        backward = fromArray(child, target);
                        forward = child;
                    }
                    int cost = 0;
                    Stack<String> moves = new Stack<>();
                    while (backward.getParentState() != null) {
                        State parent = backward.getParentState();
                        int xR = backward.getRobot().getX();
                        int yR = backward.getRobot().getY();
                        int xRParent = parent.getRobot().getX();
                        int yRParent = parent.getRobot().getY();
                        cost += 1;
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
                        backward = backward.getParentState();
                    }
                    while (!moves.isEmpty()) {

                        path += moves.pop() + " ";
                    }

                    if(forward!=null) {
                        while (forward.getParentState() != null) {

                            State parent = forward.getParentState();
                            int xR = forward.getRobot().getX();
                            int yR = forward.getRobot().getY();
                            int xRParent = parent.getRobot().getX();
                            int yRParent = parent.getRobot().getY();
                            cost += 1;
                            if (xR - xRParent == -1) {
                                //up
                                path += "D ";
                            } else if (xR - xRParent == 1) {
                                //down
                                path += "U ";
                            } else if (yR - yRParent == -1) {
                                //left
                                path += "R ";
                            } else if (yR - yRParent == 1) {
                                //right
                                path += "L ";
                            } else {

                                path += "Error ";
                            }
                            forward = forward.getParentState();
                        }
                    }

                    int depth = cost;
                    FileWriter f = new FileWriter("./output/result1_BBFS.txt");
                    f.write(path + "\n");
                    f.write(cost + "\n");
                    f.write(depth + "\n");
                    f.close();

                    System.out.println(path);
                    System.out.println(cost);
                    System.out.println(depth);
                    return child;
                } else if (!hasItInThisArray(child, start)) {
                    start.add(child);
                    queue.add(child);
                }
            }
        }
        return null;
    }

    public static boolean hasItInThisArray(State state, ArrayList<State> array) {
        for (State s : array) {
            if (s.equals(state)) {
                return true;
            }
        }
        return false;
    }

    public static State fromArray(State state, ArrayList<State> array) {
        for (State s : array) {
            if (s.equals(state)) {
                return s;
            }
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
