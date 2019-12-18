package cn.mikylin.myths.cache.map.tree;

import cn.mikylin.myths.cache.map.CacheEntry;

import java.util.*;

/**
 * 二叉搜索树
 *
 * @author mikylin
 * @date 20191211
 */
public class BinarySearchTree<K,V> extends TreeAbstractMap<K,V> {

    /**
     * 查找数据
     */
    @Override
    public V get(Object key) {

        int keyHash = getKeyHash((K)key);

        Node<K,V> n;
        try {
            lock.readLock().lock();
            n = find(keyHash);
        } finally {
            lock.readLock().unlock();
        }

        if(n == null)
            return null;
        return n.value;
    }

    /**
     * 插入数据
     */
    @Override
    public V put(K key, V value) {
        int keyHash = getKeyHash(key);
        Node<K,V> n = new Node<>(key,keyHash,value);

        try {
            lock.writeLock().lock();
            insertOrUpdate(n);
            return value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 删除数据
     */
    @Override
    public V remove(Object key) {
        int keyHash = getKeyHash((K)key);

        try {
            lock.writeLock().lock();
            return delete(keyHash);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        List<CacheEntry<K, V>> entries = preOrder();
        List<V> l = new ArrayList<>(entries.size());
        entries.forEach(e -> l.add(e.getValue()));
        return l;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new HashSet<>(preOrder());
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }


    /**
     * 删除节点
     */
    private V delete(int keyHash) {

        Node<K,V> n = find(keyHash);

        if(n == null)
            return null;


        Node<K,V> leaf = findTheBiggestNodeInLeft(n);
        if(leaf == null)
            leaf = n;

        n.keyHash = leaf.keyHash;
        n.value = leaf.value;

        deleteLeafOrSingle(leaf);

        return n.value;
    }

    /**
     * 将节点插入到树中
     * 如果已存在同样的 key，就会覆盖 value
     */
    private void insertOrUpdate(Node<K,V> newNode) {

        Node<K,V> n = root;
        if(n == null) {
            root = newNode;
            size ++;
            return;
        }

        // 循环遍历二叉树
        for(;n != null;) {

            int newKeyHash = newNode.keyHash;
            int keyHash = n.keyHash;

            // 新节点的 keyHash 和当前节点的 keyHash 相同
            // 此处做更新操作
            if(newKeyHash == keyHash) {
                n.value = newNode.value;
                return;
            }

            // 新节点的 keyHash 大于当前节点的 keyHash
            // 开始遍历节点的右边部分
            else if(newKeyHash > keyHash) {
                if(n.rightChild == null) {
                    n.rightChild = newNode;
                    newNode.parent = n;
                    size ++;
                    return;
                } else {
                    n = n.rightChild;
                    continue;
                }
            }
            // 新节点的 keyHash 小于当前节点的 keyHash
            // 开始遍历节点的左边部分
            else {
                if(n.leftChild == null) {
                    n.leftChild = newNode;
                    newNode.parent = n;
                    size ++;
                    return;
                } else {
                    n = n.leftChild;
                    continue;
                }
            }
        }
    }



    /**
     * 在树中查找节点
     */
    private Node<K,V> find(int keyHash) {
        Node<K,V> n = root;
        for(;n != null;) {
            int nKeyHash = n.keyHash;
            if(keyHash == nKeyHash)
                return n;
            else if(keyHash > nKeyHash)
                n = n.rightChild;
            else if(keyHash < nKeyHash)
                n = n.leftChild;
        }

        return null;
    }




    /**
     * 搜索某个节点的左节点中最大的那个节点
     */
    private Node<K,V> findTheBiggestNodeInLeft(Node<K,V> begin) {

        if(begin == null)
            throw new RuntimeException();

        // 沿着节点的右子节点的左边去遍历
        // 寻找比 begin 节点小的节点中最大的那个值
        for(Node<K,V> n = begin.leftChild;n != null;) {
            if(n.rightChild != null) n = n.rightChild;
            else return n;
        }
        return null;
    }

    /**
     * 删除叶节点或者单分支节点
     */
    private void deleteLeafOrSingle(Node<K,V> leaf) {

        if(leaf == null)
            throw new RuntimeException();

        Node<K,V> left = leaf.leftChild;
        Node<K,V> right = leaf.rightChild;

        // 叶子节点或者单叶节点必须有一个子节点为空
        if(left != null && right != null)
            throw new RuntimeException();

        // 如果要删除的是 root 节点，那么此处需要特殊处理
        // 正常情况来说此时 leaf 应该是 root 节点下左端最大值，也可能是 root 本身
        // 所以此处一般情况下是 leaf 的右节点是空的，左节点不为空
        // 所以 left 可能为：1 左右节点均为空 2 右节点不为空
        if(leaf == root) {

            // 左节点不为空，这是最多的情况
            if(left != null) {
                root = left;
                root.parent = null;
            }
            // 左右节点都为空，这也是可能的情况
            else if(left == null && right == null) {
                root = null;
            }
            // 右节点不为空，不会存在这两种情况
            else
                throw new RuntimeException();

            size --;
            return;
        }

        // 非 root 节点的情况下一定会有 parent 节点
        Node<K,V> parent = leaf.parent;

        // 此处判断要删除的节点是不是 parent 的右节点
        boolean isRight = parent.rightChild.keyHash == leaf.keyHash;

        // 如果是叶节点，且该节点是父节点的右节点，那么就脱开右节点，反之脱开左节点
        if(left == null && right == null) {
            if(isRight) parent.rightChild = null;
            else parent.leftChild = null;
        }
        // 如果是单枝节点，那么要把它的子节点嫁接到 parent
        else if(left == null) {
            if(isRight) parent.rightChild = right;
            else parent.leftChild = right;
        }


        else if(right == null) {
            if(isRight) parent.rightChild = left;
            else parent.leftChild = left;
        }

        size --;
    }


    /**
     * 前序遍历 - 中左右顺序
     */
    public List<CacheEntry<K,V>> preOrder() {
        Node<K,V> root = this.root;
        List<CacheEntry<K,V>> l = new ArrayList<>();
        preOrder(root,l);
        return l;
    }
    private void preOrder(Node<K,V> begin,List<CacheEntry<K,V>> l) {

        if(begin == null) return;

        l.add(factory.build(begin.key,begin.value));
        preOrder(begin.leftChild,l);
        preOrder(begin.rightChild,l);
    }

    /**
     * 中序遍历 - 左中右 顺序
     */
    public List<CacheEntry<K,V>> inOrder() {
        Node<K,V> root = this.root;
        List<CacheEntry<K,V>> l = new ArrayList<>();
        inOrder(root,l);
        return l;
    }
    private void inOrder(Node<K,V> begin,List<CacheEntry<K,V>> l) {

        if(begin == null) return;

        inOrder(begin.leftChild,l);
        l.add(factory.build(begin.key,begin.value));
        inOrder(begin.rightChild,l);
    }

    /**
     * 后序遍历 - 左右中 顺序
     */
    public List<CacheEntry<K,V>> postOrder() {
        Node<K,V> root = this.root;
        List<CacheEntry<K,V>> l = new ArrayList<>();
        postOrder(root,l);
        return l;
    }
    private void postOrder(Node<K,V> begin,List<CacheEntry<K,V>> l) {

        if(begin == null) return;

        postOrder(begin.leftChild,l);
        postOrder(begin.rightChild,l);
        l.add(factory.build(begin.key,begin.value));
    }


    public static void main(String[] args) {
        BinarySearchTree<Integer,String> t = new BinarySearchTree<>();
        t.put(3,"a");
        t.put(2,"b");
        t.put(3,"c");
        t.put(4,"d");
        t.remove(Integer.valueOf(1));
        t.remove(Integer.valueOf(2));

        System.out.println(t.get(Integer.valueOf(1)));
        System.out.println(t.get(Integer.valueOf(2)));
        System.out.println(t.get(Integer.valueOf(3)));
        System.out.println(t.get(Integer.valueOf(4)));
    }

}
