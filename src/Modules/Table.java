package Modules;

public class Table {
    private int tableID;
    private String name;
    private boolean status;

    public Table(int tableID, String name, boolean status) {
        this.tableID = tableID;
        this.name = name;
        this.status = status;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
