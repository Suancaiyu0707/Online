package com.online.concurrent;

import java.util.concurrent.ThreadLocalRandom;

public class CsGameByThreadLocal {
    private static final Integer BULLET_NUMBER = 1500;
    private static final Integer KILLED_ENEMIES = 0;
    private static final Integer LIFE_VALUE = 10;
    private static final Integer TOTAL_PLAYERS = 3;
    private static final User user = new User();

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    //用ThreadLocal包装的变量，那么点那个一个线程来访问这个变量的时候，它会根据这个变量为访问线程复制对应的副本到访问线程里，这样访问线程操作这个变量的时候，实际操作的是自己内部线程的副本

    // //静态的变量BULLET_NUMBER_THREADLOCAL，里面包装了实际的值，可以用BULLET_NUMBER_THREADLOCAL.get()获得包装的值
    //    //这个值是一个ThreadLocal，所以当一个线程调用这个变量的时候，它会在这个线程里复制一份变量，也就是BULLET_NUMBER_THREADLOCAL的副本，它们是独立的，
    //    // 如果这个BULLET_NUMBER_THREADLOCAL是一个引用的话，副本复制的是引用，如果复制的是值的话，副本复制的是基本类型的值
    private static final ThreadLocal<Integer> BULLET_NUMBER_THREADLOCAL = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return BULLET_NUMBER;
        }
    };
    // 如果这个BULLET_NUMBER_THREADLOCAL是一个引用的话，副本复制的是引用，如果复制的是值的话，副本复制的是基本类型的值
    //所以下面的player-0、player-1、player-2这三个线程，虽然都是从CsGameByThreadLocal.user复制一份引用的副本到线程自身里，但是这三个副本和CsGameByThreadLocal.user指向的是同一份数据
    //所以任何一个线程修改了这个引用里的值，别的副本都会收到影响
    private static final ThreadLocal<User> User_THREADLOCAL = new ThreadLocal<User>(){
        @Override
        protected User initialValue(){
            return user ;
        }
    };



    private static final ThreadLocal<Integer> KILLED_ENEMIES_THREADLOCAL = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return KILLED_ENEMIES;
        }
    };
    private static final ThreadLocal<Integer> LIFE_VALUE_THREADLOCAL = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue(){
            return LIFE_VALUE;
        }
    };

    static class Player extends Thread{
        private String playerName;


        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public Player(String playerName){
            this.playerName = playerName;
        }
        @Override
        public void run() {

            Integer bullets = BULLET_NUMBER_THREADLOCAL.get();
            Integer killEnemies = KILLED_ENEMIES_THREADLOCAL.get();
            Integer lifeValue = LIFE_VALUE_THREADLOCAL.get();
            if(this.playerName.equals("player-0")){
                //这个拿的是线程player0从CsGameByThreadLocal里根据引用user复制出来的新的引用，它和CsGameByThreadLocal里引用user是指向同一个对象，因为，如果ThreadLocal修饰的对象是引用的话，复制的是一个引用，而不是直接复制值
                User user = User_THREADLOCAL.get();
                user.setAge(20);//线程player-0修改的是引用副本里的值，由于player-0里复制出来的副本，和player-1、player-2里的副本引用指向的是同一个对象，所以其它两个线程里副本引用指向的内容也会受影响
                BULLET_NUMBER_THREADLOCAL.set(BULLET_NUMBER_THREADLOCAL.get()-50);//线程player-0修改的是副本值，这个值本身就是一个基本类型，由于player-0里复制出来的基本类型的副本，和player-1、player-2里的副本引用指向的不是同一个值，所以其它两个线程里副本引用指向的内容不会受影响
                KILLED_ENEMIES_THREADLOCAL.set(KILLED_ENEMIES_THREADLOCAL.get());
                LIFE_VALUE_THREADLOCAL.set(LIFE_VALUE_THREADLOCAL.get()-2);
            }

            System.out.println (this.getPlayerName() + ", BULLET_NUMBER is " + BULLET_NUMBER_THREADLOCAL.get()) ;
                    System.out.println(this.getPlayerName() + ", KILLED_ENEMIES is " + KILLED_ENEMIES_THREADLOCAL.get()) ;
                            System.out.println (this.getPlayerName() + ", LIFE_VALUE is " + LIFE_VALUE_THREADLOCAL.get() );
                            System.out.println (this.getPlayerName() + ", User age is " + User_THREADLOCAL.get().getAge() + "\n");
            BULLET_NUMBER_THREADLOCAL.remove ();//这个方法调用是会调用Thread.currentThread()里的ThreadLocalMap里把这个副本remove掉，可以避免在多线程里，由于线程复用导致把旧的数据绑定过去，造成脏数据现象
            KILLED_ENEMIES_THREADLOCAL.remove();
            LIFE_VALUE_THREADLOCAL.remove();
            User_THREADLOCAL.remove();
        }
    }

    public static void main(String[] args) {
        for(int i =0;i<TOTAL_PLAYERS;i++){
            new Player("player-"+i).start();
        }
    }
}


