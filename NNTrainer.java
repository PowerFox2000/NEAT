import java.util.ArrayList;
import java.util.Random;

public class NNTrainer {

    private int AmountOfNNsToTrain; // if its odd number, the hole thing breaks DO NOT PUT A ODD NUMBER
    private int Objective;//objective is either what is the score you'd like to obtain or how many iterations you want to do
    private boolean StopOnceScoreReached;// we stop when we get either a certain score (true) or that we reached a maximum iterations (false)
    private int ShowEvery; // this shows on the screen stats every [insert number] iteration
    private boolean DoThreads; //this decides want to thread the program

    private ArrayList<ArrayList<Integer>> AllInnovation;

    public NNTrainer(int HowManyNeuralNetworksToTrain, boolean StopOnceScoreReached, int Objective, int ShowStatsEvery, boolean DoThreads) {
        this.AmountOfNNsToTrain = HowManyNeuralNetworksToTrain;
        this.StopOnceScoreReached = StopOnceScoreReached;
        this.Objective = Objective;
        this.ShowEvery = ShowStatsEvery;
        this.DoThreads = DoThreads;
        InnovationNumber = 0;
        AllInnovation = new ArrayList<>();
    }


    private NeuralNetwork DefaultToMutate;
    private int InnovationNumber;
    private int NodesNumber;

