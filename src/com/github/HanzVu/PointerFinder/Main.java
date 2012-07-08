package com.github.HanzVu.PointerFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

	/**
	 * @param args 0 - ramdump1
	 *             1 - code 1 (real or fake addressing)
	 *             2 - ramdump2
	 *             3 - code 2 (real or fake addressing)
	 *             4 - DMA Level (optional)
	 *             5 - max offset (hex) (optional)        
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File dump1;
		File dump2;
		int code1;
		int code2;
		int DMAlevel = 1;
		int maxOffset = 0x8000;
		
		if (args.length >=4){
			dump1 = new File(args[0]);
			dump2 = new File(args[2]);
			code1 = Integer.parseInt(args[1], 16);
			code2 = Integer.parseInt(args[3], 16);
			
			DMAlevel = 1;
			maxOffset = 0x300;
			if (args.length > 4)
				DMAlevel = Integer.parseInt(args[4]);
			if(args.length > 5)
				maxOffset = Integer.parseInt(args[5], 16);
		} else {
			BufferedReader bufRead = new BufferedReader(new InputStreamReader(System.in));
			try{
				System.out.println("dump1: ");
				dump1 = new File(bufRead.readLine());
				System.out.println("code1: ");
				code1 = Integer.parseInt(bufRead.readLine(), 16);
				System.out.println("dump2: ");
				dump2 = new File(bufRead.readLine());
				System.out.println("code2: ");
				code2 = Integer.parseInt(bufRead.readLine(), 16);
				
				System.out.println("DMA level (defualt = 1): ");
				String tmp = bufRead.readLine();
				if (tmp.length() > 0)
					DMAlevel = Integer.parseInt(tmp);
				
				System.out.println("Max Offset (defualt = 8000): ");
				tmp = bufRead.readLine();
				if (tmp.length() > 0)
					DMAlevel = Integer.parseInt(tmp, 16);
				
				
				
			} catch (IOException e){
				System.out.println("Error reading input!");
				return;
			} catch (NumberFormatException e){
				System.out.println("Input must be an integer!");
				return;
			}
		}
			
			PointerFinder dmp1 = new PointerFinder(dump1, code1, maxOffset);
			PointerFinder dmp2 = new PointerFinder(dump2, code2, maxOffset);
			
			for (int i = 0; i < DMAlevel; i++){
				dmp1.findPointers();
				dmp2.findPointers();
			}
			
			dmp1.comparePointers(dmp2);
			System.out.println(dmp1.possiblePaths());
	}
	
	

}
