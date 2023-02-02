package com.httpapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.httpapp.model.Address;
import com.httpapp.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    public static void main( String[] args )
    {
        Logger logger = LogManager.getLogger(App.class);

        logger.warn("[SessionId: {}] Missing or Invalid parameter [Token]: {}", "arg1", "arg2");
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/users");
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String line = "";
            StringBuilder json = new StringBuilder();
            while (line != null) {
                line = inputStream.readLine();
                if(line != null) json.append(line);
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            // List<User> users = new ArrayList<>();
//            gson.fromJson(json.toString(), ArrayList.class).stream().forEach(object -> {
//                Map <String, Object> item = (LinkedTreeMap) object;
//                users.add(new User(
//                        String.valueOf(item.get("id")),
//                        item.get("name").toString(),
//                        item.get("username").toString(),
//                        item.get("email").toString(),
//                        Address.fromMap((Map<String, Object>) item.get("address")),
//                        item.get("phone").toString(),
//                        item.get("website").toString(),
//                        (Map) item.get("company")
//                ));
//            });
            User[] users = gson.fromJson(json.toString(), User[].class);
            System.out.println("--- Before sorting users are ordered viz ---");
            Arrays.stream(users).forEach(user -> System.out.println(user));
            System.out.println("");
            System.out.println("--- After sorting by names, users are ordered viz ---");
            Arrays.sort(users);
            Arrays.stream(users).forEach(user -> System.out.println(user));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
