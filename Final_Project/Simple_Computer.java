import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Simple_Computer {

	public static double[] memory = new double[100];
	public static double accumulator, program_counter, opcode, operand;
	public static ArrayList<String> instructRegister = new ArrayList<>();
	public static int Branchvar;
	public static Scanner s = new Scanner(System.in);
	public static int x = 0;

	public static void main(String[] args) {

		// Surround in do while so that the user can open multiple files

		do {
			for(int i = 0; i < memory.length; i++) {
				memory[i] = 0;
			}
			instructRegister.removeAll(instructRegister);
			Branchvar = 101;
			program_counter = 0;
			opcode = 0; 
			operand = 0;
			accumulator = 0;
			// The name of the file to open.
			System.out.println("What is the name of the file you want to open?");
			String fileName = s.nextLine();
			String lastFourchars = "";
			if (fileName.length() > 4)
				lastFourchars = fileName.substring(fileName.length() - 4);
			else
				lastFourchars = fileName;

			if (!lastFourchars.equals(".txt"))
				fileName = fileName + ".txt";

			// This will reference one line at a time
			String line = null;

			try {
				// FileReader reads text files in the default encoding.
				FileReader fileReader = new FileReader(fileName);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				while ((line = bufferedReader.readLine()) != null) {
					instructRegister.add(line);
				}

				System.out.println(instructRegister);

				// A for loop that runs through every machine code that is given and runs a
				x = forloop();

				memoryDump();

				// Always close files.
				bufferedReader.close();
			} catch (FileNotFoundException ex) {
				System.out.println("Unable to open file '" + fileName + "'");
			} catch (IOException ex) {
				System.out.println("Error reading file '" + fileName + "'");
			}
		} while (x != 123456789);

	}

	private static int forloop() {
		String operation = "";
		// A for loop that runs through every machine code that is given and runs a
		for (program_counter = 0; program_counter < instructRegister.size(); program_counter++) {
			if (!operation.equals(""))
				System.out.println("operation: " + operation);
			if (accumulator != 0) {
				if (accumulator > 0)
					System.out.printf("accumulator: +%04d", Math.abs((int) accumulator));
				else if (accumulator < 0) {
					System.out.printf("accumulator: +%04d", Math.abs((int) accumulator));
				}
				System.out.println("");
			}
			int a = Integer.parseInt(instructRegister.get((int) program_counter));
			opcode = a / 100;
			operand = a % 100;
			opcode = Math.abs(opcode);
			// Depending on the opcode a different function will run for the specified
			// operation
			if (opcode == 34) {
				operation = "Write Value";
				WriteValue(operand);
			} else if (opcode == 35) {
				operation = "WriteAscii";
				WriteAscii(operand);
			} else if (opcode == 33) {
				operation = "Read";
				Read(operand);
			} else if (opcode == 32) {
				operation = "Write";
				Write(operand);
			} else if (opcode == 31) {
				operation = "Load";
				Load(operand);
			} else if (opcode == 30) {
				operation = "Store";
				Store(operand);
			} else if (opcode == 28) {
				operation = "StoreZero";
				Storezero(operand);
			} else if (opcode == 21) {
				operation = "Add";
				Add(operand);
			} else if (opcode == 20) {
				operation = "Subtract";
				Subtract(operand);
			} else if (opcode == 11) {
				operation = "Divide";
				Divide(operand);
			} else if (opcode == 29) {
				operation = "LoadImm";
				LoadImm(operand);
			} else if (opcode == 6) {
				operation = "AddImm";
				AddImm(operand);
			} else if (opcode == 7) {
				operation = "DecImm";
				DecImm(operand);
			} else if (opcode == 8) {
				operation = "MultImm";
				MultImm(operand);
			} else if (opcode == 9) {
				operation = "DivideImm";
				DivideImm(operand);
			} else if (opcode == 10) {
				operation = "Multiply";
				Multiply(operand);
			} else if (opcode == 43 && Branchvar != program_counter) {
				operation = "Branch";
				Branch(operand);
			} else if (opcode == 42) {
				operation = "Branchneg";
				Branchneg(operand);
			} else if (opcode == 41) {
				operation = "Branchpos";
				Branchpos(operand);
			} else if (opcode == 40) {
				operation = "Branchzero";
				Branchzero(operand);
			} else if (opcode == 25) {
				operation = "Increment";
				Increment(operand);
			} else if (opcode == 26) {
				operation = "Decrement";
				Decrement(operand);
			} else if (opcode == 50) {
				System.out.println("accumulator: " + accumulator + "\noperation Halt");
				System.out.println("--- Basic-Computer execution terminated ---");
				x = 123456789;
				break;
			} else {
				System.out.println("\n" + a + " is not a recognized command\n");
				break;
			}
		}
		String junk = s.nextLine();
		return x;
	}

	private static void memoryDump() {
		// This is the memory dump which is what is displayed for the user
		String insreg = (int) opcode + "" + (int) operand;
		System.out.println("Registers:\n\n");
		if (accumulator < 0)
			System.out.printf("Accumulator                       -%04d", Math.abs((int) accumulator));
		else
			System.out.printf("Accumulator                       +%04d", Math.abs((int) accumulator));
		/*
		 * System.out.println( "\nProgram Counter                    " +
		 * (int)program_counter + "\n" + "Instruction Register               " + (int)
		 * opcode + "" + (int) operand + "\n" + "Operation Code                     " +
		 * (int) opcode + "\n" + "Operand                            " + (int) operand +
		 * "\n\n\n");
		 */
		System.out.println("");
		System.out.printf("Program Counter                    %02d", (int) program_counter);
		System.out.println("");
		System.out.printf("Instruction Register               %02d%02d", (int) opcode, (int) operand);
		System.out.println("");
		System.out.printf("Operation Code                     %02d", (int) opcode);
		System.out.println("");
		System.out.printf("Operand                            %02d", (int) operand);
		System.out.println("\n\n");
		System.out.println("Memory Dump\n"
				+ "       0        1        2        3        4        5        6        7        8        9\n");

		// for loop is for displaying the side numbers and the chart of all of the
		// memory array
		for (int i = 0; i <= 99; i++) {
			if ((i % 10) == 0 && i != 0) {
				if (memory[i] < 0)
					System.out.printf("\n" + i + "    -%04d     ", Math.abs((int) memory[i]));
				else
					System.out.printf("\n" + i + "    +%04d     ", Math.abs((int) memory[i]));
			} else if ((i % 10) != 0 && i != 0) {
				if (memory[i] < 0)
					System.out.printf("-%04d    ", Math.abs((int) memory[i]));
				else
					System.out.printf("+%04d    ", Math.abs((int) memory[i]));
			} else if ((i % 10) == 0 && i == 0) {
				if (memory[i] < 0)
					System.out.printf("\n00    -%04d     ", Math.abs((int) memory[i]));
				else
					System.out.printf("\n00    +%04d     ", Math.abs((int) memory[i]));
			}

		}
		System.out.println("");
	}

	private static void Decrement(double c) {
		// decrement a word in memory by 1
		memory[(int) c] -= 1;

	}

	private static void Increment(double c) {
		// increment a word in memory by 1
		memory[(int) c] += 1;

	}

	private static void Branchzero(double c) {
		// This will branch to a specific location in operation if the accumulator is
		// zero
		Branchvar = (int) program_counter;
		if (accumulator == 0)
			program_counter = ((int) c) - 1;
	}

	private static void Branchpos(double c) {
		// This will branch to a specific location in the operation if the
		// accumulator is already positive
		Branchvar = (int) program_counter;
		if (accumulator > 0)
			program_counter = ((int) c) - 2;
	}

	private static void Branchneg(double c) {
		// This will branch to a specific location in operation if the accumulator is
		// negative
		Branchvar = (int) program_counter;
		if (accumulator < 0)
			program_counter = ((int) c) - 2;
	}

	private static void Branch(double c) {
		Branchvar = (int) program_counter;
		program_counter = ((int) c) - 2;
	}

	private static void DivideImm(double c) {
		// divide the immediate value, which is a value not stored in memory, to the
		// accumulator
		accumulator /= c;
	}

	private static void MultImm(double c) {
		// multiply the immediate value, which is a value not stored in memory, to the
		// accumulator
		accumulator *= c;
	}

	private static void DecImm(double c) {
		// subtract the immediate value, which is a value not stored in memory, to the
		// accumulator
		accumulator -= c;

	}

	private static void AddImm(double c) {
		// add the immediate value, which is a value not stored in memory, to the
		// accumulator
		accumulator += c;

	}

	private static void LoadImm(double c) {
		// load the immediate value, which is a value not stored in memory, into the
		// accumulator
		accumulator = c;

	}

	private static void Multiply(double c) {
		// Multiply a word from memory to the accumulator and make the new value the
		// accumulator
		accumulator = accumulator * memory[(int) c];
	}

	private static void Divide(double c) {
		// divide a word from memory to the accumulator and make the new value the
		// accumulator
		accumulator = accumulator / memory[(int) c];

	}

	private static void Subtract(double c) {
		// subtract a word from memory to the accumulator and make the new value the
		// accumulator
		accumulator = accumulator - memory[(int) c];

	}

	private static void Add(double c) {
		// add a word from memory to the accumulator and make the new value the
		// accumulator
		accumulator = accumulator + memory[(int) c];
	}

	private static void Store(double c) {
		// Store the accumulator into a specific location in memory
		memory[(int) c] = accumulator;
	}

	private static void Storezero(double c) {
		// Store the accumulator into a specific location in memory
		memory[(int) c] = accumulator;
		if ((program_counter + 1) < instructRegister.size())
			accumulator = 0;
	}

	private static void Load(double c) {
		// load a specific word in memory into the accumulator
		accumulator = memory[(int) c];
	}

	private static void Write(double c) {
		// Display a word that is in memory to the system
		System.out.println(memory[(int) c]);
	}

	private static void Read(double c) {
		// Read a word that the user enters and store it in memory
		System.out.println("What is your number?");
		@SuppressWarnings("resource")
		double userin = s.nextDouble();
		memory[(int) c] = userin;
	}

	private static void WriteAscii(double c) {
		// This is to convert the numbers into ascii and print it out
		// make two arrays that have the same location for corresponding numbers to
		// binary
		double s = accumulator - 1;
		String[] nums = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", " ", };

		String[] ascii = { "00110000", "00110001", "00110010", "00110011", "00110100", "00110101", "00110110",
				"00110111", "00111000", "00111001", "0010000" };

		// Make an array list and store every number in that array list that needs to be
		// stored in it
		// The numbers are from specified location in memory to the amount that is in
		// the accumulator
		ArrayList<String> memnum = new ArrayList<>();
		for (int i = 0; i <= s; i++) {
			if ((c + i) <= 99) {
				memnum.add("" + (int) memory[(int) c + i] + " ");
			} else
				i += s;
		}

		// Separate out the numbers to single numbers ex. 12 becomes 1 2
		// Put a space after every separated number to show where one number ends and
		// another starts
		// Find the index of each seperated number in the nums array
		ArrayList<Integer> index = new ArrayList<>();
		for (int i = 0; i < memnum.size(); i++) {
			String a = memnum.get(i);
			for (int j = 0; j < a.length(); j++) {
				String z = "" + a.charAt(j);
				index.add(Arrays.asList(nums).indexOf(z));
			}
		}

		// Match the index of the nums array with the index of the ascii binary code and
		// print the ascii
		for (int i = 0; i < index.size(); i++) {
			System.out.print(ascii[index.get(i)] + " ");
		}
		promptEnterKey();
	}

	private static void WriteValue(double c) {
		// Print out everything from the specified location in memory and the continue
		// printing the next location for the specified location in memory
		double s = accumulator - 1;
		for (int i = 0; i <= s; i++) {
			DecimalFormat df = new DecimalFormat("##.##");
			if ((c + i) <= 99)
				System.out.println(df.format(memory[(int) (c + i)]) + " ");
			else
				i += s;
		}
		promptEnterKey();
	}

	public static void promptEnterKey() {
		System.out.println("Press \"ENTER\" to continue...");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

}
