package com.taest.mynetdisk.util;

import cn.hutool.core.io.IoUtil;
import com.taest.mynetdisk.dto.CommandResult;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommandUtils {
    public static CommandResult exec(String cmd) {
        String[] command = {"/bin/sh", "-c", cmd};
        CommandResult commandResult = new CommandResult();
        try {
            Process pro = Runtime.getRuntime().exec(command);
            InputStream inputStream = pro.getInputStream();
            InputStream errorStream = pro.getErrorStream();
            String result = IOUtils.toString(inputStream);
            String err = IOUtils.toString(errorStream);
            if ("".equals(err)) {
                commandResult.setResult(result);
            } else {
                commandResult.setErr(err);
                commandResult.setSuccess(false);
            }
            return commandResult;
        } catch (IOException e) {
            commandResult.setErr("IOException!");
            commandResult.setSuccess(false);
            return commandResult;
        }
    }

    public static void main(String[] args) throws IOException {
//        String a = "/home/taest/tmpdir/wallhaven-wyzzlp.jpg";
//        int i = a.lastIndexOf("/");
//        a = a.substring(0,i);
//        System.out.println("a = " + a+ File.separator+ "asd");
//        String cmd = "mv /home/taest/tmpdir/eyykl8.jpg /home/taest/tmpdir/ee.jpg";
//        CommandResult exec = CommandUtils.exec(cmd);
//        System.out.println("exec = " + exec.isSuccess());
        File file = new File("/home/taest/tmpdir/ee.jpg");
        System.out.println("file.getParentFile() = " + file.exists());
        System.out.println("file.getParentFile() = " + file.getParent());

//        OutputStream outputStream = pro.getOutputStream();
    }
}
