# GraphPlan A.I.
This program locates a possible solution to a provided 10x10 grid maze using a modified GraphPlan algorithm. 

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
The program was developed using JDK 16.0.1 using the IntelliJ IDEA IDE. 

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
## Map Depiction
`ST` : Starting point

`EN` : End point

`[]` : Wall; The agent is unable to pass through.

`##` : Map boundary

`::` : AI path

`;;` : AI path end point