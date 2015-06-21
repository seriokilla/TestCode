using System;

namespace ParseTest
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            var d = Convert("12345.67890");
            Console.WriteLine(d);
        }

        public static decimal Convert(string str)
        {
            decimal ret = 0M;
            int order = 0;
            bool isLeft = true;
            foreach(var s in str)
            {
                if (s == '.')
                {
                    isLeft = false;
                    order = 1;
                    continue;
                }
                    
                if (isLeft)
                {
                    ret = ret * (decimal)10;
                    ret += ((int)s-(int)'0'); 
                }
                else
                {
                    ret += ((int)s-(int)'0')/((decimal)Math.Pow(10, order++));
                }
           }
           return ret;
        }
    }
}
