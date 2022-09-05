import com.github.javafaker.Faker;
import com.opencsv.CSVReader;

import java.io.*;
import java.util.*;

public class Generator {
    private final Faker faker = new Faker();

    public void start() throws IOException {
        int branchNum = 10;
        int invitesFreq = 50;
        ArrayList<String> branches = generate_branches(branchNum);
        int peopleNum = 32000;
        int doctorsNum = peopleNum / 100;
        int donorsNum = peopleNum - doctorsNum;
        int workersPerBranch = doctorsNum / 10;
        int transfersNum = donorsNum / 300;
        ArrayList<String> phones = get_phones();
        ArrayList<String> people = generate_people(peopleNum, phones);
        ArrayList<String> doctors = generate_doctors(people, doctorsNum);
        ArrayList<String> donors = generate_donors(people, doctorsNum, donorsNum);
        ArrayList<String> invites = generate_invites(invitesFreq, donors);
        ArrayList<String> transfers = generate_transfers(branches, transfersNum);
        ArrayList<String> workplaces = generate_workplaces(doctors, branches, workersPerBranch);
        ArrayList<String> blood = generate_blood(donors, branches, workplaces);
        writeToFile("people.txt", arrayToStr(people));
        writeToFile("doctors.txt", arrayToStr(doctors));
        writeToFile("donors.txt", arrayToStr(donors));
        writeToFile("branches.txt", arrayToStr(branches));
        writeToFile("invites.txt", arrayToStr(invites));
        writeToFile("workplaces.txt", arrayToStr(workplaces));
        writeToFile("transfers.txt", arrayToStr(transfers));
        writeToFile("blood.txt", arrayToStr(blood));
    }

    private ArrayList<String> generate_blood(ArrayList<String> donors,
                                             ArrayList<String> branches, ArrayList<String> workplaces) {
        ArrayList<String> blood = new ArrayList<>(donors.size());
        int CID = Integer.parseInt(branches.get(0).split(",")[1]);
        String CEO = "semenvol";
        for (int i = 0; i < donors.size(); ++i) {
            Random random = new Random();

            Number randomDonor = faker.number().numberBetween(0, donors.size());
            Number randomWorkplace = faker.number().numberBetween(0, workplaces.size());

            int day = random.nextInt(30) + 1;
            int month = random.nextInt(12) + 1;
            int year = 2020;
            int Rh = random.nextInt(2);
            boolean flag = Rh == 1;
            int group = random.nextInt(4) + 1;
            int branchID = Integer.parseInt(workplaces.get(randomWorkplace.intValue()).split(",")[0]);
            char[] doctorID = workplaces.get(randomWorkplace.intValue()).split(",")[3].toCharArray();
            doctorID[doctorID.length - 1] = ',';
            String s1 = new String(doctorID);
            char[] donorMedCard = donors.get(randomDonor.intValue()).split(",")[0].toCharArray();
//            donorMedCard[donorMedCard.length - 1] = ',';
            String s2 = new String(donorMedCard);
            blood.add(String.format("%d,%s%s,%d,%d,'%s',%d,%d,%d,%b,%d\n",
                    i, s1, s2, branchID, CID, CEO, day, month, year, flag, group));
        }
        return blood;
    }


    private ArrayList<String> generate_transfers(ArrayList<String> branches, int transfersNum) {
        ArrayList<String> transfers = new ArrayList<>(transfersNum);
        for (int i = 0; i < transfersNum; ++i) {
            Number number = faker.number().numberBetween(0, 10);
            String[] branchesAttrs = branches.get(i % branches.size()).split(",");
            transfers.add(String.format("%s,%s,'semenvol',%d\n",
                    branchesAttrs[0], branchesAttrs[1], number.intValue() + 1));
        }
        return transfers;
    }


