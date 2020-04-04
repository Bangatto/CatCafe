import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException; 


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    
    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
        
        
        public CatNode addCat(CatNode c) {
           //check whether the root node is null and make the cat to add the root node
        	CatNode curNode = root;
        	if(curNode == null) {
        		curNode =curNode.addCat(c);
        	}else if (curNode.data.monthHired > c.data.monthHired) {
        		curNode.senior = curNode.senior.addCat(c);
        	}else if (curNode.data.monthHired < c.data.monthHired) {
        		curNode.junior = curNode.junior.addCat(c);
        	}else if (curNode.data.monthHired == c.data.monthHired) {
        		if (curNode.data.furThickness > c.data.furThickness) {
        			curNode.same = curNode.same.addCat(c);
        		}else {
            		 root = c;
            		 c.senior = curNode.senior;
            		 c.junior = curNode.junior;
            		 c.same = curNode.same;
            		 c.same.addCat(curNode);
        		}
        	}
        
            return root; 
        }
        
        
        public CatNode removeCat(CatInfo c) {
            //check if the node is empty and return this
        	if (this.senior == null) {
        		return this.senior;
        	}
        	if (c.senior < this.senior) {
        		this.senior.removeCat(c.data);
        		
        	}else if ( c.senior > this.senior) {
        		this.junior.removeCat(c.data);
        	}else {
        		//cases where C is the root
        		if (c.data.equals(this.data)) {
        			if(c.same != null) {
        				this.root = c.same.addCat(c.data);
        				this.junior = c.same;
        				this.senior = c.same;
        			}else if( c.same == null && c.senior != null) {
        				this.root = c.senior.addCat(c.data);
        				this.senior = c.senior;
        				this.junior = c.senior;
        			}else {
        				if(c.senior == null && c.same == null) {
        					this.root = c.junior.addCat(c)
        				}
        			}
        		}
        	}
        	
            return this.root; 
        }
        
        
        public int mostSenior() {
        	//Case where the call is made on junior cat, the first cat will be the most senior
        	if (this.junior.monthHired.mostSenior()){
        		return this.data.monthHired
        	}else { // case where the call is made on senior cat, the very last cat on the tree is the most senior
        		while(this.senior != null) {
        			this.senior = this.mostSenior()
        		}
        	}
            return this.data.monthHired;
        }
        
        public int fluffiest() {
            //case when the call is made on the same junior cat, the first junior cat has the fur thickenss
        	if (this.senior == null ) {
        		return this.data.furThickness;
        	}else if ( this.junior.fluffiest()) {
        		return this.data.FurThickness;
        	}else {
        		this.senior.fluffiest()        	}
            return this.data.furThickness;
        }
        
        
        public int hiredFromMonths(int monthMin, int monthMax) {
        	// define a current node to act as a root node
        	catNode curNode = this.root;
        	int hiredCount = 0;
        	//check if the monthMin is less than monthMax and return 0--> no cat was hired;
        	if (monthMin > monthMax) {
        		return 0;
        	}else if (monthMin < curNode.data.monthHired && curNode.senior != null) {
        		hiredCount ++;
        		curNode = curNode.senior.hiredFromMonths(monthMin.junior, monthMax.senior);
        	}else if (monthMax > curNode.data.monthHired && curNode.junior ! null) {
        		hiredCount ++;
        		curNode = curNode.junior.hiredFromMonths(monthMin.junior. monthMax.senior);
        	}else {   // case where the cat is in the c.same branch
        		hiredCount ++;
        	}
        	//how to add the two separate counts together.
        		
            return hiredCount;
            
        }
        
        public CatInfo fluffiestFromMonth(int month) {
           //check if the root node is null, we return the data for the root Node
        	CatNode curNode = this.root;
        	if (curNode == null) {
        		return curNode.data
        	}
        	//check whether the root node has root.same and return the current root
        	if(curNode.data.monthHired == month) {
        		curNode = curNode.same.fluffiestFromMonth(month);
        		return curNode.data;
        	}else if(month < curNode.data.monthHired) {
        		curNode = curNode.senior.fluffiestFromMonth(month);
        		return curNode.data;
        	}else {
        		curNode = curNode.junior.fluffiestFromMonth(month);
        		return curNode.data;
        	}
            return null; 
        }
        
        public int[] costPlanning(int nbMonths) {
            // ADD YOUR CODE HERE
            return null; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }
        
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
        // HERE YOU CAN ADD THE FIELDS YOU NEED
        
        public CatTreeIterator() {
            //YOUR CODE GOES HERE
        }
        
        public CatInfo next(){
            //YOUR CODE GOES HERE
            return null; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }
        
        public boolean hasNext() {
            //YOUR CODE GOES HERE
            return false; // DON'T FORGET TO MODIFY THE RETURN IF NEED BE
        }
    }
    
}

