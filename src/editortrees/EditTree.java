package editortrees;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import editortrees.Node.Code;

// A height-balanced binary tree with rank that could be the basis for a text editor.

public class EditTree {

	private Node root;
	public static final Node NULL_NODE = new Node();
	private int rotationCount = 0;
	private int size;

	/**
	 * Construct an empty tree
	 */
	public EditTree() {
		this.size = 0;
		this.root = NULL_NODE;
	}

	/**
	 * Construct a single-node tree whose element is c
	 * 
	 * @param c
	 */
	public EditTree(char c) {
		this.size = 1;
		this.root = new Node(c);
	}

	/**
	 * Create an EditTree whose toString is s. This can be done in O(N) time,
	 * where N is the length of the tree (repeatedly calling insert() would be
	 * O(N log N), so you need to find a more efficient way to do this.
	 * 
	 * @param s
	 */
	public EditTree(String s) {

	}

	/**
	 * Make this tree be a copy of e, with all new nodes, but the same shape and
	 * contents.
	 * 
	 * @param e
	 */
	public EditTree(EditTree e) {

	}

	/**
	 * 
	 * @return the height of this tree
	 */
	public int height() {
		return this.root.height();
	}
	
	public int debugHeight() {
		return this.root.debugHeight();
	}

	/**
	 * 
	 * returns the total number of rotations done in this tree since it was
	 * created. A double rotation counts as two.
	 *
	 * @return number of rotations since tree was created.
	 */
	public int totalRotationCount() {
		return this.rotationCount; // replace by a real calculation.
	}

	/**
	 * return the string produced by an inorder traversal of this tree
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("");
		Iterator<Node> itr = new InOrderNodeIterator();
		while (itr.hasNext()) {
			result.append(itr.next().element);
		}
		return result.toString();
	}

	/**
	 * This one asks for more info from each node. You can write it like the
	 * arraylist-based toString() method from the BST assignment. However, the
	 * output isn't just the elements, but the elements, ranks, and balance
	 * codes. Former CSSE230 students recommended that this method, while making
	 * it harder to pass tests initially, saves them time later since it catches
	 * weird errors that occur when you don't update ranks and balance codes
	 * correctly. For the tree with node b and children a and c, it should
	 * return the string: [b1=, a0=, c0=] There are many more examples in the
	 * unit tests.
	 * 
	 * @return The string of elements, ranks, and balance codes, given in a
	 *         pre-order traversal of the tree.
	 */
	public String toDebugString() {
		StringBuilder result = new StringBuilder("[");
		Iterator<Node> itr = new PreOrderNodeIterator();
		if (!itr.hasNext())
			return "[]";
		Node current = null;
		while (itr.hasNext()) {
			current = itr.next();
			result.append(current.element);
			result.append(current.rank);
			switch (current.balance) {
			case SAME:
				result.append('=');
				break;
			case RIGHT:
				result.append('\\');
				break;
			case LEFT:
				result.append('/');
				break;
			}
			result.append(", ");
		}
		String str = result.toString();
		return str.substring(0, str.length() - 2) + "]";
	}

	/**
	 * 
	 * @param pos
	 *            position in the tree
	 * @return the character at that position
	 * @throws IndexOutOfBoundsException
	 */
	public char get(int pos) throws IndexOutOfBoundsException {
		if (pos < 0 || pos >= size) {
			throw new IndexOutOfBoundsException();
		}
		return this.root.get(pos).element;
	}

	/**
	 * 
	 * @param c
	 *            character to add to the end of this tree.
	 */
	public void add(char c) {
		// Notes:
		// 1. Please document chunks of code as you go. Why are you doing what
		// you are doing? Comments written after the code is finalized tend to
		// be useless, since they just say WHAT the code does, line by line,
		// rather than WHY the code was written like that. Six months from now,
		// it's the reasoning behind doing what you did that will be valuable to
		// you!
		// 2. Unit tests are cumulative, and many things are based on add(), so
		// make sure that you get this one correct.
		this.size++;
		Node currentNode = this.root;
		if (this.root == NULL_NODE) {
			this.root = new Node(c);
			return;
		}
		while (currentNode.right != NULL_NODE) {
			currentNode = currentNode.right;
		}
		currentNode.right = new Node(c, currentNode);
		// TODO: Just call balance here on currentNode.right;
		balance(currentNode.right);
	}

	/**
	 * 
	 * @param c
	 *            character to add
	 * @param pos
	 *            character added in this inorder position
	 * @throws IndexOutOfBoundsException
	 *             id pos is negative or too large for this tree
	 */
	public void add(char c, int pos) throws IndexOutOfBoundsException {
		if (pos > this.size() || pos < 0) {
			throw new IndexOutOfBoundsException();
		}
		this. size++;
		if (this.root == NULL_NODE) {
			this.root = new Node(c);
			return;
		}
		
		balance(this.root.add(c, pos));
	}