    private ArrayList<String> generate_workplaces(ArrayList<String> doctors, ArrayList<String> branches, int workersPerBranch) {
        ArrayList<String> workplaces = new ArrayList<>(branches.size());
        int index = 0;
        for (int i = 0; i < branches.size(); ++i) {
            for (int j = 0; j < workersPerBranch; ++j) {
                String[] branchesAttrs = branches.get(i).split(",");
                workplaces.add(String.format("%s,%s,'semenvol',%s",
                        branchesAttrs[0], branchesAttrs[1], doctors.get(index).split(",")[1]));
                index++;
            }
        }
        return workplaces;
    }


    private ArrayList<String> generate_invites(int invitesFreq, ArrayList<String> donors) {
        ArrayList<String> invites = new ArrayList<>(donors.size() / invitesFreq);
        for (int i = 0; i < donors.size(); ++i) {
            if (i % invitesFreq == 0 && i > 0) {
                char[] s1 = donors.get(i - invitesFreq).split(",")[0].toCharArray();
                s1[s1.length - 1] = ',';
                String s = new String(s1);
                String s2 = donors.get(i).split(",")[0];
                StringBuilder builder = new StringBuilder(s);
                builder.append(s2);
                invites.add(String.format("%s\n", builder));
            }
        }
        return invites;
    }

    private ArrayList<String> generate_branches(int branchNum) {
        ArrayList<String> branches = new ArrayList<>(branchNum);
        long CID = faker.number().randomNumber();
        for (int i = 0; i < branchNum; ++i) {
            String hospital = faker.company().name();
            String city = faker.address().city();
            String street = faker.address().streetAddress();
            String zip = faker.address().zipCode();
            branches.add(String.format("%d,%d,'semenvol',%d,300,0,'%s','%s','%s','%s'\n",
                    i, CID, i, hospital, city, street, zip));
        }
        return branches;
    }


    private ArrayList<String> get_phones() {
        ArrayList<String> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader("phones.csv"));) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(values[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(records);
        return records;
    }

    private ArrayList<String> generate_donors(ArrayList<String> people, int doctorsNum, int donorsNum) {
        ArrayList<String> donors = new ArrayList<>(donorsNum);
        int[] medCard = new int[12];
        for (int i = doctorsNum; i < donorsNum; ++i) {
            donors.add(String.format("%s,'%s'\n", people.get(i).split(",")[0], arrayToStr(medCard)));
            medCard = incrementArray(medCard);
        }
        return donors;
    }

    private String arrayToStr(ArrayList<String> arr) {
        StringBuilder builder = new StringBuilder();
        arr.forEach(string -> builder.append(string));
        return builder.toString();
    }

    private String arrayToStr(int[] arr) {
        StringBuilder builder = new StringBuilder();
        for (int i : arr) {
            builder.append(i);
        }
        return builder.toString();
    }

    private ArrayList<String> generate_doctors(ArrayList<String> people, int data_size) {
        ArrayList<String> doctors = new ArrayList<>(data_size);
        for (int i = 0; i < data_size; ++i) {
            doctors.add(String.format("%s,%d\n", people.get(i).split(",")[0], i));
        }
        return doctors;
    }

    public int[] incrementArray(int[] curr_num) {
        int lastIndex = curr_num.length;
        for (int i = lastIndex - 1; i >= 0; --i) {
            if (curr_num[i] == 9) {
                curr_num[i] = 0;
            } else {
                curr_num[i]++;
                break;
            }
        }
        return curr_num;
    }

    private void writeToFile(String file, String buff) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(buff);
        writer.close();
    }

    private ArrayList<String> generate_people(int data_size, ArrayList<String> phones) {
        ArrayList<String> strings = new ArrayList<>(data_size);
        int pass = 0;
        for (int i = 0; i < data_size; ++i) {
            String fullName = faker.name().fullName();
            while (fullName.contains("'")) {
                fullName = faker.name().fullName();
            }
            if (phones.get(i).equals("NULL")) {
                strings.add(String.format("%d,'%s',%s\n", pass, fullName, phones.get(i)));
            } else {
                strings.add(String.format("%d,'%s','%s'\n", pass, fullName, phones.get(i)));
            }
            pass++;
        }
        return strings;
    }
}
