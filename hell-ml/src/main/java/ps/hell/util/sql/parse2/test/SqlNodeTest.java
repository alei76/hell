package ps.hell.util.sql.parse2.test;



import java.util.ArrayList;

import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTable;
import ps.hell.util.sql.parse2.bean.LablesystemWarehouseDatabasesTableColumn;
import ps.hell.util.sql.parse2.main.SqlNode;

public class SqlNodeTest {

	public static void main(String[] args) {
	        String input="select * from ( select case when test(a.user_id) =0 then 1 when 1=2 then 2 else 3 end as e1,a.user_id as e3,e.user_id as e4,a.uid,sum(1) as c from  (select * from dwd.dwd_user_base_full ) as e,dwd.dwd_user_base_full as a left join dwd.dwd_test as b on a.id=b.id and b.status in ('a','b') where (b.user!=0 and b.user=1) or b.user!=2 group by a.user_id,a.uid order by a.user_id desc ,a.uid) as e2 left join (select user_id from dwd.dwd_user_base_full union all select xt.user_id from dwd.dwd_user_base_full as xt) as e3 on e2.e3 =e3.user_id where e2.uid=e3.user_id and e2.e4=e4.e5 having e2.e4>100 limit 100";
	        input="use dwd;select * from dwd_user_base_full as a left join (select user_id from dwd.dwd_user_base_full where user_id =concat('fs\\(user_id\\)dasd%',test(user_id)/case when user_id ='abc' then 'a' else user_id end,',') union all select xt.user_id from dwd.dwd_user_base_full as xt) as e3 on a.user_id=e3.user_id";
	        SqlNode nodes=new SqlNode(input);
	        ArrayList<LablesystemWarehouseDatabasesTable> ta=new ArrayList<LablesystemWarehouseDatabasesTable>();
	        ArrayList<LablesystemWarehouseDatabasesTableColumn> col=new ArrayList<LablesystemWarehouseDatabasesTableColumn>();
	        LablesystemWarehouseDatabasesTable tat=new LablesystemWarehouseDatabasesTable();
	        tat.setDatabaseId(1L);
	        tat.setDwdDatabase("dwd");
	        tat.setTableTransName("dwd_user_base_full");
	        tat.setId(1L);
	        ta.add(tat);
	        tat=new LablesystemWarehouseDatabasesTable();
	        tat.setDatabaseId(1L);
	        tat.setDwdDatabase("dwd");
	        tat.setTableTransName("dwd_test");
	        tat.setId(2L);
	        ta.add(tat);
	        LablesystemWarehouseDatabasesTableColumn colt=new LablesystemWarehouseDatabasesTableColumn();
	        colt.setId(1L);
	        colt.setDatabaseId(1L);
	        colt.setTableId(1L);
	        colt.setColumnNameMap("user_id");
	        col.add(colt);
	        colt=new LablesystemWarehouseDatabasesTableColumn();
	        colt.setId(2L);
	        colt.setDatabaseId(1L);
	        colt.setTableId(1L);
	        colt.setColumnNameMap("uid");
	        col.add(colt);
	        nodes.init(ta, col);
	        colt=new LablesystemWarehouseDatabasesTableColumn();
	        colt.setId(3L);
	        colt.setDatabaseId(1L);
	        colt.setTableId(1L);
	        colt.setColumnNameMap("id");
	        col.add(colt);
	        nodes.init(ta, col);
	        colt=new LablesystemWarehouseDatabasesTableColumn();
	        colt.setId(4L);
	        colt.setDatabaseId(1L);
	        colt.setTableId(2L);
	        colt.setColumnNameMap("id");
	        col.add(colt);
	        nodes.init(ta, col);
	        colt=new LablesystemWarehouseDatabasesTableColumn();
	        colt.setId(5L);
	        colt.setDatabaseId(1L);
	        colt.setTableId(2L);
	        colt.setColumnNameMap("user");
	        col.add(colt);
	        nodes.init(ta, col);
	        colt=new LablesystemWarehouseDatabasesTableColumn();
	        colt.setId(6L);
	        colt.setDatabaseId(1L);
	        colt.setTableId(2L);
	        colt.setColumnNameMap("status");
	        col.add(colt);
	        nodes.init(ta, col);
	        System.out.println("source");
	        System.out.println(nodes.toString());
	        System.out.println("transId");
	        nodes.change();
	        System.out.println(nodes.toTransIdString());
	        colt.setColumnNameMap("status调整");
	        nodes.update(colt);
	        LablesystemWarehouseDatabasesTableColumn znl=col.get(0);
	        znl.setColumnNameMap("修改");
	        nodes.update(znl);
	        System.out.println("trans");
	        System.out.println(nodes.toTransString());     
	}
	
}