    public void RunFirstTraining() {
        int AmountOfInputs = 2;
        int AmountOfOutputs = 1;
        int Tests = 50;
        NodesNumber = AmountOfInputs + AmountOfOutputs;
        CreateDefaultNN(2,1);

        ArrayList<Node> EmptyNodes = new ArrayList<>();


        System.out.println("Initializing");
        ArrayList<NeuralNetwork> NNs = new ArrayList<>();
        for (int i = 0; i < AmountOfNNsToTrain; i ++) {
            NeuralNetwork NextNNs = DefaultToMutate.MakeACopyOfNeuralNetWork();
            NextNNs.SortNodesInEXE();
            NNs.add(NextNNs);

        }

        System.out.println("-----------------------------------\n               Start               \n-----------------------------------");

        //From UnderHere DO NOT CHANGE
        int TopScore = 0;
        int Iterations = 0;
        double TimeForIteration = System.currentTimeMillis();
        double TotalTime = System.currentTimeMillis();
        int HighestScore = 0;
        while (true) {

            //so that we know when to stop
            if (StopOnceScoreReached) {
                if (TopScore>Objective) {
                    break;
                }
            } else {
                if (Iterations > Objective) {
                    break;
                }
            }

            //Showing Stats
            if (Iterations%ShowEvery == 0) {
                //insert the stats
                System.out.println("NNTrainer - We Reached " + Iterations + " Iterations, this one took: " + (System.currentTimeMillis()-TimeForIteration) + ", In Total it has lasted: " + (System.currentTimeMillis() - TimeForIteration) + " Highest SCore: " + HighestScore);
            }

            ArrayList<Integer> Scores = new ArrayList<>();

            Random random = new Random();

            //Splitting this in Threads to minimize time loss
            if (DoThreads) {
                System.out.println("i really need to code this :/");
            } else {

                for (int i = 0; i < NNs.size(); i ++ ) {
                    NeuralNetwork HostOfNeuralNetwork = NNs.get(i);

                    int NNScore = random.nextInt(0, 3);

                    for (int Test = 0; Test < Tests; Test++) {


                        double FirstInput = random.nextDouble(0.0, 1.0);
                        double SecondInput = random.nextDouble(0.0, 1.0);

                        ArrayList<Double> Inputs = new ArrayList<>();
                        Inputs.add(FirstInput);
                        Inputs.add(SecondInput);

                        HostOfNeuralNetwork.SetInputs(Inputs);

                        HostOfNeuralNetwork.RunNeuralNetwork();

                        int Output = (int) Math.round(HostOfNeuralNetwork.ReturnOutputs().get(0));

                        //what calculates the correct answer

                        int RoundFirst = (int) Math.round(FirstInput);
                        int RoundSecond = (int) Math.round(SecondInput);

                        if (RoundFirst == 0 && RoundSecond == 0 && Output == 0) {
                            NNScore ++;
                        }

                        if (RoundFirst == 1 && RoundSecond == 0 && Output == 1) {
                            NNScore ++;
                        }

                        if (RoundFirst == 0 && RoundSecond == 1 && Output == 1) {
                            NNScore ++;
                        }

                        if (RoundFirst == 1 && RoundSecond == 1 && Output == 0) {
                            NNScore ++;
                        }

                    }
                    Scores.add(NNScore);
                    HostOfNeuralNetwork.PrintNeuralNetwork();

                    //System.out.println("Calculated NN: " + HostOfNeuralNetwork.ReturnNetworkID());


                }
                //Tested All Neural Network
                System.out.println("Finished the testing");


                HighestScore = 0;
                for (int score : Scores) {
                    if (score > HighestScore) {
                        HighestScore = score; // Update if the current score is larger
                    }
                }

                ArrayList<Integer> indices = new ArrayList<>();
                for (int i = 0; i < Scores.size(); i++) {
                    indices.add(i);
                }

                indices.sort((a, b) -> Integer.compare(Scores.get(b), Scores.get(a)));

                ArrayList<NeuralNetwork> NNsToKeep = new ArrayList<>();

                System.out.println("\nA1 ---------------------------------------------------------------------------------------------------");

                double time = System.currentTimeMillis();
                while (System.currentTimeMillis() - time < 1000) {

                }


                if (Scores.size() != NNs.size()) {
                    System.out.println("Mismatch: Scores=" + Scores.size() + " NNs=" + NNs.size());
                    // handle or throw error here
                }

                for (int i = 0; i < AmountOfNNsToTrain/2; i++) {
                    NNsToKeep.add(NNs.get(indices.get(i)));
                }

                NNs = new ArrayList<>();

                for (int i = 0; i < NNsToKeep.size();i++) {
                    NNs.add(new NeuralNetwork(NNsToKeep.get(i).ReturnNodes(), EmptyNodes, NNsToKeep.get(i).ReturnWeights()));
                }



                //Mutate
                int AmountAddedTracker = 0;
                while (true) {

                    if (NNs.size() == AmountOfNNsToTrain) {
                        break;
                    }


                    int BornFrom = random.nextInt(0, AmountOfNNsToTrain/2);
                    NeuralNetwork HostOfNeuralNetworkToMutate = NNsToKeep.get(BornFrom).MakeACopyOfNeuralNetWork();

                    if (HostOfNeuralNetworkToMutate.Mutate()) {

                        ArrayList<Integer> Informations = HostOfNeuralNetworkToMutate.MutateNewConnection(InnovationNumber, NodesNumber);

                        if (Informations.get(9) == 1) {

                            if (Informations.get(0) == 1) {
                                boolean CanCreate = true;

                                for (int j = 0; j < AllInnovation.size(); j++) {
                                    ArrayList<Integer> InvnovationInformations = AllInnovation.get(j);
                                    if (InvnovationInformations.get(1) == Informations.get(1) && InvnovationInformations.get(2) == Informations.get(2)) {
                                        CanCreate = false;
                                        Informations.set(3, j);
                                        break;
                                    }
                                }

                                if (CanCreate) {
                                    ArrayList<Integer> TempInt = new ArrayList<>();
                                    TempInt.add(Informations.get(3));
                                    TempInt.add(Informations.get(1));
                                    TempInt.add(Informations.get(2));
                                    AllInnovation.add(TempInt);
                                    InnovationNumber++;
                                }

                                HostOfNeuralNetworkToMutate.CreateConnection(Informations);

                                System.out.println("ADDED Weight -----------------------------------\n");
                            } else {

                                NodesNumber++;

                                ArrayList<Integer> TempInt = new ArrayList<>();
                                TempInt.add(Informations.get(6));
                                TempInt.add(Informations.get(1));
                                TempInt.add(Informations.get(2));
                                AllInnovation.add(TempInt);
                                InnovationNumber++;

                                TempInt = new ArrayList<>();
                                TempInt.add(Informations.get(7));
                                TempInt.add(Informations.get(3));
                                TempInt.add(Informations.get(4));
                                AllInnovation.add(TempInt);
                                InnovationNumber++;

                                HostOfNeuralNetworkToMutate.CreateConnection(Informations);
                                System.out.println("ADDED Node -----------------------------------\n");
                            }
                            NNs.add(HostOfNeuralNetworkToMutate);
                            AmountAddedTracker ++;
                            System.out.println("Amount Added so far: " + AmountAddedTracker);
                        }

                    } else {

                    }
                }

                if (NNs.size() != AmountOfNNsToTrain) {
                    System.out.println("BAA - Massive Issue :o O_o");
                    System.out.println(NNs.size());
                }

            }

            for (int i = 0; i < NNs.size(); i ++) {
                NeuralNetwork HostOfNeuralNetwork = NNs.get(i);
                HostOfNeuralNetwork.SortNodesInEXE();
            }


            Iterations ++;
        }

        System.out.println("-----------------------------------\n              Finished              \n-----------------------------------");
    }

    public void TestBigNeuralNetwork() {

    }

