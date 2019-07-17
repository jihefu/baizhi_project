package com.baizhi.day2;

import com.baizhi.entity.NewMessage;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.util.Date;

/**
 * 第一个lucene程序
 */
public class TestLucene {
    public static void main(String[] args) throws Exception {
       createIndexDB();

        findIndexDBByKeysword("张");
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

        Directory directory = FSDirectory.open(new File("F://indexDB"));


        IndexReader indexReader = IndexReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);  // 分词器一定要与 添加时，使用的一致

      /*  QueryParser queryParser = new QueryParser(Version.LUCENE_44,"author",analyzer);*/
      String[] fields = {"title","content","author"};

        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_44,fields,analyzer);


        Query query = queryParser.parse(keysword);
        /**
         * 创建高亮容器
         */
        Formatter formatter = new SimpleHTMLFormatter("<font color='red'><B>","</B></font>");
        Scorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter,scorer);
        TopDocs topDocs = indexSearcher.search(query, 1000);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc s: scoreDocs) {
            int num = s.doc;
            float score = s.score;
            System.out.println("当前的数据评分："+score);
            System.out.println("索引标号："+num);
            Document doc = indexSearcher.doc(num);
            /**
             * 参数说明：
             *      1. 分词器
             *      2. 指定doc的属性名
             *      3. 当前属性对应的属性值
             *
             *     注意：当doc属性值中不包含关键子，则高亮容器中的处理结果为null
             *
             */
            String id = doc.get("id");
            String title = highlighter.getBestFragment(analyzer, "title", doc.get("title"));
            if(null==title){
                title = doc.get("title");
            }
            String content = highlighter.getBestFragment(analyzer, "content", doc.get("content"));
            if(null==content){
                content = doc.get("content");
            }
            String author = highlighter.getBestFragment(analyzer, "author", doc.get("author"));
            if(null==author){
                author = doc.get("author");
            }
            String createDate = doc.get("createDate");
            Long aLong = Long.valueOf(createDate);
            Date date = new Date(aLong);
            NewMessage newMessage = new NewMessage(id,title,content,author,date);
            System.out.println(newMessage);
        }
    }


}
