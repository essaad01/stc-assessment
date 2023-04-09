package com.stepbystep.consultation;

import java.util.Scanner;
import java.util.Stack;

public class STCProblemSolving{

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String originalString = scanner.next();
        scanner.close();
        
		Stack<Integer> stack = new Stack<>();
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < originalString.length(); i++) {
		    char c = originalString.charAt(i);
		    if (c == '(') {
		        stack.push(i);
		    } 
		    else if (c == ')') {
		        int openingIndex = stack.pop();
		        String toBeReversed = originalString.substring(openingIndex + 1, i);
		        output.append('(');
		        output.append(reverseString(toBeReversed));
		        output.append(')');
		    } 
		    else if(stack.isEmpty()){
		        output.append(c);
		    }
		}
		System.out.println(output.toString());
	}
	
	private static String reverseString(String original) {
		StringBuilder reversed = new StringBuilder();
		reversed.append(original);
		reversed = reversed.reverse();
	    return reversed.toString();
	}

}
