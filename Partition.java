// from assignment1


import java.util.Arrays;
import java.util.Collections;



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