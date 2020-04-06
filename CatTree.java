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
        		curNode = curNode.addCat(c);
        	//case when the root node has a less seniority(higher monthHired) than our argument
        	}else if (curNode.data.monthHired > c.data.monthHired) {
        		curNode.senior = curNode.senior.addCat(c);
        	//case when the root node has a higher seniority(less monthHired) than our argument
        	}else if (curNode.data.monthHired < c.data.monthHired) {
        		curNode.junior = curNode.junior.addCat(c);
        	//case when the root node and argument have the same seniority, we compare their fur thickness
        	//the thicker the fur, the more senior hence will be at the root and the less senior goes to same node.
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
            //check if the node is empty and return the current node
        	CatNode curNode = root;
        	if (curNode == null) {
        		return curNode;
        	}
        	//case when curNode is not the argument to remove, check the senior node and the junior node
        	if (curNode.data != c) {
        		if(curNode.data.monthHired < c.monthHired) {
        			curNode.senior = curNode.senior.removeCat(c);
        		}else {
        			if (curNode.data.monthHired > c.monthHired) {
            			curNode.junior = curNode.junior.removeCat(c);
        			}
        		}
        	//The best case when the cat Node to remove is the root. We have 3 cases:
        	}else {
    			//case 1 :the root node has a another node with same seniority, c.same become the new root.
    			if(curNode.same != null) {
    				curNode.same.junior = curNode.junior;
    				curNode.same.senior = curNode.senior;
    			//case 2: the root node has no root.same and root.senior is not null, root.senior become the new root.
    			}else if( curNode.same == null && curNode.senior != null) {
    				root.data = curNode.senior.data;
    				curNode.junior= curNode.junior.addCat(junior);
    				
    			//case 3: the root node has no root.same and root.senior is null, the root.junior become the new root.
    			}else {
    				if(curNode.senior == null && curNode.same == null) {
    					root.data = curNode.junior.data;
    				}
    			}
    		}
        	
        	
            return this; 
        }
        
        
        public int mostSenior() {
        	//case when the root.senior is null, in this case we return root.monthHired,
        	if (this.senior == null){
        		return this.data.monthHired;
        	}else {
        		return this.senior.mostSenior();
        	
        	}
        }
        
        public int fluffiest() {
        //initialize a variable to be the furthickness of the root node,compare it with senior&&junior furthickness
        	int fur = this.data.furThickness;
        	if(this.senior !=null)
        		fur = Math.max(fur, this.senior.fluffiest());
        	if (this.junior != null)
        		fur = Math.max(fur, this.junior.fluffiest());
        	return fur;
        }
        
        
        public int hiredFromMonths(int monthMin, int monthMax) {
        	//keep a counter to accumulate the number of month visited.
        	int hiredCount = 0;
        	//check if the monthMin is less than monthMax and return 0--> no cat was hired;
        	if (monthMin > monthMax) {
        		return 0;
        	}
        	//check if Monthmin == Monthmax and this.same is null, cat at the root was the only one hired.
        	if( monthMin == monthMax && this.same == null) {
        		return hiredCount ++;
        	}
        	//look for the monthMin on the senior side
        	if (monthMin < this.data.monthHired && this.senior != null) {
        		hiredCount ++;
        		return this.senior.hiredFromMonths(monthMin, monthMax);
        	}
        	//look for max month on the junior side.
        	if (monthMax > this.data.monthHired && this.junior != null) {
        		hiredCount ++;
        		return this.junior.hiredFromMonths(monthMin, monthMax);
        	}
            return hiredCount;
            
        }
        
        public CatInfo fluffiestFromMonth(int month) {
           //check if the root node is null, we return the data for the root Node
        	CatNode curNode = this;
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

