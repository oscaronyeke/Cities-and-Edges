import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Name:    Oscar Onyeke
// Class:   CSCI.331.01

public class Search {
    boolean s1[];
    boolean s2[];

    //class for each city
    static class Cities{
        LinkedList<String> places;
        LinkedList<Double>x_s;
        LinkedList<Double>y_s;

        Cities(){
            places = new LinkedList<>();
            x_s = new LinkedList<>();
            y_s = new LinkedList<>();
        }
    }

    // class for the graph
    static class Graph{
        int V;
        LinkedList<Integer> graphl[];

        //constructor
        Graph(int V){
            this.V = V;

            graphl = new LinkedList[V];

            for(int i=0;i<V;i++){
                graphl[i]=new LinkedList<>();
            }
        }
    }

    // Runs the BFS search
    public void BFS(Graph graph, Cities cities,String start,String end,int i,double[][]w, PrintWriter f4){
        this.s1 = new boolean[i];
        double dist[] = new double[i];
        int depth[] = new int[i];
        LinkedList<String>path[] = new LinkedList[i];
        double total=0;
        int beq=1;
        int en=2;
        int b;
        int headloc;
        int tailloc;
        String tail;
        String Q[]= new String[i];
        Q[1]=start;
        this.s1[cities.places.indexOf(start)]=true;
        for(int a=0;a<i;a++){
            path[a]=new LinkedList<>();
        }
        while(beq<en){
            start = Q[beq];
            headloc = cities.places.indexOf(start);
            path[headloc].add(start);
            // goes to each neighbor of the head
            for (b=0;b<graph.graphl[headloc].size();b++) {
                tail = cities.places.get(graph.graphl[headloc].get(b));
                tailloc = cities.places.indexOf(tail);
                // if this is a new neighbor then it is saved
                if(!this.s1[tailloc]){
                    this.s1[tailloc]=true;
                    Q[en]=tail;
                    dist[tailloc] = dist[headloc] + w[headloc][tailloc];
                    path[tailloc].addAll(path[headloc]);
                    en++;
                }
                // checks if the neighbor if the end node
                if (tail.equals(end)){
                    f4.println("");
                    f4.println("Breadth-First Search Results: ");
                    for (int u=0;u<path[tailloc].size();u++){
                        f4.println(path[tailloc].get(u));
                    }
                    f4.println(tail);
                    f4.println("That took "+path[tailloc].size()+" hops to find.");
                    f4.println("Total distance = "+ ((int) dist[tailloc])+" miles.");
                    f4.println("\n");
                    return ;
                }
            }
            beq++;
        }

    }

    public void DFS_RUN(Graph graph, Cities cities, double[][] w,String start, String end, int i, PrintWriter print){
        // prepares the DFSS search
        this.s2 = new boolean[i];

        // list of paths taken from DFS search
        LinkedList<String>path[] = new LinkedList[i];
        for(int y=0;y<i;y++){
            path[y]=new LinkedList<>();
            this.s2[y] = false;
        }
        DFS(graph,cities,w,start,end,i,0,path,print);
    }


    public int DFS(Graph graph, Cities cities, double[][] w,String start, String end, int i,
                      double total, LinkedList<String>[] path,PrintWriter print){
        // runs the DFS search
        int head = cities.places.indexOf(start);
        int n = 0;
        int tail;
        String u;
        this.s2[head] = true;
        path[head].add(start);

        LinkedList<String> nodes=new LinkedList<>();

        // gets all neighbors of the head and puts them in a list
        // also checks for the end node
        for(n=0;n<graph.graphl[head].size();n++){
            nodes.add(cities.places.get(graph.graphl[head].get(n)));
            u = nodes.get(n);
            tail = cities.places.indexOf(u);
            if(u.equals(end)){
                total = total+w[head][tail];
                print.println("Depth-First Search Results: ");
                for(int o=0;o<path[head].size();o++){
                    print.println(path[head].get(o));
                }
                print.println(u);
                print.println("That took "+path[head].size()+ " hops to find.");
                print.println("Total distance = "+ ((int) total)+" miles.");
                print.println("\n");
                return 1;
            }
        }

        // sorts the list in alphabetical order
        Collections.sort(nodes,String.CASE_INSENSITIVE_ORDER);

        // recures on each neighbor.
        for(n=0;n<graph.graphl[head].size();n++){
            u = nodes.get(n);
            tail = cities.places.indexOf(u);
            if(!this.s2[tail]){
                this.s2[tail] = true;
                path[tail].addAll(path[head]);
                if(DFS(graph, cities, w, u, end, i, total+w[head][tail],path,print)==1){
                    return 1;
                }
            }
        }
        return 0;
    }

