package com.online.J2SE.design_pattern.command;

public class Invoker {
    private Command command;
    public void setCommand(Command command){
        this.command=command;
    }

    public void action(){
        this.command.execute();
    }
}
