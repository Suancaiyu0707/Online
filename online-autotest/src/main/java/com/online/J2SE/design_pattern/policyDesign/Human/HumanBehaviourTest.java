package com.online.J2SE.design_pattern.policyDesign.Human;

public class HumanBehaviourTest {
    public static void main(String[] args) {
        AbstractHumanBehaviour behaviour = new MaleBehaviour();
        behaviour.clothes();
        behaviour.food();
        AbstractHumanBehaviour femaleBehaviour = new FemaleBehaviour();
        femaleBehaviour.clothes();
        femaleBehaviour.food();
        AbstractHumanBehaviour childBehaviour = new ChildBehaviour();
        childBehaviour.clothes();
        childBehaviour.food();

    }
}
