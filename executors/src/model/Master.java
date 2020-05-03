package model;

import java.util.concurrent.ExecutionException;

import utilities.Tuple2;

public interface Master {
	 int compute(Tuple2<String, Integer> initTuple, int dl) throws InterruptedException, ExecutionException;
}
