/**
 * @author Sumanth
 *
 */


/** Class to handle the <file,state,iteration> tuple that goes into epidemic_word_file **/

package Phase1;

/**
 * 
 * 
 * Index object which contains <file,state,iteration> tuple 
 *
 */
public class idx 
{
	int file;
	String state;
	int iteration;
	
	public int getFile() {
		return file;
	}
	public void setFile(int file) {
		this.file = file;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getIteration() {
		return iteration;
	}
	public void setIteration(int iteration) {
		this.iteration = iteration;
	}
	
	

}
