package io.hedwig.concurrence.basic.issues;

import java.util.concurrent.TimeUnit;

public class ReentrantLock1 {

  synchronized void m1() {
    for (int i = 0; i < 10; i++) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(i);
    }
  }

  void m3(){
    m1();
  }

  synchronized void m2() {
    System.out.println("m2......");
  }

  public static void main(String[] args) {
    ReentrantLock1 r1 = new ReentrantLock1();
    new Thread(r1::m1).start();
    new Thread(r1::m3).start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    new Thread(r1::m2).start();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    new Thread(r1::m2).start();
  }
}