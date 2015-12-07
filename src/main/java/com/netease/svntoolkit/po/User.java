package com.netease.svntoolkit.po;

import org.simpleframework.xml.Element;

import java.io.Console;
import java.util.Scanner;

public class User {
    @Element
    private String name;
    @Element(required = false)
    private String password;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        if (password == null || password.length() == 0) {
            System.out.println("input the password:");
            Console console = System.console();
            if (console == null) {
                Scanner scanner = new Scanner(System.in);
                password = scanner.nextLine();
            } else {
                char[] inputs = console.readPassword();
                password= new String(inputs);
            }
        }
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
