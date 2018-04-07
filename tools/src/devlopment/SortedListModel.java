package devlopment;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;

public class SortedListModel extends AbstractListModel<Object> {
	
	private static final long serialVersionUID = 1L;
	SortedSet<Object> model;
	
	public SortedListModel() {
		model = new TreeSet<Object>();
	}//Constructor

	@Override
	public Object getElementAt(int index) {
		return  model.toArray()[index];
	}//getElementAt
	
	public void add(Object element) {
		if(model.add(element)) {
			fireContentsChanged(this,0,getSize());
		}//if
	}//add
	
	public void addAll(Object[] elements) {
		Collection<Object> collection = Arrays.asList(elements);
		model.addAll(collection);
		fireContentsChanged(this,0,getSize());
	}//addAll
	
	public void clear() {
		model.clear();
	}//clear
	
	public boolean contains(Object element) {
		return model.contains(element);
	}//contains
	
	public Object firstElement() {
		return model.first();
	}//firstElement
	
	public Object lastElement() {
		return model.last();
	}//lastElement
	
	public Iterator iterator() {
		return model.iterator();
	}//iterator
	
	public boolean removeElement(Object element) {
		boolean removed = model.remove(element);
		if(removed) {
			fireContentsChanged(this,0,getSize());
		}//if removed
		return removed;
	}//removeElement

	@Override
	public int getSize() {
		return model.size();
		
	}//getSize
	


}//class SortedListModel<Object>
