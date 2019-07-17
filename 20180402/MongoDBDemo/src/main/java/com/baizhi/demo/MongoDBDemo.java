package com.baizhi.demo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.io.*;

/**
 * Created by Administrator on 2018/4/10.
 */
public class MongoDBDemo {
    private MongoClient client=new MongoClient("192.168.100.135",27017);
    private Mongo mongo=new Mongo("192.168.100.135",27017);
    @Test
    public void testInsert(){
        MongoDatabase zpark = client.getDatabase("zpark");
        MongoCollection<Document> t_user = zpark.getCollection("t_user");
        Document document = new Document();
        document.put("name","wangwu");
        document.put("age",18);
        t_user.insertOne(document);
        client.close();
    }
    @Test
    public void testQuery(){
        MongoDatabase zpark = client.getDatabase("zpark");
        MongoCollection<Document> t_user = zpark.getCollection("t_user");
        BasicDBObject query=new BasicDBObject();
        FindIterable<Document> documents = t_user.find(query);
        for (Document document : documents) {
            System.out.println(document);
        }
        client.close();
    }
    @Test
    public void testQueryAllCollections(){
        MongoDatabase zpark = client.getDatabase("zpark");
        MongoCursor<String> iterator = zpark.listCollectionNames().iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        client.close();
    }
    @Test
    public void testUpload() throws FileNotFoundException {
        MongoDatabase zpark = client.getDatabase("zpark");
        GridFSBucket gridFSBucket = GridFSBuckets.create(zpark);

        InputStream streamToUploadFrom = new FileInputStream("C:\\Users\\Administrator\\Desktop\\aa.png");
        // Create some custom options
        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(358400)
                .metadata(new Document("author", "zhangsan"));

        ObjectId fileId = gridFSBucket.uploadFromStream("photo.png", streamToUploadFrom, options);
        System.out.println(fileId);
        client.close();
    }
    @Test
    public void testUpload2() throws IOException {
        //MongoDatabase zpark = client.getDatabase("zpark");
        DB zpark = client.getDB("zpark");
        GridFS gridFS=new GridFS(zpark);

        GridFSInputFile gridFSFile = gridFS.createFile(new File("C:\\Users\\Administrator\\Desktop\\aa.png"));
        gridFSFile.save();
        client.close();
    }
    @Test
    public void testDownLoad() throws IOException {
        MongoDatabase zpark = client.getDatabase("zpark");
        GridFSBucket gridFSBucket = GridFSBuckets.create(zpark);

        FileOutputStream streamToDownloadTo = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\bb.png");
        gridFSBucket.downloadToStream("photo.png", streamToDownloadTo);
        streamToDownloadTo.close();
        client.close();
    }
    @Test
    public void testDownLoad2() throws IOException {
        DB zpark = client.getDB("zpark");
        GridFS gridFS=new GridFS(zpark);
        GridFSDBFile gridFSOne = gridFS.findOne("aa.png");
        File file=new File("C:\\Users\\Administrator\\Desktop\\cc.png");
        gridFSOne.writeTo(file);

        client.close();
    }



}
