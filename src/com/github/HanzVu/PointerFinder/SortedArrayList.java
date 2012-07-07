package com.github.HanzVu.PointerFinder;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SortedArrayList<T extends Comparable<T>> extends ArrayList<T> {

	private int findIndex(Comparable<T> obj){
		if (this.size() == 0)
			return 0;
		
		int start = 0;
		int end = this.size();
		int middle = (start + end) / 2;
		while (start != end){
			if (obj.compareTo(this.get(middle)) > 0){
				start = middle + 1;
				middle = (start + end) / 2;
			} else if (obj.compareTo(this.get(middle)) < 0) {
				end = middle;
				middle = (start + end) / 2;
			} else
				return middle;
		}
		return start;
	}
	
	public int addWhere(T obj){
		int index = findIndex(obj);
		add(index, obj);
		return index;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(Object obj){
		if (obj instanceof Comparable)
			return findIndex(((Comparable<T>) obj));
		return -1;
	}
}
