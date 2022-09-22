package ppc_a2;

import java.util.concurrent.RecursiveTask;

public class ForkJoinCoin extends RecursiveTask<Integer>{

	private static final long serialVersionUID = 1L;
	private int [] coins;
	private int index;
	private int accumulator;

	public ForkJoinCoin(int[]coins, int index, int accumulator) {
		this.coins=coins;
		this.index=index;
		this.accumulator= accumulator;

	}

	@Override
	protected Integer compute() {

		if (index >= coins.length) {
			if (accumulator < Coin.getLimit()) {
				return accumulator;
			}
			return -1;
		}
		if (accumulator + coins[index] > Coin.getLimit()) {
			return -1;
		}
		if ( RecursiveTask.getSurplusQueuedTaskCount() > 2 ) return Coin.seq(coins, index, accumulator);
		
		ForkJoinCoin f1 = new ForkJoinCoin(coins, index +1, accumulator);
		f1.fork();
		ForkJoinCoin f2 = new ForkJoinCoin(coins, index +1, accumulator + coins[index]);
		f2.fork();
		
		int a = f1.join();
		int b = f2.join();
		
		return Math.max(a,  b);
		

	}
	



}
