public class Task {

    private String name;
    private Integer id;
    private String ressource;
    private String location;
    private Integer requiredPlayer;
    private String token ="";
    private String host=null;

    public Task(String name, Integer id, String ressource, String location, Integer requiredPlayer) {
        this.name = name;
        this.id = id;
        this.ressource = ressource;
        this.location = location;
        this.requiredPlayer = requiredPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRessource() {
        return ressource;
    }

    public void setRessource(String ressource) {
        this.ressource = ressource;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRequiredPlayer() {
        return requiredPlayer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setRequiredPlayer(Integer requiredPlayer) {
        this.requiredPlayer = requiredPlayer;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", ressource='" + ressource + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    /*
    public void loesen(){

        String ort ="";

    }
    */
}
