import java.util.*;
class Map {
    int width;
    int height;
    Node start;
    Node goal;
    ArrayList<Node> obstacles;
    // wall = 0
    // path = 1
    // start = 8
    // goal = 5
    // path = 9
    
    // int map[][] = {
    //         { 1, 0, 1, 0, 1, 1, 1, 1, 1, 0 },
    //         { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 },
    //         { 0, 1, 0, 1, 1, 1, 0, 1, 1, 1 },
    //         { 0, 1, 1, 1, 1, 1, 1, 1, 0, 5 },
    //         { 1, 0, 1, 1, 0, 1, 0, 1, 1, 1 },
    //         { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
    //         { 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 },
    //         { 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 },
    //         { 1, 0, 0, 1, 0, 1, 0, 1, 0, 1 },
    //         { 8, 1, 1, 1, 1, 1, 0, 0, 1, 1 }
    // };

    int map[][] = {
        { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0 },
        { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 },
        { 0, 1, 0, 1, 1, 1, 0, 1, 1, 5 },
        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
        { 1, 0, 1, 1, 0, 1, 0, 1, 1, 1 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
        { 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 },
        { 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 },
        { 1, 0, 0, 1, 0, 1, 0, 1, 0, 1 },
        { 1, 1, 1, 8, 1, 1, 0, 0, 1, 1 }
};

    Map() {

        this.height = map.length;
        this.width = map[0].length;
        obstacles = new ArrayList<Node>();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (map[i][j] == 0) {
                    obstacles.add(new Node(i, j, 0, null));
                } else if (map[i][j] == 8) {
                    start = new Node(i, j, 0, null);

                } else if (map[i][j] == 5) {
                    goal = new Node(i, j, 0, null);

                }
            }
        }
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.map[i][j] == 0) {
                    System.out.print("X ");
                } else if (this.map[i][j] == 8) {
                    System.out.print("S ");
                } else if (this.map[i][j] == 5) {
                    System.out.print("F ");
                } else if (this.map[i][j] == 9) {
                    System.out.print("* ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}

public class Node {
    int x;
    int y;
    int cost;
    Node parent;

    Node(int x, int y, int cost, Node parent) {
        this.x = x;
        this.y = y;
        this.cost = cost;

        this.parent = parent;
    }

    int calculateEstimate(Node goal) {
        return cost + calculateManhattanDistance(this, goal);
    }

    boolean sameCoordinates(Node n) {
        return (x == n.x) && (y == n.y);
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs) {
            return true;
        } else if (rhs == null) {
            return false;
        } else if (rhs instanceof Node) {
            Node tmp = (Node) rhs;
            return x == tmp.x
                    && y == tmp.y
                    && cost == tmp.cost;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(x,y) = (" + x + ", " + y + ")";
    }

    static int calculateManhattanDistance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y + b.y);
    }

    static List<Node> generateSuccessors(Node n, Map map) {
        ArrayList<Node> nodes = new ArrayList<>();
        Node tmp = new Node(n.x, n.y + 1, n.cost + 1, n);
        if (isValid(tmp, map))
            nodes.add(tmp);
        tmp = new Node(n.x, n.y - 1, n.cost + 1, n);
        if (isValid(tmp, map))
            nodes.add(tmp);
        tmp = new Node(n.x + 1, n.y, n.cost + 1, n);
        if (isValid(tmp, map))
            nodes.add(tmp);
        tmp = new Node(n.x - 1, n.y, n.cost + 1, n);
        if (isValid(tmp, map))
            nodes.add(tmp);
        return nodes;
    }

    static boolean isValid(Node node, Map map) {
        boolean isObstacle = false;
        for (Node obstacle : map.obstacles) {
            isObstacle = node.sameCoordinates(obstacle);
            if (isObstacle) {
                break;
            }
        }
        return node.x > 0 && node.y > 0 && node.x <= map.width && node.y <= map.height
                && (!isObstacle);
    }

    public static void main(String[] args) {
        Map gameMap = new Map();

        PriorityQueue<Node> openList = new PriorityQueue<Node>(10,
                Comparator.comparingInt((Node a) -> a.calculateEstimate(gameMap.goal)));

        HashSet<Node> closedList = new HashSet<>();

        openList.add(gameMap.start);

        while (!openList.isEmpty()) {
            Node tmp = openList.poll();

            if (tmp.x == gameMap.goal.x && tmp.y == gameMap.goal.y) {
                ArrayList<Node> pathToGoal = new ArrayList<>();

                while (tmp.parent != null) {
                    pathToGoal.add(tmp);
                    tmp = tmp.parent;
                }

                for (int i = pathToGoal.size() - 1; i >= 1; i--) {
                    // System.out.println(pathToGoal.get(i));
                    gameMap.map[pathToGoal.get(i).x][pathToGoal.get(i).y] = 9;
                }
                for (int i = 0; i < gameMap.height; i++) {
                    for (int j = 0; j < gameMap.width; j++) {
                        if (gameMap.map[i][j] == 0) {
                            System.out.print("X ");
                        } else if (gameMap.map[i][j] == 8) {
                            System.out.print("S ");
                        } else if (gameMap.map[i][j] == 5) {
                            System.out.print("F ");
                        } else if (gameMap.map[i][j] == 9) {
                            System.out.print("* ");
                        } else {
                            System.out.print("_ ");
                        }
                    }
                    System.out.println();
                }
                break;
            }
            SuccessorLoop: for (Node successor : generateSuccessors(tmp, gameMap)) {
                for (Node n : openList) {
                    if (successor.sameCoordinates(n)
                            && n.calculateEstimate(gameMap.goal) > successor.calculateEstimate(gameMap.goal)) {
                        continue SuccessorLoop;
                    }
                }

                if (closedList.contains(successor)) {
                    for (Node n : closedList) {
                        if (n.sameCoordinates(successor)
                                && n.calculateEstimate(gameMap.goal) > successor.calculateEstimate(gameMap.goal)) {
                            continue SuccessorLoop;
                        }
                    }
                }

                openList.add(successor);
            }

            closedList.add(tmp);
        }
    }
}