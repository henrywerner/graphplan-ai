# GraphPlan A.I.
This program locates a possible solution to a provided 10x10 grid maze using a modified GraphPlan algorithm. 
If the maze is solvable, the AI will identify a solution and describe the moves it takes to reach the endpoint.
If the maze is not solvable, the AI will properly identify that no solution can be found.

## Files Included
`handler.java`
: Houses the main method; Converts the input file into a byte matrix.

`graphMaster.java`
: Contains core logic for building the planning graph and solving the map.

`action.java`
: An object class for action schema. Contains methods for generating resulting states.

`state.java`
: Stores a state's information and generates available actions.

`./maps/`
: A folder of example maps used in testing. 


## Execution
The program was developed with JDK 16.0.1 using the IntelliJ IDEA IDE. I also tested compiling and running using 
Windows command prompt. Input is handled by passing through the map file name as an argument.

Here is an example of compiling and executing in command prompt while using a map file from the maps folder:
```
C:\Users\htwer\Desktop\graphplan-ai\src>javac handler.java

C:\Users\htwer\Desktop\graphplan-ai\src>java handler ../maps/testmap_1.txt
######################
#[]                  #
#          []        #
#  []        [][]    #
#          []        #
#    [][]  []        #
#      []          []#
#                    #
#[]        []  []    #
#        []      ST[]#
#        EN[][]    []#
######################

Starting point: (8, 8)
Endpoint found. 
Solution:
Left
Left
Up
Up
Left
Left
Left
Down
Down
Down
Right

######################
#[]                  #
#          []        #
#  []        [][]    #
#          []        #
#    [][]  []        #
#      []          []#
#      ::::::::      #
#[]    ::  []::[]    #
#      ::[]  ::::::[]#
#      ::;;[][]    []#
######################

C:\Users\htwer\Desktop\graphplan-ai\src>
```
NOTE: When running the program within an IDE, I had to use the argument `./maps/MapFile` instead of `../maps/MapFile`.

## Map Depiction
`ST` : Starting point

`EN` : End point

`[]` : Wall; The agent is unable to pass through.

`##` : Map boundary

`::` : AI path

`;;` : AI path end point

## PDDL Representation
```
Init(at(startPointX, startPointY))
Goal(at(endPointX, endPointY))
Action(MoveX(X1,Y1,X2),
    PRECOND: IsClear(X2, Y1) ∧ at(X1, Y1) ∧ |X1 - X2| = 1
    EFFECT:  ¬at(X1, Y1) ∧ at(X2, Y1))
Action(MoveY(X1,Y1,Y2),
    PRECOND: IsClear(X1, Y2) ∧ at(X1, Y1) ∧ |Y1 - Y2| = 1
    EFFECT:  ¬at(X1, Y1) ∧ at(X1, Y2))
```