    public static void main(String args[])throws FileNotFoundException{
        int i=0;
        int b;
        int c;
        int frt;
        int bak;
        double hyp;
        double weigth[][];

        String head;
        String end;


        File f1 = new File("city.dat");
        File f2 = new File("edge.dat");
        // checks if there is a file for city
        if(!f1.canRead()){
            System.err.println("File not found: city.dat");
            System.exit(0);
        }
        //checks if there is a file for edge
        if(!f2.canRead()){
            System.err.println("File not found: edge.dat");
            System.exit(0);
        }

        Scanner scan1 = new Scanner(f1);
        Scanner scan2 = new Scanner(f2);
        Cities cities = new Cities();

        // creates a city method.
        while(scan1.hasNext()){
            cities.places.add(scan1.next());
            scan1.next();
            cities.x_s.add(scan1.nextDouble());
            cities.y_s.add(scan1.nextDouble());
            i++;
        }

        Graph graph = new Graph(i);
        weigth = new double[i][i];

        // creates the graph m.
        while(scan2.hasNext()){
            frt = cities.places.indexOf(scan2.next());
            bak = cities.places.indexOf(scan2.next());
            hyp = Math.sqrt(Math.pow((cities.x_s.get(frt)-cities.x_s.get(bak)),2)+
                    Math.pow((cities.y_s.get(frt)-cities.y_s.get(bak)),2)) * 100;
            graph.graphl[frt].add(bak);
            graph.graphl[bak].add(frt);
            weigth[frt][bak] = hyp;
            weigth[bak][frt] = hyp;
        }

        if(args.length != 2){
            System.err.println("Usage: java Search inputFile outputFile");
            System.exit(0);
        }

        // scans the input file
        File f3;
        if(args[0].equals("-")){
            Scanner s4 = new Scanner(System.in);
            f3 = new File(s4.next());
        }
        else {
            f3 = new File(args[0]);

        }
        if(!f3.canRead()){
            System.err.println("File not found: ( "+f3.getName()+" )");
            System.exit(0);
        }


        //gets the output file
        String ou = null;
        if(args[1].equals("-")){
            Scanner s4 = new Scanner(System.in);
            ou = s4.next();
        }
        else {
            ou = args[1];

        }
        PrintWriter f4 = new PrintWriter(ou);





        Scanner s3 = new Scanner(f3);
        head = s3.next();
        end = s3.next();

        // checks if the inputs are valid
        if(!cities.places.contains(head)){
            System.err.println("No such city: ( "+head+" )");
            System.exit(0);
        }

        if(!cities.places.contains(end)){
            System.err.println("No such city: ( "+end+" )");
            System.exit(0);
        }

        Search search = new Search();
        search.BFS(graph,cities,head,end,i,weigth,f4);
        search.DFS_RUN(graph,cities,weigth,head,end,i,f4);

        //creates linked list for A*
        LinkedList<Double> h = new LinkedList<>();
        LinkedList<String> map = new LinkedList<>();
        LinkedList<LinkedList<String>> path = new LinkedList<>();

        // creating the start state for A* search
        String top = head;
        String tail = null;
        int curr =0;
        int bb =0;
        int temp=0;
        double lowest;
        double val = 0;
        map.add(head);
        h.add(0.0);
        LinkedList<String> as = new LinkedList<>();
        as.add(top);
        path.add(as);

        while(map.size()!=0){
            // gets the index of the head
            bb = cities.places.indexOf(top);

            //getting all edges associated with the head
            for(b=0;b<graph.graphl[bb].size();b++){
                temp = graph.graphl[bb].get(b);
                tail = cities.places.get(temp);
                val = weigth[bb][temp]+h.get(curr);
                map.add(tail);
                h.add(val);
                LinkedList<String> sdf = new LinkedList<>();
                sdf.addAll(path.get(curr));
                sdf.add(tail);
                path.add(sdf);
            }

            h.set(curr,Double.MAX_VALUE);
            lowest = Collections.min(h);
            temp = h.indexOf(lowest);

            // checks if the smallest node is the end node
            if(map.get(temp).equals(end)){
                f4.println("A* Search Results: ");
                for(b=0;b<path.get(temp).size();b++){
                    f4.println(path.get(temp).get(b));
                }
                f4.println("That took "+(path.get(temp).size()-1)+" hops to find.");
                f4.println("Total distance = "+ ((int) lowest)+" miles.");
                f4.println("");
                f4.close();
                System.exit(1);
            }

            // updates A*
            top = map.get(temp);
            curr = temp;
        }
        f4.close();
    }
}
