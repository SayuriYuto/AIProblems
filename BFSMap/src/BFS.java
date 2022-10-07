import java.util.*;

class Node {
    int x, y, heuristic_cost;
    Node parent;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int calculateManhattanDistance(Map map) {
        return Math.abs(this.x - map.goal.x) + Math.abs(this.y + map.goal.y);
    }

    ArrayList<Node> generateSuccessors(Map map){
        ArrayList<Node> nodes = new ArrayList<Node>();
        Node temp = new Node(this.x, this.y+1);
        if(temp.isValid(map)){
            nodes.add(temp);
        }
        temp = new Node(this.x,this.y-1);
        if(temp.isValid(map)){
            nodes.add(temp);
        }
        temp = new Node(this.x+1,this.y);
        if(temp.isValid(map)){
            nodes.add(temp);
        }
        temp = new Node(this.x-1,this.y);
        if(temp.isValid(map)){
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
        return this.x > 0 && this.y > 0 && this.x <= map.width && this.y <= map.height
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
        }
        return str;
    }
}

class BFS {
    public static void main(String[] args) {
        System.out.println(new Map());
    }
}