package com.chendu.jq.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class Adhoc {

    @Test
    public void testSubStr(){
        String name="xyzuai";
        System.out.println(name.substring(1, name.length()));
    }

    @Test
    public void testRegularExpression(){
        Pattern pattern = Pattern.compile("Windows.*余额(?!b|c)");
        System.out.println(pattern.matcher("Windows31余额").matches());
        System.out.println(pattern.matcher("Windows余额b").matches());
        System.out.println(pattern.matcher("Windows余额c").matches());
    }

    @Test
    public void printIterative() throws Exception{
        String path = "folder";
        List<File> allFiles = new ArrayList<>();

        for (File f:new File(path).listFiles()
                ) {
            getFilesIteratively(f, allFiles);
        }

        File ofile = new File("D:\\description.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(ofile));

        for (File file:allFiles
                ) {
            if(file.getName().endsWith("java")) {
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line:lines
                        ) {
                    if(line.length() > 0) {
                        bw.write(line);
                        bw.newLine();
                    }
                }
            }
        }

        bw.close();
    }

    private void getFilesIteratively(File file, List<File> allFiles){
        if(file.listFiles() == null){
            allFiles.add(file);
            return ;
        }
        else{
            for (File f:file.listFiles()
                    ) {
                getFilesIteratively(f, allFiles);
            }
        }
    }

    @Test
    public void getEncryptedPwd(){
        String pwd = "this is my password";
        String rpwd = StringUtils.reverse(pwd);
        System.out.println(rpwd);
        System.out.println(Base64Utils.encodeToString(rpwd.getBytes()));
    }
}
