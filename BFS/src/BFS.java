import java.util.*;

class Node {
    int x, y, heuristic_cost;
    Node parent;
    // TODO add parent

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int calculateDistance(Map map) {
        double x = (this.x - map.goal.x) * (this.x - map.goal.x);
        double y = (this.y - map.goal.y) * (this.y - map.goal.y);
        return (int)Math.sqrt(x + y);
    }

    ArrayList<Node> generateSuccessors(Map map) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node temp = new Node(this.x, this.y + 1);
        if (temp.isValid(map)) {
            temp.heuristic_cost = temp.calculateDistance(map);
            temp.parent = this;
            nodes.add(temp);
        }
        temp = new Node(this.x, this.y - 1);
        if (temp.isValid(map)) {
            temp.heuristic_cost = temp.calculateDistance(map);
            temp.parent = this;
            nodes.add(temp);
        }
        temp = new Node(this.x + 1, this.y);
        if (temp.isValid(map)) {
            temp.heuristic_cost = temp.calculateDistance(map);
            temp.parent = this;
            nodes.add(temp);
        }
        temp = new Node(this.x - 1, this.y);
        if (temp.isValid(map)) {
            temp.heuristic_cost = temp.calculateDistance(map);
            temp.parent = this;
            nodes.add(temp);
        }
        return nodes;
    }

    boolean isValid(Map map) {
        boolean isObstacle = false;
        for (Node obstacle : map.obstacles) {
            isObstacle = this.sameCoordinates(obstacle);
            if (isObstacle) {
                break;
            }
        }
        return this.x >= 0 && this.y >= 0 && this.x < map.width && this.y < map.height
                && (!isObstacle);
    }

    boolean sameCoordinates(Node n) {
        return (this.x == n.x) && (this.y == n.y);
    }

}
    
class Map {
    int width, height;
    Node start, goal;
    ArrayList<Node> obstacles;
    int map[][] = {
            { 8, 0, 1, 0, 1, 0, 1, 1, 1, 0 },
            { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 },
            { 0, 1, 0, 1, 1, 1, 0, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 0, 5 },
            { 1, 0, 1, 1, 0, 1, 0, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 },
            { 1, 0, 0, 1, 0, 1, 0, 1, 0, 1 },
            { 1, 1, 1, 1, 1, 1, 0, 0, 1, 0 }
    };

    Map() {
        this.height = map.length;
        this.width = map[0].length;
        obstacles = new ArrayList<Node>();
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (map[i][j] == 0) {
                    obstacles.add(new Node(i, j));
                } else if (map[i][j] == 8) {
                    start = new Node(i, j);

                } else if (map[i][j] == 5) {
                    goal = new Node(i, j);

                }
            }
        }
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.map[i][j] == 0) {
                    str += "X ";
                } else if (this.map[i][j] == 8) {
                    str += "S ";
                } else if (this.map[i][j] == 5) {
                    str += "F ";
                } else if (this.map[i][j] == 9) {
                    str += "* ";
                } else {
                    str += "_ ";
                }
            }
            str += "\n";
        }
        return str;
    }

}

public class BFS {
    public static void main(String[] args) {
        Map map = new Map();
        PriorityQueue<Node> openlist = new PriorityQueue<Node>(
                Comparator.comparingInt((Node a) -> a.calculateDistance(map)));
        ArrayList<Node> closedlist = new ArrayList<Node>();
        openlist.add(map.start);
        while (!openlist.isEmpty()) {
            Node temp = openlist.poll();

            // System.out.println(temp.heuristic_cost);
            closedlist.add(temp);
            if (temp.x == map.goal.x && temp.y == map.goal.y) {
                System.out.println("Win");

                while (temp.parent != null) {
                    map.map[temp.parent.x][temp.parent.y] = 9;
                    temp = temp.parent;
                }
                map.map[map.start.x][map.start.y] = 8;
                System.out.println(map);
                break;
            }
            ArrayList<Node> successors = temp.generateSuccessors(map);
            // System.out.println(successors);
            for (int i = 0; i < closedlist.size(); i++) {
                for (int j = 0; j < successors.size(); j++) {
                    if (closedlist.get(i).x == successors.get(j).x && closedlist.get(i).y == successors.get(j).y) {
                        successors.remove(j);
                        break;
                    }
                }
            }
            openlist.addAll(successors);

        }
    }
}