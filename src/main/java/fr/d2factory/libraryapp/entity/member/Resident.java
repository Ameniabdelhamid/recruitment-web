package fr.d2factory.libraryapp.entity.member;

public class Resident extends Member {
    static final int NBR_MAX_DAYS = 60;
    static final float INIT_TARIFF = 0.10f;
    static final float EXTRA_TARIFF = 0.20f;

    public Resident(String firstName, String lastName, String email, float wallet, boolean isLate) {
        super(firstName, lastName, email, wallet, isLate);
    }

    public Resident() {
        super();
    }

    @Override
    public void payBook(int numberOfDays) {
        // case of residents who keep a books more than the limited period
        if (numberOfDays > NBR_MAX_DAYS) {
            setWallet(getWallet() - (INIT_TARIFF * NBR_MAX_DAYS) - (EXTRA_TARIFF * (numberOfDays - NBR_MAX_DAYS)));
            setLate(true);
        } else
            setWallet(getWallet() - numberOfDays * INIT_TARIFF);

    }

    @Override
    public int getNbrOfMaxDays() {
        return NBR_MAX_DAYS;
    }
}
