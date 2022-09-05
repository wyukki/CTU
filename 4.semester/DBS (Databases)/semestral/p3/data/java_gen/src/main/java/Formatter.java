import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Formatter {

    public void start() throws IOException {
        formatPeople();
        formatDoctors();
        formatDonors();
        formatBranches();
        formatInvites();
        formatWorkplaces();
        formatTransfers();
        formatBlood();
    }

    private void formatBlood() throws IOException {
        List<String> bloodLines = readFile("blood.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String bloodLine : bloodLines) {
            stringBuilder.append(String.format("INSERT INTO public.blood(bloodid, doctorid, donormedcard, branchnumber, cid, ceo, day, month, year, rh, bloodgroup) VALUES(%s);\n",
                    bloodLine));
        }
        writeToFile("blood.txt", stringBuilder.toString());
    }

    private void formatTransfers() throws IOException {
        List<String> transfersLines = readFile("transfers.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String transferLine : transfersLines) {
            stringBuilder.append(String.format("INSERT INTO public.transfer(branchnumber, cid, ceo, amount) VALUES(%s);\n",
                    transferLine));
        }
        writeToFile("transfers.txt", stringBuilder.toString());
    }

    private void formatWorkplaces() throws IOException {
        List<String> workplacesLines = readFile("workplaces.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String workplaceLine : workplacesLines) {
            stringBuilder.append(String.format("INSERT INTO public.workplace(branchnumber, cid, ceo, doctorid) VALUES(%s);\n",
                    workplaceLine));
        }
        writeToFile("workplaces.txt", stringBuilder.toString());
    }

    private void formatInvites() throws IOException {
        List<String> invitesLines = readFile("invites.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String inviteLine : invitesLines) {
            stringBuilder.append(String.format("INSERT INTO public.invite(donormedcard, invitemedcard) VALUES(%s);\n",
                    inviteLine));
        }
        writeToFile("invites.txt", stringBuilder.toString());
    }

    private void formatBranches() throws IOException {
        List<String> branchesLines = readFile("branches.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String branchLine : branchesLines) {
            stringBuilder.append(String.format("INSERT INTO public.branch(branchnumber, cid, ceo, bankid, bankcapacity, bankusedplace, hospital, city, street, zip) VALUES(%s);\n",
                    branchLine));
        }
        writeToFile("branches.txt", stringBuilder.toString());
    }

    private void formatDonors() throws IOException {
        List<String> donorsLines = readFile("donors.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String donorLine : donorsLines) {
            stringBuilder.append(String.format("INSERT INTO public.donor(pass, medcard) VALUES(%s);\n", donorLine));
        }
        writeToFile("donors.txt", stringBuilder.toString());
    }

    private void formatDoctors() throws IOException {
        List<String> doctorsLines = readFile("doctors.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (String doctorLine : doctorsLines) {
            stringBuilder.append(String.format("INSERT INTO public.doctor(pass, workerid) VALUES(%s);\n", doctorLine));
        }
        writeToFile("doctors.txt", stringBuilder.toString());
    }

    private void formatPeople() throws IOException {
        List<String> peopleLines = readFile("people.txt");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < peopleLines.size(); ++i) {
            stringBuilder.append(String.format("INSERT INTO public.person(pass, name, phone) VALUES(%s);\n", peopleLines.get(i)));
        }
        writeToFile("people.txt", stringBuilder.toString());
    }

    private List<String> readFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        List<String> lines = new ArrayList<>();
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }
        return lines;
    }
    private void writeToFile(String file, String buff) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(buff);
        writer.close();
    }
}
