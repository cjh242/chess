package request;

public record RegisterRequest(String username, String password, String email) {
    public boolean isValid(){
        return this.username != null && this.password != null && this.email != null;
    }
}
