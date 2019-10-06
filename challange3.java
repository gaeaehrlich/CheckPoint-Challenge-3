import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class challange3 {

    public static void BellmanFord(City[][] map, int n, int col, int row) {
        List<tuple> edges = getEdges(n);
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i == col && j == row) map[i][j].dist = 0;
                else map[i][j].dist = 1000000;
            }
        }
        for(int i = 0; i < n*n; i++) {
             for(tuple t : edges) {
                 if(map[t.dest[0]][t.dest[1]].dist > map[t.source[0]][t.source[1]].dist + map[t.dest[0]][t.dest[1]].weight) {
                    map[t.dest[0]][t.dest[1]].dist = map[t.source[0]][t.source[1]].dist + map[t.dest[0]][t.dest[1]].weight;
                    map[t.dest[0]][t.dest[1]].pred = t;
                }
             }
        }
    }

    public static void printPath(City[][] map, int s1, int s2, int d1, int d2) {
        boolean term = true;
        while(term) {
            System.out.print("(" + String.valueOf(d1) + ", " + String.valueOf(d2) + ")");
            if(d1 == s1 && d2 == s2) {
                System.out.println();
                term = false;
                continue;
            }
            System.out.print(", ");
            int temp1 = map[d1][d2].pred.source[0];
            int temp2 = map[d1][d2].pred.source[1];
            d1 = temp1; d2 = temp2;
        }
    }

    public static void updateNeedArray(City[][] map, int s1, int s2, int d1,int d2) {
        for(int i = 0; i < 6; i++) {
            if(map[s1][s2].have[i] == 1) map[d1][d2].need[i] = 0;
            if(map[d1][d2].have[i] == 1) map[s1][s2].need[i] = 0;
        }
    }

    public static void getShortest(City[][] map, int n, int col, int row, int k) {
        int min = Integer.MAX_VALUE, coor1 = -1, coor2 = -1;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(map[i][j].have[k] == 1 && map[i][j].dist < min) {
                    min = map[i][j].dist;
                    coor1 = i; coor2 = j;
                }
            }
        }
        if(coor1 == -1) return;
        printPath(map, col, row, coor1, coor2);
        updateNeedArray(map, col, row, coor1, coor2);
    }


    public static void handleCity(City[][] map, String line) {
        String[] temp = line.split("\\(")[1].split("\\)");
        String[] coor = temp[0].split(", ");
        String[] resources = temp[1].split(", ");
        resources = Arrays.copyOfRange(resources, 1, resources.length);
        map[Integer.valueOf(coor[0])][Integer.valueOf(coor[1])].findHaveArray(resources);
        map[Integer.valueOf(coor[0])][Integer.valueOf(coor[1])].haveCity = true;
    }

    static int handleTerrain(City[][] map, int n, int i, String line) {
        String[] terrain = line.split("\\[")[1].split("],")[0].split(", ");
        for(int j = 0; j < n; j++) {
            map[i][j].findWeight(terrain[j]);
        }
        return 1;
    }

    public static void initializeMap(City[][] map, int n) {
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                map[i][j] = new City();
            }
        }
    }

    public static City[][] getMap(String fileName, int n) throws Exception{
        City[][] map = new City[n][n];
        initializeMap(map, n);
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        boolean flag = true;
        int i = 0;
        while((line = br.readLine()) != null) {
            if(line.equals("cities")) flag = false;
            else if(flag) i += handleTerrain(map, n, i, line);
            else handleCity(map, line);
        }
        br.close();
        return map;
    }

    public static void getEdgeEven(List<tuple> res, int i, int j) {
        res.add(new tuple(i, j, i, j-1));
        res.add(new tuple(i, j, i-1,j-1));
        res.add(new tuple(i, j, i-1,j));
        res.add(new tuple(i, j, i, j+1));
        res.add(new tuple(i, j, i+1,j));
        res.add(new tuple(i, j, i+1,j-1));
    }

    public static void getEdgeOdd(List<tuple> res, int i, int j) {
        res.add(new tuple(i, j, i, j - 1));
        res.add(new tuple(i, j, i - 1, j));
        res.add(new tuple(i, j, i - 1, j + 1));
        res.add(new tuple(i, j, i, j + 1));
        res.add(new tuple(i, j, i + 1, j + 1));
        res.add(new tuple(i, j, i + 1, j));
    }


    public static List<tuple> getEdges(int n) {
        List<tuple> temp = new ArrayList<>();
        List<tuple> res = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i % 2 == 0) getEdgeEven(temp, i, j);
                else getEdgeOdd(temp, i, j);
            }
        }
        for(tuple t : temp) {
            if(!(t.dest[0] < 0 || t.dest[1] < 0 || t.dest[0] > n-1 || t.dest[1] > n-1)) {
                res.add(t);
                res.add(new tuple(t.dest[0], t.dest[1], t.source[0], t.source[1]));
            }
        }
        return res;
    }

    public static void main(String[] args) throws Exception{
        final int n = 64;
        String fileName = "input.txt";
        City[][] map = getMap(fileName, n);
        for(int k = 0; k < 6; k++) {
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(map[i][j].haveCity && map[i][j].need[k] == 1) {
                       BellmanFord(map, n, i, j);
                       getShortest(map, n, i, j, k);
                    }
                }
            }
        }
    }
}
