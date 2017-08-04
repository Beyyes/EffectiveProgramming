
package test;

import java.util.concurrent.TimeUnit;

public class StopThread {
   

    private static boolean flag = false;
    
    //　同步保证一段时间只有一个线程访问此
    private static synchronized boolean threadStop() {
        return flag;
    }
        
    private static synchronized void stopThread() {
        flag = true;
    }
    
    // private static synchronized boolean vis;
    
    public static void main(String[] args) throws InterruptedException {
        
        Thread s1 = new Thread( new Runnable() {
            int i = 0;
        	public void run() {
                while(!threadStop()) {  // 只有单个变量 while(!flag)编译器才会优化为if(!flag) { while(true) }
                    System.out.println("..");
                	i++;
                }
            }
        });

        s1.start();

        TimeUnit.SECONDS.sleep(1);

        stopThread();
    }
}

