package com.company;
import java.io.*;
public class TestClass {
    /* arr[]  ---> Input Array
        data[] ---> Temporary array to store current combination
        start & end ---> Staring and Ending indexes in arr[]
        index  ---> Current index in data[]
        r ---> Size of a combination to be printed */
    static void combinationUtil(int arr[], int length_arr, int r, int index, int data[], int i) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            for (int j=0; j<r; j++)
                System.out.print(data[j]+" ");
            System.out.println("");
            return;
        }

        // When no more elements are there to put in data[]
        if (i >= length_arr)
            return;

        // current is included, put next at next location
        data[index] = arr[i];
        combinationUtil(arr, length_arr, r, index+1, data, i+1);

        // current is excluded, replace it with next (Note that
        // i+1 is passed, but index is not changed)
        combinationUtil(arr, length_arr, r, index, data, i+1);
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    static void printCombination(int arr[], int length, int r) {
        // A temporary array to store all combination one by one
        int data[]=new int[r];

        // Print all combination using temprary array 'data[]'
        combinationUtil(arr, length, r, 0, data, 0);
    }

    /*Driver function to check for above function*/
    public static void main (String[] args) {
        int arr[] = {1, 2, 3, 4, 5};
        int r = 3;
        printCombination(arr, arr.length , r);
    }
}


    /* This code is contributed by Devesh Agrawal */

