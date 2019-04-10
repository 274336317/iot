package com.mackerelpike.kapua.proxy;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.Gson;

public class Main 
{
    public static void main( String[] args ) throws UnsupportedEncodingException
    {
    	
    	
        String url = "http://192.168.0.111:8081/v1/authentication/user";
        
        String userName = "User123";
        
        String password = "User@12345678";
        
        UserInfo user = LoginHelper.login(url, userName, password);
        
        System.out.println(url);
        
        if(user != null)
        {
            
            Gson g = new Gson();
            
           System.out.println(g.toJson(user));
            
            url = "http://192.168.0.111:8081/v1/"+user.getScopeId()+"/devices?offset=0&limit=50";
            
            System.out.println(url);
            
            String result = HttpHelper.httpGet(url, user.getTokenId());
            
           
            
            if(result != null)
            {
            	DeviceResult dr = g.fromJson(result, DeviceResult.class);
            	
            	System.out.println(g.toJson(dr));
            }
            
            url = "http://192.168.0.111:8081/v1/"+user.getScopeId()+"/data/channels";
            
            System.out.println(url);
            
            result = HttpHelper.httpGet(url, user.getTokenId());
            
            ChannelsResult cr = g.fromJson(result, ChannelsResult.class);
            
            result = g.toJson(cr);
            
            System.out.println(result);
            
            ChannelInfo ci = cr.getItems()[1];
            
            url = "http://192.168.0.111:8081/v1/"+user.getScopeId()+"/data/messages?";
            url += "scopeId=" + URLEncoder.encode(user.getScopeId(),"UTF-8");
            url += "&clientId=" + URLEncoder.encode(ci.getClientId(),"UTF-8");
            url += "&channel=" + URLEncoder.encode(ci.getName(),"UTF-8");
            url += "&strictChannel=true";
            //url += "&startDate=" + URLEncoder.encode("2018-01-19T03:15:05.903","UTF-8");
            //url += "&endDate=" + URLEncoder.encode("2019-09-19T03:15:05.903Z","UTF-8");
            url += "&offset=0";
            url += "&limit=1";
            //url += "&datastoreMessageId=19d4a58a-6c9f-4f9a-a4d7-7172f6002cd8";
           // url = "http://192.168.0.111:9200/1475789760546563171-*/message/_search";
            
            System.out.println(url);
            
            result = HttpHelper.httpGet(url, user.getTokenId());
            
            System.out.println("result:" + result);
        }
        else
        {
        	System.out.println("Empty Result!");
        }
        
    }
}
