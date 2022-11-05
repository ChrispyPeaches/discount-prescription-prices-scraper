public class Medication {
    String name;
    int dosageAmnt;
    String dosageUnit;
    int monthlyCnt;

    public Medication(String name, int dosageAmnt, String dosageUnit, int monthlyCnt) {
        this.name = name;
        this.dosageAmnt = dosageAmnt;
        this.dosageUnit = dosageUnit;
        this.monthlyCnt = monthlyCnt;
    }
}