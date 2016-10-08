package TheRipper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.net.ssl.HttpsURLConnection;
import org.unbescape.html.HtmlEscape;

public class PractoRip{
    private final static String SWEET_DATA3 = "";
    private String SWEET_DATA2 = "";
    private String SWEET_DATA = "";
    private String time_Data = "";
    private String doc_Data = "";
    private int pitbull = 1;
    private LinkedHashMap<String, String[]> clinicMap = new LinkedHashMap<>();
    //private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.113 Safari/537.36";
    private final String USER_AGENT = "RocketBot/1.7";
    public static void main(String args[])throws IOException{
        try{
            if(args[0].equalsIgnoreCase("n")){
                final String[] LOC_CITY = {"batangas","caloocan","cavite","cebu-city","las-pinas","makati","malabon","mandaluyong","manila","marikina","muntinlupa","navotas","paranaque","pasay","pasig","pateros","quezon","san-juan","taguig","valenzuela"};
                for(int y=0;y<20;y++){
                    int x = 1;
                    System.out.println("City : "+LOC_CITY[y]);
                    while(new PractoRip().testIt(x++,LOC_CITY[y]));
                }
                    FileWriter writer = new FileWriter("doctorlist.txt");
                    writer.append(SWEET_DATA3);
                    writer.flush();
                    writer.close();
                    System.out.println("Data compiled.");
            }
            else if(args[0].equalsIgnoreCase("c")){
                PractoRip op = new PractoRip();
                op.ripDocs();
                /*
                FileWriter writer = new FileWriter("doctordata.csv");
                writer.append(SWEET_DATA);
                writer.flush();
                writer.close();
                System.out.println("Data compiled.");

                FileWriter writer2 = new FileWriter("daaataaa.csv");
                writer2.append(SWEET_DATA2);
                writer2.flush();
                writer2.close();
                System.out.println("Data compiled.");
                */
            }
            else{
                System.out.println("Usage : PractoRip -options");
                System.out.println("n or N to get doctor list");
                System.out.println("c or C to rip from doctor list(Default name is doctorlist.txt)");
            }
        }
        catch(Exception e){
            System.out.println("Usage : PractoRip -options");
            System.out.println("n or N to get doctor list");
            System.out.println("c or C to rip from doctor list(Default name is doctorlist.txt)");
        }
    }
    private void ripDocs(){
        try (BufferedReader br = new BufferedReader(new FileReader("doctorlist.txt"))){
            int num = 0;
            String sCurrentLine;
            FileWriter writer = new FileWriter("doctordata.csv");
            FileWriter writer2 = new FileWriter("daaataaa.csv");
            FileWriter timeWrite = new FileWriter("doctorTime.csv");
            while ((sCurrentLine = br.readLine()) != null) {
                //httpDocs(sCurrentLine);
                num++;
                //SWEET_DATA += httpDocs(sCurrentLine);
                httpDocs(sCurrentLine);
                writer.append(doc_Data);
                writer.flush();
                doc_Data = "";
                writer2.append(SWEET_DATA2);
                writer2.flush();
                SWEET_DATA2 = "";
                timeWrite.append(time_Data);
                timeWrite.flush();
                time_Data = "";
                System.out.println("ENTRY NO: "+num);
                pitbull++;
            }
            writer.close();
            writer2.close();
            //Writes the clinics
            FileWriter clinicWrite = new FileWriter("clinicData.csv");
            clinicWrite.append(hash2Array(clinicMap));
            clinicWrite.flush();
            clinicWrite.close();

        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void httpDocs(String url){
        HttpsURLConnection con = null;
        for(int x=0;x<5;x++){
            con = startCon(url);
            if(con!=null){
                System.out.println("Connection Established");
                break;
            }
        }
        
        String heto = "";
        
        try{
            heto = con.getResponseCode()+"";
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            int count = 0, count2 = 0, therock = 0;
            String[][] rawData = new String[50][13];
            String[] rawData2 = new String[50];
            String[] clinicArray = new String[3];
            
            //boolean check = false;
            String day = "";
            while ((inputLine = in.readLine()) != null){
                inputLine = HtmlEscape.unescapeHtml(inputLine);
                if(inputLine.contains("<h1 title=\"")){
                    System.out.println(charRip(inputLine,0));
                    rawData[count][0] = charRip(inputLine,0);
                    doc_Data += "\""+pitbull+"\",";
                    doc_Data += charRip(inputLine,0)+",";
                }
                if(inputLine.contains("doctor-qualifications")){
                    inputLine = in.readLine();
                    System.out.println(charRip(inputLine,2));
                    rawData[count][1] = charRip(inputLine,2);
                    doc_Data += charRip(inputLine,2)+"\n";
                }
                if(inputLine.contains("strong black")){
                    clinicArray[0] = charRip(inputLine,2);
                    System.out.println(charRip(inputLine,2));
                    //System.out.println(clinicArray[0]);
                    rawData[count][2] = charRip(inputLine,2);
                    inputLine = in.readLine();
                    clinicArray[1] = charRip(inputLine,3);
                    System.out.println(charRip(inputLine,3));
                    rawData[count][3] = charRip(inputLine,3);
                    
                }
                if(inputLine.contains("<h2 title=\"")){
                    clinicArray[2] = charRip(inputLine,1);
                    System.out.println(charRip(inputLine,1));
                    rawData[count][4] = charRip(inputLine,1);
                }
                if(inputLine.contains("streetAddress")){
                    clinicMap.put(charRip(inputLine,2), clinicArray);
                    for(String thing: clinicArray){
                        System.out.println(thing);
                    }
                    System.out.println(charRip(inputLine,2));
                    rawData[count][5] = charRip(inputLine,2);
                    //hash2Array(clinicMap);
                }
                if(inputLine.contains("clinic-timings-day")){
                    inputLine = in.readLine();
                    System.out.println(charRip(inputLine,2));
                    //rawData[count][6] = charRip(inputLine,2);
                    day = charRip(inputLine,2);
                }
                if(inputLine.contains("clinic-timings-session")){
                
                    inputLine = in.readLine();
                    System.out.println(charRip(inputLine,2));
                    day = day.replaceAll("[^\\-\\,\\w]","");
                    day = day.replaceAll(","," , ");
                    day = day.replaceAll("-"," - ");
                    String[] dayz = day.trim().split(" ");
                    System.out.println("data: "+day);
                    System.out.println("day lengt: "+dayz.length);
                    if(dayz.length==1){
                        //System.out.println("SHOW ME THE MONEY");
                        rawData[count][stupidDay(day)]="\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"";
                        timeDrink("\""+pitbull+"\"",rawData[count][5],
                                "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"",stupidDay(day)+"");
                    }
                    else
                        for(int x = 0;x<dayz.length;x++){
                            //System.out.println(dayz[x]);
                            if(dayz[x].contains(",")){
                                //System.out.println(x);
                                if(rawData[count][stupidDay(dayz[x-1])]==null){
                                    rawData[count][stupidDay(dayz[x-1])] = "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"";
                                    timeDrink("\""+pitbull+"\"",rawData[count][5],
                                "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"",stupidDay(dayz[x-1])+"");
                                }
                            }
                            if(dayz[x].contains("-")){
                                for(int y=stupidDay(dayz[x-1]);y<=stupidDay(dayz[x+1]);y++){
                                    rawData[count][y] = "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"";
                                    timeDrink("\""+pitbull+"\"",rawData[count][5],
                                "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"",y+"");
                                }
                            }
                            if(dayz[x].trim().matches("...")){
                                //System.out.println("WA");
                                try{
                                    //System.out.println(dayz[x-2]+" shuting");
                                    if(dayz[x-1].contains(",")){
                                       if(rawData[count][stupidDay(dayz[x])]==null){
                                            rawData[count][stupidDay(dayz[x])] = "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"";
                                            timeDrink("\""+pitbull+"\"",rawData[count][5],
                                "\""+inputLine.trim().replaceAll("<*.br*.>"," ")+"\"",stupidDay(dayz[x])+"");
                                       }
                                    }
                                }
                                catch(Exception e){}
                            }
                        }
                    if(count>=1){
                        rawData[count][0]=rawData[count-1][0];
                        rawData[count][1]=rawData[count-1][1];
                    }
                    //rawData[count][7] = charRip(inputLine,2);
                    
                }
                if(inputLine.contains("clinic-block")){
                    count2++;
                    if(count2>1){
                        count++;
                    }
                }
                if(inputLine.contains("specialty-row")){
                    inputLine = in.readLine();
                    rawData2[0] = pitbull+"";
                    //rawData2[1] = rawData[count][1];
                    rawData2[1] = charRip(inputLine,2);
                    for(int y=0;y<1;y++)
                        SWEET_DATA2 += rawData2[y]+",";
                    SWEET_DATA2 += rawData2[1] + "\n";
                }
            }
            in.close();

            for(int x=0;x<=count;x++){
                //System.out.println("ARRAY DATA : ");
                for(int y=0;y<12;y++){
                    heto += rawData[x][y]+",";
                }
                heto += rawData[x][12] + "\n";
            }
        }
        catch(Exception e){
            System.out.println(heto);
            e.printStackTrace();
        }
    }
    /*
    private void timeDrink2(String a, String b, String c, String d){
        String[] timeThing = new String[4];
        timeThing[0]=a;
        timeThing[1]=b;
        timeThing[2]=c;
        timeThing[3]=""+(Integer.getInteger(d)-6);
        try(FileWriter writer = new FileWriter("doctortimes.csv",true)){
            for(int i=0;i<timeThing.length-1;i++)
                time_Data += timeThing[i]+",";
            
            time_Data timeThing[3]+"\n");
            writer.flush();
            writer.close();
            //System.out.println("Data compiled.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    */
    private void timeDrink(String a,String b ,String times,String d){
        String[] timeThing = new String[5];
        timeThing[0]=a;
        timeThing[1]=b;
        timeThing[2]="";
        //System.out.println(times);
        timeThing[3]=""+(Integer.parseInt(d)-6);
        String[] ina = times.replaceAll("\"", "").split(" ");
        String temps = "";
        for(int i=0;i<ina.length;i++){
            //System.out.print(ina[i]);
            try{
                
                if(ina[i].contains("(\\d{1,}:\\d{2}")){
                    if(ina[i-1].contains("[A-z][A-z]")){
                        temps += "-";
                        temps += ina[i].trim();
                    }
                }
                else{
                    temps += ina[i].trim();
                }
            }
            catch(Exception e){
                temps += ina[i].trim();
            }
        }
        //System.out.println(temps+"d");
        try{ 
            String[] matcha = temps.split("-");
            //System.out.println("BONERS:"+matcha.length);
            int reset = 1;
            for(int i=0;i<matcha.length;i++){
                String datum = matcha[i].toUpperCase();
                if(datum.contains("PM")){
                    datum = datum.replaceAll("[a-zA-Z]{2}", "").trim();
                    String de[] = datum.split("\\:");
                    //TANGINGA MO TIME
                    datum = ""+(Integer.parseInt(de[0])+12);
                    datum += de[1];
                }
                else{
                    datum = datum.replaceAll("[a-zA-Z]{2}", "").trim();
                }
                datum = "\""+datum+"\"";
                timeThing[2] +=datum;
                if(reset==1)
                    timeThing[2]+=",";
                if(reset==2){
                    reset = 0;
                    for(int x=0;x<timeThing.length-2;x++){
                        //System.out.print(timeThing[x]);
                        time_Data += timeThing[x]+",";
                    }
                    
                    time_Data += "\""+timeThing[3] + "\"\n";
                    //System.out.println(time_Data);
                }
                reset++;
              
            }
            
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String hash2Array(LinkedHashMap<String,String[]> hmap){
        Set set2 = hmap.entrySet();
        int count = 1;
        String mads = "";
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
          
          Map.Entry mentry2 = (Map.Entry)iterator2.next();
          //System.out.print("Key is: "+mentry2.getKey() + " & Value is: ");
          String[] tool = (String[])mentry2.getValue();
          //tool[3] = ""+mentry2.getKey();
          for(int x=0;x<tool.length;x++){
              if(x==0)
                mads+=count+",";
              if(x==tool.length-1){
                mads+=mentry2.getKey()+",";
                mads+=tool[x]+"\n";
              }
              else
                mads+=tool[x]+",";
          }
          count++;
        }
        return mads;
    }
    private int stupidDay(String in){
        in = in.trim();
        int a = 0;
        if(in.equalsIgnoreCase("mon")){
            a = 1;
        }else if(in.equalsIgnoreCase("tue")){
            a = 2;
        }else if(in.equalsIgnoreCase("wed")){
            a = 3;
        }else if(in.equalsIgnoreCase("thu")){
            a = 4;
        }else if(in.equalsIgnoreCase("fri")){
            a = 5;
        }else if(in.equalsIgnoreCase("sat"))
            a = 6;
        return a+6;
    }
    private String charRip(String input){
        Pattern pattern = Pattern.compile("<span href=\"((.*))\" class=\"block lh_1\">");
        Matcher pod = pattern.matcher(input);
        pod.find();
        return pod.group(1);
    }
    private String charRip(String input,int d){
        Pattern patterns[] = new Pattern[4];
        patterns[0] = Pattern.compile("<h1 title=\"(.*)\" itemprop=\"name\">");
        patterns[1] = Pattern.compile("<h2 title=\"(.*)\"><");
        patterns[3] = Pattern.compile("<(\\w+)( +.+)*>(.*)</\\1>");
        patterns[2] = Pattern.compile("<(\\w+)( +.+)>(.*)</\\1>");
        try{
            Matcher pod = patterns[d].matcher(input);
            pod.find();
            if(d<=1){
                return "\""+pod.group(1).replaceAll("\"","").replaceFirst("Dr\\.","").trim()+"\"";
            }
            return "\""+pod.group(3).trim()+"\"";
        }
        catch(Exception e){
            //e.printStackTrace();
            return "\""+input.trim()+"\"";
        }
        //return "";
    }
    private HttpsURLConnection startCon(String urlin){
        try{
            URL url = new URL(urlin);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setConnectTimeout(30000);
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            if(con.getResponseCode()==200){
                return con;
            }
        }
        catch(Exception e){
            System.out.println("sad");
            //return con;
        }
        return null;
    }
    private boolean testIt(int pageNo,String loc){
        boolean verdict = true;
        String https_url = "https://www.practo.com/philippines/"+loc+"?page="+pageNo;
        //URL url;
        try{
            HttpsURLConnection con = null;
            //  url = new URL(https_url);
            for(int x=0;x<5;x++){
                con = startCon(https_url);
                if(con!=null){
                    System.out.println("Connection Established");
                    break;
                }
            }
                //print_https_cert(con);
                /*// optional default is GET
                 con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("User-Agent", USER_AGENT);

                */

                int responseCode = con.getResponseCode();
                //System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Page No : "+pageNo);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                int count = 0, count2 = 0;

                while ((inputLine = in.readLine()) != null){
                        if(inputLine.contains("block lh_1")){
                                count++;
                                System.out.println(charRip(inputLine)+count2);
                                SWEET_DATA += charRip(inputLine)+"\n";

                                //I GOT NINE LIVES CATS EYES BACK IN BLAAAACCK
                        }
                        count2++;
                        //System.out.println(count2);
                        if(count2>=1500&&count==0){
                                //todo: exit code
                                //System.out.println("NO DATA");
                                verdict = false;
                                break;
                        }
                }

                in.close();

             //dumpl all cert info


             //dump all the content
            // print_content(con);
                con.disconnect();
      } 
          /*
          catch (MalformedURLException e) {
             e.printStackTrace();
      } catch (IOException e) {
             e.printStackTrace();
      }*/ catch(Exception e){
                  //e.printStackTrace();
                  verdict = false;
          }
          return verdict;

   }
}