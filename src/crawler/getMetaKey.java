/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author yash1
 */
class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }
    public int compare(String a, String b) {
        if (base.get(a)<  base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}
public class getMetaKey {
    
    Map<String, Integer> map = new HashMap<String, Integer>();
    ValueComparator bvc = new ValueComparator(map);
    TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
    public  void  set(String body)
    {
        body.replaceAll("\\b\\w{1,4}\\b\\s?", "");
        String[] word = body.split(" ");
        for (String s : word) {
        if (map.containsKey(s)) {
            map.put(s, map.get(s) + 1);
        } else {
            map.put(s, 1);
        }
    }
    sorted_map.putAll(map);
    
    }
    public  String getMeta()
    {
        Set set = sorted_map.entrySet();
        Iterator it = set.iterator();
        StringBuilder sb = new StringBuilder();
        int i=0;
        while(it.hasNext() && i<10) {
        Map.Entry me = (Map.Entry)it.next();
        sb.append(me.getKey());
        sb.append(" ");
        //System.out.print("Key is: "+me.getKey() + " & ");
        //System.out.println("Value is: "+me.getValue());
        i++;
        }
        return sb.toString();
    } 
    public String getKeyword()
    {
        Set set = sorted_map.entrySet();
        Iterator it = set.iterator();
        StringBuilder sb = new StringBuilder();
        int i=0;
        while(it.hasNext() && i<4) {
        Map.Entry me = (Map.Entry)it.next();
        sb.append(me.getKey());
        sb.append(" ");
        //System.out.print("Key is: "+me.getKey() + " & ");
        //System.out.println("Value is: "+me.getValue());
        i++;
        }
        return sb.toString();
    }
}
