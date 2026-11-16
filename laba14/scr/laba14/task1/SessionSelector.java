package laba14.task1;

public class SessionSelector extends FormComponent {
    private String sessionType = "2D";
    private String movieTitle = "";

    public SessionSelector(OrderFormMediator mediator) {
        super(mediator, "ВибірСеансу");
    }

    public void selectSession(String movieTitle, String type) {
        this.movieTitle = movieTitle;
        this.sessionType = type;
        EventLogger.log(name, "Обрано сеанс: «" + movieTitle + "» (" + type + ")");
        mediator.notify(this, "SESSION_CHANGED");
    }

    public String getSessionType() {
        return sessionType;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}
