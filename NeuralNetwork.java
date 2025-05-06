

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Random;

public class NeuralNetwork {

    private ArrayList<Node>  Nodes, NodesInEXEOrder;
    private ArrayList<Weight> Weights;
    private ArrayList<Double> Input;
    private ArrayList<Double> Output;

    private ArrayList<Integer> IDInEXEOrder;

    private Random random = new Random();

    private Integer IDOfNeuralNetwork;

    //to debug new id is: AEA

    ///Nodes in exe orders has all the nodes in the order of which it has to run, meaning that index 1 is the first to compute.
    ///Weights are in innovation number.
    ///Nodes are in any order

    public NeuralNetwork(ArrayList<Node> Nodes,ArrayList<Node> nodesinexe, ArrayList<Weight> weights) {
        this.Nodes = Nodes;
        this.NodesInEXEOrder = nodesinexe;
        this.Weights = weights;
        this.IDOfNeuralNetwork = random.nextInt(0, 999999999);
    }

    public void RunNeuralNetwork() {

        Node HostOfNode;

        Output = new ArrayList<>();

        for (int i=0;i<NodesInEXEOrder.size();i++) {
            HostOfNode = NodesInEXEOrder.get(i);
            HostOfNode.EmptyValues();
        }

        for (int i = 0; i < NodesInEXEOrder.size(); i++) {

            int StateOfTheNode;

            Node SelectedNode = NodesInEXEOrder.get(i);
            StateOfTheNode = SelectedNode.ReturnState();

            if (StateOfTheNode == 1) {
                HostOfNode = NodesInEXEOrder.get(i);

                //System.out.println("ADA - Issue Line 48? Asked: " + i + " Length: " + Input.size() + " Inside: " + Input + " ID is " + IDOfNeuralNetwork);

                HostOfNode.SetValueInputOnly(Input.get(i));

                //System.out.println("NodeState=1; Here is the value received: " + Input.get(i));

                UpdateAllNodesChildren(HostOfNode.ReturnChildNodesIndex(), HostOfNode.ReturnChildWeightIndex(), Input.get(i));
            }

            if (StateOfTheNode == 2) {
                HostOfNode = NodesInEXEOrder.get(i);
                HostOfNode.ActivateNode();
                double Value = HostOfNode.ReturnValue();

                //System.out.println("Same as next, here is all the inputsvalue: " + HostOfNode.ReturnInputValues() + " Total: " + HostOfNode.ReturnBeforeSigmoide());
                //System.out.println("NodeState=2; Here is the value outputted: " + HostOfNode.ReturnValue());

                NodesInEXEOrder.set(i, HostOfNode);
                UpdateAllNodesChildren(HostOfNode.ReturnChildNodesIndex(), HostOfNode.ReturnChildWeightIndex(), Value);
            }

            if (StateOfTheNode == 3) {
                HostOfNode = NodesInEXEOrder.get(i);
                HostOfNode.ActivateNode();
                double Value = HostOfNode.ReturnValue();

                //System.out.println("Same as next, here is all the inputsvalue: " + HostOfNode.ReturnInputValues() + " Total: " + HostOfNode.ReturnBeforeSigmoide());
                //System.out.println("NodeState=3; here is what is outputted: " + Value);

                NodesInEXEOrder.set(i, HostOfNode);

                Output.add(Value);
            }
        }
    }

    private void UpdateAllNodesChildren(ArrayList<Integer> NIndexs, ArrayList<Integer> WIndexs, double Value) {
        System.out.println("--------------------------------------------------------\nUsage of thing: " + NIndexs + " " + WIndexs);
        for (int Update = 0; Update<WIndexs.size();Update++) {
            Weight HostOfWeightsClassToUpdate;
            Node HostOfNodesClassToUpdate;

            HostOfNodesClassToUpdate = NodesInEXEOrder.get(NIndexs.get(Update));
            System.out.println("AA: " + NIndexs.get(Update));
            HostOfWeightsClassToUpdate = Weights.get(WIndexs.get(Update));
            System.out.println("AB: " + WIndexs.get(Update));

            if (HostOfWeightsClassToUpdate.ReturnEnabled()) {
                HostOfNodesClassToUpdate.UpdateValues(Value * HostOfWeightsClassToUpdate.ReturnWeight());
            }
        }
    }


