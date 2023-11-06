package com.crio.shorturl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XUrlImpl implements XUrl{

    private static Map<String,String> urlMap;//key,value-shortUrl,longUrl
    private static Map<String,Integer> hitCountMap;//key,value-longUrl,count
    private static String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String URL_FORMAT="http://short.url/";

    public XUrlImpl(){
        urlMap=new HashMap<>();
        hitCountMap=new HashMap<>();
    }

    private String isLongUrlPresent(String longUrl){
        List<String> shortUrl = urlMap.entrySet().stream().filter(entry->entry.getValue().equals(longUrl)).map(entry->entry.getKey()).collect(Collectors.toList());
        return shortUrl.isEmpty() ? null : shortUrl.get(0);
    }

    private String generateCodeForShortUrl(){
        int characterCount=9;
        StringBuilder urlCode = new StringBuilder("");
        while(urlCode.toString().length()==0 || urlMap.get(URL_FORMAT+urlCode)!=null){
            urlCode = new StringBuilder("");
            for(int i=0;i<characterCount;i++){
                urlCode.append(AlphaNumericString.charAt((int)(Math.random()*AlphaNumericString.length())));
            }
        }
        return urlCode.toString();
    }

    @Override
    public String registerNewUrl(String longUrl) {
        String shortUrl = isLongUrlPresent(longUrl);
        if(shortUrl==null){
            shortUrl=URL_FORMAT+generateCodeForShortUrl();
            urlMap.put(shortUrl,longUrl);
            hitCountMap.put(longUrl, 0);
        }
        return shortUrl;
    }

    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        if(urlMap.get(shortUrl)!=null){
            return null;
        }
        urlMap.put(shortUrl, longUrl);
        return shortUrl;
    }

    @Override
    public String getUrl(String shortUrl) {
        String longUrl=urlMap.get(shortUrl);
        if(longUrl!=null){
            hitCountMap.put(longUrl, hitCountMap.getOrDefault(longUrl,0)+1);
        }
        return longUrl;
    }

    @Override
    public Integer getHitCount(String longUrl) {
        Integer count = hitCountMap.get(longUrl);
        return count!=null ? count : 0;
    }

    @Override
    public String delete(String longUrl) {
        return urlMap.remove(isLongUrlPresent(longUrl));
    }
    
}