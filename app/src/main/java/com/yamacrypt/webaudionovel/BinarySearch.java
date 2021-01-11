package com.yamacrypt.webaudionovel;

public class BinarySearch {
    Integer[] a;
    public BinarySearch(Integer[] res){
        this.a=res;
    }
    public  boolean binary_search(int x) {
        int pos = -1;
        int lower = 0; // 下限
        int upper = a.length - 1; // 上限
        while (lower <= upper) {
            int mid = (lower + upper) / 2;
            if (a[mid] == x) {
                pos = mid;
                break;
            } else if (a[mid] < x) {
                lower = mid + 1;
            } else {
                upper = mid - 1;
            }
        }
        if (pos < 0) {
           return false;
        } else {
           return true;
        }
    }
}
