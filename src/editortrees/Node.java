package editortrees;

import editortrees.Node.Code;

// A node in a height-balanced binary tree with rank.
// Except for the NULL_NODE (if you choose to use one), one node cannot
// belong to two different trees.

public class Node {

	enum Code {
		SAME, LEFT, RIGHT;
		// Used in the displayer and debug string
		public String toString() {
			switch (this) {
			case LEFT:
				return "/";
			case SAME:
				return "=";
			case RIGHT:
				return "\\";
			default:
				throw new IllegalStateException();
			}
		}
	}

	// The fields would normally be private, but for the purposes of this class,
	// we want to be able to test the results of the algorithms in addition to
	// the
	// "publicly visible" effects

	char element;
	Node left, right; // subtrees
	int rank; // inorder position of this node within its own subtree.
	Code balance;
	Node parent; // You may want this field.
	// Feel free to add other fields that you find useful

	// You will probably want to add several other methods
	public Node(char element) {
		this.element = element;
		this.balance = Code.SAME;
		this.parent = EditTree.NULL_NODE;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
	}

	public Node(char element, Node parent) {
		this.element = element;
		this.balance = Code.SAME;
		this.parent = parent;
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
	}

	// Null node constructor
	public Node() {
		this.left = EditTree.NULL_NODE;
		this.right = EditTree.NULL_NODE;
		this.rank = 0;
		this.balance = Code.SAME;
	}

	// For the following methods, you should fill in the details so that they
	// work correctly
	public int height() {
		if (this == EditTree.NULL_NODE)
			return -1;
		switch (this.balance) {
		case LEFT:
			return this.left.height() + 1;
		default:
			return this.right.height() + 1;
		}
	}

	public Node get(int pos) {
		if (pos < this.rank) {
			return this.left.get(pos);
		} else if (pos == this.rank) {
			return this;
		} else {
			return this.right.get(pos - this.rank - 1);
		}

	}

	/**
	 * Use rank to determine where to add the new node.
	 * 
	 * @param c
	 * @param pos
	 * @return The node inserted on the bottom
	 */
	public Node add(char c, int pos) {

		if (pos <= this.rank) {
			this.rank++;
			if (this.left == EditTree.NULL_NODE) {
				this.left = new Node(c, this);
				return this.left;
			} else {
				return this.left.add(c, pos);
			}
		} else {
			if (this.right == EditTree.NULL_NODE) {
				this.right = new Node(c, this);
				return this.right;
			} else {
				return this.right.add(c, pos - this.rank - 1);
			}
		}
	}
	
	public boolean isRight() {
		return this == this.parent.right;
	}

	public int size() {
		return 0;
	}

	public int debugHeight() {
		if (this == EditTree.NULL_NODE) {
			return -1;
		}
		return 1 + (int) Math.max(this.left.height(), this.right.height());
	}

	public boolean isLeftChild() {
		return this.parent.left.equals(this);
	}

	public boolean isRightChild() {
		return this.parent.right.equals(this);
	}
}