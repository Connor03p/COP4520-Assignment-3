# COP4520 Assignment 3
## Part 1
The Minotaur’s birthday party was a success. The Minotaur received a lot of presents from his guests. The next day he decided to sort all of his presents and start writing “Thank you” cards. Every present had a tag with a unique number that was associated with the guest who gave it. Initially all of the presents were thrown into a large bag with no particular order. The Minotaur wanted to take the presents from this unordered bag and create a chain of presents 
hooked to each other with special links (similar to storing elements in a linked-list). In this chain (linked-list) all of the presents had to be ordered according to their tag numbers in increasing order. The Minotaur asked 4 of his servants to help him with creating the chain of presents and writing the cards to his guests.

Each servant would do one of three actions in no particular order: 
1. Take a present from the unordered bag and add it to the chain in the correct location by hooking it to the predecessor’s link. The servant also had to make sure that the newly added present is also linked with the next present in the chain. 
2. Write a “Thank you” card to a guest and remove the present from the chain. To do so, a servant had to unlink the gift from its predecessor and make sure to connect the predecessor’s link with the next gift in the chain. 
3. Per the Minotaur’s request, check whether a gift with a particular tag was present in the chain or not; without adding or removing a new gift, a servant would scan through the chain and check whether a gift with a particular tag is already added to the ordered chain of gifts or not.

As the Minotaur was impatient to get this task done quickly, he instructed his servants not 
to wait until all of the presents from the unordered bag are placed in the chain of linked 
and ordered presents. Instead, every servant was asked to alternate adding gifts to the ordered 
chain and writing “Thank you” cards. The servants were asked not to stop or even take a 
break until the task of writing cards to all of the Minotaur’s guests was complete. After 
spending an entire day on this task the bag of unordered presents and the chain of ordered 
presents were both finally empty! Unfortunately, the servants realized at the end of the day 
that they had more presents than “Thank you” notes. What could have gone wrong? Can we help the
Minotaur and his servants improve their strategy for writing “Thank you” notes? Design
and implement a concurrent linked-list that can help the Minotaur’s 4 servants with this
task. In your test, simulate this concurrent “Thank you” card writing scenario by
dedicating 1 thread per servant and assuming that the Minotaur received 500,000
presents from his guests.

### Solution
Once the bag of presents are created and randomized, the servants begin going through the presents.
Until all "Thank you" cards are written, servants will randomly assign themselves one of three tasks:
1. Take a present from the bag and add it to the correct position in the linked list
2. Remove a present from the linked list and write a "Thank you" card for it
3. Check whether a guest's gift is in the chain or not. (Though nothing is done with this information)

To minimize waiting, the program uses a concurrent linked list with lazy synchronization from the textbook. Instead of the whole list being locked when a thread is working on it, this method only locks the nodes being accessed and modified. This way, concurrent threads can run operations on the list, so long as they are working on different parts of the list.

Unfortunately, I found that the runtime for a single servant is slightly faster than for four in my testing. This could be caused by a few factors. This could be due to how the bags are handled, as they use synchronized arraylists instead of concurrent lists. The third task of checking for gifts in the chain can also contribute to this since nothing is done with the results of that check.

To ensure the results are correct, there are several checks that can be enabled by the DEBUG constant. These tests check that the bag of presents and the list of presents are empty, that each guest got a note written for them, and that there are no duplicates in any of the lists


## Part 2
You are tasked with the design of the module responsible for measuring the atmospheric temperature of the next generation Mars Rover, equipped with a multicore CPU and 8 temperature sensors. The sensors are responsible for collecting temperature readings at regular intervals and storing them in shared memory space. The atmospheric temperature module has to compile a report at the end of every hour, comprising the top 5 highest temperatures recorded for that hour, the top 5 lowest temperatures recorded for that hour, and the 10-minute interval of time when the largest temperature difference was observed. The data storage and retrieval of the shared memory region must be carefully handled, as we do not want to delay a sensor and miss the interval of time when it is supposed to conduct temperature reading. Design and implement a solution using 8 threads that will offer a solution for this task. Assume that the temperature readings are taken every 1 minute. In your solution, simulate the operation of the temperature reading sensor by generating a random number from -100F to 70F at every reading.

### Solution
To simulate time passing, I used java's ScheduledExecutorService, which can be used to schedule threads to run after some delay. Tasks keep track of when they were last run, and will keep track of how well they are keeping on schedule with a intervalOffset variable. This is used to report if a sensor is running far enough behind schedule to miss a recording, as well as adjust how often a new sensor recording is scheduled. Along with this, a PriorityBlockingQueue is used to ensure that sensors that are running behind get top priority. Combined, they ensure that sensors will always be able to take their recording within reasonable time constraints. To further improve performance, the shared memory uses a concurrent linked queue, Java's built-in version of the linked list made for part one.

Since simulating this situation with real minutes or hours would take too long, there is a timeMultiplier constant that can change how quickly one simulated minute passes. It is currently set to 1800, so that each second simulates 30 minutes. There are also constants for how much information the program prints, but the time scale should be reduced if these are used, as it causes delays and can easily result in threads falling behind.
In my testing without printing, the time scale could be increased up to 2400 (40 minutes / second) before some sensors would regularly fall behind.

To check that the output is correct, there are constants to toggle logging for any sensor recordings and to see how each sensor task is scheduled. These options cause significant delay to threads though, and can easily cause them to fall behind schedule. If used, the time multiplier should be reduced to give the sensor threads enough time to print their recordings. There is also a constant to end the program if a thread misses an interval.

## How to compile and run
### Part 1

Open a command prompt window in `\Part 1\src`

Run `javac App.java` to compile

Run `java App` to run the program

### Part 2

Open a command prompt window in `\Part 2\src`

Run `javac App.java` to compile

Run `java App` to run the program