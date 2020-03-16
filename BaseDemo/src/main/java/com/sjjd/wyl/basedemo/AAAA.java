package com.sjjd.wyl.basedemo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wyl on 2020/2/24.
 */
public class AAAA {


    /**
     * @author xiyou
     * @version 1.2
     * @date 2019/11/6 11:45
     * 根据字符串文件相对路径生成文件树结构
     */


    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static Boolean isExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }


    /***
     * 获取一个文件夹下面所有文件的方法
     * @param path
     * @return
     */
    public static Set<String> getAllFileName(String path) {
        Boolean b = isExist(path);
        Set<String> result = new HashSet<>();
        if (b == true) {
            result = getAllFileName(path, result);
        }
        return result;
    }

    /**
     * 递归获取某个文件夹下的所有文件
     *
     * @param fileNameList 存放文件名称的list
     * @param path         文件夹的路径
     * @return
     */
    public static Set<String> getAllFileName(String path, Set<String> fileNameList) {
        boolean flag = false;
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                fileNameList.add(tempList[i].getName());
            }
            if (tempList[i].isDirectory()) {
                fileNameList = getAllFileName(tempList[i].getAbsolutePath(), fileNameList);
            }
        }
        return fileNameList;
    }


    public static String getType(String filename, String path) {
        if (path.endsWith(filename)) {
            return "file";
        }
        return "folder";
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        StringBuffer tempString = null;
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));

            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((reader.readLine()) != null) {
                // 显示行号
                tempString = tempString.append(reader.readLine());
                System.out.println("line " + line + ": " + tempString.toString());
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return tempString.toString();
    }


    public static void addPath(HashMap<String, Object> root, String path, String fileId) {
        String url = "";
        StringBuffer urlBuffer = new StringBuffer();
        if (path.charAt(0) == (char) '/') {
            path = path.substring(1, path.length());
        }
        String[] pathArr = path.split("/");
        for (int i = 0; i < pathArr.length; i++) {
            String name = pathArr[i];
            if (i == 0) {
                urlBuffer.append(name);
            } else {
                url += "/" + name;
                urlBuffer.append("/").append(name);
            }
            url = urlBuffer.toString();

            boolean flag = true;
            for (HashMap<String, Object> node : (ArrayList<HashMap<String, Object>>) root.get("content")) {

                if (node.get("name").equals(name)) {
                    root = node;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                HashMap<String, Object> newNode = new HashMap<>(8);
                newNode.put("name", name);
                String type = getType(name, path);
                if (i < pathArr.length - 1) {
                    type = "folder";
                } else {
                    newNode.put("fileId", fileId);
                }
                newNode.put("type", type);
                newNode.put("url", url);
                newNode.put("content", new ArrayList<HashMap<String, Object>>());
                ((ArrayList<HashMap<String, Object>>) root.get("content")).add(newNode);
                root = newNode;
            }
        }
    }

    public static HashMap<String, Object> generateData(List<Map<String, String>> mapList) {
        HashMap<String, Object> root = new HashMap<>(8);
        root.put("name", "");
        root.put("url", "");
        root.put("type", "");
        root.put("fileId", "");
        ArrayList<String> arrayList = new ArrayList<>();
        root.put("content", arrayList);
        String onePath = "";
        String oneFileId = "";
        for (int i = 0; i < mapList.size(); i++) {
            oneFileId = mapList.get(i).get("file_id");
            onePath = zhuanYi(mapList.get(i).get("user_real_path"));

            addPath(root, onePath, oneFileId);
        }

        return root;
    }

    public static String readFile(String pathname) throws Exception {
        String str = "";
        File file = new File(pathname);
        try {
            FileInputStream in = new FileInputStream(file);
            // size  为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();

            str = new String(buffer, "GB2312");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;

        }

        return str;


    }


    /****
     * 1. 主分析程序，拿到路径总的字符串，进行分析
     * @param path
     * @return
     */

    public static String zhuanYi(String path) {

        //将双引号去掉，将多余的空字符去掉
        path = path.replaceAll("\"", "").replaceAll(" ", "");
        if (path.contains("\\\\")) {
            path = path.replaceAll("\\\\", "/");
//            System.out.println("1把\\\\转成/ 之后的:     "+path);
        }

        if (path.contains("\\")) {
            path = path.replaceAll("\\\\", "/");
//            System.out.println("2把单斜杠转化为/之后的:           "+path);
        }

        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
//            System.out.println("3把//变成/之后的     "+path);
        }
        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
//            System.out.println("4再次把//变成/之后的     "+path);
        }

        return path;
    }

    public static void main(String[] args) throws Exception {
        List<Map<String, String>> mapList = new ArrayList<>(8);
        Map<String, String> one = new HashMap<>(4);
        one.put("file_id", "A");
        one.put("user_real_path", "E:\\CONETNEW\\upload\\390208028533862400\\3dstool\\bin\\A.java");
        mapList.add(one);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("单个绝对路径", generateData(mapList));
        System.out.println(jsonObject);
        System.out.println("----------------");
        Map<String, String> two = new HashMap<>(4);
        two.put("file_id", "B");
        two.put("user_real_path", "E:\\CONETNEW\\upload\\390208028533862400\\3dstool\\dep\\include\\B.java");
        mapList.add(two);
        JSONObject jsonObject2 = new JSONObject();

        jsonObject2.put("2个绝对路径", generateData(mapList));
        System.out.println(jsonObject2);
        System.out.println("----------------");
        Map<String, String> three = new HashMap<>(4);
        three.put("file_id", "C");
        three.put("user_real_path", "E:\\CONETNEW\\upload\\390208028533862400\\3dstool\\dep\\include\\linux_x86_32\\curl\\C.java");
        mapList.add(three);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("3个绝对路径", generateData(mapList));
        System.out.println(jsonObject3);

    }

}
