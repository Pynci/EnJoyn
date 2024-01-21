package it.unimib.enjoyn.source.user;

public abstract class BaseUserLocalDataSource {
    protected UserCallback userCallback;

    public void setUserCallback(UserCallback userCallback){
        this.userCallback = userCallback;
    }

    public abstract void getCurrentUser();
    public abstract void insertCurrentUser();
    public abstract void updateCurrentUser();
    public abstract void clearCurrentUser();
}
