package com.github.HanzVu.PointerFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


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
		
		if (args.length >=4){
			File dump1 = new File(args[0]);
			File dump2 = new File(args[2]);
			int code1 = Integer.parseInt(args[1], 16);
			int code2 = Integer.parseInt(args[3], 16);
			
			int DMAlevel = 1;
			int maxOffset = 0x300;
			if (args.length > 4)
				DMAlevel = Integer.parseInt(args[4]);
			if(args.length > 5)
				maxOffset = Integer.parseInt(args[5], 16);
			
			PointerFinder dmp1 = new PointerFinder(dump1, code1, maxOffset);
			PointerFinder dmp2 = new PointerFinder(dump2, code2, maxOffset);
			
			for (int i = 0; i < DMAlevel; i++){
				dmp1.findPointers();
				dmp2.findPointers();
			}
			
			dmp1.compareOffsets(dmp2);
			System.out.println(dmp1.possiblePaths());
			
			
		} else
			System.out.println("Usage: PointerFinder <dump1> <code1> <dump2> <code2> <DMA level> <max offset>");
	}
	
	

}
