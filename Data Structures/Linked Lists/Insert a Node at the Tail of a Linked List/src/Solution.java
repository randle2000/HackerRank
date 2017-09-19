/*
  Insert Node at the end of a linked list 
  head pointer input could be NULL as well for empty list
  Node is defined as 
  class Node {
     int data;
     Node next;
  }
*/
Node Insert(Node head,int data) {
// This is a "method-only" submission. 
// You only need to complete this method. 
    Node node = new Node();
    node.next = null;
    node.data = data;
    
    Node cur = head;
    while (cur.next != null)
        cur = cur.next;
    cur.next = node;
    return head;
}