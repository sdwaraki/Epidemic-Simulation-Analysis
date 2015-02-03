/**
 * @author Sumanth
 *
 */


/** Class to handle the <window> tuple that goes into epidemic_word_file **/

package Phase1;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Window object that contains size, shiftLength and List that contains the window.
 *
 */
public class window 
{
	int size;
	int shiftLength;
	List<Double> win;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getShiftLength() {
		return shiftLength;
	}
	public void setShiftLength(int shiftLength) {
		this.shiftLength = shiftLength;
	}
	
	window()
	{
		this.win = new ArrayList<Double>();
	}
	
	public void addToList(double value)
	{
		this.win.add(value);
	}
	
	public List<Double> getList()
	{
		return this.win;
	}
	
	public void setList(List<Double> x)
	{
		this.win=x;
	}
	
}
