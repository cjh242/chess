package request;

public record RegisterRequest(String username, String password, String email) {
    public boolean isValid(){
        return this.username != null && this.password != null && this.email != null;
    }

    public RegisterRequest withHashedPassword(String password) {
        return new RegisterRequest(this.username(), password, this.email());
    }
}
