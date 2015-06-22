using System;

namespace FibTest
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            for (int i=0; i<8; i++)
            {
                int fib = FibTest(i);
                Console.WriteLine(fib);
            }
        }
        
        public static int FibTest(int idx)
        {
            // 0, 1, 1, 2, 3, 5, 8, 13....
            if (idx < 2)
                return idx;
                
            int pre = 0;
            int cur = 1;
            int fib = 0;
            
            for(int i=1; i<idx; i++)
            {
                fib = pre + cur;
                pre = cur;
                cur = fib;
            }
            
            return fib;
        }
    }
}
