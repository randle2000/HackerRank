/* Hidden stub code will pass a root argument to the function below. Complete the function to solve the challenge. Hint: you may want to write one or more helper functions.  

The Node class is defined as follows:
    class Node {
        int data;
        Node left;
        Node right;
     }
*/
    void traverseInOrder(Node node, List<Integer> arr) {
        if (node.left != null)
            traverseInOrder(node.left, arr);
        
        arr.add(node.data);
        
        if (node.right != null)
            traverseInOrder(node.right, arr);
    }

    boolean checkBST(Node root) {
        
        List<Integer> arr = new ArrayList<>();
        
        traverseInOrder(root, arr);
        
        Set<Integer> sortedSet = new TreeSet<>(arr);
        
        return arr.equals(new ArrayList<Integer>(sortedSet));
    }
