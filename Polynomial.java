package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked p node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked p. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked p that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	
	
	
	public Polynomial add(Polynomial p)
	{
		Polynomial sum = new Polynomial();

	    Node s1 = null;
		Node ptr = this.poly;
		Node ptr1 = p.poly;
		
		while(ptr!=null)
		{
			s1 = addPoly(ptr, s1);    // helper method
			ptr=ptr.next;
		}
		while(ptr1!=null)
		{
			s1 = addPoly(ptr1,s1);   //helper method
			ptr1=ptr1.next;
		}
		sum.poly=s1;
		return sum;		
	
	}
	
	private Node addPoly(Node add, Node p)//helper method for addition and multiplication
	{
		Node b = new Node(add.term.coeff, add.term.degree, null);
		if(p==null)
		{
			return b;
		}
		
		else
		{			
		Node ptr = p;
		Node prev = null;

		while(ptr!=null)
			if(b.term.degree>ptr.term.degree)
			{					
				if(ptr.next==null)
				{
					ptr.next=b;
					return p;
				}
				prev=ptr;
				ptr=ptr.next;
			}

			else if(b.term.degree==ptr.term.degree)
			{
				if(ptr.term.coeff+b.term.coeff==0)
				{
					if(prev==null)
						return ptr.next;
					else
					{
						prev.next=ptr.next;
						return p;
						}
				}

				if(prev==null)
					p = new Node(ptr.term.coeff+b.term.coeff, 
							     ptr.term.degree, ptr.next);
				else
				{
					prev.next= new Node(ptr.term.coeff+b.term.coeff,
							            ptr.term.degree, ptr.next);
				}
				return p;				
			}

			else if(b.term.degree<ptr.term.degree)
			{				
				b.next=ptr;
				prev.next=b;
				return p;
			}	
		}		
		return null;	
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		Node e1 = this.poly;
		Node e2 = p.poly;
		Node ne = null;
		Node mult = null;
		Polynomial product = new Polynomial();
       
		if(e1==null || e2==null)
		{
			if(e1==null)
			{
				e2=e1;
				
			}
			if(e2==null)
			{
				e1=e2;
			}
		}
		while(e1!=null)
		{			
			while(e2!=null)
			{
			    ne = new Node(e1.term.coeff*e2.term.coeff,
				              e1.term.degree+e2.term.degree, null);
				e2=e2.next;
				mult = addPoly(ne, mult);
			}
			e1=e1.next;
			e2=p.poly;
		}
		product.poly=mult;
		return product;
	}
		/* Polynomial p1 = new Polynomial();
	     Polynomial p2 = new Polynomial();
	     Node p2Ptr = p2.poly;
	     Node p1Ptr = p1.poly;
	     Node ptr1 = this.poly;
	     Node ptr2 = p.poly;
	     
	     if(poly == null || p.poly == null)
	     {
	    	 if(poly==null)
	    	 {
	    		 p2.poly = p.poly;
	    		 return p2;
	    	 }
	    	 p2.poly = poly;
	    	 return p2;
	     }
	     while(ptr1!=null)
	     {
	    	 while(ptr2!=null)
	    	 {
	    		 if(p1Ptr == null)
	    		 {
	    			 p1.poly = new Node (ptr1.term.coeff* ptr2.term.coeff,ptr1.term.degree+ ptr2.term.degree,null );
	    		     p1Ptr = p1.poly;
	    		 }
	    		 else
	    		 {
	    			 p1Ptr.next = new Node(ptr1.term.coeff* ptr2.term.coeff,ptr1.term.degree+ ptr2.term.degree,null);
	    		     p1Ptr = p1Ptr.next; 
	    		 }
	    		 ptr2 = ptr2.next;
	    	 }
	    	 p2 = p1.add(p2);
	    	 p1.poly = null;
	    	 p1Ptr = p1.poly;
	    	 ptr2 = p.poly;
	    	 ptr1 = ptr1.next;
	    	 
	     }
		return p2;
	}*/
	
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) 
	{
		float x1,x2;
		x1=0;
		x2=0;
		
		if(poly==null)
		{
			return 0;
		}
		while(poly!=null)
		{
			x1=poly.term.coeff * ((float)Math.pow(x,poly.term.degree));
			x2 = x1 + x2;
			poly = poly.next;
		}
		
		return x2;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
