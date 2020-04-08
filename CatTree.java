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
        	if(this.senior == null) {
        		this.senior = c;
        	//case when the root node has a less seniority(higher monthHired) than our argument
        	}else if (this.data.monthHired > c.data.monthHired) {
        		this.senior = this.senior.addCat(c);
        	//case when the root node has a higher seniority(less monthHired) than our argument
        	}else if (this.data.monthHired < c.data.monthHired) {
        		this.junior = this.junior.addCat(c);
        	//case when the root node and argument have the same seniority, we compare their fur thickness
        	//the thicker the fur, the more senior hence will be at the root and the less senior goes to same node.
        	}else if (this.data.monthHired == c.data.monthHired) {
        		if (this.data.furThickness > c.data.furThickness) {
        			this.same = this.same.addCat(c);
        		}else {
            		 root = c;
            		 c.senior = this.senior;
            		 c.junior = this.junior;
            		 c.same = this.same;
            		 c.same.addCat(this);
        		}
        	}
        
            return this; 
        }
        
        
        public CatNode removeCat(CatInfo c) {
            //check if the node is empty and return the current node
        	if (this == null) {
        		return this;
        	}
        	//case when this is not the argument to remove, check the senior node and the junior node
        	if (this.data != c) {
        		if(this.data.monthHired < c.monthHired) {
        			this.senior = this.senior.removeCat(c);
        		}else {
        			if (this.data.monthHired > c.monthHired) {
            			this.junior = this.junior.removeCat(c);
        			}
        		}
        	//The best case when the cat Node to remove is the root. We have 3 cases:
        	}else {
    			//case 1 :the root node has a another node with same seniority, c.same become the new root.
    			if(this.same != null) {
    				this.same.junior = this.junior;
    				this.same.senior = this.senior;
    			//case 2: the root node has no root.same and root.senior is not null, root.senior become the new root.
    			}else if( this.same == null && this.senior != null) {
    				root.data = this.senior.data;
    				this.junior= this.junior.addCat(junior);
    				
    			//case 3: the root node has no root.same and root.senior is null, the root.junior become the new root.
    			}else {
    				if(this.senior == null && this.same == null) {
    					root.data = this.junior.data;
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
        	if( monthMin == monthMax && this.same != null) {
        		return hiredCount ++;
        	}
        	//look for the monthMin on the senior side
        	if (monthMin < this.data.monthHired && monthMax > this.data.monthHired) {
        		hiredCount ++;
        		if ( this.senior != null) {
        			hiredCount += this.senior.hiredFromMonths(monthMin, monthMax);	
        		}
        		if ( this.junior != null) {
        			hiredCount += this.junior.hiredFromMonths(monthMin, monthMax);	
        		}
        		if ( this.same != null) {
        			hiredCount += this.same.hiredFromMonths(monthMin, monthMax);	
        		}
        	}
            return hiredCount;
            
        }
        
        public CatInfo fluffiestFromMonth(int month) {
        	//check whether monthhired equals the root node and return its data, its the mostfluffiest
        	if (this.data.monthHired == month)
        		return this.data;
        	//chech the mostfluffiest on the senior side recursively
        	else if( month < this.data.monthHired)
        		return this.fluffiestFromMonth(month);
        	//check the mostfluffiest on the junior side recursively
        	else {
        		return this.junior.fluffiestFromMonth(month);
        	}
        }
        
        public int[] costPlanning(int nbMonths) {
            int[] catCostArr = new int[nbMonths];
            while(iterator().hasNext()) {
            	 CatInfo cat =iterator().next();
            	 if(cat.nextGroomingAppointment >= 243 && cat.nextGroomingAppointment < 243 + nbMonths) {
            		 catCostArr[cat.nextGroomingAppointment-243] += cat.expectedGroomingCost;
            	 }
            }
      
            return catCostArr; 
        }
        
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
        //field attributes
    	ArrayList<CatInfo> listOfCats; 
    	CatInfo cat;
    	int index;
    	//use inorder traversal to access all the nodes of the Cat tree.
    	private void inOrderTraversal(CatNode node) {
    		//check whether the root node has senior and junior node
    	 	if(node.senior == null && node.junior == null && node.same == null) {
    		
    			listOfCats.add(root.data);
    			return;
    		}
    		
    		//traversal over the most senior
    		if( node.senior != null) {
    			inOrderTraversal(node.senior);
    		}
    		if(node.same != null) {
    			inOrderTraversal(node.same);
    		}
    		listOfCats.add(root.data);
    														
    		//check the junior side
    		if(node.junior != null) {
    			inOrderTraversal(node.junior);
    		}
    		listOfCats.add(root.data);
    	}
        public CatTreeIterator() {
            listOfCats = new ArrayList<CatInfo>();
            inOrderTraversal(root);
            index = 0;
            cat = listOfCats.get(index);
        }
        
        public CatInfo next(){
        	if(index == listOfCats.size() - 1) {
        		//index++;
        		return cat;
        	}else {
        		CatInfo temp = cat;
        		index++;
        		cat = listOfCats.get(index);
        		return temp;
        	}
        }
        
        public boolean hasNext() {
            if(index < listOfCats.size())
            	return true;  
          	return false; 
            	
        }
    }
    
}

