package com.online.J2SE.design_pattern.command;

public class Client {
    public static void main(String[] args) {

        Recevier recevier = new Recevier();
        Command command = new ConcreteCommand(recevier);

        Invoker invoker = new Invoker();

        invoker.setCommand(command);
        invoker.action();
    }
}