	private Node singleLeftRotate(Node parent, Node child) {
		child.left.parent = parent;
		parent.right = child.left;
		child.left = parent;
		parent.balance = Code.SAME;
		child.balance = Code.SAME;
		// Figure out which child is parent of grandparent.
		if (this.root == parent) {
			this.root = child;
		} else {
			if (parent.isLeftChild()) {
				parent.parent.left = child;
			} else {
				parent.parent.right = child;
			}
		}
		child.parent = parent.parent;
		parent.parent = child;
		child.rank += parent.rank + 1;
		this.rotationCount++;
		return child;
	}

	private Node singleRightRotate(Node parent, Node child) {
		child.right.parent = parent;
		parent.left = child.right;
		child.right = parent;
		parent.balance = Code.SAME;
		child.balance = Code.SAME;
		// Figure out which child is parent of grandparent.
		if (this.root == parent) {
			this.root = child;
		} else {
			if (parent.isLeftChild()) {
				parent.parent.left = child;
			} else {
				parent.parent.right = child;
			}
		}
		child.parent = parent.parent;
		parent.parent = child;
		parent.rank -= (child.rank + 1);
		this.rotationCount++;
		return child;
	}

	public void balance(Node bottom) {
		Node currentNode = bottom;
		while (currentNode != this.root) {
			if (currentNode == currentNode.parent.right) {
				switch (currentNode.parent.balance) {
				case LEFT:
					currentNode.parent.balance = Code.SAME;
					return;

				case RIGHT:
					if (currentNode.balance == Code.RIGHT) {
						singleLeftRotate(currentNode.parent, currentNode);
						return;
					} else { // SAME will not be passed back. It is safe to
								// assume LEFT
						rightLeftRotate(currentNode.parent, currentNode, currentNode.left);
						return;
					}
				default: // SAME
					currentNode.parent.balance = Code.RIGHT;
					break;
				}
			} else {
				switch (currentNode.parent.balance) {
				case RIGHT:
					currentNode.parent.balance = Code.SAME;
					return;

				case LEFT:
					if (currentNode.balance == Code.LEFT) {
						singleRightRotate(currentNode.parent, currentNode);
						return;
					} else { // SAME will not be passed back. It is safe to
								// assume RIGHT
						leftRightRotate(currentNode.parent, currentNode, currentNode.right);
						return;
					}
				default: // SAME
					currentNode.parent.balance = Code.LEFT;
					break;
				}
			}
			currentNode = currentNode.parent;
		}
		return;
	}

	private void leftRightRotate(Node parent, Node currentNode, Node child) {// (A,
																				// C,
																				// B)
		Code childBalance = child.balance;
		child.right.parent = parent;
		parent.left = child.right;
		child.right = parent;
		child.left.parent = currentNode;
		currentNode.right = child.left;
		child.left = currentNode;
		if (this.root == parent) {
			this.root = child;
		} else {
			if (parent.isRightChild()) {
				parent.parent.right = child;
			} else {
				parent.parent.left = child;
			}
		}
		child.parent = parent.parent;
		parent.parent = child;
		currentNode.parent = child;
		child.balance = Code.SAME;
		if (childBalance == Code.LEFT) {
			currentNode.balance = Code.SAME;
			parent.balance = Code.RIGHT;
		} else if (childBalance == Code.RIGHT) {
			currentNode.balance = Code.LEFT;
			parent.balance = Code.SAME;
		} else {
			currentNode.balance = Code.SAME;
			parent.balance = Code.SAME;
		}
		parent.rank -= (currentNode.rank + 1 + child.rank + 1);
		child.rank += currentNode.rank + 1;
		this.rotationCount += 2;
	}

	private void rightLeftRotate(Node parent, Node currentNode, Node child) {
		Code childBalance = child.balance;
		child.left.parent = parent;
		parent.right = child.left;
		child.left = parent;
		child.right.parent = currentNode;
		currentNode.left = child.right;
		child.right = currentNode;
		if (this.root == parent) {
			this.root = child;
		} else {
			if (parent.isRightChild()) {
				parent.parent.right = child;
			} else {
				parent.parent.left = child;
			}
		}
		child.parent = parent.parent;
		parent.parent = child;
		currentNode.parent = child;
		child.balance = Code.SAME;
		if (childBalance == Code.RIGHT) {
			currentNode.balance = Code.SAME;
			parent.balance = Code.LEFT;
		} else if (childBalance == Code.LEFT) {
			currentNode.balance = Code.RIGHT;
			parent.balance = Code.SAME;
		} else {
			currentNode.balance = Code.SAME;
			parent.balance = Code.SAME;
		}
		currentNode.rank -= (child.rank + 1);
		child.rank += parent.rank + 1;
		this.rotationCount += 2;
	}

