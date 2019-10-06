import javax.rmi.ssl.SslRMIClientSocketFactory;

public class City {
    public int weight;
    public int[] have;
    public int[] need;
    public int dist;
    public tuple pred;
    public boolean haveCity = false;

    public City() {
        this.weight = 0;
        this.have = new int[6];
        this.dist = 0;
        this.pred = null;
    }

    public void findWeight(String terrain) {
        if(terrain.equals("mountain")) this.weight = 6;
        else if(terrain.equals("wood")) this.weight = 2;
        else if(terrain.equals("open")) this.weight = 1;
        else if(terrain.equals("swamp")) this.weight = 4;
        else this.weight = 7;
    }

    public void findHaveArray(String[] recources) {
        //produce, wood, stone, clay, ore, textile
        int[] have = new int[6];
        for(String s : recources) {
            if(s.equals("Produce")) have[0] = 1;
            else if(s.equals("Wood")) have[1] = 1;
            else if(s.equals("Stone")) have[2] = 1;
            else if(s.equals("Clay")) have[3] = 1;
            else if(s.equals("Ore")) have[4] = 1;
            else have[5] = 1;
        }
        this.have = have;
        this.findNeedArray();
    }

    public void findNeedArray() {
        int[] need = new int[6];
        for(int i = 0; i < 6; i++) {
            if(this.have[i] == 0) need[i] = 1;
        }
        this.need = need;
    }
}