    public void RunStartCode() {
        //custom NN to test stuff

        ArrayList<Node> Random = new ArrayList<>();
        ArrayList<Node> Nodes = new ArrayList<>();
        Node HostOfNode;
        ArrayList<Node> EmptyNodes = new ArrayList<>();

        ArrayList<Weight> Weights = new ArrayList<>();
        Weight HostOfWeight;

        ArrayList<Integer> ChildWeightsID;
        ArrayList<Integer> ChildNodesID;
        ArrayList<Integer> ChildWeightIndex;
        ArrayList<Integer> ChildNodesIndex;
        ArrayList<Integer> ParentNodeID;
        ArrayList<Integer> ParentNodeIndex;



        //int ID, double Bias, int State, ArrayList<Integer> ChildWeightsID, ArrayList<Integer> ChildNodesID, ArrayList<Integer> ChildWeightIndex, ArrayList<Integer> ChildNodesIndex, int IndexInExe

        ChildWeightsID = new ArrayList<>(); ChildNodesID = new ArrayList<>(); ChildWeightIndex = new ArrayList<>(); ChildNodesIndex = new ArrayList<>(); ParentNodeID = new ArrayList<>(); ParentNodeIndex = new ArrayList<>();

        ChildWeightsID.add(0); ChildNodesID.add(1);
        ChildWeightsID.add(2); ChildNodesID.add(2);
        HostOfNode = new Node(0, 0, 1, ChildWeightsID, ChildNodesID, ChildWeightIndex, ChildNodesIndex, ParentNodeID, ParentNodeIndex, 0);

        Nodes.add(HostOfNode);

        ChildWeightsID = new ArrayList<>(); ChildNodesID = new ArrayList<>(); ChildWeightIndex = new ArrayList<>(); ChildNodesIndex = new ArrayList<>(); ParentNodeID = new ArrayList<>(); ParentNodeIndex = new ArrayList<>();

        ChildWeightsID.add(1); ChildNodesID.add(3); ParentNodeID.add(0);
        HostOfNode = new Node(1, 1, 2, ChildWeightsID, ChildNodesID, ChildWeightIndex, ChildNodesIndex, ParentNodeID, ParentNodeIndex, 1);

        Nodes.add(HostOfNode);

        ChildWeightsID = new ArrayList<>(); ChildNodesID = new ArrayList<>(); ChildWeightIndex = new ArrayList<>(); ChildNodesIndex = new ArrayList<>(); ParentNodeID = new ArrayList<>(); ParentNodeIndex = new ArrayList<>();

        ChildWeightsID.add(3); ChildNodesID.add(3); ParentNodeID.add(0);
        HostOfNode = new Node(2, 0, 2, ChildWeightsID, ChildNodesID, ChildWeightIndex, ChildNodesIndex, ParentNodeID, ParentNodeIndex, 2);

        Nodes.add(HostOfNode);

        ChildWeightsID = new ArrayList<>(); ChildNodesID = new ArrayList<>(); ChildWeightIndex = new ArrayList<>(); ChildNodesIndex = new ArrayList<>(); ParentNodeID = new ArrayList<>(); ParentNodeIndex = new ArrayList<>();

        ParentNodeID.add(1); ParentNodeID.add(2);
        HostOfNode = new Node(3, 1, 3, ChildWeightsID, ChildNodesID, ChildWeightIndex, ChildNodesIndex, ParentNodeID, ParentNodeIndex, 3);

        Nodes.add(HostOfNode);

        //int ID, double Value, double Weight, int ParentNodeID, int ParentNodeIndex, int ChildNodeId, int ChildNodeIndex
        HostOfWeight = new Weight(0, 0, 1, 0, 0, 1, 1,0, false);// links node 0 to 1
        Weights.add(HostOfWeight);

        HostOfWeight = new Weight(1, 0, 1, 1, 1, 3, 3,1, true);// links node 1 to 3
        Weights.add(HostOfWeight);

        HostOfWeight = new Weight(2, 0, 1, 0, 0, 2, 2,2, true);// links node 0 to 2
        Weights.add(HostOfWeight);

        HostOfWeight = new Weight(3, 0, 1, 2, 2, 3, 3,3, true);// links node 2 to 3
        Weights.add(HostOfWeight);

        System.out.println("NN created :o");
        NeuralNetwork NeatTest = new NeuralNetwork(Nodes,EmptyNodes, Weights);

        System.out.println("Turing NN to an EXE version of itself");

        NeatTest.SortNodesInEXE();

        System.out.println("\n\nEnd Of Sorting ---------------------------------------------------------------------------------------------\n\n");

        ArrayList<Double> Inputs = new ArrayList<>();

        double InputValue = 1.5;

        System.out.println("Inputs are set to: " + InputValue);

        Inputs.add(InputValue);

        NeatTest.SetInputs(Inputs);

        NeatTest.RunNeuralNetwork();

        System.out.println("Here are the Outputs: " + NeatTest.ReturnOutputs());
    }


    private void CreateDefaultNN(int AmountOfInputs, int AmountOfOutputs) {

        ArrayList<Weight> Weights = new ArrayList<>();
        ArrayList<Node> Nodes = new ArrayList<>();
        Node HostOfNode;
        ArrayList<Integer> EmptyInt = new ArrayList<>();
        int IDs = 0;

        ArrayList<Node> EmptyNode = new ArrayList<>();

        for (int i = 0; i < AmountOfInputs; i++) {
            IDs = i;
            HostOfNode = new Node(IDs, 0.0, 1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), -1);
            Nodes.add(HostOfNode);
        }

        for (int i = 0; i < AmountOfOutputs; i++) {
            IDs += 1;
            HostOfNode = new Node(IDs, 0.0, 3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), -1);
            Nodes.add(HostOfNode);
        }

        DefaultToMutate = new NeuralNetwork(Nodes, EmptyNode, Weights);

    }
}
