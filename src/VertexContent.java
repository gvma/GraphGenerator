public class VertexContent {

    private String tagPipe;
    private String repID;
    private String pipeType;

    public VertexContent(String tagPipe, String repID, String pipeType) {
        this.tagPipe = tagPipe;
        this.repID = repID;
        this.pipeType = isPipe(pipeType);
    }

    public String isPipe(String pipeType) {
        String types[] = {
                "Conditioned Air Return",
                "Secondary Piping",
                "Primary Piping",
                "OPC",
                "Ventilation",
                "Capillary",
                "Hydraulic",
                "Exhaustion",
                "Connect to Process",
                "Conditioned Air Supply",
                "Hose",
                "Pipe-in-Pipe",
                "Tubing",
                "Pneumatic Binary"
        };
        for (String type : types) {
            if (type.equals(pipeType)) {
                return "True";
            }
        }
        return "False";
    }

    public String getTagPipe() {
        return tagPipe;
    }

    public void setTagPipe(String tagPipe) {
        this.tagPipe = tagPipe;
    }

    public String getRepID() {
        return repID;
    }

    public void setRepID(String repID) {
        this.repID = repID;
    }

    public String getPipeType() {
        return pipeType;
    }

    public void setPipeType(String pipeType) {
        this.pipeType = pipeType;
    }

    @Override
    public String toString() {
        return "TagPipe: " + tagPipe + "\nRepID: " + repID + "\nPipeType: " + pipeType + "\n";
    }
}
