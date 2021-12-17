import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class AnalogClock {
	public static String assignPath(int nume,int deno) {
		String path = "";
		//Simplify the fraction 
		int[] arr = reduceFraction(nume, deno);
		nume = arr[0];
		deno = arr[1];
		while(nume!=deno) {
			if(nume>deno) {
				nume = nume-deno;
				path+="R";
			}
			else if(nume<deno) {
				deno = deno-nume;
				path+="L";
			}
		}
		return path;
	}
	public static int findGCD(int a,int b) {
		if(a==0)return b;
		if(b==0)return a;
		if(a==b)return a;
		if(a>b)return findGCD(a-b,b);
		return findGCD(a, b-a);
	}
	public static int[] makeArray(String path) {
		String str = "";
		if(path.length()==0)return null;
		str+=path.charAt(0);
		for (int i = 1; i < path.length(); i++) {
			char ch = path.charAt(i);
			if(ch==str.charAt(str.length()-1)) {
				str+=ch;
			}
			else {
				str+=",";
				str+=ch;
			}
		}
		String s[] = str.split(",");
		int len = s.length;
		int arr[] = new int[len];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = s[i].length();
		}
		arr[len-1]++;
		
		return arr;
		//Generate the fraction
	}
	public static int[] getFraction(int arr[],int count) {
		if(count == arr.length-1) {
			int x[] = {1,arr[count]};
			return x;
		}
		int[] y = getFraction(arr, count+1);
		int nume = y[0];
		int deno = y[1];
		nume = arr[count]*deno+nume;
		int temp = nume;
		nume = deno;
		deno = temp;
		int gcd = findGCD(nume, deno);
		nume = nume/gcd;
		deno = deno/gcd;
		int[] x = {nume,deno};
//		System.out.println(nume+"/"+deno);
		return x;
	}
	
	public static void returnBest(String path) {
		int[] arr = makeArray(path);
		int[] frac = getFraction(arr, 0);
		if(path.startsWith("R")) {
			int temp = frac[0];
			frac[0] = frac[1];
			frac[1] = temp;
		}
		if(isReducible(frac)) {
			System.out.println("This fraction can be approximated to "+frac[0]+"/"+frac[1]);
			product(frac);
			return;
		}
		int len = path.length();
		path = path.substring(0, len-1);
		returnBest(path);
	}
	
	public static boolean isReducible(int[] frac) {
		//Reduce the fraction by gcd
		//Check if fraction can be written as a product of fractions,i.e, check if parts are factorizable
		//If not factorizable,return n where fraction parts are of order 10^n
		int fraction[] = reduceFraction(frac[0], frac[1]);
		int nume = fraction[0];
		int deno = fraction[1];
		if(nume == 1)return true;
		if(nume<100&&deno<100)return true;
		int[] numeFact = factorize(nume);
		int[] denoFact = factorize(deno);
		for (int i = 0; i < denoFact.length; i++) {
			if(numeFact[i]>100||denoFact[i]>100)return false;//To be confirmed
		}
		return true;
	}
	
	public static int[] factorize(int num) {
		int count = 0;
		int factors[] = new int[10000];
		for(int i = 2; i< num; i++) {
	         while(num%i == 0) {
	            num = num/i;
	            factors[count++] = i;
	         }
		}
	    if(num >2) {
	    	factors[count++] = num;
	    }
	    return factors;
	}
	
	public static void product(int[] frac) {
		int numeFact[] = factorize(frac[0]);
		int denoFact[] = factorize(frac[1]);
		int numeFactLength = 0;
		int denoFactLength = 0;
		for (int i = 0; i < numeFact.length; i++) {
			int ele = numeFact[i];
			if(ele == 0) {
				break;
			}
			numeFactLength++;
		}
		for (int i = 0; i < denoFact.length; i++) {
			int ele = denoFact[i];
			if(ele == 0) {
				break;
			}
			denoFactLength++;
		}
		int max = (numeFactLength>denoFactLength)?numeFactLength:denoFactLength;
		int fracWork[] = new int[max];
		System.out.println("Gear train :");
		if(numeFactLength < denoFactLength) {
			int i = 0;
			for (; i < numeFactLength; i++) {
				fracWork[i] = numeFact[i];
			}
			for(;i<max;i++) {
				fracWork[i] = 1;
			}
			for (int j = 0; j < max; j++) {
				System.out.print(fracWork[j]+"/"+denoFact[j]+" ");
			}
		}
		else {
			int i = 0;
			for (; i < denoFactLength; i++) {
				fracWork[i] = denoFact[i];
			}
			for(;i<max;i++) {
				fracWork[i] = 1;
			}
			for (int j = 0; j < max; j++) {
				System.out.print(numeFact[j]+"/"+fracWork[j]+" ");
			}
		}
	}
	
	public static int[] reduceFraction(int nume,int deno) {
		int gcd = findGCD(nume, deno);
		nume = nume/gcd;
		deno = deno/gcd;
		int[] arr = {nume,deno};
		return arr;
	}
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the numerator");
		int nume = scan.nextInt();
		System.out.println("Enter the denominator");
		int deno = scan.nextInt();
		returnBest(assignPath(nume, deno));
	}

}
