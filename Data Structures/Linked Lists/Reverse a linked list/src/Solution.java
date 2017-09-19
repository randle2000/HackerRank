/*
  Reverse a linked list and return pointer to the head
  The input list will have at least one element  
  Node is defined as  
  class Node {
     int data;
     Node next;
  }
*/
    // This is a "method-only" submission. 
    // You only need to complete this method. 
Node Reverse(Node head) {
    Node currNode = head, nextNode = null, prevNode = null;
    
    while(currNode!=null){
         nextNode = currNode.next;
         currNode.next = prevNode;
         prevNode = currNode;
         currNode = nextNode;
    }
    
    return prevNode;
}