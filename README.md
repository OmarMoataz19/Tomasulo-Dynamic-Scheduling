
# Tomasulo-Dynamic-Scheduling


Tomasulo's Algorithm is a revolutionary computer architecture hardware algorithm designed for dynamic scheduling of instructions. By introducing out-of-order execution, the algorithm maximizes the utilization of multiple execution units in a processor, significantly enhancing the efficiency of instruction execution in modern superscalar processors. The primary goal is to increase the number of instructions executed per clock cycle, thereby improving overall performance.

## Key Features

- **Out-of-Order Execution:** Instructions are not constrained by program order, allowing for parallel execution when resources are available.
- **Increased Clock Cycle Efficiency:** Maximizes the number of instructions executed per clock cycle in superscalar processors.
- **Dynamic Scheduling:** Instructions are scheduled based on the availability of execution units and operands, optimizing overall performance.
- **Reservation Stations:** Serve as dynamic buffers for instructions, facilitating efficient resource management.

## Approach and Structure

The implementation of Tomasulo's Algorithm follows a structured object-oriented approach, dividing the logical units into distinct classes. This modular design enhances code organization and readability, ensuring clear responsibilities for each class.

### 1. Classes Determination

The initial step involved identifying the essential classes for the implementation. Given the object-oriented coding structure, we decided on the following key classes:

- **Parser Class:** Responsible for reading instructions line by line.
- **Register Class:** Holds information such as name, content, and tag for each register.
- **Register File Class:** Manages an array of registers with corresponding setters and getters.

### 2. Reservation Stations

Then the reservation slots were implemented, to facilitate dynamic scheduling. Each reservation slot includes:

- Slot name (e.g., A0 for addition slot).
- Opcode of the instruction.
- Values (vj, vk) if ready; otherwise, qj and qk for waiting.
- Busy and ready flags.
- Effective address.
- Timer for the time left for instruction execution.

Additionally, a class manages the size and type of reservation stations, grouping similar slots together for efficient handling.

### 3. Memory and Buffers

- **Memory Class:** Handles the logic of memory using an array.
- **Buffer and Buffer Slot Classes:** Responsible for loads and stores. Buffer slots include information like busy, ready flags, vj, qj, and address.

### 4. Processor Class

The heart of the implementation is the Processor class, which orchestrates the Tomasulo processor's key stages:

- **Issuing:** Ensures there is a reservation station before successfully issuing an instruction, implementing stall mechanisms if necessary.
- **Execution:** Manages the decrementing of the time left by an instruction each cycle, provided it has the required data and is in a reservation station.
- **Writing Back:** Updates necessary fields during the writing back stage, ensuring data is given to anyone with the corresponding tag on the bus.

### 5. Common Data Bus (CDB) Class

A class named Common Data Bus (CDB) handles the logic of publishing on the bus. Only one operation is allowed to publish on the bus at a time, and stores don't publish on the bus. The data published includes the tag and content, and a busy flag ensures exclusive access to the bus.

### 6. Assumptions

In our implementation, we made the following assumptions:

1. Only 1 memory instruction (load or store) is permitted to access the memory (execute) at a time; no two loads or stores can be executing simultaneously.

2. When writing back, we determine who writes back by traversing the Addition/Sub reservation stations in a top-down manner. If no instruction wants to write back, we access the Mul/Div reservation stations in a top-down manner. If still no match, we check the load buffers.

3. If something wants to be issued and something is writing back, the issuing process is stalled until the next cycle.

### 7. Test Cases

#### Test Case 1

- ADD.D F1 F1 3
- ADD.D F2 F1 2
- ADD.D F5 F5 3
- ADD.D F6 F6 2
- MUL.D F3 F1 F2
- S.D F3 10
- L.D F4 11
- HALT

#### Test Case 2
- L.D F6 32
- L.D F2 44
- MUL.D F0 F2 F4
- SUB.D F8 F2 F6
- DIV.D F10 F0 F6
- ADD.D F6 F8 F2

## Usage

1) Clone the repository
2) Open the cloned repository in an IDE (eg. Eclipse).
3) Change Test.txt to match a desired program.
4) Run the main class.



