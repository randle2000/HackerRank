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
    
    Node tmp = new Node();

    Node cur = head;
    while (cur != null) {
		Node node = cur;
        cur = cur.next;
		node.next = tmp.next;
		tmp.next = node;
	}

	return tmp.next;

}