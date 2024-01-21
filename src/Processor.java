public class Processor {

	String[] InstructionMemory;
	Memory dataMemory;
	RegisterFile regFile;
	ReservationStation addStation;
	ReservationStation mulStation;
	Buffer loadBuffer;
	Buffer storeBuffer;
	Parser parser;
	CDB bus;
	int addSubLatency;
	int multLatency;
	int divLatency;
	int loadLatency;
	int storeLatency;
	int instructionIndex = 0;
	int cycle = 1;
	boolean memAccess = false;

	public Processor(String path, int addSubLatency, int multLatency, int divLatency, int loadLatency,
			int storeLatency) {
		parser = new Parser();
		try {
			InstructionMemory = parser.parse(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataMemory = new Memory(127);
		dataMemory.setMemory(120, 120.0f);
		regFile = new RegisterFile();
		addStation = new ReservationStation(3, "add");

		mulStation = new ReservationStation(2, "mult");
		loadBuffer = new Buffer(4, "load");
		storeBuffer = new Buffer(4, "store");
		this.addSubLatency = addSubLatency;
		this.multLatency = multLatency;
		this.divLatency = divLatency;
		this.loadLatency = loadLatency;
		this.storeLatency = storeLatency;
		bus = new CDB();
		// for(int i = 0; i <regFile.getSize();i++){

		// 	regFile.setRegister(i,i, "0");

		// }
		// print instruction memory
		// for (int i = 0; i < InstructionMemory.length; i++) {
		// System.out.println(InstructionMemory[i]);
		// }

	}

	public void issue() {
		if (InstructionMemory[instructionIndex].equals("HALT")) {
			return;
		}
		String currentInstruction = InstructionMemory[instructionIndex];
		String[] inst = currentInstruction.split(" ");
		switch (inst[0]) {
		case "ADD.D", "SUB.D":
			System.out.println("Issuing " + currentInstruction);
			issueToReservationStation(addStation, inst);
			break;
		case "MUL.D", "DIV.D":
			System.out.println("Issuing " + currentInstruction);
			issueToReservationStation(mulStation, inst);
			break;
		case "L.D":
			System.out.println("Issuing " + currentInstruction);
			issueToLoadBuffer(inst);
			break;
		case "S.D":
			System.out.println("Issuing " + currentInstruction);
			issueToStoreBuffer(inst);
			break;
		}
		for (int i=0; i<loadBuffer.getSize(); i++){
			BufferSlot tempSlot=loadBuffer.getBufferSlot(i);
			tempSlot.setReady(true);
			loadBuffer.setBufferSlot(i, tempSlot);
		}
		for (int i=0; i<addStation.getSize(); i++){
			ReservationSlot tempSlot=addStation.getReservationSlot(i);
			tempSlot.setReady(true);
			addStation.setReservationSlot(i,tempSlot);
		}
		for (int i=0; i<mulStation.getSize(); i++){
			ReservationSlot tempSlot=mulStation.getReservationSlot(i);
			tempSlot.setReady(true);
			mulStation.setReservationSlot(i,tempSlot);
		}
		
	}

	public void issueToReservationStation(ReservationStation type, String[] instruction) {
		int freeSlot = type.getFreeReservationSlot();
		if (freeSlot != -1) {
			
			ReservationSlot slot = type.getReservationSlot(freeSlot);
			if (slot.isReady()) {

			String op = instruction[0];
			int destination = Integer.parseInt(instruction[2].replace("F", ""));
			Register firstOperand = regFile.getRegister(destination);
			if (firstOperand.getTag().equals("0")) {
				slot.setVj(firstOperand.getContent());
			} else {
				slot.setQj(firstOperand.getTag());
			}
			try {
				float immediate = (float) Integer.parseInt(instruction[3]);
				slot.setVk(immediate);
			} catch (Exception e) {
				destination = Integer.parseInt(instruction[3].replace("F", ""));
				Register secondOperand = regFile.getRegister(destination);
				if (secondOperand.getTag().equals("0")) {
					slot.setVk(secondOperand.getContent());
				} else {
					slot.setQk(secondOperand.getTag());
				}
			}
			destination = Integer.parseInt(instruction[1].replace("F", ""));
			Register destinationRegister = regFile.getRegister(destination);
			regFile.setRegister(destination, destinationRegister.getContent(), slot.getName());
			slot.setBusy(true);
			slot.setOp(op);
			switch (instruction[0]) {
			case "ADD.D", "SUB.D":
				slot.setTimer(addSubLatency);
				break;
			case "MUL.D":
				slot.setTimer(multLatency);
				break;
			case "DIV.D":
				slot.setTimer(divLatency);
				break;
			}

			type.setReservationSlot(freeSlot, slot);
			instructionIndex++;}
			else{
				slot.setReady(true);
				System.out.println("Can't issue this cycle because reservation slot has been just cleared");
			}
		} else {
			System.out.println("No more free slots available in the Reservation station so this issue Will be stalled");
		}


	}

	public void issueToLoadBuffer(String[] instruction) {
		int freeSlot = loadBuffer.getFreeBufferSlot();
		if (freeSlot != -1) {
			BufferSlot slot = loadBuffer.getBufferSlot(freeSlot);
			if (slot.isReady()) {
			int destination = Integer.parseInt(instruction[1].replace("F", ""));
			Register destinationRegister = regFile.getRegister(destination);
			regFile.setRegister(destination, destinationRegister.getContent(), slot.getName());
			slot.setA(Integer.parseInt(instruction[2]));
			slot.setBusy(true);
			slot.setTimer(loadLatency);
			loadBuffer.setBufferSlot(freeSlot, slot);
			instructionIndex++;}
			else{
				slot.setReady(true);
				System.out.println("Can't issue this cycle because reservation slot has been just cleared");	
			}
		
		} else {
			System.out.println("No more free slots available in the Reservation station so this issue Will be stalled");

		}

	}

	public void issueToStoreBuffer(String[] instruction) {
		int freeSlot = storeBuffer.getFreeBufferSlot();
		if (freeSlot != -1) {
			BufferSlot slot = storeBuffer.getBufferSlot(freeSlot);
			int source = Integer.parseInt(instruction[1].replace("F", ""));
			Register sourceRegister = regFile.getRegister(source);
			if (sourceRegister.getTag().equals("0")) {
				slot.setVj(sourceRegister.getContent());
			} else {
				slot.setQj(sourceRegister.getTag());
			}
			slot.setA(Integer.parseInt(instruction[2]));
			slot.setBusy(true);
			slot.setTimer(storeLatency);
			storeBuffer.setBufferSlot(freeSlot, slot);
			instructionIndex++;

		} else {
			System.out.println("No more free slots available in the Reservation station so this issue Will be stalled");
		}
	}

	public void execute() {
		boolean addFlag = false;
		for (int i = 0; i < addStation.getSize(); i++) {
			ReservationSlot currentSlot = addStation.getReservationSlot(i);
			if (currentSlot.isBusy()) {
				if (currentSlot.getQj().equals("0") && currentSlot.getQk().equals("0") && currentSlot.isReady()) {
					if (currentSlot.getTimer() == addSubLatency) {
						currentSlot.setTimer(currentSlot.getTimer() - 1);
						System.out.println(currentSlot.getName() + " has started executing this cycle");
					} else {
					if (currentSlot.getTimer() != 0) {
						if (currentSlot.getTimer() == 1) {
							System.out.println(
									currentSlot.getName() + " continues executing and finish by the end of this cycle");
						} else {
							System.out.println(currentSlot.getName() + " continues executing this cycle");
						}
						currentSlot.setTimer(currentSlot.getTimer() - 1);
					}
					 }
				} else {
					if (!currentSlot.getQj().equals("0"))
						System.out.println(
								currentSlot.getName() + " can't execute as it is waiting for :" + currentSlot.getQj());
					if (!currentSlot.getQk().equals("0"))
						System.out.println(
								currentSlot.getName() + " can't execute as it is waiting for :" + currentSlot.getQk());
					if (!currentSlot.isReady())
						System.out.println(currentSlot.getName()
								+ " can't execute as it just got what it is waiting for" + " this cycle");
				}
				currentSlot.setReady(true);
			}
		}
		boolean mulFlag = false;
		boolean divFlag = false;
		for (int i = 0; i < mulStation.getSize(); i++) {
			ReservationSlot currentSlot = mulStation.getReservationSlot(i);
			String type = currentSlot.getOp();
			if (currentSlot.isBusy()) {
				if (currentSlot.getQj().equals("0") && currentSlot.getQk().equals("0") && currentSlot.isReady()) {

					if (type.equals("MUL.D") && currentSlot.getTimer() == multLatency) {
						System.out.println(currentSlot.getName() + " has started executing this cycle");
						currentSlot.setTimer(currentSlot.getTimer() - 1);

					} else if (type.equals("DIV.D") && currentSlot.getTimer() == divLatency) {
						System.out.println(currentSlot.getName() + " has started executing this cycle");
						currentSlot.setTimer(currentSlot.getTimer() - 1);
					}  else {
					if (currentSlot.getTimer() != 0) {
						if (currentSlot.getTimer() == 1) {
							System.out.println(
									currentSlot.getName() + " continues executing and finish by the end of this cycle");
						} else {
							System.out.println(currentSlot.getName() + " continues executing this cycle");
						}
						currentSlot.setTimer(currentSlot.getTimer() - 1);
					}
					 }
				} else {
					if (!currentSlot.getQj().equals("0"))
						System.out.println(
								currentSlot.getName() + " can't execute as it is waiting for :" + currentSlot.getQj());
					if (!currentSlot.getQk().equals("0"))
						System.out.println(
								currentSlot.getName() + " can't execute as it is waiting for :" + currentSlot.getQk());
					if (!currentSlot.isReady())
						System.out.println(currentSlot.getName()
								+ " can't execute as it just got what it is waiting for" + " this cycle");
				}
				currentSlot.setReady(true);
			}
		}

		for (int i = 0; i < loadBuffer.getSize(); i++) {
			BufferSlot currentSlot = loadBuffer.getBufferSlot(i);
			if (currentSlot.isBusy()) {
				if (!memAccess) {
					currentSlot.setTimer(currentSlot.getTimer() - 1);
					memAccess = true;
					System.out.println(currentSlot.getName() + " has started executing this cycle");
				} else {
					if (currentSlot.getTimer() != loadLatency && currentSlot.getTimer() != 0) {
						if (currentSlot.getTimer() == 1) {
							System.out.println(
									currentSlot.getName() + " continues executing and finish by the end of this cycle");
						} else {
							System.out.println(currentSlot.getName() + " continues executing this cycle");
						}
						currentSlot.setTimer(currentSlot.getTimer() - 1);
					}
				}
			}
		}
		for (int i = 0; i < storeBuffer.getSize(); i++) {
			BufferSlot currentSlot = storeBuffer.getBufferSlot(i);
			if (currentSlot.isBusy()) {
				if (currentSlot.getQj().equals("0") && currentSlot.isReady()) {
					if (!memAccess) {
						currentSlot.setTimer(currentSlot.getTimer() - 1);
						memAccess = true;
						System.out.println(currentSlot.getName() + " has started executing this cycle");
					} else {
						if (currentSlot.getTimer() != storeLatency && currentSlot.getTimer() != 0) {
							if (currentSlot.getTimer() == 1) {
								System.out.println(currentSlot.getName()
										+ " continues executing and finish by the end of this cycle");
							} else {
								System.out.println(currentSlot.getName() + " continues executing this cycle");
							}
							currentSlot.setTimer(currentSlot.getTimer() - 1);
						}
					}
				} else {
					if (!currentSlot.getQj().equals("0"))
						System.out.println(
								currentSlot.getName() + " can't execute as it is waiting for :" + currentSlot.getQj());
					if (!currentSlot.isReady())
						System.out.println(currentSlot.getName()
								+ " can't execute as it just got what it is waiting for" + " this cycle");
				}
				currentSlot.setReady(true);
			}
		}
	}

	public void writeBack() {

		for (int i = 0; i < addStation.getSize(); i++) {
			ReservationSlot currentSlot = addStation.getReservationSlot(i);
			if (currentSlot.isBusy()) {
				if (!bus.isBusy()) {
					if (currentSlot.getTimer() == 0) {
						float result = 0;
						switch (currentSlot.getOp()) {
						case "ADD.D":
							result = currentSlot.getVj() + currentSlot.getVk();
							break;
						case "SUB.D":
							result = currentSlot.getVj() - currentSlot.getVk();
							break;
						}
						bus.setContent(result);
						bus.setBusy(true);
						bus.setTag(currentSlot.getName());
						addStation.clearReservationSlot(i);
						currentSlot.setReady(false);
						System.out.println(currentSlot.getName() + " will write back on the bus next cycle");
					}

				}
			}

		}

		for (int i = 0; i < mulStation.getSize(); i++) {
			ReservationSlot currentSlot = mulStation.getReservationSlot(i);
			if (currentSlot.isBusy()) {
				if (!bus.isBusy()) {
					if (currentSlot.getTimer() == 0) {
						float result = 0;
						switch (currentSlot.getOp()) {
						case "MUL.D":
							result = currentSlot.getVj() * currentSlot.getVk();
							break;
						case "DIV.D":
							result = currentSlot.getVj() / currentSlot.getVk();
							break;
						}
						bus.setContent(result);
						bus.setBusy(true);
						bus.setTag(currentSlot.getName());
						mulStation.clearReservationSlot(i);
						currentSlot.setReady(false);
						System.out.println(currentSlot.getName() + " will write back on the bus next cycle");
					}
				}

			}

		}

		for (int i = 0; i < loadBuffer.getSize(); i++) {
			BufferSlot currentSlot = loadBuffer.getBufferSlot(i);
			if (currentSlot.isBusy()) {
				if (!bus.isBusy()) {
					if (currentSlot.getTimer() == 0) {
						float result = 0;
						memAccess = false;
						result = dataMemory.getMemory(currentSlot.getA());
						bus.setContent(result);
						bus.setBusy(true);
						bus.setTag(currentSlot.getName());
						loadBuffer.clearBufferSlot(i);
						currentSlot.setReady(false);
						System.out.println(currentSlot.getName() + " will write back on the bus next cycle");
					}
				}
			}
		}

		for (int i = 0; i < storeBuffer.getSize(); i++) {
			BufferSlot currentSlot = storeBuffer.getBufferSlot(i);
			if (currentSlot.isBusy()) {
				if (currentSlot.getTimer() == 0) {
					float result = 0;
					memAccess = false;
					result = storeBuffer.getBufferSlot(i).getVj();
					dataMemory.setMemory(currentSlot.getA(), result);
					storeBuffer.clearBufferSlot(i);
					System.out.println(currentSlot.getName() + " finished executing");
				}
			}
		}

		for (int i = 0; i < regFile.getSize(); i++) {
			if (regFile.getRegister(i).getTag().equals(bus.getTag()) && bus.isBusy()) {
				regFile.getRegister(i).setContent(bus.getContent());
				regFile.getRegister(i).setTag("0");
			}
		}
		for (int i = 0; i < addStation.getSize(); i++) {
			ReservationSlot currentSlot = addStation.getReservationSlot(i);
			if (currentSlot.isBusy()) {
				if (currentSlot.getQj().equals(bus.getTag()) && bus.isBusy()) {
					currentSlot.setVj(bus.getContent());
					currentSlot.setQj("0");
					currentSlot.setReady(false);
				}
				if (currentSlot.getQk().equals(bus.getTag()) && bus.isBusy()) {
					currentSlot.setVk(bus.getContent());
					currentSlot.setQk("0");
					currentSlot.setReady(false);
				}
			}
		}
		for (int i = 0; i < mulStation.getSize(); i++) {
			ReservationSlot currentSlot = mulStation.getReservationSlot(i);
			if (currentSlot.isBusy()) {
				if (currentSlot.getQj().equals(bus.getTag()) && bus.isBusy()) {
					currentSlot.setVj(bus.getContent());
					currentSlot.setQj("0");
					currentSlot.setReady(false);
				}
				if (currentSlot.getQk().equals(bus.getTag()) && bus.isBusy()) {
					currentSlot.setVk(bus.getContent());
					currentSlot.setQk("0");
					currentSlot.setReady(false);
				}
			}

		}

		for (int i = 0; i < storeBuffer.getSize(); i++) {
			BufferSlot currentSlot = storeBuffer.getBufferSlot(i);
			if (currentSlot.isBusy()) {
				if (currentSlot.getQj().equals(bus.getTag()) && bus.isBusy()) {
					currentSlot.setVj(bus.getContent());
					currentSlot.setQj("0");
					currentSlot.setReady(false);
				}
			}
		}
		bus.setBusy(false);
	}

	public boolean isFinished() {
		for (int i = 0; i < addStation.getSize(); i++) {
			if (addStation.getReservationSlot(i).isBusy())
				return false;
		}
		for (int i = 0; i < mulStation.getSize(); i++) {
			if (mulStation.getReservationSlot(i).isBusy())
				return false;
		}
		for (int i = 0; i < loadBuffer.getSize(); i++) {
			if (loadBuffer.getBufferSlot(i).isBusy())
				return false;
		}
		for (int i = 0; i < storeBuffer.getSize(); i++) {
			if (storeBuffer.getBufferSlot(i).isBusy())
				return false;
		}
		if (!InstructionMemory[instructionIndex].equals("HALT")) {
			return false;
		}
		return true;
	}

	public void run() {
		while (!isFinished()) {
			writeBack();
			System.out.println("-------------------------------------------------------------------");
			System.out.println("Cycle " + cycle + "\n");
			execute();
			issue();
			this.print(InstructionMemory);
			System.out.println("\n" + "Add/Sub_Reservation_Stations" + "\n" + addStation.toString());
			System.out.println("Mul/Div_Reservation_Stations" + "\n" + mulStation.toString());
			System.out.println("LoadBuffers" + "\n" + loadBuffer.toString());
			System.out.println("StoreBuffers" + "\n" + storeBuffer.toString());
			System.out.println(bus.toString() + "\n");
			//this.print();
			cycle++;
		}
		System.out.println("Data Memory");
		System.out.println(dataMemory.toString());
	}

	public void print() {
		System.out.println("Registers");
		System.out.println(regFile.toString());
		//System.out.println("Data Memory");
		// System.out.println(dataMemory.toString());
	}

	void print(String [] array){
		System.out.println();
		System.out.println("Instruction Queue: [");
		int i=instructionIndex;
		while(!array[i].equals("HALT")){
			System.out.println(array[i]);
			i++;
		}
		System.out.println("]");
	}
	public static void main(String[] args) {
		Processor processor = new Processor("test1.txt", 3, 3, 6, 1, 1);
		processor.run();
	}
}