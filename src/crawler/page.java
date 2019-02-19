/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

/**
 *
 * @author yash1
 */
public class page {
    private String s;
    private double val;

    public String getS() {
        return s;
    }

    public double getVal() {
        return val;
    }

    public page(String s, double val)
    {
        this.s = s;
        this.val=val;
    }
}
