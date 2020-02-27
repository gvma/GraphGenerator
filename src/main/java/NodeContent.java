package main.java;

public class NodeContent {

    private String tagPipe;
    private String repID;
    private String pipeType;
    private String drawing;
    private Long vertexID;

    public NodeContent(String tagPipe, String repID, String pipeType, String drawing, Long vertexID) {
        this.tagPipe = tagPipe;
        this.repID = repID;
        this.pipeType = isPipe(pipeType);
        this.drawing = drawing;
        this.vertexID = vertexID;
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

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public Long getVertexID() {
        return vertexID;
    }

    public void setVertexID(Long vertexID) {
        this.vertexID = vertexID;
    }

    @Override
    public String toString() {
        return "TagPipe: " + tagPipe.replace('$', ' ') + "\nRepID: " + repID + "\nPipeType: " + pipeType + "\nDrawing: " + drawing + "\nVertexID: " + vertexID.intValue() + "\n";
    }
}
