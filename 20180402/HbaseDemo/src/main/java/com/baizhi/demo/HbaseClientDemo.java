package com.baizhi.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */
public class HbaseClientDemo {
    private Admin admin;//DDL操作
    private Connection conn;//DML操作
    @Before
    public void before() throws IOException {
        Configuration conf= HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","CentOS");
        conn= ConnectionFactory.createConnection(conf);
        admin=conn.getAdmin();
    }
    @Test
    public void testCreateNamespace() throws IOException {
        NamespaceDescriptor nd= NamespaceDescriptor.create("zpark")
                .addConfiguration("author","zhangsan")
                .build();
        admin.createNamespace(nd);
    }
    @Test
    public void testModifyNamespace() throws IOException {
        NamespaceDescriptor nd= NamespaceDescriptor.create("zpark")
                .removeConfiguration("author")
                .addConfiguration("create","2018-04-20")
                .build();
        admin.modifyNamespace(nd);
    }
    @Test
    public void testListNamespace() throws IOException {
        NamespaceDescriptor[] nds = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor nd : nds) {
            System.out.println(nd.getName());
        }
    }

    /**
     * 只可以删除 为空的namespace
     * @throws IOException
     */
    @Test
    public void testDropNamespace() throws IOException {
        admin.deleteNamespace("baizhi");
    }
    @Test
    public void testList() throws IOException {
        TableName[] tableNames = admin.listTableNames();
        for (TableName tableName : tableNames) {
            System.out.println(new String(tableName.getName()));
        }
    }
    @Test
    public void testListNamespaceTables() throws IOException {
        HTableDescriptor[] tbs = admin.listTableDescriptorsByNamespace("baizhi");
        for (HTableDescriptor tb : tbs) {
            System.out.println(tb.getNameAsString());
        }
    }
   // create 'zpark:t_user',{NAME=>'cf1',VERSIONS=>3},{NAME=>'cf2',VERSIONS=>6,TTL=>60}
   @Test
    public void testCreateTable() throws IOException {
        TableName tname=TableName.valueOf("zpark:t_user");
        HTableDescriptor td=new HTableDescriptor(tname);

        //构建列簇
        HColumnDescriptor cf1=new HColumnDescriptor("cf1");
        cf1.setMaxVersions(3);

        HColumnDescriptor cf2=new HColumnDescriptor("cf2");
        cf2.setMaxVersions(6);
        cf2.setTimeToLive(60);//存活1分钟

        //添加列簇
        td.addFamily(cf1);
        td.addFamily(cf2);

        admin.createTable(td);
    }
    @Test
    public void testSave() throws IOException {
        TableName tname=TableName.valueOf("zpark:t_user");
        Table table = conn.getTable(tname);

        List<Put> puts=new ArrayList<Put>();
        for(int i=0;i<10;i++){
            Put put=new Put(Bytes.toBytes(i+""));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("name"),Bytes.toBytes("张三"));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("age"),Bytes.toBytes("18"));
            put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("sex"),Bytes.toBytes("男"));
            puts.add(put);
        }
        table.put(puts);
    }
    @Test
    public void testUpdate() throws IOException {
        TableName tname=TableName.valueOf("zpark:t_user");
        Table table = conn.getTable(tname);
        Put put=new Put(Bytes.toBytes("2"));
        put.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("name"),Bytes.toBytes("zhangxiaosan"));
        table.put(put);
    }
    @Test
    public void testDelete() throws IOException {
        TableName tname=TableName.valueOf("zpark:t_user");
        Table table = conn.getTable(tname);

        Delete delete=new Delete(Bytes.toBytes("1"));
        delete.addColumn(Bytes.toBytes("cf1"),Bytes.toBytes("sex"));
        table.delete(delete);
    }

    @Test
    public void testGet() throws IOException {
        TableName tname=TableName.valueOf("zpark:t_user");
        Table table = conn.getTable(tname);

        Get get=new Get(Bytes.toBytes("2"));
        get.setMaxVersions(3);
        //get.setTimeStamp(1524189546635L);
        Result result = table.get(get);// 包含若干个column
        showSpecifiedCell(result,"name");

    }
    @Test
    public void testScan() throws IOException {
        TableName tname=TableName.valueOf("zpark:t_user");
        Table table = conn.getTable(tname);

        Scan scan=new Scan();
        //scan.addFamily("cf1".getBytes());
        //scan.setMaxVersions(3);
        //scan.setStartRow("2".getBytes());
        //scan.setStopRow("5".getBytes());
        //scan.setReversed(true);
        //age sex name
        Filter filter= new PrefixFilter("0".getBytes());
                //new ColumnRangeFilter("b".getBytes(),true,"z".getBytes(),true);//new ColumnPrefixFilter("n".getBytes());
        scan.setFilter(filter);
       // scan.addColumn("cf1".getBytes(),"name".getBytes());
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            showResult2(result);
        }
    }

    public void showResult1( Result result){
        String  rowKey=Bytes.toString(result.getRow());
        String name = Bytes.toString(result.getValue("cf1".getBytes(), "name".getBytes()));
        String age = Bytes.toString(result.getValue("cf1".getBytes(), "age".getBytes()));
        String sex = Bytes.toString(result.getValue("cf1".getBytes(), "sex".getBytes()));
        System.out.println(rowKey+" "+name+" "+ age+" "+sex);
    }
    public void showResult2( Result result) throws IOException {
        CellScanner cellScanner = result.cellScanner();
        while(cellScanner.advance()){
            Cell cell = cellScanner.current();
            showCell(cell);
        }
    }
    public void showResult3( Result result) throws IOException {
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            showCell(cell);
        }
    }
    public void showSpecifiedCell( Result result,String name) throws IOException {
        List<Cell> cells = result.getColumnCells("cf1".getBytes(), name.getBytes());
        for (Cell cell : cells) {
            showCell(cell);
        }
    }

    public void showCell(Cell cell){
        String rk=Bytes.toString(CellUtil.cloneRow(cell));//CellUtil.getCellKeyAsString(cell);
        String cf=Bytes.toString(CellUtil.cloneFamily(cell));
        String q=Bytes.toString(CellUtil.cloneQualifier(cell));
        String v=Bytes.toString(CellUtil.cloneValue(cell));
        long ts=cell.getTimestamp();
        System.out.println(rk+" "+cf+":"+q+" "+v+" ,ts="+ts);
    }




    @After
    public void close() throws IOException {
        admin.close();
        conn.close();
    }

}
