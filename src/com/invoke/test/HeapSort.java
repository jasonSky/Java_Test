package com.invoke.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class HeapSort {

	//http://blog.sina.com.cn/s/blog_54f82cc20100zuho.html
	public static int[] buildHead(int[] arr, int size){//二叉树去半进行处理
		for(int i=(size-1)/2;i>=1;i--){
			arr = headAdjust(arr, i, size);
		}
		return arr;
	}
	
	//左右子节点 与 顶点对比 大的往上移动 - 建立大顶堆
	public static int[] headAdjust(int[] arr, int i, int size){
		int lch = 2 * i;
		int rch = 2 * i +1;
		int max = i;//数组位置
		if(i<=(size-1)/2){
			if(lch<=size-1 && arr[lch] > arr[max]){
				max = lch;
			}
			if(rch<=size-1 && arr[rch] > arr[max]){
				max = rch;
			}
			
			if(max != i){
				//交换值
				int temp = arr[max];
				arr[max] = arr[i];
				arr[i] = temp; //大顶
				
				headAdjust(arr, max, size);//子树调整
			}
		}
		
		return arr;
	}
	
	public static void main(String args[]){
//		int[] i = new int[]{0, 16, 7, 3, 20, 17, 8, 21};
//		//初始一个大顶
//		i = buildHead(i, i.length);
//		for(int j=i.length-1;j>=2;j--){
//			int temp = i[j];//将大的顶点往后置换
//			i[j] = i[1];
//			i[1] = temp;
//			
//			headAdjust(i, 1, j);//排除置换的值, 继续进行大顶堆, 数组长度控制
//		}
//		
//		System.out.println(i);
		List<String> lz = new ArrayList<String>(){{ add("qwe"); } };
		Gson gson = new Gson();
		System.out.println(gson.toJson(lz, List.class));
		List<String> lz2 = new ArrayList<String>();
		lz2.add("qwe");
		System.out.println(gson.toJson(lz2, List.class));
	}
	
}
