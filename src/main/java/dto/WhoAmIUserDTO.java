package dto;

public class WhoAmIUserDTO {

    //private List<> _links;
    private _Links _links;
    private String[] deliverables_done;
    private String[] delivered;
    private String ip;
    private String location;
    private String name;

    public _Links get_links() {
        return _links;
    }

    public void set_links(_Links _links) {
        this._links = _links;
    }

    public String[] getDeliverables_done() {
        return deliverables_done;
    }

    public void setDeliverables_done(String[] deliverables_done) {
        this.deliverables_done = deliverables_done;
    }

    public String[] getDelivered() {
        return delivered;
    }

    public void setDelivered(String[] delivered) {
        this.delivered = delivered;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
