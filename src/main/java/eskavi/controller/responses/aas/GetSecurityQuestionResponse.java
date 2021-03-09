package eskavi.controller.responses.aas;

public class GetSecurityQuestionResponse {
    private String securityQuestion;

    public GetSecurityQuestionResponse(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}
