package com.nl.launcher;

import java.io.InputStream;
import java.util.function.Consumer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class StreamGobbler  implements Runnable {
	    private InputStream inputStream;
	    private Consumer<String> consumer;
	 
	    public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
	        this.inputStream = inputStream;
	        this.consumer = consumer;
	    }
	 
	    @Override
	    public void run() {
	        new BufferedReader(new InputStreamReader(inputStream)).lines()
	          .forEach(consumer);
	    }
}
