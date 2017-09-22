/*
Detect a cycle in a linked list. Note that the head pointer may be 'null' if the list is empty.

A Node is defined as: 
    class Node {
        int data;
        Node next;
    }
*/

boolean hasCycle(Node head) {
	List<Node> list = new LinkedList<>();
	while (head != null) {
		if (list.contains(head))
			return true;
		list.add(head);
		head = head.next;
	}
	return false;
}
