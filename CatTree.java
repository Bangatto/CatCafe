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
        	//case when the root node and argument have the same seniority, we compare their fur thickness
        	//the thicker the fur, the more senior hence will be at the root and the less senior goes to same node.
        	if (this.data.monthHired == c.data.monthHired) {
        			if (this.data.furThickness > c.data.furThickness) {
        				if(this.same != null)
        					this.same.addCat(c);
        				else
        					
        					this.same = c;
        			}else{
        				CatInfo temp = this.data;
        				this.data = c.data;
        				c.data = temp;
        				c.same = this.same;
        				this.same= c;
  
        		}
        	//case when the root node has a less seniority(higher monthHired) than our argument
        	}else if (this.data.monthHired > c.data.monthHired)
        			if( this.senior != null)
        				this.senior.addCat(c);
        			else
        				this.senior = c;
        	//case when the root node has a higher seniority(less monthHired) than our argument
        	else {
        		if (this.data.monthHired < c.data.monthHired) {
        			if(this.junior != null)
        				this.junior.addCat(c);
        			else
        				this.junior = c;
        		}
        	}
            return this; 
        }
        
        public CatNode removeCat(CatInfo c) {
        	//cat to remove is SENIOR to this.
        	if(this.data.monthHired > c.monthHired) {
        		if(this.senior != null)
        			this.senior = this.senior.removeCat(c);
        		else
        			return this;
        	}
        	//cat to remove is JUNIOR to this.
        	else if(this.data.monthHired < c.monthHired ) {
        		if(this.junior !=null)
            		this.junior = this.junior.removeCat(c);
        		else
        			return this;
        	}
        	//cat to remove is SAME to this(have same monthHired), compare furThickness.
        	else {
        		//if c`s Fur is SMALLER than THIS, removeCAt(c) on the SAME node;
        		if(c.furThickness < this.data.furThickness) {
        			if(this.same != null)
        				this.same = this.same.removeCat(c);
        			else
        				return this;
        		}
        		//both c and THIS have the same furThickness and monthHired
        		else if(c.furThickness == this.data.furThickness) {
        			//We`ve found Cat to remove as the root.
        			if(c.equals(this.data)) {
        				//case 1: cat has a SAME: set the parent link to be the root and set reference to this and return same
        				if(this.same != null) {
            				//this.same.junior = this.junior;
            				//this.same.senior = this.senior;
        					this.data = this.same.data;
        					this.same = this.same.same;
        					
        				
            				return this;
        				}
        				//case 2: cat has NO SAME but has SENIOR, set the parent link to this SENIOR
        				else if(this.senior != null) {
        					//if this senior has a JUNIOR, place it in the SENIOR`s subtree.
        					if(this.junior != null) {
        						this.senior.addCat(this.junior);
        					}
        					return this.senior;
        				}
        				//case 3: cat has no SAME, no SENIOR but has JUNIOR. set parent link to this`s JUNIOR
        				else if(this.junior != null) {
        					return this.junior;
        				}
        				//cat has no SAME, no JUNIOR, no SENIOR, set parent link to null
        				else {
        					return null;
        				}
        			}
        		}
        	}
        			return this;
        	}
 /**
    			//case 2: the root node has no root.same and root.senior is not null, root.senior become the new root.
    			//}else 
    				CatNode temp = this.junior;
    		/		this.data = this.senior.data;
    				this.senior =this.senior.senior;
    				this.junior = this.senior.junior;
    				this.junior = this.junior.addCat(temp);
    				
    			//case 3: the root node has no root.same and root.senior is null, the root.junior become the new root.
    			}else {
    				if(this.senior == null && this.same == null && this.junior !=null) {
    					this.data = this.junior.data;
    					this.senior = this.junior.senior;
    					this.same = this.junior.same;
    					this.junior = this.junior.junior;
    					
    				}else {
    					return null;
    				}
    			}
    		}
  */
        
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
        	
        	//look for the monthMin on the senior side
        	if (this.data.monthHired >= monthMin && this.data.monthHired <= monthMax) {
        		hiredCount ++;
        		if ( this.senior != null) {
        			hiredCount += this.senior.hiredFromMonths(monthMin, monthMax);	
        		}
        	}
        	if ( this.junior != null) {
        			hiredCount += this.junior.hiredFromMonths(monthMin, monthMax);	
        		}
        	if ( this.same != null) {
        			hiredCount += this.same.hiredFromMonths(monthMin, monthMax);	
        		}
        	return hiredCount;
            
        	}
        public CatInfo fluffiestFromMonth(int month) {
        	//set a cat to return info
        	CatInfo catInfo = null;
        	//if this is this.
        	if (this.data.monthHired == month)
        		catInfo = this.data;
        	//chech the mostfluffiest on the senior side recursively
        	else if( month < this.data.monthHired)
        		catInfo = this.senior.fluffiestFromMonth(month);
        	//check the mostfluffiest on the junior side recursively
        	else 
        		catInfo = this.junior.fluffiestFromMonth(month);
        	
        	return catInfo;
        }
        
        public int[] costPlanning(int nbMonths) {
            int[] catCostArr = new int[nbMonths];
            CatTree t = new CatTree(this);
            Iterator<CatInfo> iter = t.iterator();
            while(iter.hasNext()) {
            	 CatInfo cat = iter.next();
            	 if(cat.nextGroomingAppointment >= 243 && cat.nextGroomingAppointment <243 + nbMonths) {
            		 catCostArr[cat.nextGroomingAppointment-243] += cat.expectedGroomingCost;
            	 }
            }
      
            return catCostArr; 
        }
        
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
        //field attributes
    	ArrayList<CatInfo> listOfCats = new ArrayList<CatInfo>();
    	int index;
    	
    	public CatTreeIterator() {
              inOrderTraversal(root);
              index = 0;
          }
          
    	//use inorder traversal to access all the nodes of the Cat tree.
    	private void inOrderTraversal(CatNode node) {
    		//traversal over the most senior node first
    		if( node.senior != null) {
    			inOrderTraversal(node.senior);
    		}
    		//visit the JUNIOR of a node second, since the lower has thicker fur.
    		if(node.same != null) {
    			inOrderTraversal(node.same);
    		}
    		//visit this node.
    		listOfCats.add(root.data);
    														
    		//Visit the JUNIOR 
    		if(node.junior != null) {
    			inOrderTraversal(node.junior);
    		}
    	}
      
        public CatInfo next(){
        	
        	if(index < listOfCats.size()) {
        		CatInfo temp = listOfCats.get(index);
        		index ++;
        		
        		return temp;
        	}
        	
        	return null;
        	
        }
        
        public boolean hasNext() {
            if(index +1 > listOfCats.size())
            	return false;  
          	return true; 
            	
        }
    }
    
}

