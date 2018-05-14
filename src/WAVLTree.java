/**
 * WAVLTree
 * <p>
 * An implementation of a WAVL Tree.
 * (Haupler, Sen & Tarajan ‘15)
 */


public class WAVLTree {

    private WAVLNode EXTERNAL_NODE = null;
    private WAVLNode root;
    private WAVLNode minNode;
    private WAVLNode maxNode;
    private int treeSize = 0;


    public WAVLTree() {
        this.EXTERNAL_NODE = new WAVLNode(-1, "OUT_NODE");
        this.EXTERNAL_NODE.rank = -1;
        this.EXTERNAL_NODE.subTreeSize = 0;

        this.root = null;
        this.minNode = null;
        this.maxNode = null;
        this.treeSize = 0;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.getRoot() == null;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        if (empty()) {
            return null;
        }

        if (k < minNode.getKey() || k > maxNode.getKey()) {
            return null;
        }

        WAVLNode closestNode = getClosestNode(k);
        if (closestNode.getKey() == k) {
            return closestNode.getValue();
        } else {
            return null;
        }

    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the WAVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        ++this.treeSize;
        WAVLNode newNode = new WAVLNode(k, i);
        if (empty()) {
            this.setRoot(newNode);
            this.maxNode = this.getRoot();
            this.minNode = this.getRoot();
            return 0;
        }

        WAVLNode closestNode = getClosestNode(k);

        if (closestNode.getKey() == k) {
            --this.treeSize;
            return -1;
        } else if (closestNode.getKey() > k) {
            closestNode.setLeft(newNode);
        } else {
            closestNode.setRight(newNode);
        }

        if (this.maxNode == null) {
            this.maxNode = newNode;
            this.minNode = newNode;
        } else {
            if (k > this.maxNode.getKey()) {
                this.maxNode = newNode;
            }
            if (k < this.minNode.getKey()) {
                this.minNode = newNode;
            }
        }

        return insertBalanceTree(closestNode);
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        WAVLNode nodeToDelete = getClosestNode(k);

        if (nodeToDelete == null || nodeToDelete.key != k) {
            return -1;
        }

        if (nodeToDelete == this.maxNode) {
            this.maxNode = this.getPredecessor(nodeToDelete);
        }
        if (nodeToDelete == this.minNode) {
            this.minNode = this.getSuccessor(nodeToDelete);
        }

        --this.treeSize;

        if (!isLeaf(nodeToDelete) && !isUnary(nodeToDelete)) {
            WAVLNode successor = getSuccessor(nodeToDelete);

            WAVLNode parent = successor.getParent();
            WAVLNode child = successor.getRight() == EXTERNAL_NODE ? successor.getLeft() : successor.getRight();
            if (isLeaf(successor)) {
                replaceUnaryNode(parent, successor, EXTERNAL_NODE);
            } else {
                replaceUnaryNode(parent, successor, child);
            }

            successor.rank = nodeToDelete.getRank();
            successor.setParent(nodeToDelete.getParent());
            successor.setRight(nodeToDelete.getRight());
            successor.setLeft(nodeToDelete.getLeft());

            if (child != EXTERNAL_NODE) {
                replaceUnaryNode(child, EXTERNAL_NODE, nodeToDelete);
            } else {
                replaceUnaryNode(parent, EXTERNAL_NODE, nodeToDelete);
            }
            nodeToDelete.setLeft(EXTERNAL_NODE);
            nodeToDelete.setRight(EXTERNAL_NODE);

        }

        if (isLeaf(nodeToDelete)) {
            WAVLNode parent = nodeToDelete.getParent();

            if (parent == null) {
                this.setRoot(null);
                return 0;
            }

            if (parent.getRank() - parent.getRight().getRank() == 1 && parent.getRank() - parent.getLeft().getRank() == 1) {
                removeLeaf(parent, nodeToDelete);
                return 0;
            }

            WAVLNode otherChild = getOtherChild(parent, nodeToDelete);
            if (otherChild == EXTERNAL_NODE) {
                removeLeaf(parent, nodeToDelete);
                --parent.rank;
                return deleteBalanceTree(parent);
            }

            removeLeaf(parent, nodeToDelete);

            if (!isLeaf(otherChild)) {
                return singleRotate(parent, otherChild);
            }

            return deleteBalanceTree(parent);
        } else if (isUnary(nodeToDelete)) {
            WAVLNode child = nodeToDelete.getRight() == EXTERNAL_NODE ? nodeToDelete.getLeft() : nodeToDelete.getRight();
            WAVLNode parent = nodeToDelete.getParent();

            if (parent == null) {
                setRoot(child);
                return 0;
            }

            if (parent.getRank() - nodeToDelete.getRank() == 1) {
                replaceUnaryNode(parent, nodeToDelete, child);
                return 0;
            }

            replaceUnaryNode(parent, nodeToDelete, child);
            return deleteBalanceTree(parent);
        }

        // Unreachable
        throw new RuntimeException();
    }

