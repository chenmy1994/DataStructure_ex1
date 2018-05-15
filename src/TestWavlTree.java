import org.junit.Assert;
import org.junit.Test;

public class TestWavlTree {
    private int assertNodeSize(WAVLTree.WAVLNode node) {
        if (node.getRank() == -1) {
            return 0;
        }

        int size = 1 + assertNodeSize(node.getRight()) + assertNodeSize(node.getLeft());
        Assert.assertEquals(node.getSubtreeSize(), size);
        return size;
    }

    @Test
    public void test1() {
        WAVLTree tree = new WAVLTree();
        tree.insert(1217, "a");
        tree.insert(1716, "b");
        tree.insert(1012, "c");
        Assert.assertArrayEquals(new int[]{1012, 1217, 1716}, tree.keysToArray());
        Assert.assertArrayEquals(new String[]{"c", "a", "b"}, tree.infoToArray());
    }

    @Test
    public void test2() {
        WAVLTree tree = new WAVLTree();
        tree.insert(3588, "a");
        tree.insert(3588, "b");
        tree.insert(3844, "c");
        tree.insert(124, "d");
        Assert.assertEquals("a", tree.select(2));
    }

    @Test
    public void test3() {
        WAVLTree tree = new WAVLTree();
        tree.insert(609, "a");
        tree.insert(1400, "b");
        tree.insert(544, "c");
        tree.delete(1400);
        tree.insert(2652, "c");
        tree.delete(609);
    }

    @Test
    public void test4() {
        WAVLTree tree = new WAVLTree();
        tree.delete(306);
        tree.delete(454);
        tree.insert(1996, "b");
        tree.insert(414, "c");
        tree.insert(1672, "c");
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(3, tree.getRoot().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getRight().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getLeft().getSubtreeSize());
    }

