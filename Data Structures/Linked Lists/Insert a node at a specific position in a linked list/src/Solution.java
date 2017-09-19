/*
  Insert Node at a given position in a linked list 
  head can be NULL 
  First element in the linked list is at position 0
  Node is defined as 
  class Node {
     int data;
     Node next;
  }
*/
    

Node InsertNth(Node head, int data, int position) {
   // This is a "method-only" submission. 
    // You only need to complete this method. 
    
    Node node = new Node();
    node.data = data;

    if (head == null || position == 0) {
        node.next = head;
        return node;
    }

    Node cur = head;
    while (--position > 0) 
        cur = cur.next;
    
    node.next = cur.next;
    cur.next = node;
    
    return head;
}