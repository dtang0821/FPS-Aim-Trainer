import java.util.ArrayList;

public class Methods {
    public static void Methods(String[]args)
    {
       ArrayList<String>strArray = new ArrayList<>();
       strArray.size();
       strArray.add(3,"hi");
       strArray.get(3);
       strArray.set(3,"no");
       String sum="";
       for(int i=0;i<strArray.size();i++)
       {
           sum+=i;
           if(strArray.get(i).equals("no"))
           {
               strArray.remove(i);
               i++;
           }
       }
       String[][]string2DArray=new String[2][2];
       int[][]int2DArray={{1,2},{1,2}};

       int sumint2D=0;

        for(int r=0;r<int2DArray.length;r++) {
            for (int c = 0; c < int2DArray[r].length; c++)
            {
                sumint2D+=int2DArray[r][c];

            }
        }

        for(int c = 0; c < int2DArray[0].length; c++)
        {
            for(int r = 0; r < int2DArray.length; r++)
            {
                sumint2D+=int2DArray[r][c];
            }
        }

        for(int[] ints: int2DArray)
        {
            for(int i: ints)
            {
                System.out.println(i + " ");
            }
            System.out.println();
        }







    }
    public static void selectionSort(int[] elements)
    {
        int swaps = 0;
        int mins = 0;
        for (int j = 0; j < elements.length - 1; j++)
        {
            int minIndex = j;
            for (int k = j + 1; k < elements.length; k++)
            {
                if (elements[k] < elements[minIndex])
                {
                    minIndex = k;
                    mins++;
                }
            }
            if (j != minIndex)
            {
                int temp = elements[j];
                elements[j] = elements[minIndex];
                elements[minIndex] = temp;
                swaps++;
            }
        }
        System.out.println("Minima: " + mins + ", Swaps: " + swaps);
    }
    public static void insertionSort(int[] elements)
    {
        int decrements = 0;
        int insertions = 0;
        for (int j = 1; j < elements.length; j++)
        {
            int temp = elements[j];
            int possibleIndex = j;
            while (possibleIndex > 0 && temp < elements[possibleIndex - 1])
            {
                elements[possibleIndex] = elements[possibleIndex - 1];
                possibleIndex--;
                decrements++;
            }
            elements[possibleIndex] = temp;
            insertions++;
        }
        System.out.println("Decrements: " + decrements + ", Insertions: " + insertions);
    }
}
