package com.OOMDemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLocalTest {
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final ReentrantLock lock = new ReentrantLock();
	public static final ThreadLocal<SimpleDateFormat> LOCAL = new ThreadLocal<>();
	
	public static class formatDate implements Runnable{
		int i;
		public formatDate(int i){
			this.i=i;
		}
		@Override
		public void run() {
			try {
				lock.lock(); // 方法1：使用重入锁
				lock.unlock();
				LOCAL.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));//方法2：使用ThreadLocal
				Date date = LOCAL.get().parse("2015-10-1 10:10:"+i%60);
//				Date date = format.parse("2015-10-1 10:10:"+i%60);
				System.out.println(i+":"+date);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		for(int i=0;i<1000;i++){
			formatDate date1 = new formatDate(i);
			newFixedThreadPool.execute(date1);
//			Future<String> submit = (Future<String>) newFixedThreadPool.submit(new formatDate(i));
		}
		System.out.println("线程:" + Thread.currentThread().getId()+"-结束LOCAL.get():"+LOCAL.get());
		Thread.sleep(2000);
	}
}
