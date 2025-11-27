public class Node<E>{
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