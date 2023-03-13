package cz.cvut.fel.ear.meetingroomreservation.security.model;

public class LoginStatus {

    private boolean isLogged;
    private String username;
    private String errorMessage;
    private boolean success;


    /**
     * instance of new Login Status
     */
    public LoginStatus(){

    }


    /**
     *
     * @param isLogged the login status
     * @param username the username
     * @param errorMessage the error message
     * @param success the success
     */
    public LoginStatus(boolean isLogged, String username, String errorMessage, boolean success){
        this.isLogged = isLogged;
        this.username = username;
        this.errorMessage = errorMessage;
        this.success = success;
    }


    public boolean isLogged(){
        return isLogged;
    }

    public String getUsername(){
        return username;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setLoginStatus(boolean isLogged){
        this.isLogged = isLogged;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }
}
