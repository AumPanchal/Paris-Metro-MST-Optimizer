// Aum Panchal 400415468

import java.io.*;
import java.util.*;


public class ParisMetro {

    static int n, m;
    static Partition<Integer> partition;
    static Node<Integer>[] nodes;
    static ArrayList<String> stationNames;
    static ArrayList<Edge> trainEdges;

    public static void main (String[] args) {

        String inputFileName = "metro.txt";
        if (args.length > 0) {
            inputFileName = args[0];
        }

        try {
            System.setIn(new FileInputStream(inputFileName));
            readMetro();
            solveKruskal();

        } catch (Exception e) {
            System.out.println("Error: Could not read the file.");
        }
    }



    public static void readMetro()
    {

        System.out.println("\nStart Reading Metro");
        Scanner scan = new Scanner(System.in); 

        n = scan.nextInt(); // number of vertices
        m = scan.nextInt(); // number of edges
        System.out.println("Paris Metro Graph has "+n+" vertices and "+m+" edges.");
        
        partition = new Partition<>();
        nodes = new Node[n];
        stationNames = new ArrayList<String>();
        trainEdges = new ArrayList<>();

        for (int i=0; i <n; i++)
        {   int vertexNumber=Integer.valueOf(scan.next()); // vertex number (unique, example: 0016)
            String stationName=scan.nextLine(); 	    // vertex name (not unique, example: Bastille)

            stationNames.add(stationName);
            nodes[i] = partition.makeCluster(i);
        }

        scan.nextLine();//read the $ sign 

        for (int i=0; i <m; i++)
        {
            int u=scan.nextInt(); int v=scan.nextInt(); int weight=scan.nextInt(); // edge information
            //System.out.println("u="+u+" v="+v+" weight="+weight);

            if (weight == -1) {
                if (partition.find(nodes[u]) != partition.find(nodes[v])) {
                    partition.union(nodes[u], nodes[v]);
                }
            } else {
                Edge edge = new Edge(u, v, weight);
                trainEdges.add(edge);
            }
  
        }

       System.out.println("End Reading Metro\n");
    }

    public static void solveKruskal()
    {
        Collections.sort(trainEdges);

        int totalWeight = 0;
        ArrayList<Edge> mstEdges = new ArrayList<>();

        for (Edge edge : trainEdges) {
            Node<Integer> node1 = nodes[edge.u];
            Node<Integer> node2 = nodes[edge.v];

            if (partition.find(node1) != partition.find(node2)) {
                partition.union(node1, node2);
                mstEdges.add(edge);
                totalWeight += edge.weight;
            }
        }

        System.out.println("Total cost (weight of MST): " + totalWeight);
        System.out.println("Edges to buy (Edges in the MST):");
        for (Edge edge : mstEdges) {
            System.out.println("From " + edge.u + " to " + edge.v + " with weight " + edge.weight);
        }
    }
}

class Edge implements Comparable<Edge> {
  int u, v, weight;

  public Edge(int u, int v, int weight) {
      this.u = u;
      this.v = v;
      this.weight = weight;
  }

  @Override
  public int compareTo(Edge other) {
      return this.weight - other.weight; // Sorts by cheapest weight first
  }
}
class Partition<E>{
/**
 * Implements Partition as a linked list
 */

   
    Cluster<E> pheader;
    Cluster<E> ptrailer;
    int size=0;

    public Partition() {
        pheader=new Cluster<>(null, null);
        ptrailer=new Cluster<>(pheader, null);
        pheader.setNextCluster(ptrailer);

    }

    public Node<E> makeCluster(E x){
        Cluster<E> c=new Cluster<>(ptrailer.getPrevCluster(), ptrailer);
        Node<E> pos= c.addLast(x);
        this.addLast(c);
        return pos;

    }
    
    public Node<E> find(Node<E> n){
    
        return n.getCluster().first();

    }
     
    public void union(Node<E> p, Node<E> q){
       
        Cluster<E> a = find(p).getCluster();
        Cluster<E> b = find(q).getCluster();
        if (a==b){ return;}
        Cluster<E> c1 = min(a, b); //cluster to get merged
        Cluster<E> c2 = c1.equals(a)? b: a; //cluster to merge into
        
        c2.add(c1);

        Node<E> curr=c2.first();
        while(curr!=null) {          
            curr.setCluster(c2);
            curr=c2.after(curr);
        }
        
        remove(c1);

    }

    public E element(Node<E> n){
        return n.getElement();

    } 

    public int clusterSize(Node<E> n){
        Node<E> leader=find(n);
        return leader.getCluster().size();
        
    }

    public Node<E>[] clusterPositions(Node<E> p){
        Node<E> n= find(p);
        Cluster<E> c = n.getCluster();
        Node<E>[] positions=new Node[c.size()];
        Node<E> curr=c.first();
        for (int i=0; i<c.size(); i++){
            positions[i]=curr;
            curr=c.after(curr);

        }
        return positions;


    } 
    
    public Integer[] clusterSizes(){
        Cluster<E> curr=pheader.getNextCluster();
        Integer[] sizes=new Integer[size()];
        for (int i=0; i<size(); i++){
            sizes[i]=curr.size();
            curr=after(curr);

        }
        Arrays.sort(sizes, Collections.reverseOrder());

        return sizes;

    }

    public int numberOfClusters(){
        return size;
    }

