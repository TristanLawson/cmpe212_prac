import java.util.Scanner;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

public class Assn2_16tjl4 {
	
	//reads csv file, returns 2D array of doubles
	public static Double[][] readData() {
		Double[][] MotorData = new Double[1000][8];
		try {
			Scanner read;
			read = new Scanner (new File("logger.csv"));
			
			for(int i=0;i<1000;i++) {
				String temp = read.nextLine();
				String[] row = temp.split(",");
				
				for(int j=0; j<8; j++) {
					MotorData[i][j] = Double.parseDouble(row[j]);
				}
			}
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return MotorData;
	}
	
	//return time value corresponding to end of a pulse
	public static int pulseUntil(int time, Double motorData[][],int motorNum) {
		while(motorData[time][motorNum]>=1) {
			time++;
		}
		return time;
	}
	
	//return average current over one pulse
	public static Double avgCur(int start,int end,Double motorData[][],int motorNum) {
		Double totalCur = 0.0;
		int i = 0;
		for(i=start;i<end;i++) {
			totalCur += motorData[start][motorNum];
		}
		return totalCur/(end-start);
	}
	
	//returns 1.0 if current exceeds 8 amps during a pulse, else 0.0
	public static Double checkMax(int start,int end,Double motorData[][],int motorNum) {
		for (int i=start;i<end;i++) {
			if (motorData[i][motorNum]>8) return 1.0;
		}
		return 0.0;
	}
	
	//returns 2D array with start, end times and average current for pulses
	public static Double[][] getMotorData(int motorNum, Double motorData[][]) {
		Double dataSummary[][] = new Double[8][5];
		int i = 0, numPulses = 0, end;
		while(i<1000) {
			if(motorData[i][motorNum]>=1) {
				end = pulseUntil(i,motorData,motorNum);
				dataSummary[numPulses][0] = (double) i;
				dataSummary[numPulses][1] = (double) end-1;
				dataSummary[numPulses][2] = avgCur(i,end,motorData,motorNum);
				dataSummary[numPulses][3] = checkMax(i,end,motorData,motorNum);
				numPulses++;
				i = end;
			} else i++;
		}
		dataSummary[0][4] = (double) numPulses;
		return dataSummary;
	}
	
	public static String outputForm(Double motorStats[][],int motorNum) {
		String motorX = ("start (sec), end (sec), current (amps)\n");
		return motorX;
	}
	
	public static void main(String[] args) {
		Double motorData[][] = readData();
		
		Double motor1[][] = getMotorData(1,motorData);
		
		Path outputFile;
		for(int motorNum=1; motorNum<=NUM_MOTORS; motorNum++ ) {
			outputFile = Paths.get( first: "Motor" + motorNum + ".csv");
			try (var writer = Files.newBufferedWriter(outputFile)) {
				writer.write(analyzedData[motorNum - 1]);
			} catch(IOException err) {
				System.err.println(err.getMessage())
				System.exit(status: 1);
			}
		}
	}

}