    private void setRoot(WAVLNode node) {
        this.root = node;
        if (node != null) {
            node.setParent(null);
        }
    }


    /**
     * Gets the successor
     *
     * @param node The node
     * @return The successor of the node
     */
    private WAVLNode getSuccessor(WAVLNode node) {
        if (node == this.maxNode) {
            return null;
        }

        if (node.getRight() != EXTERNAL_NODE) {
            node = node.getRight();
            while (node.getLeft() != EXTERNAL_NODE) {
                node = node.getLeft();
            }
        } else {
            while (node.getParent() != null && node.getParent().getRight() == node) {
                node = node.getParent();
            }

            if (node.getParent() == null) {
                return null;
            } else {
                node = node.getParent();
            }
        }
        return node;
    }

    /**
     * Gets the predecessor
     *
     * @param node The node
     * @return The predecessor of the node
     */
    private WAVLNode getPredecessor(WAVLNode node) {
        if (node == this.minNode) {
            return null;
        }

        if (node.getLeft() != EXTERNAL_NODE) {
            node = node.getLeft();
            while (node.getRight() != EXTERNAL_NODE) {
                node = node.getRight();
            }
        } else {
            while (node.getParent() != null && node.getParent().getLeft() == node) {
                node = node.getParent();
            }

            if (node.getParent() == null) {
                return null;
            } else {
                node = node.getParent();
            }
        }

        return node;
    }


    private void replaceUnaryNode(WAVLNode parent, WAVLNode nodeToRemove, WAVLNode node) {
        if (parent.getLeft() == nodeToRemove) {
            parent.setLeft(node);
        } else {
            parent.setRight(node);
        }
    }

    private void removeLeaf(WAVLNode parent, WAVLNode nodeToDelete) {
        if (parent.getLeft() == nodeToDelete) {
            parent.setLeft(EXTERNAL_NODE);
        } else {
            parent.setRight(EXTERNAL_NODE);
        }
    }

    private WAVLNode getOtherChild(WAVLNode parent, WAVLNode node) {
        if (parent.getRight() == node) {
            return parent.getLeft();
        } else {
            return parent.getRight();
        }
    }

    private boolean isUnary(WAVLNode node) {
        return !isLeaf(node) && (node.getLeft() == EXTERNAL_NODE || node.getRight() == EXTERNAL_NODE);
    }

    private boolean isLeaf(WAVLNode node) {
        return node.getLeft() == EXTERNAL_NODE && node.getRight() == EXTERNAL_NODE;
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (this.minNode == null) {
            return null;
        }
        return this.minNode.getValue();
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (this.maxNode == null) {
            return null;
        }
        return this.maxNode.getValue(); // to be replaced by student code
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        if (empty()) {
            return new int[0];
        }

        int[] arr = new int[this.size()];
        inOrderKey(this.getRoot(), arr, 0);
        return arr;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        if (empty()) {
            return new String[0];
        }

        String[] arr = new String[this.size()];
        inOrderInfo(this.getRoot(), arr, 0);
        return arr;
    }

