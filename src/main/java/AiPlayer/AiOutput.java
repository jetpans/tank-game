package AiPlayer;

public class AiOutput {
    String FireDecision;
    String AngularDecision;
    String LinearDecision;

    public AiOutput(String fireDecision, String angularDecision, String linearDecision) {
        FireDecision = fireDecision;
        AngularDecision = angularDecision;
        LinearDecision = linearDecision;
    }

    public AiOutput() {
    }

    public String getFireDecision() {
        return FireDecision;
    }

    public void setFireDecision(String fireDecision) {
        FireDecision = fireDecision;
    }

    public String getAngularDecision() {
        return AngularDecision;
    }

    public void setAngularDecision(String angularDecision) {
        AngularDecision = angularDecision;
    }

    public String getLinearDecision() {
        return LinearDecision;
    }

    public void setLinearDecision(String linearDecision) {
        LinearDecision = linearDecision;
    }
}