    public void SetInputs(ArrayList<Double> Inputs) {
        this.Input = Inputs;
    }

    public ArrayList<Double> ReturnOutputs() {
        return Output;
    }

    public int ReturnNetworkID() {
        return IDOfNeuralNetwork;
    }

    public boolean Mutate() {
        Random random = new Random();

        boolean NeedsInformation = false;

        int Mutation = random.nextInt(0, 101);

        if (Mutation>80) {
            NeedsInformation = true;
        } else {
            RunMutation();
        }

        return NeedsInformation;
    }

    public ArrayList<Integer> MutateNewConnection(int InnovationNumber, int NodeNumber) {
        Random random = new Random();

        ArrayList<Integer> ToCheck = new ArrayList<>();

        int TypeOfMutation = random.nextInt(0,10);

        if (TypeOfMutation < 7) {

            // make a new weight to connect to nodes
            int ParentID;int ChildID;

            SortNodesInEXE();

            while (true) {//finds a child with a lower index than parent
                ChildID = random.nextInt(0, Nodes.size());

                Node PossibleChild = Nodes.get(ChildID);

                int Index = PossibleChild.ReturnIndexInExe();

                ParentID = random.nextInt(0, Nodes.size());

                Node PossibleParent = Nodes.get(ParentID);

                if (Index > PossibleParent.ReturnIndexInExe()) {
                    //Correct Child to parent
                    if (((PossibleParent.ReturnState() == 1) && (PossibleChild.ReturnState() == 2)) || ((PossibleParent.ReturnState() == 2) && (PossibleChild.ReturnState() == 2)) || ((PossibleParent.ReturnState() == 2) && (PossibleChild.ReturnState() == 3)) || ((PossibleParent.ReturnState() == 1) && (PossibleChild.ReturnState() == 3))) {
                        //we can make node
                        ToCheck.add(1); // [0] = What type, in this case its weight (W=1, N=2)
                        ToCheck.add(ParentID); // [1] = parent
                        ToCheck.add(ChildID); // [2] = Child
                        ToCheck.add(InnovationNumber); //[3] is well u can read
                        ToCheck.add(1);
                        ToCheck.add(1);
                        ToCheck.add(1);
                        ToCheck.add(1);
                        ToCheck.add(1);
                        ToCheck.add(1);
                        break;
                    }
                }

            }

        } else {
            //Weight -> Node -> Weight
            if (Weights.size()>1) {
                ToCheck.add(2); // is a node
                Weight HostOfWeight = Weights.get(0);
                int WeightToHijack = 0;
                boolean Error = true;
                for (int i = 0; i < 200; i ++) {
                    WeightToHijack = random.nextInt(0, Weights.size());//index not ID

                    HostOfWeight = Weights.get(WeightToHijack);
                    if (HostOfWeight.ReturnEnabled()) {
                        Error = false;
                        break;
                    }

                }

                if (Error) {
                } else {
                    int FirstParent = HostOfWeight.ReturnParentNodeID();

                    int NewNodeID = NodeNumber;

                    int LastChild = HostOfWeight.ReturnChildNodeID();

                    ToCheck.add(FirstParent); // [1] is Parent1
                    ToCheck.add(NewNodeID); // [2] is Child1
                    ToCheck.add(NewNodeID); // [3] is Parent2
                    ToCheck.add(LastChild); // [4] is Child2
                    ToCheck.add(WeightToHijack); // so that we know which to do so
                    ToCheck.add(InnovationNumber); // :/ [6]
                    ToCheck.add(InnovationNumber + 1); // for the second node
                    ToCheck.add(NodeNumber); // :/
                    ToCheck.add(1); // 1 means that there isn't any errors
                }
            } else {
                ToCheck.add(0); // [1] is Parent1
                ToCheck.add(0); // [2] is Child1
                ToCheck.add(0); // [3] is Parent2
                ToCheck.add(0); // [4] is Child2
                ToCheck.add(0); // so that we know which to do so
                ToCheck.add(0); // :/ [6]
                ToCheck.add(0); // for the second node
                ToCheck.add(0); // :/
                ToCheck.add(0);
                ToCheck.add(0);
            }
        }

        return ToCheck;
    }