    //Utility methods
    public int size(){
        return size;
    }

    public void addLast(Cluster<E> c){
        ptrailer.getPrevCluster().setNextCluster(c);
        ptrailer.setPrevCluster(c);
        size++;

    }

    public Cluster<E> after(Cluster<E> c){
        return c.getNextCluster();

    }

    public Cluster<E> before(Cluster<E> c){
        return c.getPrevCluster();

    }

    public Cluster<E> first() {
        return pheader.getNextCluster();
    }

    public void remove(Cluster<E> c){
        c.getPrevCluster().setNextCluster(c.getNextCluster());
        c.getNextCluster().setPrevCluster(c.getPrevCluster());
        size--;
    }

    //Helper methods
    public Cluster<E> min(Cluster<E> c1, Cluster<E> c2){
        return (c1.size()<=c2.size()? c1:c2);
    }

    public Node<E>[] getLeaders(){
        Node<E>[] leaders=new Node[size()];
        Cluster<E> curr = first();
        for (int i=0; i<size(); i++){
            leaders[i]=curr.first();
            curr=after(curr);
        }
        return leaders;
    }




    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("Partition\n|\n");
        Cluster<E> curr=this.first();
        while (curr !=ptrailer){
            sb.append(curr.toString());
            curr = this.after(curr);
        if (curr != ptrailer)
            sb.append("\n");
        }
        return sb.toString();
    }

}
class Cluster<E>{
    
  private Cluster<E> prevCluster; //pointer to previous cluster
  private Cluster<E> nextCluster; //pointer to the head of this cluster

  private Node<E> head;                      
  private Node<E> tail;                     
  /** Number of elements in the list */
  private int size = 0;    

  /** Constructs a new empty list. */
  public Cluster(Cluster<E> prevCluster, Cluster<E> nextCluster) {
      head = null;      // head
      tail = head;   // tail 
      this.prevCluster=prevCluster;
      this.nextCluster=nextCluster;
  }

  public void setNextCluster(Cluster<E> c){
      this.nextCluster=c;
  }

  public void setPrevCluster(Cluster<E> c){
      this.prevCluster= c;
  }

  public Cluster<E> getNextCluster(){
      return this.nextCluster;
  }

  public Cluster<E> getPrevCluster(){
      return this.prevCluster;
  }
      
      /**
   * Returns the number of elements in the list.
   * @return number of elements in the list
   */
  public int size(){
      return size;
  };

  /**
   * Tests whether the list is empty.
   * @return true if the list is empty, false otherwise
   */
  public boolean isEmpty(){
      return size==0;
  };

  /**
   * Returns the first Node in the list.
   *
   * @return the first Node in the list (or null, if empty)
   */
  public Node<E> first(){
      if (this.isEmpty()){
      return null;
      }
      return head;
  };

  /**
   * Returns the last Node in the list.
   *
   * @return the last Node in the list (or null, if empty)
   */
  public Node<E> last(){
      if (this.isEmpty()){
      return null;
      }
      return tail;
  }

  /**
   * Returns the Node immediately after Position p.
   * @param p   a Node of the list
   * @return the subsequent Node 
   */

  public Node<E> after(Node<E> p) {
      return p.getNext();
  }

  
  /**
   * Appends a cluster c to the end of this Cluster
   * @param c Cluster to be appended
   */
 public void add(Cluster<E> c){
      Node<E> n = c.first();
      n.setPrev(tail);
      tail.setNext(n);
      this.tail=c.tail;
      size=size+c.size;
      
 }

  /**
   * Inserts an element at the back of the list.
   *
   * @param e the new element
   * @return the Node representing the location of the new element
   */

  public Node<E> addLast(E e) {
      Node<E> n = new Node(null, e, null, this); //(prev, element, next, )
      if (this.isEmpty()) {
          head=n;
          tail=head;
          size++;
          return n;
      }
      
      n.setPrev(tail);
      tail.setNext(n);
      tail=n;
      size++;
      return n;    
  }

  // Debugging code
  /**
   * Produces a string representation of the contents of the list.
   * This exists for debugging purposes only.
   */
  @Override
  public String toString() {
      StringBuilder sb = new StringBuilder("(");
      Node<E> walk = head;
      while (walk != null) {
          sb.append(walk.getElement());
          walk = walk.getNext();
          if (walk != null)
              sb.append(", ");
      }
      sb.append(")");
      return sb.toString();
  }       
      
}
class Node<E> {
    E element;
    Node<E> prev;
    Node<E> next;
    Cluster<E> cluster;
 
    Node(Node<E> var1, E var2, Node<E> var3, Cluster var4) {
       this.prev = var1;
       this.element = var2;
       this.next = var3;
       this.cluster = var4;
    }
 
    public E getElement() {
       return this.element;
    }
 
    public Cluster<E> getCluster() {
       return this.cluster;
    }
 
    public Node<E> getNext() {
       return this.next;
    }
 
    public Node<E> getPrev() {
       return this.prev;
    }
 
    public void setElement(E var1) {
       this.element = var1;
    }
 
    public void setCluster(Cluster<E> var1) {
       this.cluster = var1;
    }
 
    public void setNext(Node<E> var1) {
       this.next = var1;
    }
 
    public void setPrev(Node<E> var1) {
       this.prev = var1;
    }
 
    public String toString() {
       return "Node(" + String.valueOf(this.element) + ")";
    }
 } 