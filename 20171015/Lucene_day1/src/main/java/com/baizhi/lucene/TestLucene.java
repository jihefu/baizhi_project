package com.baizhi.lucene;

import com.baizhi.entity.NewMessage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 第一个lucene程序
 */
public class TestLucene {
    public static void main(String[] args) throws Exception {
       createIndexDB();

        findIndexDBByKeysword("今天");
    }
    /**
     * 创建本地索引库
     */

    public static void createIndexDB() throws Exception {
        // 1. 指定索引库位置
        Directory directory = FSDirectory.open(new File("F://indexDB"));
        // 2. 准备数据
        NewMessage newMessage = new NewMessage("2","今天今天今天今天真热","今天郑州气温高达27°，这样会死人的","张勇",new Date());

        // 3. 通过Lucene  IndexWriter 输出流将数据写入 索引库
        /**
         * 分词器不一样，分词策略不一样，分词结果不一样
         */
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
        /**
         * 参数一：Lucnen的当前版本
         * 参数二：分词器   使用默认的分词器  单个分词
         */
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_44,analyzer);
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        /**
         * 将javaBean  转换为  Document
         */
        Document doc = new Document();
        Field field;
        /**
         * StringField   处理字符串数据的     不支持数据分词
         *
         * TextField     处理字符串数据的     支持数据分词
         *
         * intField      处理整数类型数据
         *
         * longField     处理日期类型数据
         *
         * DoubleField   处理浮点数类型  双精度
         *
         * floatField    处理浮点数类型  单精度
         */
        /**
         * 参数一：doc 的属性名    建议 于javaBean 属性名一致
         * 参数二：doc 当前属性的 值
         * 参数三：  store    YES ：数据存放至索引库    NO  数据不存放
         */
        doc.add(new StringField("id",newMessage.getId(), Field.Store.YES));
        doc.add(new TextField("title",newMessage.getTitle(), Field.Store.YES));
        doc.add(new TextField("content",newMessage.getContent(), Field.Store.YES));
        doc.add(new TextField("author",newMessage.getAuthor(), Field.Store.YES));
        doc.add(new LongField("createDate",newMessage.getCreateDate().getTime(), Field.Store.YES));
        //  完成数据添加
        indexWriter.addDocument(doc);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     *
     * 索引库的检索
     */
    public static void findIndexDBByKeysword(String keysword) throws Exception {
        // 1. 指定索引库位置
            Directory directory = FSDirectory.open(new File("F://indexDB"));

        // 2. 创建查询器对象
        IndexReader indexReader = IndexReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        /**
         * 参数一：version
         *
         * 参数二：f  field   doc的列/属性    指定根据哪一个属性查询   title   content  author
         *
         * 参数三：分词器  需要对输入文本进行分词
         */
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);  // 分词器一定要与 添加时，使用的一致
        // 指定查询的列/属性
      /*  QueryParser queryParser = new QueryParser(Version.LUCENE_44,"author",analyzer);*/
      String[] fields = {"title","content","author"};
        // 多列查询
        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_44,fields,analyzer);

        // 封装关键字
        Query query = queryParser.parse(keysword);
        /**
         * 参数说明
         *      一、query  查询关键字对象
         *      二、 最终返回的结果条数
         */

        TopDocs topDocs = indexSearcher.search(query, 1000);
        /**
         * TopDocs
         *
         *      ScoreDocs[]   索引标号 数组
         *
         *      totalHits     关键字命中条数   根据关键字 找到了多少条数据  方便分页  命中总条数
         */

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 遍历索引标号数组
        for (ScoreDoc s: scoreDocs) {
            int num = s.doc;
            /*
            * 数据评分的计算策略
            *   根据关键词在数据中出现的频率相关，频率越高  分值越高  排名越靠前
            *
            * */
            float score = s.score;
            System.out.println("当前的数据评分："+score);
            // 通过查询器 根据索引标号返回doc 对象
            System.out.println("索引标号："+num);
            Document doc = indexSearcher.doc(num);
            // 将doc 转换为 javaBean
            String id = doc.get("id");
            String title = doc.get("title");
            String content = doc.get("content");
            String author = doc.get("author");
            // 毫秒数
            String createDate = doc.get("createDate");
            Long aLong = Long.valueOf(createDate);
            Date date = new Date(aLong);
            NewMessage newMessage = new NewMessage(id,title,content,author,date);
            System.out.println(newMessage);

        }

    }





}
