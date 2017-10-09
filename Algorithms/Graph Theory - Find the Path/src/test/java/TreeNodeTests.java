import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// this is needed for @Mock annotations to work
// alternatively you can do MockitoAnnotations.initMocks(this); in setUp()
// see https://static.javadoc.io/org.mockito/mockito-core/2.7.6/org/mockito/Mockito.html#9
@RunWith(MockitoJUnitRunner.class)
public class TreeNodeTests {
	
	@Mock
	TreeVis<Object> visitor;
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFromTo() {
		new TreeNode<>(1, 1);
	}
	
	// 1
	@Test
	public void test1Number() {
		TreeNode<Object> root = new TreeNode<>(1, 2);
		assertNotNull(root);
		TreeNode<Object> left = root.getLeft();
		TreeNode<Object> right = root.getRight();
		assertNull(right);
		assertNull(left);
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, root, root);
		assertTrue(lca.equals(root));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called only once and with argument equal root
		verify(visitor, times(1)).visitTreeNode(Mockito.eq(root));
	}

	//   2
	//  /
	// 1
	@Test
	public void test2Numbers() {
		TreeNode<Object> root = new TreeNode<>(1, 3);
		TreeNode<Object> left = root.getLeft();
		TreeNode<Object> right = root.getRight();
		assertNull(right);
		assertNotNull(left);
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, left, root);
		assertTrue(lca.equals(root));

		lca = TreeNode.<Object>lowestCommonAncestor(root, root, left);
		assertTrue(lca.equals(root));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called in particular order
		InOrder inOrder = Mockito.inOrder(visitor);
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 2 && tn.getFrom() == 1 && tn.getTo() == 3));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 1 && tn.getFrom() == 1 && tn.getTo() == 2));
	}
	
	//   2
	//  / \
	// 1   3
	@Test
	public void test3Numbers() {
		TreeNode<Object> root = new TreeNode<>(1, 4);
		TreeNode<Object> left = root.getLeft();
		TreeNode<Object> right = root.getRight();
		
		assertEquals(1, left.getData());
		assertEquals(2, root.getData());
		assertEquals(3, right.getData());
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, left, root);
		assertTrue(lca.equals(root));

		lca = TreeNode.<Object>lowestCommonAncestor(root, left, right);
		assertTrue(lca.equals(root));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called in particular order
		InOrder inOrder = Mockito.inOrder(visitor);
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 2 && tn.getFrom() == 1 && tn.getTo() == 4));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 1 && tn.getFrom() == 1 && tn.getTo() == 2));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 3 && tn.getFrom() == 3 && tn.getTo() == 4));
	}
	
	//     3
	//    / \
	//   2   4
	//  / 
	// 1
	@Test
	public void test4Numbers() {
		TreeNode<Object> root = new TreeNode<>(1, 5);
		TreeNode<Object> left = root.getLeft();
		TreeNode<Object> right = root.getRight();
		
		assertEquals(1, left.getLeft().getData());
		assertEquals(2, left.getData());
		assertNull(left.getRight());
		
		assertEquals(3, root.getData());
		
		assertEquals(4, right.getData());
		assertNull(right.getLeft());
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, left.getLeft(), root);
		assertTrue(lca.equals(root));

		lca = TreeNode.<Object>lowestCommonAncestor(root, left.getLeft(), right);
		assertTrue(lca.equals(root));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called in particular order
		InOrder inOrder = Mockito.inOrder(visitor);
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 3));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 2));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 1));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 4));
	}
	
	//     3
	//    / \
	//   2   5
	//  /   /
	// 1   4
	@Test
	public void test5Numbers() {
		TreeNode<Object> root = new TreeNode<>(1, 6);
		TreeNode<Object> left = root.getLeft();
		TreeNode<Object> right = root.getRight();

		assertEquals(3, root.getData());

		assertEquals(1, left.getLeft().getData());
		assertEquals(2, left.getData());
		assertNull(left.getRight());
		
		assertEquals(5, right.getData());
		assertEquals(4, right.getLeft().getData());
		assertNull(right.getRight());
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, left.getLeft(), root);
		assertTrue(lca.equals(root));

		lca = TreeNode.<Object>lowestCommonAncestor(root, left.getLeft(), right.getLeft());
		assertTrue(lca.equals(root));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called in particular order
		InOrder inOrder = Mockito.inOrder(visitor);
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 3));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 2));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 1));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 5));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 4));
	}

	//        5
	//       / \
	//      /   \
	//     3     7
	//    / \   / \
	//   2   4 6   8
	//  /   
	// 1  
	@Test
	public void test8Numbers() {
		TreeNode<Object> root = new TreeNode<>(1, 9);
		TreeNode<Object> n5 = root; 
		TreeNode<Object> n3 = root.getLeft();
		TreeNode<Object> n7 = root.getRight();
		TreeNode<Object> n4 = root.getLeft().getRight();
		TreeNode<Object> n1 = root.getLeft().getLeft().getLeft();
		TreeNode<Object> n6 = root.getRight().getLeft();
		TreeNode<Object> n8 = n7.getRight();
		assertEquals(5, n5.getData());
		assertEquals(3, n3.getData());
		assertEquals(7, n7.getData());
		assertEquals(4, n4.getData());
		assertNull(root.getLeft().getLeft().getRight());
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, n1, n4);
		assertTrue(lca.equals(n3));

		lca = TreeNode.<Object>lowestCommonAncestor(root, n6, n1);
		assertTrue(lca.equals(root));
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 10, 9);
		assertTrue(lca.equals(n8));
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 8, 2);
		assertTrue(lca.equals(root));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called in particular order
		InOrder inOrder = Mockito.inOrder(visitor);
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 5));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 3));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 2));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 1));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 4));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 7));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 6));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 8));
	}
	
	//        5
	//       / \
	//      /   \
	//     3     7
	@Test
	public void test8NumbersMinRange3() {
		TreeNode<Object> root = new TreeNode<>(1, 9, 3);
		TreeNode<Object> n5 = root; 
		TreeNode<Object> n3 = root.getLeft();
		TreeNode<Object> n7 = root.getRight();
		assertEquals(5, n5.getData());
		assertEquals(3, n3.getData());
		assertEquals(7, n7.getData());
		assertNull(n3.getLeft());
		assertNull(n3.getRight());
		assertNull(n7.getLeft());
		assertNull(n7.getRight());
		
		TreeNode<Object> lca = TreeNode.<Object>lowestCommonAncestor(root, 10, 9);
		assertEquals(n7, lca);
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 4, 1);
		assertTrue(lca.equals(n3));
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 1, 2);
		assertTrue(lca.equals(n3));
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 6, 8);
		assertTrue(lca.equals(n7));
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 3, 2);
		assertTrue(lca.equals(n3));
		
		lca = TreeNode.<Object>lowestCommonAncestor(root, 2, 6);
		assertTrue(lca.equals(n5));
		
		root.traversePreOrder(visitor);
		// verify that visitTreeNode() was called in particular order
		InOrder inOrder = Mockito.inOrder(visitor);
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 5));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 3));
		inOrder.verify(visitor, times(1)).visitTreeNode(Mockito.argThat(tn -> tn.getData() == 7));
	}

	

}