    public void CreateConnection(ArrayList<Integer> ToCheck) {

        ArrayList<Integer> Information = new ArrayList<>(ToCheck);

        if (Information.get(0) == 1) {

            //System.out.println("New Weight Created, Here is all the information. Its ID: " + Weights.size() + " first Information: " + Information.get(1) + " second information: " + Information.get(2) + " third information: " + Information.get(3));
            Weight CreateWeightHost = new Weight(Weights.size(), random.nextDouble(-2.0, 3.0), Information.get(1), -1, Information.get(2), -1, Information.get(3), true);
            Weights.add(CreateWeightHost);


//            for (int i = 0; i < Nodes.size(); i ++) {
//                Nodes.get(i).ReturnParentNodesID();
//                //remove this for loop
//            }

            for (int i = 0; i < Nodes.size(); i++) {
                Node HostOfNode = Nodes.get(i);

                //System.out.println("\nBtw everything under nn id is: " + IDOfNeuralNetwork + " ---------------------------------------");
                //System.out.println("EAC - ID: " + HostOfNode.ReturnID() + " Parents: " + HostOfNode.ReturnParentNodesID());

                if (HostOfNode.ReturnID() == Information.get(1)) {
                    //System.out.println("EAA - ID: " + HostOfNode.ReturnID() + " == " + Information.get(1) + ", ChildNodes: " + HostOfNode.ReturnChildNodesID() + " New thing to add " + Information.get(2) + ", ChildWeights " + HostOfNode.ReturnChildWeightsID() + ", New thing to add: " + (Weights.size()-1) + ", btw here are its parents: " + HostOfNode.ReturnParentNodesID() );
                    HostOfNode.AddChildNode(Information.get(2));
                    HostOfNode.AddChildWeight(Weights.size()-1);

                }

                if (HostOfNode.ReturnID() == Information.get(2)) {
                    //System.out.println("EAB - ID " + HostOfNode.ReturnID() + " == " + Information.get(2) + " Parents: " + HostOfNode.ReturnParentNodesID() + " Will Add: " + Information.get(1));
                    HostOfNode.AddParentNode(Information.get(1));
                    System.out.println(Information.get(1));
                    System.out.println(Nodes.get(i).ReturnParentNodesID());//remove this
                    double time = System.currentTimeMillis();
                    PrintNeuralNetwork();
                    while (System.currentTimeMillis() - time < 2000) {

                    }
                }
            }
        }

        if (Information.get(0) == 2) {
            int WeightToHijack = Information.get(5);

            Weight HostOfWeight = Weights.get(WeightToHijack);

            int WeightIDToHijack = HostOfWeight.ReturnID();


            HostOfWeight.SetEnabled(false);

            double Weight = HostOfWeight.ReturnWeight();

            int FirstWeightID = Weights.size();
            int SecondWeightID = Weights.size() + 1;

            Weight FirstWeight = new Weight(Weights.size(), Weight, Information.get(1), -1, Information.get(2), -1, Information.get(6), true);

            Weights.add(FirstWeight);

            Weight SecondWeight = new Weight(Weights.size(), random.nextDouble(-2.0, 3.0), Information.get(3), -1, Information.get(4), -1, Information.get(7), true);

            Weights.add(SecondWeight);

            ArrayList<Integer> ParentNode = new ArrayList<>();
            ParentNode.add(Information.get(1));

            ArrayList<Integer> ChildNode = new ArrayList<>();
            ChildNode.add(Information.get(4));

            ArrayList<Integer> ChildWeight = new ArrayList<>();
            ChildWeight.add(SecondWeightID);

            ArrayList<Integer> Empty = new ArrayList<>();

            Node NewNode = new Node(Information.get(8), random.nextDouble(-3, 5), 2, ChildWeight, ChildNode, Empty, Empty, ParentNode, Empty, -1);

            Nodes.add(NewNode);

            for (int i = 0; i < Nodes.size(); i++) {
                Node HostOfNode = Nodes.get(i);

                if (HostOfNode.ReturnID() == Information.get(1)) {

                    ArrayList<Integer> AllChildrenWeight;
                    ArrayList<Integer> AllChildrenNode;
                    AllChildrenWeight = HostOfNode.ReturnChildWeightsID();
                    AllChildrenNode = HostOfNode.ReturnChildNodesID();

                    for (int j = 0; j < AllChildrenWeight.size(); j ++) {
                        if (AllChildrenWeight.get(j) == WeightIDToHijack) {
                            AllChildrenWeight.remove(j);
                            AllChildrenNode.remove(j);
                        }
                    }

                    AllChildrenWeight.add(FirstWeightID);

                    AllChildrenNode.add(Information.get(7));


                }

                if (HostOfNode.ReturnID() == Information.get(4)) {
                    ArrayList<Integer> AllParents;
                    AllParents = HostOfNode.ReturnParentNodesID();

                    for (int j = 0; j < AllParents.size(); j ++) {
                        if (AllParents.get(j) == WeightIDToHijack) {
                            AllParents.remove(j);
                        }
                    }

                    AllParents.add(Information.get(7));
                }
            }
        }
        if (Information.get(0) != 1 && Information.get(0) != 2) {
            System.out.println("ACA - We've got an issue here :o");
        }
    }

