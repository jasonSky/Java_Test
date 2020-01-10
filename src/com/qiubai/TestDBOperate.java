package com.qiubai;

public class TestDBOperate{
	
	static DBOperator dbOperate = new DBOperator();
	
	public static void main(String args[]){
//		t_app_store app = (t_app_store)dbOperate.selectObjectFromDB(t_app_store.class, "id=465");
//		System.out.println("********" + app.id);
		
		dbOperate.creatObjectFromDB("content", true);
	}
	
}
