
import java.util.ArrayList;

public class Node {

    private double Value, Bias;
    private ArrayList<Integer> ChildWeightsID, ChildNodesID, ChildWeightsIndex, ChildNodesIndex, ParentNodesID, ParentNodesIndex;
    private int ID, State, IndexInExe; // added state to track if it's an input hidden or output node
    private ArrayList<Double> InputValues = new ArrayList<>();
    //if the state is 0 than there is an error; if its 1 than it's an input; 2 is a hidden; 3 is the output

    private double ValueBeforeSigmoid;


    public Node(int ID, double Bias, int State, ArrayList<Integer> ChildWeightsID, ArrayList<Integer> ChildNodesID, ArrayList<Integer> ChildWeightsIndex, ArrayList<Integer> ChildNodesIndex, ArrayList<Integer> ParentNodesID, ArrayList<Integer> ParentNodesIndex, int IndexInExe) {
        this.ID = ID; //is the ID of the Node
        this.Bias = Bias;
        this.State = State;
        this.ChildWeightsID = ChildWeightsID;// contains all IDs of all child weights
        this.ChildNodesID = ChildNodesID;// contain all Ids of all child nodes
        this.ChildWeightsIndex = ChildWeightsIndex;// contains the IDs of each of said weights
        this.ChildNodesIndex = ChildNodesIndex;// contains the IDS of each of said nodes
        this.IndexInExe = IndexInExe;
        this.ParentNodesID = ParentNodesID;
        this.ParentNodesIndex = ParentNodesIndex;
    }



    //changes inside the function
    public void SetIndexInExe(int SetIndexInExe) {
        this.IndexInExe = SetIndexInExe;
    }

    public void SetValueInputOnly(double Value) {
        this.Value = Value;
    }

    public void UpdateValues(double Value) {
        InputValues.add(Value);
    }

    public void EmptyValues() {
        InputValues = new ArrayList<>();
    }

    public void AddParentNode(int ID) {ParentNodesID.add(ID);}

    public void AddChildWeight(int ID) {ChildWeightsID.add(ID);}

    public void AddChildNode(int ID) {ChildNodesID.add(ID);}

    public void ActivateNode() {
        double Sum = Bias;
        for (int i = 0; i < InputValues.size(); i++) {

            double FirstValue = Math.max(0, InputValues.get(i));// to remove ?

            Sum += FirstValue;
        }

        ValueBeforeSigmoid = Sum;

        //sigmoid time

        Value = 1 / (1 + Math.exp(-Sum)); // sigmoid

    }


    //return

    public double ReturnBeforeSigmoide() {
        return ValueBeforeSigmoid;
    }



    public int ReturnIndexInExe() {
        return IndexInExe;
    }

    public int ReturnID() {
        return ID;
    }

    public double ReturnBias() {
        return Bias;
    }

    public int ReturnState() {
        return State;
    }

    public double ReturnValue() {
        return Value;
    }

    public ArrayList<Integer> ReturnChildWeightsID() {
        return ChildWeightsID;
    }

    public ArrayList<Integer> ReturnChildNodesID() {
        return ChildNodesID;
    }

    public ArrayList<Integer> ReturnChildWeightIndex() {
        return ChildWeightsIndex;
    }

    public ArrayList<Integer> ReturnChildNodesIndex() {
        return ChildNodesIndex;
    }

    public ArrayList<Double> ReturnInputValues() {
        return InputValues;
    }

    public ArrayList<Integer> ReturnParentNodesID() {
        //System.out.println("FAA - ID " + ID + " Parents: " + ParentNodesID);
        return ParentNodesID;
    }

    public ArrayList<Integer> ReturnParentNodesIndex() {
        return ParentNodesIndex;
    }

    public void SetChildWeightIndex(ArrayList<Integer> ChildWeightsIndex) {
        this.ChildWeightsIndex = ChildWeightsIndex;
    }

    public void SetChildNodeIndex(ArrayList<Integer> ChildNodesIndex) {
        this.ChildNodesIndex = ChildNodesIndex;
    }

    public void SetParentNodeIndex(ArrayList<Integer> ParentNodesIndex) {
        this.ParentNodesIndex = ParentNodesIndex;
    }

    public void SetBias(double Bias) {this.Bias = Bias;}

    public Node MakeACopyOfNode() {

         double NBias;
         ArrayList<Integer> NChildWeightsID, NChildNodesID, NChildWeightsIndex, NChildNodesIndex, NParentNodesID, NParentNodesIndex;
         int NID, NState, NIndexInExe;

         NBias = Bias;
         NID = ID;
         NState = State;
         NIndexInExe = IndexInExe;

         NChildWeightsID = new ArrayList<>();
         NChildWeightsIndex = new ArrayList<>();
         NChildNodesID = new ArrayList<>();
         NChildNodesIndex = new ArrayList<>();
         NParentNodesID = new ArrayList<>();
         NParentNodesIndex = new ArrayList<>();

         for (Integer i : ChildWeightsID) {
             NChildWeightsID.add(i);
         }

        for (Integer i : ChildWeightsIndex) {
            NChildWeightsIndex.add(i);
        }

        for (Integer i : ChildNodesID) {
            NChildNodesID.add(i);
        }

        for (Integer i : ChildNodesIndex) {
            NChildNodesIndex.add(i);
        }

        for (Integer i : ParentNodesID) {
            NParentNodesID.add(i);
        }

        for (Integer i : ParentNodesIndex) {
            NParentNodesIndex.add(i);
        }

        return new Node(NID, NBias, NState, NChildWeightsID, NChildNodesID, NChildWeightsIndex, NChildNodesIndex, NParentNodesID, NParentNodesIndex, NIndexInExe);
    }

    public void PrintNode() {
        System.out.println("Node ID: " + ID + ", Value? " + Value + ", Bias: " + Bias + ", State: " + State + ", Parents: " + ParentNodesID + "|" + ParentNodesIndex + ", Children Nodes: " + ChildNodesID + "|" + ChildNodesIndex + ", Children Weights: " + ChildWeightsID + "|" + ChildWeightsIndex + ", IndexInEXE: " + IndexInExe);
    }

}