	/**
	 * 
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return this.size; // replace by a real calculation.
	}
	
	public int debugSize() {
		int result = 0;
		Node currentNode;
		for (Iterator<Node> iter = new InOrderNodeIterator(); iter.hasNext();) {
			currentNode = iter.next();
			result++;
		}
		return result;
	}

	/**
	 * 
	 * @param pos
	 *            position of character to delete from this tree
	 * @return the character that is deleted
	 * @throws IndexOutOfBoundsException
	 */
	public char delete(int pos) throws IndexOutOfBoundsException {
		// Implementation requirement:
		// When deleting a node with two children, you normally replace the
		// node to be deleted with either its in-order successor or predecessor.
		// The tests assume assume that you will replace it with the
		// *successor*.
		return '#'; // replace by a real calculation.
	}

	/**
	 * This method operates in O(length*log N), where N is the size of this
	 * tree.
	 * 
	 * @param pos
	 *            location of the beginning of the string to retrieve
	 * @param length
	 *            length of the string to retrieve
	 * @return string of length that starts in position pos
	 * @throws IndexOutOfBoundsException
	 *             unless both pos and pos+length-1 are legitimate indexes
	 *             within this tree.
	 */
	public String get(int pos, int length) throws IndexOutOfBoundsException {
		return "";
	}

	/**
	 * This method is provided for you, and should not need to be changed. If
	 * split() and concatenate() are O(log N) operations as required, delete
	 * should also be O(log N)
	 * 
	 * @param start
	 *            position of beginning of string to delete
	 * 
	 * @param length
	 *            length of string to delete
	 * @return an EditTree containing the deleted string
	 * @throws IndexOutOfBoundsException
	 *             unless both start and start+length-1 are in range for this
	 *             tree.
	 */
	public EditTree delete(int start, int length) throws IndexOutOfBoundsException {
		if (start < 0 || start + length >= this.size())
			throw new IndexOutOfBoundsException(
					(start < 0) ? "negative first argument to delete" : "delete range extends past end of string");
		EditTree t2 = this.split(start);
		EditTree t3 = t2.split(length);
		this.concatenate(t3);
		return t2;
	}

	/**
	 * Append (in time proportional to the log of the size of the larger tree)
	 * the contents of the other tree to this one. Other should be made empty
	 * after this operation.
	 * 
	 * @param other
	 * @throws IllegalArgumentException
	 *             if this == other
	 */
	public void concatenate(EditTree other) throws IllegalArgumentException {

	}

	/**
	 * This operation must be done in time proportional to the height of this
	 * tree.
	 * 
	 * @param pos
	 *            where to split this tree
	 * @return a new tree containing all of the elements of this tree whose
	 *         positions are >= position. Their nodes are removed from this
	 *         tree.
	 * @throws IndexOutOfBoundsException
	 */
	public EditTree split(int pos) throws IndexOutOfBoundsException {
		return null; // replace by a real calculation.
	}

	/**
	 * Don't worry if you can't do this one efficiently.
	 * 
	 * @param s
	 *            the string to look for
	 * @return the position in this tree of the first occurrence of s; -1 if s
	 *         does not occur
	 */
	public int find(String s) {
		return -2;
	}

	/**
	 * 
	 * @param s
	 *            the string to search for
	 * @param pos
	 *            the position in the tree to begin the search
	 * @return the position in this tree of the first occurrence of s that does
	 *         not occur before position pos; -1 if s does not occur
	 */
	public int find(String s, int pos) {
		return -2;
	}

	/**
	 * @return The root of this tree.
	 */
	public Node getRoot() {
		return this.root;
	}

	public class InOrderNodeIterator implements Iterator<Node> {
		Stack<Node> s;
		char current;

		public InOrderNodeIterator() {
			this.s = new Stack<Node>();
			this.s.push(root);
			if (!root.equals(NULL_NODE)) {
				while (!this.s.peek().left.equals(NULL_NODE)) {
					this.s.push(this.s.peek().left);
				}
			}

		}

		// public void remove() {
		// if (this.current == null) {
		// throw new IllegalStateException();
		// }
		// EditTree.this.remove(current);
		// this.current = null;
		// }

		@Override
		public boolean hasNext() {
			if (root.equals(NULL_NODE)) {
				return false;
			}
			return !this.s.isEmpty();
		}

		@Override
		public Node next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			Node temp = this.s.pop();
			if (!temp.right.equals(NULL_NODE)) {
				this.s.push(temp.right);
				while (!this.s.peek().left.equals(NULL_NODE)) {
					this.s.push(this.s.peek().left);
				}
			}
			this.current = temp.element;
			return temp;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}
	}

	public class PreOrderNodeIterator implements Iterator<Node> {
		Stack<Node> s;
		Character current;

		public PreOrderNodeIterator() {
			this.s = new Stack<Node>();
			this.s.push(root);

		}

		@Override
		public boolean hasNext() {
			if (root.equals(NULL_NODE)) {
				return false;
			}
			while ((!this.s.isEmpty()) && s.peek().equals(NULL_NODE)) {
				s.pop();
			}
			return !this.s.isEmpty();
		}

		@Override
		public Node next() {
			if (this.hasNext()) {
				Node temp = s.pop();
				if (temp != NULL_NODE) {
					this.s.push(temp.right);
					this.s.push(temp.left);
				}
				this.current = temp.element;
				return temp;
			}
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}

	}
}
