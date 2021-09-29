import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.lang.String;
import java.lang.*;



class bank{
Dictionary df = new Hashtable();
List<String> id =  new ArrayList<>();
List<String> data = new ArrayList<>();
List<Integer>acc = new ArrayList<>();
String active_id;
boolean auth = false;
	public static void main(String[] args){
		BufferedReader reader;
		int i,j,k,l;
		try{
			reader = new BufferedReader(new FileReader("bank_db.txt"));
			String line = reader.readLine();
			while(line!= null){
				data.add(line);
			}
			reader.close;
		}catch(IOException e){
			e.printStackTrace();
		}
		for(i=1;i<data.size();i++){
			id.add(data.get(i).split("\\s+")[0]);
		}
		String[] values;
		for(i=1;i<data.size();i++){
			String[] values = data.get(i).split("\\s+");
			df.put(id.get(i),values);
			acc.add(values[1]);
		}// { id,[id,acc,name,bal,pass]}
		System.out.println("1. Add New Customer");
		System.out.println("2. ATM Withdawl");
		System.out.println("3. Cash Deposit");
		System.out.println("4. Account Transfer");
		System.out.println("5. Account Statement");
		System.out.println("6. Change Password");
		System.out.println("7. Log In");
		System.out.println("8. Top Customers");
		System.out.println("9. Exit");
		Scanner in  =  new Scanner(System.in);
		System.out.println("Enter Your option");
		int opt = sc.nextInt();
		int n=1;
		while(n!=0){
			switch(opt){
				case 1:
				add_cust();
				break;
				case 2:
				withdraw();
				break;
				case 3:
				deposit();
				break;
				case 4:
				transfer();
				break;
				case 6:
				chpass();
				break;
				case 7:
				log_in();
				case 9:
				n=0;
				auth = false;
				active_id = -1;
				break;
				default:
				System.out.println("Invalid Option! Try Again");
			}
		}
	}// main method


	public void add_cust(){

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Name");
		String cust_name = sc.nextLine();
		System.out.println("Enter Password");
		String cust_pass = sc.nextLine();
		System.out.println("Re-Enter Password");
		String cust_repass = sc.nextLine();
		if(cust_pass.equals(cust_repass)&&pcheck(cust_pass)){
			int cust_id = Collections.max(strtoint(id,Integer::parseInt))+1;
			int cust_acc = Collections.max(strtoint(acc,Integer::parseInt))+1;
			String[] details;
			String[] details = {cust_id.toString(),cust_acc.toString(),cust_name,"100000",encrypt(cust_pass)}
			df.put(cust_id,details);
			String li = details[0]+"	"+details[1]+"	"+details[2]+"	"+details[3]+"	"+details[4];
			FileWriter fw = new FileWriter("bank_db.txt",true);
			BufferedReader bw = new BufferedReader(fw);
			bw.newLine();
			bw.write(li);
			bw.close();
			System.out.println("Your Account is created Successfully Please Remmeber the ID Shown Below ");
			System.out.println(cust_acc);
		else{
			System.out.println("Both passwords Mismatch");
		}
	}

	public String encrypt(String x){
		char[] pass = new char[x.length()];
		for(int i=0;i<x.length();i++){
			pass[i] = x.charAt(i);
			pass[i]+=1;
		}
		return toString(pass);
	}

	public String encrypt(String x){
		char[] pass = new char[x.length()];
		for(int i=0;i<x.length();i++){
			pass[i] = x.charAt(i);
			pass[i]-=1;
		}
		return toString(pass);
	}

	public void log_in(){

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Account Id");
		String acc_id = sc.nextLine();
		System.out.println("Enter Password");
		String acc_pass = sc.nextLine();
		String[] values;
		values = new String[5];
		for(int i=0;i<id.size();i++){
			values = df.get(id[i]);
			if(values[1]==acc_id){
				active_id=id[i];
				System.out.println("You have Succesfully logged in !");
			}
			else if(i==id.size()-1) {
				System.out.println("Ther is no Account Matching your details. Try Again !");
				log_in();
		}
	}

	public void deposit(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Amount to Deposit");
		int amount = sc.nextInt();
		String[] values=  new String[5];
		values = df.get(active_id);
		int x = Integer.parseInt(values[3]);
		values[3] = (x+amount).toString();
		df.replace(active_id,values);
	}


	public void withdraw(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Amount to withdraw");
		int amount = sc.nextInt();
		String[] values=  new String[5];
		values = df.get(active_id);
		int x = Integer.parseInt(values[3]);
		if(x-amount>1000){
			values[3] = (x-amount).toString();
			df.replace(active_id,values);
		}
		else{
			System.out.println("You Should hava atlease 1000 Rupees in your Bank So you Cannot Withdraw");
		}
		
	}

	public void transfer(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Amount to Transfer");
		int amount = sc.nextInt();
		System.out.println("Enter the Account number of Reciver");
		String acc_no = sc.nextLine();

		String[] values1;
		values1 = new String[5];
		for(int i=0;i<id.size();i++){
			values1 = df.get(id[i]);
			if(values1[1]==acc_no){
				String t2 =id[i];
				String[] values=  new String[5];
				values = df.get(active_id);
				int x = Integer.parseInt(values[3]);
				if(x-amount>1000){
						int t2 = Integer.parseInt(values[3]);
						t2+=amount;
						values[3] = (x-amount).toString();
						df.replace(active_id,values);
						values1[3] = (x+amount).toString();
						df.replace(t2,values1);
						System.out.println("You Transaction Completed Successfully !");
					}
				}
			else if(i>=id.size()-1) {
				System.out.println("Ther is no Reciever account on this . Try Again!");
				transfer();	
		}
	}
	public static <T,U> List<U> strtoint(List<T> los,Function<T,U> func){
		return los.stream().map(func).collect(Collectors.toList());
	}

	public void pcheck(String x){
		int upper,lower,num;
		for(int i=0;i<x.length();i++){
			char ch = x.atChar(i);
			if(ch>='A'&&ch<='Z') upper++;
			else if(ch>'a'&&ch<'z') lower++;
			else if(ch>='0' && ch<='9') num++;
		}
		if(x.length()>5 && upper>1 && lower>1 && num>1) return true;
		else return false;
	}
	public void chpass(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the new Pqssword");
		String x = sc.nextLine();
		String[] values=  new String[5];
		values = df.get(active_id);
		if(pcheck(x)&&!vales[4].equals(x)){
			values[4]=x;
			df.replace(active_id,values);
		}
		else {
			System.out.println("Password Doesnt Meat Criteria ! Try Again");
			chpass();
		}
	}

}// main class