    @Test
    public void test5() {
        WAVLTree tree = new WAVLTree();
        tree.insert(939, "a");
        tree.insert(2476, "b");
        tree.insert(2535, "c");
        Assert.assertEquals(3, tree.size());
        Assert.assertEquals(1, tree.getRoot().getRank());
        Assert.assertEquals(0, tree.getRoot().getRight().getRank());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRank());
    }

    @Test
    public void test6() {
        WAVLTree tree = new WAVLTree();
        tree.insert(1134, "a");
        tree.insert(345, "b");
        tree.insert(463, "c");
        tree.delete(345);
        tree.delete(463);
        Assert.assertEquals(1, tree.size());
    }

    @Test
    public void test7() {
        WAVLTree tree = new WAVLTree();
        tree.delete(5311);
        tree.insert(4695, "a");
        tree.delete(4695);
        Assert.assertEquals(0, tree.size());
        Assert.assertEquals(null, tree.getRoot());
    }

    @Test
    public void test8() {
        WAVLTree tree = new WAVLTree();
        tree.insert(2584, "a");
        tree.delete(3198);
        Assert.assertEquals(1, tree.size());
    }

    @Test
    public void test9() {
        WAVLTree tree = new WAVLTree();
        tree.delete(473);
        tree.insert(293, "a");
        tree.insert(1049, "b");
        tree.insert(1071, "c");
        tree.insert(2956, "d");
        Assert.assertEquals(4, tree.getRoot().getSubtreeSize());
        Assert.assertEquals(2, tree.getRoot().getRight().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getLeft().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getSubtreeSize());
    }

    @Test
    public void test10() {
        WAVLTree tree = new WAVLTree();
        tree.delete(6054);
        tree.insert(872, "a");
        tree.insert(5458, "b");
        tree.insert(2065, "c");
        tree.insert(4511, "d");
        tree.delete(5458);
        tree.insert(872, "e");
        tree.delete(4511);
        Assert.assertEquals(2, tree.getRoot().getSubtreeSize());
        Assert.assertEquals(2, tree.size());
    }

    @Test
    public void test11() {
        WAVLTree tree = new WAVLTree();
        tree.insert(3090, "a");
        tree.insert(242, "b");
        tree.insert(5349, "c");
        tree.insert(4780, "d");
        tree.delete(242);
        Assert.assertEquals(3, tree.getRoot().getSubtreeSize());
        Assert.assertEquals(3, tree.size());
    }

    @Test
    public void test12() {
        WAVLTree tree = new WAVLTree();
        tree.insert(3721, "a");
        tree.insert(7729, "b");
        tree.insert(5779, "c");
        tree.insert(1829, "d");
        tree.delete(5779);
        Assert.assertEquals(3, tree.getRoot().getSubtreeSize());
        Assert.assertEquals(3, tree.size());
    }

    @Test
    public void test13() {
        WAVLTree tree = new WAVLTree();
        tree.delete(2573);
        tree.insert(7464, "a");
        tree.insert(5094, "b");
        tree.insert(5508, "c");
        tree.insert(7464, "d");
        tree.insert(5798, "e");
        tree.delete(5508);
        tree.delete(5094);
    }

    @Test
    public void test14() {
        WAVLTree tree = new WAVLTree();
        tree.insert(5628, "a");
        tree.delete(5628);
        Assert.assertTrue(tree.empty());
    }

    @Test
    public void test15() {
        WAVLTree tree = new WAVLTree();
        tree.insert(1234, "a");
        tree.insert(1466, "b");
        tree.insert(1435, "c");
        tree.delete(1234);
        tree.delete(1435);
        tree.delete(1466);
        Assert.assertTrue(tree.empty());
    }

    @Test
    public void test16() {
        WAVLTree tree = new WAVLTree();
        tree.insert(5366, "a");
        tree.insert(2197, "b");
        tree.delete(1363);
        tree.insert(3414, "c");
        tree.insert(5835, "d");
        tree.insert(4805, "e");

        Assert.assertEquals(2, tree.getRoot().getRank());
        Assert.assertEquals(1, tree.getRoot().getRight().getRank());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRank());
        Assert.assertEquals(0, tree.getRoot().getRight().getRight().getRank());
        Assert.assertEquals(0, tree.getRoot().getRight().getLeft().getRank());

        Assert.assertEquals(5, tree.getRoot().getSubtreeSize());
        Assert.assertEquals(3, tree.getRoot().getRight().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getLeft().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getRight().getRight().getSubtreeSize());
        Assert.assertEquals(1, tree.getRoot().getRight().getLeft().getSubtreeSize());
    }

    @Test
    public void test17() {
        WAVLTree tree = new WAVLTree();
        tree.delete(4601);
        tree.insert(4048, "a");
        tree.delete(4048);
        tree.insert(2469, "b");
        tree.insert(2676, "c");
        tree.insert(788, "d");
        tree.delete(1453);
        tree.insert(2657, "e");
        tree.delete(1429);
        tree.insert(4628, "f");
        tree.insert(4905, "g");
        tree.insert(2538, "h");
        tree.insert(4078, "i");
        tree.insert(3454, "l");
        tree.insert(1063, "l");
        tree.delete(4905);
        tree.insert(298, "");
        tree.insert(661, "");
        tree.insert(1950, "");
        tree.delete(2469);

        assertNodeSize(tree.getRoot());
    }

    @Test
    public void test18() {
        WAVLTree tree = new WAVLTree();
        tree.insert(439, "");
        tree.delete(439);
        tree.insert(3153, "");
        tree.delete(3153);
        tree.insert(1662, "");
        tree.delete(1662);
        tree.insert(4245, "");
        tree.insert(957, "");
        tree.insert(3951, "");
        tree.delete(957);
        tree.insert(4849, "");
        tree.insert(983, "");
        tree.insert(3296, "");

        assertNodeSize(tree.getRoot());
        Assert.assertEquals(2, tree.getRoot().getRank());
        Assert.assertEquals(0, tree.getRoot().getRight().getRank());
        Assert.assertEquals(1, tree.getRoot().getLeft().getRank());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRight().getRank());
        Assert.assertEquals(0, tree.getRoot().getLeft().getLeft().getRank());
    }

    @Test
    public void test19() {
        WAVLTree tree = new WAVLTree();
        tree.insert(466, "");
        tree.insert(2689, "");
        tree.delete(466);
        tree.insert(706, "");
        tree.delete(2689);
        tree.delete(706);
        tree.insert(840, "");
        tree.insert(1057, "");
        tree.insert(2345, "");
        tree.insert(5398, "");
        tree.delete(2345);
        tree.insert(4753, "");
        tree.delete(5398);
        tree.insert(5036, "");
        tree.delete(1057);

        assertNodeSize(tree.getRoot());
        Assert.assertEquals(2, tree.getRoot().getRank());
        Assert.assertEquals(0, tree.getRoot().getRight().getRank());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRank());
    }

    @Test
    public void test20() {
        WAVLTree tree = new WAVLTree();
        tree.insert(6924, "");
        tree.delete(6924);
        tree.insert(6959, "");
        tree.insert(4889, "");
        tree.insert(8332, "");
        tree.insert(6879, "");
        System.out.println(WAVLTreePrinter.toString(tree));
        tree.delete(6959);

        System.out.println(WAVLTreePrinter.toString(tree));

        assertNodeSize(tree.getRoot());
        Assert.assertEquals(2, tree.getRoot().getRank());
        Assert.assertEquals(0, tree.getRoot().getRight().getRank());
        Assert.assertEquals(0, tree.getRoot().getLeft().getRank());
    }

}
