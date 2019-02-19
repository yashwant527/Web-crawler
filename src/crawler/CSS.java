/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author yash1
 */
public class CSS {
    String s1,s2;
    public static double consineTextSimilarity(String[] left, String[] right) {
        Map<String, Integer> leftWordCountMap = new HashMap<String, Integer>();
        Map<String, Integer> rightWordCountMap = new HashMap<String, Integer>();
        Set<String> uniqueSet = new HashSet<String>();
        Integer temp = null;
        for (String leftWord : left) {
            temp = leftWordCountMap.get(leftWord);
            if (temp == null) {
                leftWordCountMap.put(leftWord, 1);
                uniqueSet.add(leftWord);
            } else {
                leftWordCountMap.put(leftWord, temp + 1);
            }
        }
        for (String rightWord : right) {
            temp = rightWordCountMap.get(rightWord);
            if (temp == null) {
                rightWordCountMap.put(rightWord, 1);
                uniqueSet.add(rightWord);
            } else {
                rightWordCountMap.put(rightWord, temp + 1);
            }
        }
        int[] leftVector = new int[uniqueSet.size()];
        int[] rightVector = new int[uniqueSet.size()];
        int index = 0;
        Integer tempCount = 0;
        for (String uniqueWord : uniqueSet) {
            tempCount = leftWordCountMap.get(uniqueWord);
            leftVector[index] = tempCount == null ? 0 : tempCount;
            tempCount = rightWordCountMap.get(uniqueWord);
            rightVector[index] = tempCount == null ? 0 : tempCount;
            index++;
        }
        return consineVectorSimilarity(leftVector, rightVector);
    }


    private static double consineVectorSimilarity(int[] leftVector,int[] rightVector) {
        if (leftVector.length != rightVector.length)
             return 1;
         double dotProduct = 0;
        double leftNorm = 0;
        double rightNorm = 0;
        for (int i = 0; i < leftVector.length; i++) {
            dotProduct += leftVector[i] * rightVector[i];
            leftNorm += leftVector[i] * leftVector[i];
            rightNorm += rightVector[i] * rightVector[i];
            }

            double result = dotProduct/ (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
        return result;
    }

    public void set(String s1,String s2) {
        this.s1 = s1.toLowerCase();
        this.s2 = s2.toLowerCase();
    }
    public double calculate()
    {
        String[] left = s1.split(" ");    
        String[] right = s2.split(" ");    

        return consineTextSimilarity(left,right);
    }
}
