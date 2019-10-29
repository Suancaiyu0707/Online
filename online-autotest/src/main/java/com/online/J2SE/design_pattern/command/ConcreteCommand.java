package com.online.J2SE.design_pattern.command;

public class ConcreteCommand implements Command {
    public Recevier recevier;
    public ConcreteCommand(Recevier recevier){
        this.recevier=recevier;
    }
    @Override
    public void execute() {
        recevier.action();
    }
}
