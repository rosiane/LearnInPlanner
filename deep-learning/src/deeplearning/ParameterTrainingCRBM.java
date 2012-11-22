
package deeplearning;

public class ParameterTrainingCRBM {
    private double learning_rate_weights;
    private double theta_low;
    private double theta_high;
    private double sigma;
    private double learning_rate_aj;

    public double getLearning_rate_aj() {
        return learning_rate_aj;
    }

    public void setLearning_rate_aj(double learning_rate_aj) {
        this.learning_rate_aj = learning_rate_aj;
    }

    public double getLearning_rate_weights() {
        return learning_rate_weights;
    }

    public void setLearning_rate_weights(double learning_rate_weights) {
        this.learning_rate_weights = learning_rate_weights;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getTheta_high() {
        return theta_high;
    }

    public void setTheta_high(double theta_high) {
        this.theta_high = theta_high;
    }

    public double getTheta_low() {
        return theta_low;
    }

    public void setTheta_low(double theta_low) {
        this.theta_low = theta_low;
    }
}
