import org.junit.Assert;
import org.junit.Test;

public class TestWavlTree {
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

}
