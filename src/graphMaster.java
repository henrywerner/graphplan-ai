public class graphMaster {

    /* PDDL:
    Init(Poss(StartPoint))
    Goal(Poss(EndPoint))
    Action(Move(Left)
        PRECOND: IsClear(Left)
        EFFECT:  Poss(x-1,y))
    Action(Move(Right)
        PRECOND: IsClear(Right)
        EFFECT:  Poss(x+1,y))
    Action(Move(Up)
        PRECOND: IsClear(Up)
        EFFECT:  Poss(x,y-1))
    Action(Move(Down)
        PRECOND: IsClear(Down)
        EFFECT:  Poss(x,y+1))

    CAN YOU DO A HLA THAT WOULD MOVE N TIMES IN A DIRECTION? WOULD THAT REDUCE TREE SIZE?

    I think the best plan is to just do one action per level with no HLAs. That way I can just do a level count heuristic.
    Like it would be super trash on memory cost, but it'd work.
     */


    //RECORDING 22 28:38

    /*
        The Plan:
        - reuse the file reading code from last time
        - somehow parse that into a planning graph
        - create a set of rules/methods that will be used for navigation
        - define the mutexes
        - Store the nogoods somehow???


        The challenge of this project comes from building the graph and then extracting the solution from said graph.

        The Graph:
        - Linked list?
        - Maybe all nodes are objects and each level is its own array?
        - event object: contains an action value and a list of possible results
        - For mutexes: maybe have a global map that maps the objects together? this might cut down on redundancy of private vars

        Node Object:
            Has:
            - Parent
            - Outcomes
            - Action
         */
}
