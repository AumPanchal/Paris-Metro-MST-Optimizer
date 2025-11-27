
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