    private int inOrderKey(WAVLNode node, int[] arr, int index) {
        if (node.getLeft() != EXTERNAL_NODE) {
            index = inOrderKey(node.getLeft(), arr, index);
        }
        arr[index] = node.getKey();
        index++;
        if (node.getRight() != EXTERNAL_NODE) {
            index = inOrderKey(node.getRight(), arr, index);
        }
        return index;
    }

    private int inOrderInfo(WAVLNode node, String[] arr, int index) {
        if (node.getLeft() != EXTERNAL_NODE) {
            index = inOrderInfo(node.getLeft(), arr, index);
        }
        arr[index] = node.getValue();
        index++;
        if (node.getRight() != EXTERNAL_NODE) {
            index = inOrderInfo(node.getRight(), arr, index);
        }
        return index;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     */
    public int size() {
        return this.treeSize;
    }

    /**
     * public WAVLNode getRoot()
     * <p>
     * Returns the root WAVL node, or null if the tree is empty
     */
    public WAVLNode getRoot() {
        return this.root;
    }

    /**
     * public int select(int i)
     * <p>
     * Returns the value of the i'th smallest key (return -1 if tree is empty)
     * Example 1: select(1) returns the value of the node with minimal key
     * Example 2: select(size()) returns the value of the node with maximal key
     * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor
     */
    public String select(int i) {
        if (empty()) {
            return null;
        }

        if (i < 0 || i > size()) {
            return null;
        }

        WAVLNode node = this.minNode;
        for (int j = 1; j < i; j++) {
            node = getSuccessor(node);
        }
        return node.getValue();
    }

    /**
     * @return The location of the node or the location it should be inserted in
     */
    private WAVLNode getClosestNode(int key) {
        if (empty()) {
            return null;
        }

        if (minNode.getKey() >= key) {
            return minNode;
        }
        if (maxNode.getKey() <= key) {
            return maxNode;
        }

        WAVLNode next = this.getRoot();
        while (true) {
            if (next.getKey() == key) {
                return next;
            }

            if (key > next.getKey()) {
                if (next.getRight() == EXTERNAL_NODE) {
                    return next;
                }

                next = next.getRight();
            } else {
                if (next.getLeft() == EXTERNAL_NODE) {
                    return next;
                }
                next = next.getLeft();
            }
        }
    }

    private int deleteBalanceTree(WAVLNode node) {
        if (isLegalState(node)) {
            return 0;
        }

        if (isSingleDemoteState(node)) {
            --node.rank;
            node.calculateSize();
            return deleteBalanceTree(node.getParent()) + 1;
        }
        WAVLNode otherChild = getOtherChild(node.getParent(), node);
        if (otherChild.getRank() - otherChild.getRight().getRank() == 2 && otherChild.getRank() - otherChild.getLeft().getRank() == 2) {
            --otherChild.rank;
            --node.getParent().rank;
            return deleteBalanceTree(node.getParent().getParent()) + 2;
        }

        if (node.getParent().getRight() == otherChild) {
            if (otherChild.getRank() - otherChild.getRight().getRank() == 2) {
                return doubleRotate(node.getParent(), otherChild);
            }

            return singleRotate(node.getParent(), otherChild);
        } else {
            if (otherChild.getRank() - otherChild.getLeft().getRank() == 2) {
                return doubleRotate(node.getParent(), otherChild);
            }

            return singleRotate(node.getParent(), otherChild);
        }
    }

    private int insertBalanceTree(WAVLNode node) {
        if (isLegalState(node)) {
            return 0;
        }
        if (isPromoteState(node)) {
            ++node.rank;
            node.calculateSize();
            return insertBalanceTree(node.getParent()) + 1;
        }

        return insertRotate(node);
    }

    private boolean isLegalState(WAVLNode node) {
        if (empty() || node == null) {
            return true;
        }

        int leftChildDiff = node.getRank() - node.getLeft().getRank();
        int rightChildDiff = node.getRank() - node.getRight().getRank();

        return leftChildDiff > 0 && rightChildDiff > 0 && leftChildDiff < 3 & rightChildDiff < 3;
    }

    private boolean isSingleDemoteState(WAVLNode node) {
        int leftChildDiff = node.getRank() - node.getLeft().getRank();
        int rightChildDiff = node.getRank() - node.getRight().getRank();

        return (leftChildDiff == 3 && rightChildDiff == 2) ||
                (leftChildDiff == 2 && rightChildDiff == 3);
    }

    private boolean isPromoteState(WAVLNode node) {
        int leftChildDiff = node.getRank() - node.getLeft().getRank();
        int rightChildDiff = node.getRank() - node.getRight().getRank();

        return (leftChildDiff == 0 && rightChildDiff == 1) ||
                (leftChildDiff == 1 && rightChildDiff == 0);
    }

    private int insertRotate(WAVLNode node) {
        int rightChildDiff = node.getRank() - node.getRight().getRank();

        if (rightChildDiff == 0) {
            if (node.getRight().getRank() - node.getRight().getLeft().getRank() == 2) {
                return singleRotate(node, node.getRight());
            } else {
                return doubleRotate(node, node.getRight());
            }
        } else {
            if (node.getLeft().getRank() - node.getLeft().getRight().getRank() == 2) {
                return singleRotate(node, node.getLeft());
            } else {
                return doubleRotate(node, node.getLeft());
            }
        }
    }


    private int singleRotate(WAVLNode parent, WAVLNode child) {
        int actionCount = 1;
        WAVLNode grandParent = parent.getParent();
        if (grandParent == null) {
            child.setParent(null);
        } else {
            if (grandParent.getRight() == parent) {
                grandParent.setRight(child);
            } else {
                grandParent.setLeft(child);
            }
            ++child.rank;
            ++actionCount;
        }

        WAVLNode grandChild;
        if (parent.getRight() == child) {
            grandChild = child.getLeft();
            child.setLeft(parent);
            parent.setRight(grandChild);
        } else {
            grandChild = child.getRight();
            child.setRight(parent);
            parent.setLeft(grandChild);
        }
        --parent.rank;
        ++actionCount;

        grandChild.calculateSize();
        parent.calculateSize();
        child.calculateSize();

        return actionCount;
    }


    private int doubleRotate(WAVLNode grandParent, WAVLNode parent) {
        int actionCount = 0;
        if (grandParent.getRight() == parent) {
            actionCount += singleRotate(parent, parent.getLeft());
            actionCount += singleRotate(grandParent, grandParent.getRight());
        } else {
            actionCount += singleRotate(parent, parent.getRight());
            actionCount += singleRotate(grandParent, grandParent.getLeft());
        }
        return actionCount;
    }

    /**
     * public class WAVLNode
     */
    public class WAVLNode {
        private int key;
        private int rank;
        private int subTreeSize;
        private String value;
        private WAVLNode left;
        private WAVLNode right;
        private WAVLNode parent;

        public WAVLNode(int key, String value) {
            this.key = key;
            this.value = value;
            this.rank = 0;
            this.left = EXTERNAL_NODE;
            this.right = EXTERNAL_NODE;
            this.parent = null;
            this.subTreeSize = 1;
        }

        public void setParent(WAVLNode parent) {
            this.parent = parent;
            if (parent == null) {
                root = this;
            }
        }

        public void setRight(WAVLNode right) {
            this.right = right;
            right.setParent(this);
            calculateSize();
        }

        public void setLeft(WAVLNode left) {
            this.left = left;
            left.setParent(this);
            calculateSize();
        }

        public int getRank() {
            return rank;
        }

        public WAVLNode getParent() {
            return parent;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public WAVLNode getLeft() {
            return left;
        }

        public WAVLNode getRight() {
            return right;
        }

        public boolean isInnerNode() {
            return this != EXTERNAL_NODE;
        }

        public int getSubtreeSize() {
            return this.subTreeSize;
        }

        private void calculateSize() {
            if (this != EXTERNAL_NODE) {
                this.subTreeSize = this.getRight().getSubtreeSize() + this.getLeft().getSubtreeSize() + 1;
            }
        }
    }
}
