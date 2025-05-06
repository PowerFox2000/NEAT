
public class Weight {

    private double Value, Weight;
    private int ID, InnovationNumber;
    private int ParentNodeID, ParentNodeIndex, ChildNodeID, ChildNodeIndex;
    private boolean Enabled;

    public Weight(int ID, double Weight, int ParentNodeID, int ParentNodeIndex, int ChildNodeId, int ChildNodeIndex, int InnovationNumber, boolean Enabled) {
        this.ID = ID;
        this.Weight = Weight;
        this.ChildNodeIndex = ChildNodeIndex;
        this.ChildNodeID = ChildNodeId;
        this.ParentNodeIndex = ParentNodeIndex;
        this.ParentNodeID = ParentNodeID;
        this.InnovationNumber = InnovationNumber;
        this.Enabled = Enabled;
    }




    //return

    public int ReturnID() {
        return ID;
    }

    public double ReturnValue() {
        return Value;
    }

    public double ReturnInnovationNumber() {
        return InnovationNumber;
    }

    public double ReturnWeight() {
        return Weight;
    }

    public int ReturnChildNodeIndex() {
        return ChildNodeIndex;
    }

    public int ReturnChildNodeID() {
        return ChildNodeID;
    }

    public int ReturnParentNodeIndex() {
        return ParentNodeIndex;
    }

    public int ReturnParentNodeID() {
        return ParentNodeID;
    }

    public boolean ReturnEnabled() {return Enabled;}

    public void SetParentNodeIndex(int ParentNodeIndex) {
        this.ParentNodeIndex = ParentNodeIndex;
    }

    public void SetChildNodeIndex(int ChildNodeIndex) {
        this.ChildNodeIndex = ChildNodeIndex;
    }

    public void SetWeight(double Weight) {
        this.Weight = Weight;
    }

    public void SetEnabled(boolean Enabled) {
        this.Enabled = Enabled;
    }

    public Weight MakeACopyOfWeight() {

        double NWeight;
        int NID, NInnovationNumber;
        int NParentNodeID, NParentNodeIndex, NChildNodeID, NChildNodeIndex;
        boolean NEnabled;

        NWeight = Weight;
        NID = ID;
        NInnovationNumber = InnovationNumber;
        NParentNodeID = ParentNodeID;
        NParentNodeIndex = ParentNodeIndex;
        NChildNodeID = ChildNodeID;
        NChildNodeIndex = ChildNodeIndex;
        NEnabled = Enabled;

        Weight NewWeight = new Weight(NID , NWeight, NParentNodeID, NParentNodeIndex, NChildNodeID, NChildNodeIndex, NInnovationNumber, NEnabled);

        return NewWeight;
    }

    public void PrintWeight() {
        System.out.println("Weight ID: " + ID + ", Value: " + Value + ", Weight: " + Weight + ", InnovationNumber: " + InnovationNumber + ", Parents: " + ParentNodeID + "|" + ParentNodeIndex + ", Children: " + ChildNodeID + "|" + ChildNodeIndex + ", Enabled: " + Enabled);
    }

}
