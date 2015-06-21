using System;

namespace TreeTest
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            var head = BuildTree();
            PrintTraverseInOrder(head);
        }

        
        public static TreeNode<int> BuildTree()
        {
            var head = new TreeNode<int>(5);
            head.Left = new TreeNode<int>(3);
            head.Left.Left = new TreeNode<int>(1);
            head.Left.Left.Right = new TreeNode<int>(2);
            head.Left.Right = new TreeNode<int>(4);

            head.Right = new TreeNode<int>(7);
            head.Right.Left = new TreeNode<int>(6);
            head.Right.Right = new TreeNode<int>(9);
            head.Right.Right.Left = new TreeNode<int>(8);
            head.Right.Right.Right = new TreeNode<int>(10);

            return head;
        }

        public static void PrintTraverseInOrder(TreeNode<int> head)
        {
            if (head == null)
                return;

            PrintTraverseInOrder(head.Left);
            Console.WriteLine(head.Item);
            PrintTraverseInOrder(head.Right);
        }
    }

    public class TreeNode<T>
    {
        public TreeNode(T item){Item = item;}
        public TreeNode<T> Left;
        public TreeNode<T> Right;

        public T Item;
    }
}
