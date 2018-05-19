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
    private ActionsCount actionsCount;

    public WAVLTree() {
        this.EXTERNAL_NODE = new WAVLNode(-1, "OUT_NODE");
        this.EXTERNAL_NODE.rank = -1;
        this.EXTERNAL_NODE.subTreeSize = 0;

        this.root = null;
        this.minNode = null;
        this.maxNode = null;
        this.actionsCount = new ActionsCount();

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
        this.actionsCount.clear();
        WAVLNode newNode = new WAVLNode(k, i);

        if (empty()) {
            setRoot(newNode);
            this.maxNode = getRoot();
            this.minNode = getRoot();
            return 0;
        }

        WAVLNode closestNode = getClosestNode(k);
        if (closestNode.getKey() == k) {
            return -1;
        } else if (closestNode.getKey() > k) {
            closestNode.setLeft(newNode);
        } else {
            closestNode.setRight(newNode);
        }

        setSpecialNodes(newNode);
        insertBalanceTree(newNode);
        newNode.updateSubTreeSizeUp();

        return this.actionsCount.getCount();
    }

    private void setSpecialNodes(WAVLNode newNode) {
        if (this.maxNode == null) {
            this.maxNode = newNode;
            this.minNode = newNode;
        } else {
            if (newNode.getKey() > this.maxNode.getKey()) {
                this.maxNode = newNode;
            }
            if (newNode.getKey() < this.minNode.getKey()) {
                this.minNode = newNode;
            }
        }
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
        actionsCount.clear();
        WAVLNode nodeToDelete = getClosestNode(k);

        if (nodeToDelete == null || nodeToDelete.key != k) {
            return -1;
        }

        if (nodeToDelete == this.maxNode) {
            this.maxNode = nodeToDelete.getPredecessor();
        }
        if (nodeToDelete == this.minNode) {
            this.minNode = nodeToDelete.getSuccessor();
        }

        boolean notLeafOrUnary = !(nodeToDelete.isLeaf() || nodeToDelete.isUnary());
        if (notLeafOrUnary) {
            replaceWithSuccessor(nodeToDelete);
        }

        if (nodeToDelete.isLeaf()) {
            if (getRoot() == nodeToDelete) {
                this.setRoot(null);
                return 0;
            }

            WAVLNode parent = nodeToDelete.getParent();

            if (parent.getRightRankDiff() == 1 && parent.getLeftRankDiff() == 1) {
                removeLeaf(parent, nodeToDelete);
                parent.updateSubTreeSizeUp();
                return 0;
            }

            WAVLNode otherChild = getOtherChild(parent, nodeToDelete);
            if (otherChild == EXTERNAL_NODE) {
                removeLeaf(parent, nodeToDelete);
                parent.setRank(parent.getRank() - 1);
                parent.calculateSize();

                deleteBalanceTree(parent.getParent());

                return actionsCount.getCount();
            }

            removeLeaf(parent, nodeToDelete);
            deleteBalanceTree(parent);

            return actionsCount.getCount();
        }

        // If reached here then `nodeToDelete` is unary
        WAVLNode child = nodeToDelete.getRight() == EXTERNAL_NODE ? nodeToDelete.getLeft() : nodeToDelete.getRight();
        WAVLNode parent = nodeToDelete.getParent();

        if (getRoot() == nodeToDelete) {
            setRoot(child);
            return 0;
        }

        if (parent.getRank() - nodeToDelete.getRank() == 1) {
            replaceUnaryNode(parent, nodeToDelete, child);
            parent.updateSubTreeSizeUp();
            return 0;
        }

        replaceUnaryNode(parent, nodeToDelete, child);
        deleteBalanceTree(parent);
        return actionsCount.getCount();

    }

    private void replaceWithSuccessor(WAVLNode nodeToDelete) {
        WAVLNode successor = nodeToDelete.getSuccessor();

        WAVLNode successorParent = successor.getParent();
        WAVLNode successorChild = successor.getRight() == EXTERNAL_NODE ? successor.getLeft() : successor.getRight();
        if (successor.isLeaf()) {
            replaceUnaryNode(successorParent, successor, EXTERNAL_NODE);
        } else {
            replaceUnaryNode(successorParent, successor, successorChild);
        }

        if (getRoot() == nodeToDelete) {
            setRoot(successor);
        } else {
            replaceUnaryNode(nodeToDelete.getParent(), nodeToDelete, successor);
        }

        successor.rank = nodeToDelete.getRank(); // TODO: should increase action count?
        successor.setRight(nodeToDelete.getRight());
        successor.setLeft(nodeToDelete.getLeft());

        if (successorChild != EXTERNAL_NODE) {
            replaceUnaryNode(successorChild, EXTERNAL_NODE, nodeToDelete);
            successorChild.rank = successorChild.getRank() + 1; // TODO: should increase action count?
        } else {
            if (successorParent == nodeToDelete) {
                replaceUnaryNode(successor, EXTERNAL_NODE, nodeToDelete);
            } else {
                replaceUnaryNode(successorParent, EXTERNAL_NODE, nodeToDelete);
            }
        }
        nodeToDelete.setLeft(EXTERNAL_NODE);
        nodeToDelete.setRight(EXTERNAL_NODE);
    }

    private void setRoot(WAVLNode node) {
        this.root = node;
        if (node != null) {
            node.setParent(null);
        }
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
        return this.getRoot() == null ? 0 : this.getRoot().getSubtreeSize();
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
        WAVLNode node;
        if (i <= this.getRoot().getLeft().getSubtreeSize()) {
            node = this.minNode;
            while (node.getSubtreeSize() < i) {
                node = node.getParent();
            }
        } else {
            node = this.getRoot();
        }
        node = selectHelp(node, i);
        //for (int j = 1; j < i; j++) {
        //    node = node.getSuccessor();
        //    }
        return node.getValue(); //TODO: make O(logk)
    }

    public WAVLNode selectHelp(WAVLNode x, int i) {
        int r = x.getLeft().getSubtreeSize();
        if (i - 1 == r) {
            return x;
        } else if (i - 1 < r) {
            return selectHelp(x.getLeft(), i);
        } else {
            return selectHelp(x.getRight(), i - r - 1);
        }

    }

    /**
     * @return The location of the node or the location it should be inserted in
     */
    private WAVLNode getClosestNode(int key) {
        if (empty()) {
            return null;
        }

        if (key <= minNode.getKey()) {
            return minNode;
        }
        if (key >= maxNode.getKey()) {
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

    private void deleteBalanceTree(WAVLNode node) {
        if (isLegalState(node)) {
            if (node != null) {
                node.updateSubTreeSizeUp();
            }
            return;
        }

        if (isSingleDemoteState(node)) {
            node.setRank(node.getRank() - 1);
            node.calculateSize();
            deleteBalanceTree(node.getParent());
            return;
        }

        WAVLNode otherChild = node.getLeftRankDiff() == 3 ? node.getRight() : node.getLeft();
        boolean isCase2 = otherChild.getRightRankDiff() == 2 && otherChild.getLeftRankDiff() == 2;
        if (isCase2) {
            otherChild.setRank(otherChild.getRank() - 1);
            node.setRank(node.getRank() - 1);
            deleteBalanceTree(node.getParent());

            node.updateSubTreeSizeUp();
            return;
        }

        if (node.getRight() == otherChild) {
            boolean isCase4 = (otherChild.getRightRankDiff() == 2);
            if (isCase4) {
                deleteDoubleRotate(node, otherChild);
            } else {
                // Case 3
                deleteSingleRotate(node, otherChild);
            }
        } else {
            boolean isCase4 = (otherChild.getLeftRankDiff() == 2);
            if (isCase4) {
                deleteDoubleRotate(node, otherChild);
            } else {
                // Case 3
                deleteSingleRotate(node, otherChild);
            }
        }

        node.updateSubTreeSizeUp();
    }

    private void deleteSingleRotate(WAVLNode node, WAVLNode otherChild) {
        singleRotate(node, otherChild);

        otherChild.setRank(otherChild.getRank() + 1);

        if (node.isLeaf() && node.getRightRankDiff() == 3 && node.getLeftRankDiff() == 3) {
            node.setRank(node.getRank() - 2);
        } else {
            node.setRank(node.getRank() - 1);
        }
    }

    private void deleteDoubleRotate(WAVLNode node, WAVLNode otherChild) {
        WAVLNode grandChild = doubleRotate(node, otherChild);
        node.setRank(node.getRank() - 2);
        otherChild.setRank(otherChild.getRank() - 1);
        grandChild.setRank(grandChild.getRank() + 2);
    }

    private void insertBalanceTree(WAVLNode node) {
        boolean isLegalInsertState = this.getRoot() == node || node.getRank() < node.getParent().getRank();
        if (isLegalInsertState) {
            node.updateSubTreeSizeUp();
            return;
        }

        WAVLNode parent = node.getParent();

        boolean isPromoteState = Math.abs(parent.getRightRankDiff() - parent.getLeftRankDiff()) == 1;
        if (isPromoteState) {
            parent.setRank(parent.getRank() + 1);
            insertBalanceTree(parent);
            return;
        }

        insertRotate(parent, node);
    }

    private boolean isLegalState(WAVLNode node) {
        if (empty() || node == null) {
            return true;
        }

        int leftChildDiff = node.getLeftRankDiff();
        int rightChildDiff = node.getRightRankDiff();

        return leftChildDiff > 0 && rightChildDiff > 0 && leftChildDiff < 3 & rightChildDiff < 3;
    }

    private boolean isSingleDemoteState(WAVLNode node) {
        int leftChildDiff = node.getLeftRankDiff();
        int rightChildDiff = node.getRightRankDiff();

        return (leftChildDiff == 3 && rightChildDiff == 2) ||
                (leftChildDiff == 2 && rightChildDiff == 3);
    }

    private void insertRotate(WAVLNode parent, WAVLNode node) {
        boolean isRightChild = parent.getRight() == node;

        if (isRightChild) {
            if (node.getLeftRankDiff() == 2) {
                insertSingleRotate(parent, node);
            } else {
                insertDoubleRotate(parent, node);
            }
        } else {
            if (node.getRightRankDiff() == 2) {
                insertSingleRotate(parent, node);
            } else {
                insertDoubleRotate(parent, node);
            }
        }
    }

    private void insertDoubleRotate(WAVLNode grandParent, WAVLNode parent) {
        WAVLNode child = doubleRotate(grandParent, parent);

        child.setRank(child.getRank() + 1);
        parent.setRank(parent.getRank() - 1);
        grandParent.setRank(grandParent.getRank() - 1);

        parent.updateSubTreeSizeUp();
    }

    private void insertSingleRotate(WAVLNode parent, WAVLNode node) {
        singleRotate(parent, node);
        parent.setRank(parent.getRank() - 1);

        node.updateSubTreeSizeUp();
    }

    private void singleRotate(WAVLNode parent, WAVLNode node) {
        actionsCount.addAction();

        if (getRoot() == parent) {
            setRoot(node);
        }

        WAVLNode grandParent = parent.getParent();
        if (grandParent == null) {
            node.setParent(null);
        } else {
            if (grandParent.getRight() == parent) {
                grandParent.setRight(node);
            } else {
                grandParent.setLeft(node);
            }
        }

        WAVLNode child;
        if (parent.getRight() == node) {
            child = node.getLeft();
            node.setLeft(parent);
            parent.setRight(child);
        } else {
            child = node.getRight();
            node.setRight(parent);
            parent.setLeft(child);
        }

        parent.calculateSize();
        node.calculateSize();
    }

    private WAVLNode doubleRotate(WAVLNode grandParent, WAVLNode parent) {
        if (grandParent.getRight() == parent) {
            WAVLNode child = parent.getLeft();
            singleRotate(parent, child);
            singleRotate(grandParent, grandParent.getRight());
            return child;
        } else {
            WAVLNode child = parent.getRight();
            singleRotate(parent, child);
            singleRotate(grandParent, grandParent.getLeft());
            return child;
        }
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
        }

        public void setLeft(WAVLNode left) {
            this.left = left;
            left.setParent(this);
        }

        public void setRank(int rank) {
            this.rank = rank;
            actionsCount.addAction();
        }

        public int getRank() {
            return rank;
        }

        public int getRightRankDiff() {
            return this.rank - this.getRight().rank;
        }

        public int getLeftRankDiff() {
            return this.rank - this.getLeft().rank;
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

        public boolean isLeaf() {
            return getLeft() == EXTERNAL_NODE && getRight() == EXTERNAL_NODE;
        }

        public boolean isUnary() {
            return !isLeaf() && (getLeft() == EXTERNAL_NODE || getRight() == EXTERNAL_NODE);
        }


        /**
         * Gets the successor
         *
         * @return The successor of the node
         */
        private WAVLNode getSuccessor() {
            WAVLNode node = this;
            if (maxNode == node) {
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
         * @return The predecessor of this node
         */
        private WAVLNode getPredecessor() {
            WAVLNode node = this;
            if (minNode == node) {
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


        private void calculateSize() {
            if (this != EXTERNAL_NODE) {
                this.subTreeSize = this.getRight().getSubtreeSize() + this.getLeft().getSubtreeSize() + 1;
            }
        }

        public void updateSubTreeSizeUp() {
            WAVLNode next = this;
            while (next != null) {
                next.calculateSize();
                next = next.getParent();
            }
        }

    }

    public class ActionsCount {
        private int count = 0;

        public void clear() {
            this.count = 0;
        }

        public void addAction() {
            ++this.count;
        }

        public int getCount() {
            return this.count;
        }
    }

}
