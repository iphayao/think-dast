package com.phayao.thinkdast;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

public class JedisMaker {

    public static Jedis make() throws IOException {
        // assemble the directory name
        String slash = File.separator;
        //String fileName = "resources" + slash + "redis_url.txt";
        String fileName = "redis_url.txt";
        URL fileURL = JedisMaker.class.getClassLoader().getResource(fileName);
        String filePath = URLDecoder.decode(fileURL.getFile(), "UTF-8");

        // open the file
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            printInstruction();
            return null;
        }

        // read the file
        while (true) {
            String line = br.readLine();
            if(line == null) break;
            sb.append(line);
        }
        br.close();

        // parse the URL
        URI uri;
        try {
            uri = new URI(sb.toString());
        }
        catch (URISyntaxException e) {
            System.out.println("Reading file: " + fileName);
            System.out.println("It looks like this file does not contain a valid URL.");
            printInstruction();
            return null;
        }

        String host = uri.getHost();
        int port = uri.getPort();

        String[] array = uri.getAuthority().split("[:@]");
        String auth = array[1];

        // connect to the server
        Jedis jedis = new Jedis(host, port);

        try {
            jedis.auth(auth);
        }
        catch (Exception e) {
            System.out.println("Trying to connect to " + host);
            System.out.println("on port " + port);
            System.out.println("with authcode " + auth);
            System.out.println("Get exception" + e);
            printInstruction();
            return null;
        }
        return jedis;

    }

    private static void printInstruction() {
        System.out.println("");
        System.out.println("To connect to RedisToGo, you have to provide a file called");
        System.out.println("redis_url.txt that contains the URL of your Redis server.");
        System.out.println("If you select a instance on the RedisToGo web page, ");
        System.out.println("you should see a URL that contains the information you need");
        System.out.println("redis://redistogo:AUTH@HOST:PORT");
        System.out.println("Create a file called redis_url.txt in the src/resources");
        System.out.println("directory, and paste in URL.");
    }

    public static void main(String[] args) throws IOException {
        Jedis jedis = make();

        // String
        jedis.set("mykey", "myvalue");
        String value = jedis.get("mykey");
        System.out.println("Get value: " + value);

        // Set
        jedis.sadd("myset", "element1", "element2", "element3");
        System.out.println("elements is member: " + jedis.sismember("myset", "element2"));

        // List
        jedis.rpush("myList", "element1", "element2", "element3");
        System.out.println("element at index 1: " + jedis.lindex("mylist", 1));

        // Hash
        jedis.hset("myhash", "word1", Integer.toString(2));
        jedis.hincrBy("myhash", "word2", 1);
        System.out.println("frequency of word1: " + jedis.hget("myhash", "word1"));
        System.out.println("frequency of word2: " + jedis.hget("myhash", "word2"));

        jedis.close();
    }
}
