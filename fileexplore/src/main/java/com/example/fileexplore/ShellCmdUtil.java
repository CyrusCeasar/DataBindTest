package com.example.fileexplore;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chenlei2 on 2016/9/8 0008.
 */
public class ShellCmdUtil {

    public static void do_exec(final String cmd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = "/n";
                try {
                    Process p = Runtime.getRuntime().exec(cmd);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(p.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        s += line + "/n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(ShellCmdUtil.class.getSimpleName(),"cmd:--->"+cmd+"*****content****"+s);
            }
        }).start();
    }
}
