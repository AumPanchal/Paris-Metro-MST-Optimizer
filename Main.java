// Aum Panchal 400416468


// Expensive Subway problem (P2-part A)
// Startup code given in the Fall 2025 for csi2110/csi2510
// This file only contains basic commands to read the data from the input files.
// Use and modify it freely
// 
// Do not forget to add your name and student number on each program file you submit
//

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

class Main {

    public static void main(String[] args) {


      Scanner scanner = new Scanner(System.in); // Note that for Online Judge the input must be given in the standard I/O
            // to read from input files use the command 'java Main < test1.txt', test1.txt should be in the same folder
                                

      int nv, ne; 

      while (true) {

        nv = scanner.nextInt();
        ne = scanner.nextInt();
        
        if ((nv==0) && (ne==0)) break;
        
        HashMap<String, Integer> map = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>(); 

        for (int v=0; v<nv; v++) {
            String vName = scanner.next(); 
            map.put(vName, v);
        }

        for (int i=0; i<ne; i++) {
            String uName = scanner.next(); 
            String vName = scanner.next(); 
            int w = scanner.nextInt(); 
            
            int u = map.get(uName);
            int v = map.get(vName);
            
            edges.add(new Edge(u, v, w));
        } 

        String home = scanner.next();

        Collections.sort(edges);

        Partition<Integer> p = new Partition<>();
        Node<Integer>[] nodes = new Node[nv];

        for (int i = 0; i < nv; i++) {
            nodes[i] = p.makeCluster(i);
        }

        int totalCost = 0;
        int edgesCount = 0;

        for (Edge e : edges) {
            Node<Integer> uNode = nodes[e.u];
            Node<Integer> vNode = nodes[e.v];

            if (p.find(uNode) != p.find(vNode)) {
                p.union(uNode, vNode);
                totalCost += e.weight;
                edgesCount++;
            }
        }
        
        if (edgesCount == nv - 1 || nv <= 1) {
            System.out.println(totalCost);
        } else {
            System.out.println("Impossible");
        }

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
class Node<E>{
  E element; //element
  Node<E> prev; //preceeding Node
  Node<E> next; //succeeding Node
  Cluster<E> cluster;


  Node(Node<E> prev, E element, Node<E> next, Cluster cluster){
      this.prev=prev;
      this.element=element;
      this.next=next;
      this.cluster=cluster;
  }

  //getters
  public E getElement(){return this.element;}
  public Cluster<E> getCluster(){return this.cluster;}
  public Node<E> getNext(){return next;}
  public Node<E> getPrev(){return prev;}


  //setters
  public void setElement(E elem){this.element=elem;}
  public void setCluster(Cluster<E> n){this.cluster=n;}
  public void setNext(Node<E> n){next=n;}
  public void setPrev(Node<E> n){prev=n;}
 


@Override
public String toString() {
  return "Node("+ this.element+ ")";
}
  
  
  

}