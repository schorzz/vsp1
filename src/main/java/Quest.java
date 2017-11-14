import java.util.ArrayList;
import java.util.List;

public class Quest {


    private Integer id;
    private String name;
    private String beschreibung;
    private Integer reward;
    private List<Task> taskliste= new ArrayList<Task>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public List<Task> getTaskliste() {
        return taskliste;
    }

    public void setTaskliste(List<Task> taskliste) {
        this.taskliste = taskliste;
    }

    public void addTask(Task ttask){taskliste.add(ttask);}

    public Task getTaskById(Integer id){

        for(Task e: taskliste){
            if(e.getId()==id){
                return e;
            }
        }

        return null;

    }

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", beschreibung='" + beschreibung + '\'' +
                ", reward=" + reward +
                ", taskliste=" + taskliste +
                '}';
    }
}