    private void RunMutation() {
        Random random = new Random();
        int Mutation = random.nextInt(0, 101);

        if (Mutation <= 45) {
            int NodeToMutate = random.nextInt(0, Nodes.size());

            Node NodeMutating = Nodes.get(NodeToMutate);

            NodeMutating.SetBias(random.nextDouble(-3.0, 5.0));
        }
        if (Mutation > 45 && Mutation <= 90) {

            if (Weights.size() > 2) {

                //should not be anymore errors here "bound must be greater than origin"
                int WeightToMutate = random.nextInt(0, Weights.size());

                Weight WeightsMutating = Weights.get(WeightToMutate);

                WeightsMutating.SetWeight(random.nextDouble(-3.0, 5.0));
            }

        }
        if (Mutation > 90) {

            if (Weights.size() > 2) {

                //should not be anymore errors here "bound must be greater than origin"
                int WeightToMutate = random.nextInt(0, Weights.size());

                Weight WeightsMutating = Weights.get(WeightToMutate);

                WeightsMutating.SetEnabled(!WeightsMutating.ReturnEnabled());
            }

        }
    }


    public void SortNodesInEXE() {
        //creating a matrix to know who has who as parent and then sort them

        NodesInEXEOrder = new ArrayList<>();
        IDInEXEOrder = new ArrayList<>();

        ArrayList<ArrayList<Integer>> StillHasParent = new ArrayList<>();
        ArrayList<Integer> Parents;
        Node HostOfNode;

        ArrayList<Node> AllNode = Nodes;
        ArrayList<Boolean> IsProcessed = new ArrayList<>();

        // Setup parent lists and processed tracking
        for (int AddNodesParent = 0; AddNodesParent < AllNode.size(); AddNodesParent++) {
            Parents = new ArrayList<>(AllNode.get(AddNodesParent).ReturnParentNodesID());
            //System.out.println("DDD - " + AllNode.get(AddNodesParent).ReturnParentNodesID());
            //System.out.println("DDDDDDD - " + AllNode.get(AddNodesParent).ReturnID() + " Parents " + Parents);
            StillHasParent.add(Parents);
            IsProcessed.add(false);
        }

        //System.out.println(StillHasParent);

        for (int i = 0; i < Nodes.size(); i++) {
            Node HostOfNodePrint = Nodes.get(i);
            Parents = StillHasParent.get(i);
            //System.out.println("Information Sheet of nn: " + IDOfNeuralNetwork + " ID: " + HostOfNodePrint.ReturnID() + " Parents: " + Parents);
        }

        int Indexs = -1;

        for (int i = 0; i < AllNode.size(); i++) {

            for (int RemoveParents = 0; RemoveParents < AllNode.size(); RemoveParents++) {

                if (IsProcessed.get(RemoveParents)) continue; // Skip already processed nodes

                Parents = StillHasParent.get(RemoveParents);

                if (Parents.isEmpty()) {
                    Indexs ++;
                    NodesInEXEOrder.add(Nodes.get(RemoveParents));
                    HostOfNode = Nodes.get(RemoveParents);

                    IDInEXEOrder.add(HostOfNode.ReturnID());

                    IsProcessed.set(RemoveParents, true);
                    Parents.add(-1); // Prevent this node from being selected again
                    HostOfNode.SetIndexInExe(Indexs);

                    // Part that removes this node from others' parent lists
                    ArrayList<Integer> ToRemoveAsParent = HostOfNode.ReturnChildNodesID();
                    int IDToRemove = HostOfNode.ReturnID();

                    //System.out.println("ABC - ListOfParentsToRemove: " + ToRemoveAsParent);

                    for (int j = 0; j < ToRemoveAsParent.size(); j++) {
                        int childID = ToRemoveAsParent.get(j);

                        for (int k = 0; k < Nodes.size(); k++) {
                            Node DisDaNode = Nodes.get(k);
                            if (DisDaNode.ReturnID() == childID) {
                                Parents = StillHasParent.get(k);
                                //System.out.println("ABD - " + k + " List: " + Parents + " ID: " + RemoveParents);

                                for (int l = 0; l < Parents.size(); l++) {
                                    if (Parents.get(l) == IDToRemove) {
                                        Parents.remove(l);
                                    }
                                }
                                break;
                            }
                        }
                    }

                    //System.out.println("Sorted a new node: " + RemoveParents);
                    break; // Go back to top to restart scanning
                }
            }
        }

        //System.out.println(" ");

        //Add the check if everything is -1

        boolean ShouldWork = true;

        for (int i = 1; i < StillHasParent.size();i++) {
            Parents = StillHasParent.get(i);

            if (Parents.get(0) != -1) {
                ShouldWork = false;
                System.out.println("There is something wrong in: " + i + " here is list " + Parents);
            }

        }

        if (!ShouldWork) {
            System.out.println("ABA - Something went wrong");
        } else {
            System.out.println("----------------------\nSorted With No Issues\n----------------------");
        }

        //Time to update everything to be able to run

        for (int i = 0; i < Nodes.size();i++) {

            HostOfNode = Nodes.get(i);

            ArrayList<Integer> ToTurnInIndexs = HostOfNode.ReturnChildNodesID();


            ArrayList<Integer> CorrectIndexs = new ArrayList<>();
            for (int j = 0; j < ToTurnInIndexs.size(); j ++) {
                CorrectIndexs.add(FindIndexOfNode(ToTurnInIndexs.get(j)));
                //System.out.println("ABB - A");
            }
            HostOfNode.SetChildNodeIndex(CorrectIndexs);

            //-------------------------------------------------------------------------

            ToTurnInIndexs = HostOfNode.ReturnParentNodesID();

            CorrectIndexs = new ArrayList<>();
            for (int j = 0; j < ToTurnInIndexs.size(); j ++) {
                CorrectIndexs.add(FindIndexOfNode(ToTurnInIndexs.get(j)));
                //System.out.println("ABB - B");
            }
            HostOfNode.SetParentNodeIndex(CorrectIndexs);

            //-------------------------------------------------------------------------

            ToTurnInIndexs = HostOfNode.ReturnChildWeightsID();


            CorrectIndexs = new ArrayList<>();
            for (int j = 0; j < ToTurnInIndexs.size(); j ++) {
                CorrectIndexs.add(FindIndexOfWeight(ToTurnInIndexs.get(j)));
                //System.out.println("ABB - C - ID: " + HostOfNode.ReturnID() + "\n");
            }
            HostOfNode.SetChildWeightIndex(CorrectIndexs);
        }

        for (int i = 0; i < Weights.size();i++) {
            Weight HostOfWeights = Weights.get(i);

            //-------------------------------------------------------------------------
            int ToTurnInIndexes = HostOfWeights.ReturnChildNodeID();
            HostOfWeights.SetChildNodeIndex(FindIndexOfNode(ToTurnInIndexes));
            //-------------------------------------------------------------------------
            ToTurnInIndexes = HostOfWeights.ReturnParentNodeID();
            HostOfWeights.SetParentNodeIndex(FindIndexOfNode(ToTurnInIndexes));
        }

        System.out.println("Updated everything!");
        System.out.println("In order: " + IDInEXEOrder);



    }

