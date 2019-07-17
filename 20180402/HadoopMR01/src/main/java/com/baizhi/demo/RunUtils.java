package com.baizhi.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/4/16.
 */
public class RunUtils {
    public static void main(String[] args) throws IOException, InterruptedException {
        Process ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c",
                "hadoop jar  ipcount.jar com.baizhi.demo.CustermJobsubmitter"});
        ps.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String result = sb.toString();
        System.out.println(result);
    }
}
