Project for ICS 440 Parallel and Distributed Algorithms

StringHandoff is used to pass a string from one thread to another. The passer and the receiver meet inside an instance for the hand-off. 
For example, if pass() is called and there is no receiver waiting, the thread calling pass() will block until a receiver arrives.
Similarly, if a receive() is called and there is no passer waiting, the thread calling receive() will block until a passer arrives.