    private int FindIndexOfNode(int IDToFind) {
        Node HostOfNode;
        int Index = -1;

        //System.out.print("Searching for: " + IDToFind);

        for (int i = 0; i < Nodes.size(); i ++) {
            HostOfNode = Nodes.get(i);
            //System.out.print("; " + HostOfNode.ReturnID() + " ");
            if (HostOfNode.ReturnID() == IDToFind) {
                Index = i;
                break;
            }
        }

        //System.out.println("");

        if (Index == -1) {
            System.out.println("ABB - issues: " + IDToFind + " Size " + Nodes.size());
        }


        return Index;
    }

    private int FindIndexOfWeight(int IDToFind) {
        Weight HostOfWeight;
        int Index = -1;

        //System.out.print("Searching for: " + IDToFind);

        for (int i = 0; i < Weights.size(); i ++) {
            HostOfWeight = Weights.get(i);
            //System.out.print("; " + HostOfWeight.ReturnID() + " ");
            if (HostOfWeight.ReturnID() == IDToFind) {
                Index = i;
                break;
            }
        }

        //System.out.println("");

        if (Index == -1) {
            System.out.println("ABC - Issue here: " + IDToFind + " Size " + Weights.size());
        }

        return Index;
    }



    //return
    public ArrayList<Node> ReturnNodes() {
        return new ArrayList<>(Nodes);
    }
    public ArrayList<Weight> ReturnWeights() {
        return new ArrayList<>(Weights);
    }

    public NeuralNetwork MakeACopyOfNeuralNetWork() {
        ArrayList<Node>  NNodes, NNodesInEXEOrder;
        ArrayList<Weight> NWeights;

        NNodes = new ArrayList<>();
        NNodesInEXEOrder = new ArrayList<>();
        NWeights = new ArrayList<>();

        for (Node n : Nodes) {
            NNodes.add(n.MakeACopyOfNode());
        }

        for (Weight w : Weights) {
            NWeights.add(w.MakeACopyOfWeight());
        }

        return new NeuralNetwork(NNodes, NNodesInEXEOrder, NWeights);
    }

    public void PrintNeuralNetwork() {

        System.out.println("\nID of Neural Network " + IDOfNeuralNetwork + " -----------------------------------------------------------------------");

        for (int i = 0; i < Weights.size(); i++) {
            Weights.get(i).PrintWeight();
        }
        for (int i = 0; i < Nodes.size(); i++) {
            Nodes.get(i).PrintNode();
        }
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------");
    }
}

//Important Values:
//Node's bias can go from -3 -> 5
//Weights go from -2 -> 3
