package fr.d2factory.libraryapp.entity.member;

public class Student extends Member {
    static final int NBR_MAX_DAYS = 30;
    static final int NBR_FREE_DAYS = 15;
    static final float INIT_TARIFF = 0.10f;
    static final float EXTRA_TARIFF = 0.15f;
    private boolean isFirstYear = false;

    public Student(String firstName, String lastName, String email, float wallet, boolean isLate) {
        super(firstName, lastName, email, wallet, isLate);
    }

    public Student() {
        super();
    }

    @Override
    public void payBook(int numberOfDays) {
        // case of first year students
        if (isFirstYear) {
            if (numberOfDays > NBR_FREE_DAYS) {
                if (numberOfDays > NBR_MAX_DAYS) {
                    // case of students who keep a book more than the limited period
                    int nbrLateDays = numberOfDays - NBR_MAX_DAYS;
                    int nbrExtraDays = numberOfDays - NBR_FREE_DAYS - nbrLateDays;
                    setWallet(getWallet() - (INIT_TARIFF * nbrExtraDays) - (EXTRA_TARIFF * nbrLateDays));
                    setLate(true);
                } else
                    setWallet(getWallet() - INIT_TARIFF * (numberOfDays - NBR_FREE_DAYS));

            }
        }
        // case of students regardless of what year they are enrolled in
        else {
            // case of students who keep a book more than the limited period
            if (numberOfDays > NBR_MAX_DAYS) {
                setWallet(getWallet() - (INIT_TARIFF * NBR_MAX_DAYS) - (EXTRA_TARIFF * (numberOfDays - NBR_MAX_DAYS)));
                setLate(true);
            } else
                setWallet(getWallet() - (INIT_TARIFF * numberOfDays));
        }


    }

    @Override
    public int getNbrOfMaxDays() {
        return NBR_MAX_DAYS;
    }


    public boolean isFirstYear() {
        return isFirstYear;
    }

    public void setFirstYear(boolean firstYear) {
        isFirstYear = firstYear;
    }

}
