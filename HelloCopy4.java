package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloCopy4 {
 //Դ�ļ���Ŀ���ļ�
 final static String SOURCESTRING = "C://test";
 final static String TARGETSTRING = "C://testcopy";
 //final static String SOURCESTRING = "C://FIAS";
 //final static String TARGETSTRING = "C://FIASCOPY";
 //����ɾ���Ŀ�ʼ���ֺͽ�������
 //final static String DELBEGIN = "��";
 //final static String DELBEGIN = "STORAGE";
 final static String DELBEGIN = "PCTFREE";
 //final static String DELEND = "��";
 final static String DELEND = "\\)"; 
 //�����滻�Ŀ�ʼ���ֺͽ������֣��Լ�Ҫ�滻������
 final static String REPBEGIN = "";
 final static String REPEND = "";
 final static String REPSTR = "";
 static StringBuffer sb = new StringBuffer("");
 
 public static void main(String[] args) throws FileNotFoundException, IOException{
  // TODO Auto-generated method stub
  System.out.println("HelloWorld");  
  if(!(new File(SOURCESTRING)).exists()){
       System.out.println("Դ�ļ�" + SOURCESTRING + "�����ڣ��޷����ƣ�");
                return;
     }else{
      readfilefolder(DELBEGIN,DELEND,REPBEGIN,REPEND,REPSTR); 
      }  
 }
 
 //��ȡԴ�ļ����е������ļ����ļ���
 public static boolean readfilefolder(String delebegin,String deleend,String repbegin,String repend,String repstr) throws IOException {
  if((new File(SOURCESTRING)).isFile()){
         readfile(SOURCESTRING);
         writefile(TARGETSTRING,delebegin,deleend,repbegin,repend,repstr);
     }else if((new File(SOURCESTRING)).isDirectory()){
         readDirectory(SOURCESTRING,TARGETSTRING,delebegin,deleend,repbegin,repend,repstr);
    }
  return true;
 }
 
 //��ȡ�ļ����е����� 
 private static void readDirectory(String sourcePathString,String targetPathString,String delebegin,String deleend,String repbegin,String repend,String repstr) throws IOException{           
    (new File(targetPathString)).mkdirs();
    System.out.println("��ʼ��ȡ�ļ���" + sourcePathString +"�е�����");
    File[] files = new File(sourcePathString).listFiles();
    for(int i = 0; i < files.length; i++){
        if(files[i].isFile()){
            readfile(sourcePathString + File.separator + files[i].getName());
            writefile(targetPathString + File.separator + files[i].getName(),delebegin,deleend,repbegin,repend,repstr);
         }else if(files[i].isDirectory()){
            readDirectory(sourcePathString + File.separator + files[i].getName(),targetPathString + File.separator + files[i].getName(),delebegin,deleend,repbegin,repend,repstr);
         }    
   }
          System.out.println("��ȡ�ļ���" + sourcePathString + "�ɹ�");
   } 
 
   //��ȡĳ���ļ�������   
   public static void readfile(String absolutePath) throws IOException {
    System.out.println("��ʼ��ȡ�ļ�" + absolutePath + "�е�����");
    if(sb.length()>0) {
     sb.setLength(0);
    }    
    //FileReader reader = new FileReader(absolutePath); 
       //BufferedReader br = new BufferedReader(reader);
       FileInputStream fis = new FileInputStream(absolutePath);
       InputStreamReader isr = new InputStreamReader(fis, "Shift_JIS"); //GBK�滻�����ļ�����ʹ�õı���
       BufferedReader br = new BufferedReader(isr); 
       //str�ǰ��ж�ȡ��
       String str = null;     //   
       while((str = br.readLine()) != null) {
        /*if(!str.trim().equals("")) {     
         sb.append(str+System.getProperty("line.separator"));//���ڻ���
        } */ 
            byte[] bytes = str.getBytes(); 
               //String str = new String(bytes,"Shift-JIS");
               //String str2 = new String(bytes,"Shift-JIS");
         sb.append(str+System.getProperty("line.separator"));//���ڻ���         
       }
       //ȥ�������һ������
       /*sb.deleteCharAt(sb.length()-1);  
       sb.deleteCharAt(sb.length()-1); */ 
       br.close();
       //reader.close(); 
       isr.close();
       fis.close();
       //System.out.println("��ȡ�ļ�:"+sb.toString());
       System.out.println("��ȡ�ļ�" + absolutePath + "�ɹ�");      
   }
 
   //����ƥ�䣺ƥ��CREATE TABLE�����ź��Ӧ��������
   public static int match(int index,String str) {
  Stack<Character> st = new Stack<Character>();
  Stack<Integer> num = new Stack<Integer>();
  int result = -1;
     for(int i=0;i<str.length();i++) {
     if(st.isEmpty()) {
      if(str.charAt(i)=='(' || str.charAt(i)==')') {
       st.push(str.charAt(i));
       num.push(i);
       i++;
      }
     }
     char temp = 0;
     if(!st.isEmpty()) {
          temp = st.peek();
          //System.out.println("st.peek():"+st.peek());
     }         
     if(temp=='('&&str.charAt(i)==')') {
      int tnum = num.peek();
      //System.out.println("num.peek():"+num.peek());
         num.pop();
      if(tnum==index) {
       result = i;
       break;
      }
     }else {
      if(str.charAt(i)=='(' || str.charAt(i)==')') {
       st.push(str.charAt(i));
          num.push(i); 
      }     
     } 
    }
    return result;     
   }

   //��ĳ���ļ�������д���޸ĺ���ļ�
   public static void writefile(String absolutePath,String delebegin,String deleend,String repbegin,String repend,String repstr) throws IOException {  
     String str = sb.toString();
     //д�����ļ��Ĺ����ж��ļ����е�ɾ������ 
     String table = "CREATE TABLE";
  int tableindex = str.indexOf(table);//�м�Ĳ���ΪҪɾ���ֶεĿ�ʼ����
  int braleftindex = -1;
  if(tableindex!=-1) {
  int beginindex = tableindex+table.length();
  String mulstr = str.substring(beginindex);
  braleftindex = str.indexOf("(");    
  System.out.println("sqlindex" + tableindex +"�ո�"+mulstr.substring(beginindex, beginindex+5)+"�ո�"+braleftindex);
  }  
  int brarightindex = match(braleftindex,str);
  System.out.println("brarightindex:"+brarightindex);
  String result = sb.toString();
  String finalresult =result;
  if(brarightindex!=-1) {
  //CREATE TABLE(����)���������
  String mul2str = str.substring(brarightindex);
  int index = mul2str.indexOf(delebegin);    
  if(index!=-1) {
    String delepart = mul2str.substring(index);
    //String sub = delepart.replaceAll("��[^��]*��", ""); 
    String sub = delepart.replaceFirst(delebegin+"[^"+deleend+"]*"+deleend, "");
    //result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.trim(); 
    //str.substring(0, brarightindex)��CREATE TABLE(����)
    //mul2str��CREATE TABLE(����)��������ݣ�mul2str.substring(0,index)��CREATE TABLE(����)���棬
    //PCTFREE֮ǰ�����ݣ�sub��PCTFREE����ɾ��PCTFREE��Storage֮�������
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub; 
   
   //ɾ��LOB()
   String delebegin2 ="STORAGE";
   String regex = "LOB(.*?)STORE AS";
   String regex2 = "STORAGE(.*?)PCTVERSION [0-9]+";
   String prestr = "";
   Pattern pattern = Pattern.compile(regex);
   Matcher matcher = pattern.matcher(sub);
   if (matcher.find()) {
    System.out.println("sqlbia:"+matcher.group());
    prestr = matcher.group();
   }
   int sqlindex = sub.indexOf(prestr);
   if(sqlindex!=-1&&prestr!="") {
    int sqlbeginindex = sqlindex+prestr.length();
    if(sqlbeginindex!=-1) {
       String lobstr = sub.substring(sqlbeginindex);
       int sqlfinalindex = lobstr.indexOf(";");
       //System.out.println("sqlfinalindex:"+sqlfinalindex+" lobstr:"+lobstr);
       String finalsql = lobstr.substring(0, sqlfinalindex+1); 
       StringBuffer finalsql1  = new StringBuffer();
    // ���� Pattern �����ұ���һ���򵥵�������ʽ"cat"
       Pattern pattern2 = Pattern.compile(regex2);
    // �� Pattern ��� matcher() ��������һ�� Matcher ����
    Matcher matcher2 = pattern2.matcher(finalsql);
    while(matcher2.find()){
    //��ʱsbΪfatdogfatdog��cat���滻Ϊdog,���ҽ����ƥ�䵽֮ǰ���Ӵ�����ӵ�sb������
         matcher2.appendReplacement(finalsql1,"");
    }
    //��ʱsbΪfatdogfatdogfat�������ƥ�䵽������Ӵ���ӵ�sb������
    matcher2.appendTail(finalsql1);
    //�������Ϊfatdogfatdogfat
    //System.out.println("finalsql1:"+finalsql1.toString());
    String finalsql2 = finalsql1.toString().replaceAll(delebegin2+"[^"+deleend+"]*"+deleend, "");    
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.substring(0,sqlbeginindex)+finalsql2.toString()+lobstr.substring(sqlfinalindex+1);
    sub = sub.substring(0,sqlbeginindex)+finalsql2.toString()+lobstr.substring(sqlfinalindex+1);
    //System.out.println("str.substring(sqlfinalindex):"+str.substring(sqlfinalindex)+"finalsql:"+finalsql);
    //System.out.println("finalsql2:"+result.substring(0,sqlbeginindex)+finalsql2.toString());
    }
    }
   
    //ɾ��PARTITION
   /* int partindex = sub.indexOf("PARTITION BY RANGE");
    if(partindex!=-1) {
    int len = partindex+"PARTITION BY RANGE".length();
    String partition = sub.substring(len);
    int finalindex = partition.indexOf(")");
    String finalstr = partition.substring(finalindex);
    String finalsub = finalstr.replaceAll(delebegin+"[^"+deleend+"]*"+deleend+System.getProperty("line.separator"), "");
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.substring(0,len)+partition.substring(0,finalindex)+finalsub; 
    }*/
    
    int partindex = sub.indexOf("PARTITION BY RANGE");
    if(partindex!=-1) {
    int len = partindex+"PARTITION BY RANGE".length();
    String partition = sub.substring(len);
    int finalindex = partition.indexOf(")");
    String finalstr = partition.substring(finalindex);
    int patrfinalindex = finalstr.indexOf(";");
    String partfinalstr = finalstr.substring(0,patrfinalindex+1);
    String finalsub = partfinalstr.replaceAll(delebegin+"[^"+deleend+"]*"+deleend+System.getProperty("line.separator"), "");
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.substring(0,len)+partition.substring(0,finalindex)+finalsub+finalstr.substring(patrfinalindex+1); 
    }  
  }   
  }     
    //д�����ļ��Ĺ����ж��ļ����е�ɾ������        
   /* int index = str.lastIndexOf(delebegin);//�м�Ĳ���ΪҪɾ���ֶεĿ�ʼ����
    String result = sb.toString();
    if(index!=-1) {
     String delepart = str.substring(index);
     //String sub = delepart.replaceAll("��[^��]*��", "");    
     String sub = delepart.replaceAll(delebegin+"[^"+deleend+"]*"+deleend, "");
     result = str.substring(0, index)+sub.trim();     
    } 
    String finalresult =result;*/
    
    //д�����ļ��Ĺ����ж��ļ����е��滻����
    int index2 = result.lastIndexOf(repbegin);//�м�Ĳ���ΪҪ�滻�ֶεĿ�ʼ����
    if(index2!=-1) {
     String reppart = result.substring(index2);
     String rep="";
     if(repbegin!="" && repend!="") {
     rep = reppart.replaceFirst(repbegin+"[^"+repend+"]*"+repend, repstr);   
     }    
   finalresult = result.substring(0, index2)+rep.trim();
    }   
    /*  File f = new File(filefolder); 
    if (!f.exists()) { 
     f.mkdirs(); 
    } */
    /*File writename = new File(absolutePath); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�         
        if(!writename.exists()) {
         boolean flag = writename.createNewFile(); 
         if(flag==true) {
          System.out.println("�ļ������ɹ�");  
         }else {
          System.out.println("�ļ�����ʧ��");  
         }         
        } 
    // �������ļ�  
       BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
       out.write(result); // \r\n��Ϊ����
       //out.write(finalresult); // \r\n��Ϊ����
       //out.write(str2+"\r\n"); // \r\n��Ϊ����
       out.flush(); // �ѻ���������ѹ���ļ�  
       out.close(); // ���ǵùر��ļ�  */       
       
       FileOutputStream fos = new FileOutputStream(absolutePath);
       OutputStreamWriter osr = new OutputStreamWriter(fos, "Shift_JIS"); //GBK�滻�����ļ�����ʹ�õı���
       BufferedWriter br = new BufferedWriter(osr);
       br.write(result); // \r\n��Ϊ����
       br.flush();
       br.close();
       osr.close();
       fos.close();

   }
}