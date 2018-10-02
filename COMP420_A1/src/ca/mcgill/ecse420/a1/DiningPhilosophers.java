package ca.mcgill.ecse420.a1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
	
	public static void main(String[] args) {

		int numberOfPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
        ReentrantLock[] chopsticks = new ReentrantLock[numberOfPhilosophers];
        
        // Instantiate chopsticks
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new ReentrantLock();
        }
 
        // Instantiate philosophers and assign chopsticks
        for (int i = 0; i < philosophers.length; i++) {
        	ReentrantLock leftchopstick = chopsticks[i];
        	ReentrantLock rightchopstick = chopsticks[(i + 1) % chopsticks.length];
 
            philosophers[i] = new Philosopher(leftchopstick, rightchopstick);
             
            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.setPriority(5);
            t.start();
        }
	}

	public static class Philosopher implements Runnable {
		private ReentrantLock leftCS;
		private ReentrantLock rightCS;

		public Philosopher(ReentrantLock lf, ReentrantLock rf){
			this.leftCS = lf;
			this.rightCS = rf;
		}

		@Override
		public void run() {
			try {
				while(true){
					if (pickUpChopStick(leftCS)){
						if (pickUpChopStick(rightCS)){
							doAction("Eating");
							putDownChopStick(rightCS);
							putDownChopStick(leftCS);
							Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Reset thread priority to minimum
						}
						else{
							putDownChopStick(leftCS);
						}
					}
					else{
						doAction("Thinking");
						Thread.currentThread().setPriority(Math.min(Thread.currentThread().getPriority() + 1, Thread.MAX_PRIORITY)); // Augment thread priority
					}
				}
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
			}
		}
		
		/**
		 * Try lock on specified chopstick
		 * @param cs
		 * @return Whether or not the lock was acquired
		 * @throws InterruptedException
		 */
		private boolean pickUpChopStick(ReentrantLock cs) throws InterruptedException{
			if (cs.tryLock(10, TimeUnit.MILLISECONDS)){
				if (cs == this.leftCS)
					doAction("Pick up left chopstick");
				else if (cs == this.rightCS)
					doAction("Pick up right chopstick");
				return true;
			}
			else{
				return false;
			}
		}
		
		/**
		 * Unlock specified chopstick
		 * @param cs
		 * @throws InterruptedException
		 */
		private void putDownChopStick(ReentrantLock cs) throws InterruptedException{
			cs.unlock();
			if (cs == this.leftCS)
				doAction("Put down left chopstick");
			else if (cs == this.rightCS)
				doAction("Put down right chopstick");
		}

		private void doAction(String action) throws InterruptedException{
			System.out.println(Thread.currentThread().getName() + " : " + action);
			Thread.sleep(((int) (Math.random() * 100)));
		}
	}
	
	/**
	 * Philosopher class for Question 3.2 - No deadlock but possible starvation
	 * @author natha
	 *
	 */
	public static class Philosopher_2 implements Runnable {
		private ReentrantLock leftCS;
		private ReentrantLock rightCS;

		public Philosopher_2(ReentrantLock lf, ReentrantLock rf){
			this.leftCS = lf;
			this.rightCS = rf;
		}

		@Override
		public void run() {
			try {
				while(true){
					if (pickUpChopStick(leftCS)){
						if (pickUpChopStick(rightCS)){
							doAction("Eating");
							putDownChopStick(rightCS);
							putDownChopStick(leftCS);
						}
						else{
							putDownChopStick(leftCS);
						}
					}
					else{
						doAction("Thinking");
					}
				}
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
			}
		}
		
		/**
		 * Try lock on specified chopstick with 10 millisecond timeout
		 * @param cs
		 * @return Whether or not the lock was acquired
		 * @throws InterruptedException
		 */
		private boolean pickUpChopStick(ReentrantLock cs) throws InterruptedException{
			if (cs.tryLock(10, TimeUnit.MILLISECONDS)){
				if (cs == this.leftCS)
					doAction("Pick up left chopstick");
				else if (cs == this.rightCS)
					doAction("Pick up right chopstick");
				return true;
			}
			else{
				return false;
			}
		}
		
		/**
		 * Unlock specified chopstick
		 * @param cs
		 * @throws InterruptedException
		 */
		private void putDownChopStick(ReentrantLock cs) throws InterruptedException{
			cs.unlock();
			if (cs == this.leftCS)
				doAction("Put down left chopstick");
			else if (cs == this.rightCS)
				doAction("Put down right chopstick");
		}

		/**
		 * Print performed action and wait a random amount of time 
		 * @param action
		 * @throws InterruptedException
		 */
		private void doAction(String action) throws InterruptedException{
			System.out.println(Thread.currentThread().getName() + " : " + action);
			Thread.sleep(((int) (Math.random() * 100)));
		}
	}
	
	/**
	 * Philosopher class for Question 3.1 - No deadlock prevention
	 * @author natha
	 *
	 */
	public static class Philosopher_1 implements Runnable {
		private Object leftchopstick;
		private Object rightchopstick;

		public Philosopher_1(Object lf, Object rf){
			this.leftchopstick = lf;
			this.rightchopstick = rf;
		}

		@Override
		public void run() {
			try {
				while(true){
					doAction("Thinking");
					synchronized(leftchopstick){
						doAction("Pick up left chopstick");
						synchronized(rightchopstick){
							doAction("Pick up right chopstick");
							doAction("Eating");
							doAction("Put down right chopstick");
						}
						doAction("Put down left chopstick");
					}
				}
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
			}
		}

		/**
		 * Print performed action and wait a random amount of time 
		 * @param action
		 * @throws InterruptedException
		 */
		private void doAction(String action) throws InterruptedException{
			System.out.println(Thread.currentThread().getName() + " : " + action);
			Thread.sleep(((int) (Math.random() * 100)));
		}
	}

}
