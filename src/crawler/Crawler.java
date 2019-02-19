/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import crawler.DBconnect;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.net.SocketTimeoutException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yash1
 */
public class Crawler {

    public static Comparator<page> comparator =  new Comparator<page>() {
        @Override
        public int compare(page o1, page o2) {
            if(o1.getVal()<o2.getVal())
                return 1;
            else
                return -1;
        }
    };
     static Scanner yash  = new Scanner(System.in);
    public static DBconnect db = new DBconnect();
    public static CSS obj = new CSS();
    public static PriorityQueue<page> queue = new PriorityQueue<>(5,comparator);
    public static Set<String> marked = new HashSet<>();
    public static String  regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
    public static void start() throws IOException , SQLException, InterruptedException
    {
        System.out.println("Domain");
        String sql;
        ResultSet rs;
        Document doc;
        BufferedReader br = null;
        String domain = yash.nextLine();
        while(!queue.isEmpty())
        {
            page crawledurl = queue.poll();
            System.out.println("\n*** site crawled : "+ crawledurl.getS() + "***");
            boolean ok = false;
            URL url = null;
            
            while(!ok)
            {
                try{
                    url = new URL(crawledurl.getS());
                    br = new BufferedReader(new InputStreamReader(url.openStream()));
                    //System.out.print("here!!");
                    ok = true;
                }
                catch(MalformedURLException e)
                {
                    System.out.println("***MalformedURl :" + crawledurl.getS());
                    crawledurl = queue.poll();
                    ok = false;
                }
                catch(IOException ioe)
                {
                     System.out.println("***IOException :" + crawledurl.getS());
                    crawledurl = queue.poll();
                    ok = false;
                }
            }
            
        StringBuilder sb = new StringBuilder();
        String  tmp = null;
        while(br.readLine()!=null)
        {
            tmp= br.readLine();
            sb.append(tmp);
        }
        tmp = sb.toString();
        //System.out.println(tmp);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tmp);
        double val=0;
        String keyword=" ";
        String description=" ";
        String title=" ";
        String body=" ";
        while(matcher.find())
        {
            //System.out.println("match found");
            Thread.sleep(1000);
            String w = matcher.group();
            if(!marked.contains(w))
            {
                marked.add(w);
                System.out.println("sites added for crawling :" + w);
                
                try{
                        sql = "select * from table1 where url = '"+ w + "';";
                        rs = db.runSql(sql);
                        if(rs.next()){
                        }
                        else{
                        doc =  (Document)Jsoup.connect(w)
                                    .ignoreHttpErrors(true).userAgent("Jsoup client")           
                                    .get();
                        title = doc.title();
                        body = doc.body().text();
                        try{
                            description = doc.select("meta[name=description]").first().attr("content");
                        }catch(NullPointerException e)
                        {
                            
                            getMetaKey temp = new getMetaKey();
                            temp.set(body);
                            description = temp.getMeta();
                            description.replace("'","''");
                        }
                        try{
                            keyword =  doc.select("meta[name=keywords]").first().attr("content");
                        }
                        catch(NullPointerException e)
                        {
                           
                            getMetaKey temp = new getMetaKey();
                            temp.set(body);
                            keyword = temp.getKeyword();
                        }
                        w = w.replaceAll("'","''");
                        w = w.replaceAll("-","");
                        title = title.replaceAll("'","''");
                        title  = title.replaceAll("-","");
                        description = description.replaceAll("'","''");
                        description = description.replaceAll("-","");
                        body = body.replaceAll("'", "''");
                        body = body.replaceAll("-","");
                        keyword = keyword.replaceAll("-","");
                        keyword = keyword.replaceAll("'", "''");
                        keyword = keyword.replaceAll("`","''");
                        keyword = keyword.substring(0, Math.min(keyword.length(), 100));
                        System.out.println("URL : " + w);
                        System.out.println("title : " + title);
                        System.out.println("Description : " + description);
                        System.out.println("Keyword : "+ keyword);
                        System.out.println(body + "\n");
                        obj.set(body,keyword);
                        val = obj.calculate();
                       
                        if(body!="" && title!= ""  && keyword!= ""  && description!= "" && body!=null && title!= null && keyword!=null && description!= null)
                        {
                            try{
                        sql = "INSERT INTO  table1 " + "VALUES " 
                            + "('" + w + "' , '" + title + "' , '"+ description + "' ,  0 , ' " + body + "', '" + domain+ "');";
                        Statement st = db.conn.createStatement();
                        st.execute(sql);
                            }
                            catch(SQLException ex)
                            {}
                        }
                        }
                        sql = "select * from table2 where keywords = '" + 
                        keyword + "'  and url ='"+ w + "';";          
                        rs = db.runSql(sql);
                        if(rs.next()){
                        }
                        else
                            if(val!=0.0 && body!="" && title!= "" && keyword!="" && description!= "" && body!=null && title!= null && keyword!=null && description!= null )
                        {                     
                            sql= "insert into table2 values ('" +
                            keyword + "' , '" + w + "' , " + val +");";
                            Statement stat = db.conn.createStatement();
                            stat.execute(sql);
                        }
                    }
                    catch(NullPointerException e)
                    {
                        System.out.println("Null pointer exception");
                    }
                catch(IOException ioe)
                {
                     System.out.println("***IOException :" + w);
                }
                queue.add(new page(w,val));
            }
        }
        showresult();
        }
        if(br!=null)
            br.close();
}
    public static void showresult()
    {
        System.out.println("\n\nResults:");
        System.out.println("website crawled : "+ marked.size() + "\n");
        for(String s: marked)
        {
            System.out.println("* " + s);
        }
        
    }
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
       
        String url = yash.nextLine();
        while(!url.equals(""))
        {
            queue.add(new page(url,1));
            url = yash.nextLine();
        }
        start();
        
    }

}
