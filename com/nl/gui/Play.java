package com.nl.gui;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.nl.launcher.NuovoGenericError;
import com.nl.launcher.Launcher;

public class Play {
  public static void play_mc() throws IOException, ParseException, NuovoGenericError, InterruptedException {
	  String[] version = {"1.7.10", "Vanilla"};
	  Launcher.main(version);
  }
  public static void main(String[] args) throws IOException, ParseException, NuovoGenericError, InterruptedException {
	  play_mc();
  }
}

