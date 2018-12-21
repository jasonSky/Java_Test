package com.invoke.test;

public class CompleteNum {

		public static void main(String args[]){
			for(int i=2;i<1000;i++){
				int sum=0;
				for(int y=1;y<i;y++){
					if(i%y==0)
						sum +=y;
				}
				
				if(sum==i)
					System.out.println("sum: " + sum);
			}
		}
	
}
