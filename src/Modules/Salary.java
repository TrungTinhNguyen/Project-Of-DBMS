package Modules;

public class Salary {
    private Staff staff;
    private float salary;
    private float bonus;

    public Salary(Staff staff, float salary, float bonus) {
        this.staff = staff;
        this.salary = salary;
        this.bonus = bonus;
    }

    public Staff getStaff() {
        return staff;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }
}
