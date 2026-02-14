package src;

import java.util.*;
import java.io.*;
import java.util.PriorityQueue;

public class Correlation {
    private static ArrayList<Person> people = new ArrayList<Person>();

    public static void main(String[] args) {
        ProfileCreation.main(null);
        people = ProfileCreation.getPeople();
        printMatches();
    }

    // Method to print matches in LATEX format for easy copy paste into editor like Overleaf
    public static void printMatches() {
        String outputFile = "Matches.tex";
    
        Map<Integer, String> gradeToString = Map.of(
            0, "9",
            1, "10",
            2, "11",
            3, "12"
        );
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
    
            // ---------- LaTeX Preamble ----------
            writer.println("\\documentclass[11pt,landscape]{article}");
            writer.println("\\usepackage[a4paper,left=0.5cm,right=0.5cm,top=0.6cm,bottom=0.6cm]{geometry}");
            writer.println("\\usepackage{multicol}");
            writer.println("\\usepackage{pifont}");
            writer.println("\\usepackage{xcolor}");
            writer.println("\\usepackage{fancyhdr}");
            writer.println("\\pagestyle{empty}");
            writer.println("\\setlength{\\columnsep}{1.0cm}");
            writer.println("\\renewcommand{\\baselinestretch}{1.15}");
            writer.println("\\begin{document}");
            writer.println("\\normalsize"); // slightly larger text
    
            int cardCount = 0;
    
            for (Person person : people) {
    
                // New page every 9 cards (3x3)
                if (cardCount > 0 && cardCount % 9 == 0) {
                    writer.println("\\newpage");
                }
    
                // Card container
                writer.println("\\noindent\\begin{minipage}[t][0.32\\textheight][t]{0.325\\textwidth}");
    
                // ---------- Header ----------
                String thirdPeriod = person.getThirdPeriod();
                if (thirdPeriod == null || thirdPeriod.isBlank()) {
                    thirdPeriod = "No Period Listed";
                }
    
                writer.println("\\textbf{\\underline{" +
                    escapeLatex(person.getName()) + ", " +
                    gradeToString.getOrDefault(person.getGrade(), "?") + ", " +
                    escapeLatex(thirdPeriod) +
                "}}\\\\");
                writer.println("Name, Grade, Contact - Percentage Match\\\\[-2pt]");
    
                // ---------- Matches ----------
                for (Person[] match : calculateCorr(people, person)) {
                    if (match[0] == null) continue;
    
                    Person m = match[0];
                    Percent p = (Percent) match[1];
    
                    String contact = m.getContact();
                    if (contact == null || contact.isBlank()) {
                        contact = "No Contact Given";
                    }
    
                    writer.printf(
                        "%s, %s, %s - %d\\%%\\\\\n",
                        escapeLatex(m.getName()),
                        gradeToString.getOrDefault(m.getGrade(), "?"),
                        escapeLatex(contact),
                        p.getPercent()
                    );
                }
    
                // ---------- Footer ----------
                writer.println("\\vspace{4pt}");
                writer.println(
                    "\\textcolor{red}{\\ding{170}} " +
                    "\\textit{\\textbf{Happy Valentine's Day!} from L2's A-Team} " +
                    "\\textcolor{red}{\\ding{170}}"
                );
    
                writer.println("\\end{minipage}");
    
                // Horizontal spacing between columns
                if (cardCount % 3 != 2) {
                    writer.println("\\hfill");
                }
    
                // Vertical spacing between rows
                if (cardCount % 3 == 2) {
                    writer.println("\\vspace{0.6cm}");
                }
    
                cardCount++;
            }
    
            writer.println("\\end{document}");
    
        } catch (IOException e) {
            System.err.println("Error writing LaTeX file: " + e.getMessage());
        }
    }

    public static String escapeLatex(String text) {
        return text.replace("_", "\\_").replace("%", "\\%");
    }

    // Main method to calculate each correlation and manage top 5 in a priority queue
    public static Person[][] calculateCorr(ArrayList<Person> allPeople, Person n) {
        PriorityQueue<PersonMatch> topMatches = new PriorityQueue<>();
    
        // Define max indices for similarity calculations (number of options - 1)
        final int maxTraitIndex = 6;       // Q5: 7 options
        final int maxGhostIndex = 2;       // Q6: 3 options
        final int maxFreeTimeIndex = 5;    // Q7: 6 options
        final int maxNoticeIndex = 3;      // Q8: 4 options
        final int maxPersonalityIndex = 3; // Q9: 4 options
        final int maxStressIndex = 6;      // Q10: 7 options
    
        for (Person candidate : allPeople) {
            if (!candidate.equals(n)
                    && isCompatibleGrade(n.getGrade(), candidate.getGrade())
                    && isCompatibleGender(n.getGender(), n.getOrientation(), candidate.getGender(), candidate.getOrientation())
                    && isCompatibleHeight(n.getHeight(), candidate.getPHeight())) {
    
                // 1Numeric answers correlation (Q18-22)
                double numericScore = calculateCorrelation(n.getAnswers(), candidate.getAnswers());
    
                // Categorical similarities (0-1) using distance-based grading
                double traitScore = similarity(n.getPTrait(), candidate.getTrait(), maxTraitIndex);
                double ghostScore = similarity(n.getPGhost(), candidate.getGhost(), maxGhostIndex);
                double freeTimeScore = similarity(n.getPDate(), candidate.getFreeTime(), maxFreeTimeIndex);
                double approachScore = similarity(n.getPApproach(), candidate.getNotice(), maxNoticeIndex);
                double personalityScore = similarity(n.getPAboutThem(), candidate.getFriendAboutYou(), maxPersonalityIndex);
                double stressScore = similarity(n.getPStress(), candidate.getStress(), maxStressIndex);
    
                // Combine scores with arbritarily set weights (thought these as the best way for the proper and necessary
                // representation of each question)
                double correlation = 0.33 * numericScore
                                   + 0.16 * traitScore
                                   + 0.07 * ghostScore
                                   + 0.12 * freeTimeScore
                                   + 0.10 * approachScore
                                   + 0.14 * personalityScore
                                   + 0.08 * stressScore;
    
                // Add to priority queue (min-heap)
                topMatches.offer(new PersonMatch(candidate, correlation));
                if (topMatches.size() > 5) topMatches.poll(); // keep top 5
            }
        }
    
        // Convert priority queue to array (sorted descending)
        Person[][] matches = new Person[5][2];
        int index = 4;
        while (!topMatches.isEmpty()) {
            PersonMatch match = topMatches.poll();
            matches[index][0] = match.person;
            matches[index][1] = new Percent((int) (50 + (50 * match.correlation))); // as in your original
            index--;
        }
        return matches;
    }
    
    // Distance-based similarity for ordinal categorical questions
    public static double similarity(int want, int candidate, int maxIndex) {
        double dist = Math.abs(want - candidate);
        return 1.0 - (dist / maxIndex);
    }

    // Checks for compatible grade (touching grades + one more for freshman and seniors)
    public static boolean isCompatibleGrade(int personGrade, int checkingGrade) {
        if (personGrade == 0) return (checkingGrade == 0 || checkingGrade == 1 || checkingGrade == 2);
        if (personGrade == 1) return (checkingGrade == 0 || checkingGrade == 1 || checkingGrade == 2);
        if (personGrade == 2) return (checkingGrade == 1 || checkingGrade == 2 || checkingGrade == 3);
        if (personGrade == 3) return (checkingGrade == 2 || checkingGrade == 3 || checkingGrade == 1);
        return false;
    }

    // Checks if gender and sexual orientation match
    public static boolean isCompatibleGender(int genderOne, int orientationOne, int genderTwo, int orientationTwo) {
        // If either is "Other/Prefer not to say" gender, allow match
        if (genderOne == 2 || genderTwo == 2) return true;
    
        // Only allow match if both people like each other's genders
        if (
            ((orientationOne == 0 && ((genderOne == 0 && genderTwo == 1) || (genderOne == 1 && genderTwo == 0))) ||  // One straight
             (orientationOne == 1 && ((genderOne == 0 && genderTwo == 0) || (genderOne == 1 && genderTwo == 1))) ||  // One gay
             (orientationOne == 2 && (genderTwo == 0 || genderTwo == 1))) &&                                         // One bi
             ((orientationTwo == 0 && ((genderTwo == 0 && genderOne == 1) || (genderTwo == 1 && genderOne == 0))) || // Two straight
             (orientationTwo == 1 && ((genderTwo == 0 && genderOne == 0) || (genderTwo == 1 && genderOne == 1))) ||  // Two gay
             (orientationTwo == 2 && (genderOne == 0 || genderOne == 1)))                                            // Two bi
        ) {
            return true;
        }
    
        return false;
    }

    // Checks if height requirement is met
    public static boolean isCompatibleHeight(int ownHeight, int[] preferredHeights) {
        for (int h : preferredHeights) {
            if (ownHeight == h) return true;
        }
        return false;
    }

    // Calculating the exact correlation for the 5 integer responses, returning a float x s.t {0<x<=1}
    public static double calculateCorrelation(int[] a, int[] b) {
        int n = a.length;
        double sumX = 0, sumY = 0, sumXY = 0;
        double sumX2 = 0, sumY2 = 0;
        for (int i = 0; i < n; i++) {
            sumX += a[i];
            sumY += b[i];
            sumXY += a[i] * b[i];
            sumX2 += a[i] * a[i];
            sumY2 += b[i] * b[i];
        }
        double num = (n * sumXY) - (sumX * sumY);
        double denom = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        if (denom == 0) return 0;
        return (num / denom);
    }
}