package com.github.HanzVu.PointerFinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class PointerFinder {
	private final File dmp;
	private final int maxOff;
	private int curLevel;

	private ArrayList<SortedArrayList<Integer> > pointers;
	private ArrayList<ArrayList<Integer> > values;
	private ArrayList<ArrayList<Integer> > offsets;


	public PointerFinder(File file, int code, int maxOffset){
		if (code < 0x8800000)
			code += 0x8800000;

		dmp = file;
		maxOff = maxOffset;
		curLevel = 0;

		pointers = new ArrayList<SortedArrayList<Integer> >();
		pointers.add(new SortedArrayList<Integer>());
		pointers.get(0).add(code);

		offsets = new ArrayList<ArrayList<Integer> >();
		offsets.add(new ArrayList<Integer>());
		offsets.get(0).add(0);

		values = new ArrayList<ArrayList<Integer> >();
		values.add(new ArrayList<Integer>());
		values.get(0).add(0);
	}

	/**
	 * Finds all potential pointers 1 level deeper than
	 * the current level then increments the current level.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void findPointers() throws FileNotFoundException, IOException{
		//if (curLevel > 0){
		pointers.add(new SortedArrayList<Integer>());
		offsets.add(new ArrayList<Integer>());
		values.add(new ArrayList<Integer>());

		
		int addr = 0;
		int offset = 0;
		byte[] fileBytes = new byte[4];
		ByteBuffer buf = ByteBuffer.wrap(fileBytes);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.mark();
		int numFound = 0;

		try (FileInputStream fis = new FileInputStream(dmp)){
			
			while (fis.read(fileBytes) == 4){
				int val = buf.getInt();
				buf.reset();

				for (int i = 0; i < pointers.get(curLevel).size(); i++){
					int lowerPtr = pointers.get(curLevel).get(i);

					offset = lowerPtr - val;
					if (offset < maxOff && offset > 0){
						
						int index = pointers.get(curLevel+1).addWhere(addr + 0x8800000);
						values.get(curLevel+1).add(index, val);
						offsets.get(curLevel+1).add(index, offset);
						numFound ++;
						System.out.println(numFound);
					}
				}
				addr += 4;
			}
		}

		curLevel++;
	}

	/**
	 * Gets all the pointer addresses at all levels
	 * @return an Array containing all pointers
	 */
	public ArrayList<SortedArrayList<Integer>> getPointers(){
		return pointers;
	}

	/**
	 * Gets all the offsets at all levels
	 * Offsets are defined such that pointers.get(curlevel-1).contains(values.get(curlevel).get(i) + offsets.get(curlevel).get(i))
	 * will always return a valid index 
	 * @return an array of offsets
	 */
	public ArrayList<ArrayList<Integer>> getOffsets(){
		return offsets;
	}

	/**
	 * Gets the value associated with each pointer address for all levels
	 * @return a array of values
	 */
	public ArrayList<ArrayList<Integer>> getValues(){
		return values;
	}

	/**
	 * Compares all pointers at the current level. 
	 * After pointers.get(curlevel) for each PointerFinder object
	 * is equal to the intersection of obj1.pointers.get(curlevel) and obj2.pointers.get(curlevel) 
	 * @param other the other PointerFinder with which to compare
	 */
	public void comparePointers(PointerFinder other){		
		
		ArrayList<Integer> otherPointers = other.getPointers().get(curLevel);
		ArrayList<Integer> otherOffsets = other.getOffsets().get(curLevel);
		ArrayList<Integer> otherValues = other.getValues().get(curLevel);

		int i = 0;
		int j = 0;
		while (i < pointers.get(curLevel).size() && j < otherPointers.size()){
			if (pointers.get(curLevel).get(i) < otherPointers.get(j)){
				pointers.get(curLevel).remove(i);
				offsets.get(curLevel).remove(i);
				values.get(curLevel).remove(i);
			} else if (pointers.get(curLevel).get(i) > otherPointers.get(j)){
				otherPointers.remove(j);
				otherOffsets.remove(j);
				otherValues.remove(j);
			} else if (offsets.get(curLevel).get(i) != otherOffsets.get(j)){
				pointers.get(curLevel).remove(i);
				offsets.get(curLevel).remove(i);
				values.get(curLevel).remove(i);
				otherPointers.remove(j);
				otherOffsets.remove(j);
				otherValues.remove(j);
				
			} else {
				
				i++; 
				j++;
			}
		}
		
		for (i=i; i < pointers.get(curLevel).size(); i += 0){
			pointers.get(curLevel).remove(i);
		}

	}

	/**
	 * Finds all possible paths from the curent level down to the original code
	 * @return a String containing all possible paths
	 */
	public String possiblePaths(){
		String path = new String();
		System.out.format("NUMBER OF POINTERS FOUND: %d\n", pointers.get(curLevel).size());
		for (int j = 0; j < pointers.get(curLevel).size(); j++){
			path += String.format("pointer: 0x%08X\n", pointers.get(curLevel).get(j));
			path += String.format("     offset: 0x%X\n", offsets.get(curLevel).get(j));
			int address = values.get(curLevel).get(j) + offsets.get(curLevel).get(j);

			for (int i = curLevel - 1; i > 0; i--){
				int idx = pointers.get(i).indexOf(address);
				if (idx >= 0){
					path += String.format("     offset: 0x%X\n", offsets.get(i).get(idx));
					address = values.get(i).get(idx) + offsets.get(i).get(idx);
				}
			}
			path += "\n";
		}

		return path;
	}
	
}
