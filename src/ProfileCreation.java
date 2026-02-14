package src;

import java.util.*;
import java.io.*;

public abstract class ProfileCreation {
    private static ArrayList<Person> people = new ArrayList<>();
    public static void main(String[] args) {
        String inputFile = "People.txt";

        /* ---------- ENCODERS ----------
         * - All questions are mapped to their responses.
         * - Note certain answers repeated due to errors as seen in the next section.
         * - For broader questions, the questions are ranked in similarity so that instead
         *   of only matching those with exact matching preferences, choices that are close
         *   are rewarded. Specifically, instead of just 1 for an equal response, it follows
         *   |a-b|/n, giving a float x s.t. {0<x<=1}.
         */
        
        Map<String, Integer> gradeMap = Map.of(
            "9", 0,
            "10", 1,
            "11", 2,
            "12", 3
        );
        
        Map<String, Integer> genderMap = Map.of(
            "Male", 0,
            "Female", 1,
            "Other/Prefer not to say", 2
        );
        
        Map<String, Integer> orientationMap = Map.of(
            "Straight", 0,
            "Gay/Lesbian", 1,
            "Bisexual", 2,
            "Other/No Preference/Prefer Not to Say", 3
        );

        Map<String, Integer> ownHeightMap = Map.of(
            "under 4'9\"", 0,
            "4'9\" to 5'0\"", 1,
            "5'1\" to 5'4\"", 2,
            "5'5\" to 5'8\"", 3,
            "5'9\" to 5'11\"", 4,
            "over 6'0\"", 5
        );

        // Used for PREFERRED height (Q11)
        Map<String, Integer> prefHeightMap = new HashMap<>(ownHeightMap);
        prefHeightMap.put("No preference", -1);
        
        Map<String, Integer> traitMap = Map.of(
            "Incredible intelligence", 0,
            "Gut splitting humor", 3,
            "Amazing honesty", 2,
            "Super looks", 5,
            "Compassionate caring", 1,
            "Exceptional enthusiasm", 4
        );

        Map<String, Integer> pTraitMap = Map.of(
            "Intelligence", 0,
            "Sense of humor", 3,
            "Honesty", 2,
            "Looks", 5,
            "Caring", 1,
            "Enthusiasm", 4
        );
        
        Map<String, Integer> ghostMap = Map.of(
            "Text back trying to change the topic of the conversation", 1,
            "Keep texting them and confront them in person", 0,
            "Keep texting you and confronting you in person", 0,
            "Max 1 text; I won't respond if you're not going to", 2,
            "Max 1 text; Doesn't respond if you're not going to", 2,
            "Nothing; I'm only ghosting them because I'm already too icked out", 2
        );
        
        Map<String, Integer> freeTimeMap = Map.of(
            "Computers and video games", 4,
            "Shopping at the mall", 2,
            "Books and the library", 3,
            "Sports and more sports", 1,
            "The couch and TV", 5,
            "Music and performances", 0
        );
        
        Map<String, Integer> noticeMap = Map.of(
            "Walk up to them and strike up a conversation", 0,
            "Get a friend to introduce you", 1,
            "Stuff a note in their locker", 2,
            "Follow them around", 3
        );
        
        Map<String, Integer> personalityMap = Map.of(
            "Calm cool and collected", 1,
            "Calm, cool, collected", 1,
            "Hyper and chatty", 0,
            "Hella chalant (hyper, chatty, always has something to say)", 0,
            "Strong silent type", 3,
            "Quiet, nonchalant (epitome of Theodore Roosevelt's quote \"Carry a big stick and speak softly\")", 3,
            "Sincere and compassionate", 2,
            "Sincere, kindhearted, always helping others", 2
        );
        
        Map<String, Integer> stressMap = Map.of(
            "Stay calm", 3,
            "Talk it out", 4,
            "Distract yourself", 2,
            "Power through", 5,
            "Cry", 0,
            "Talk to my teachers and counselors", 1,
            "Stress? I don't feel anything!", 6
        );
        
        Map<String, Integer> dateMap = Map.of(
            "Chips, cookies, and all snacks layed out as you play Valorant (or another video game) all night long", 4,
            "Chips, cookies, and all snacks layed out as you play Valorent (or another video game) all night long", 4,
            "A day at the mall trying out new clothes and shopping", 2,
            "Quiet study time in a cafe or the library", 3,
            "Out to watch a football or basketball game", 1,
            "Cuddling on the couch watching TV with some pizza", 5,
            "Going out to watch the next concert in SF or Levi's Stadium", 0
        );
        
        Map<String, Integer> approachMap = Map.of(
            "Randomly walks up to you and starts chatting", 0,
            "Your friend introduces them and you guys hit it off", 1,
            "A subtle note in your locker", 2,
            "Series of \"coincidences\" where you meet each other and begin talking", 3
        );

        // ---------- FILE PARSING ----------

        try (Scanner scanner = new Scanner(new File(inputFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] c = line.split("\t");
                String name = c[0].trim();
                String email = c[1].trim();
                String thirdPeriod = c[2].trim();
                String contact = c[3].trim();

                // Extra checkers to prevent errors in mapping (if for some reason the strings do not exacly match)
                // Allows the addition of an extra mapping for that specific response
                Integer gradeInt = gradeMap.get(c[5].trim());
                if (gradeInt == null) {
                    System.err.println("Unknown grade: '" + c[5] + "' for " + name);
                    continue;
                }
                int grade = gradeInt;

                Integer genderInt = genderMap.get(c[6].trim());
                if (genderInt == null) {
                    System.err.println("Unknown gender: '" + c[6] + "' for " + name);
                    continue;
                }
                int gender = genderInt;

                Integer orientationInt = orientationMap.get(c[7].trim());
                if (orientationInt == null) {
                    System.err.println("Unknown orientation: '" + c[7] + "' for " + name);
                    continue;
                }
                int orientation = orientationInt;

                // Q4: own height (single int)
                Integer ownHeightInt = ownHeightMap.get(c[8].trim());
                if (ownHeightInt == null) {
                    System.err.println("Unknown height: '" + c[8] + "' for " + name);
                    continue;
                }
                int ownHeight = ownHeightInt;

                Integer traitInt = traitMap.get(c[9].trim());
                if (traitInt == null) {
                    System.err.println("Unknown trait: '" + c[9] + "' for " + name);
                    continue;
                }
                int trait = traitInt;

                Integer ghostInt = ghostMap.get(c[10].trim());
                if (ghostInt == null) {
                    System.err.println("Unknown ghost: '" + c[10] + "' for " + name);
                    continue;
                }
                int ghost = ghostInt;

                Integer freeTimeInt = freeTimeMap.get(c[11].trim());
                if (freeTimeInt == null) {
                    System.err.println("Unknown freeTime: '" + c[11] + "' for " + name);
                    continue;
                }
                int freeTime = freeTimeInt;

                Integer noticeInt = noticeMap.get(c[12].trim());
                if (noticeInt == null) {
                    System.err.println("Unknown notice: '" + c[12] + "' for " + name);
                    continue;
                }
                int notice = noticeInt;

                Integer friendAboutYouInt = personalityMap.get(c[13].trim());
                if (friendAboutYouInt == null) {
                    System.err.println("Unknown personality: '" + c[13] + "' for " + name);
                    continue;
                }
                int friendAboutYou = friendAboutYouInt;

                // Q10: own stress
                Integer stressInt = stressMap.get(c[14].trim());
                if (stressInt == null) {
                    System.err.println("Unknown stress: '" + c[14] + "' for " + name);
                    continue;
                }
                int stress = stressInt;

                // Q11: preferred height (int[])
                int[] preferredHeight = parsePreferredHeight(c[15].trim(), prefHeightMap);

                Integer pTraitInt = pTraitMap.get(c[16].trim());
                if (pTraitInt == null) {
                    System.err.println("Unknown preferred trait: '" + c[16] + "' for " + name);
                    continue;
                }
                int pTrait = pTraitInt;

                Integer pGhostInt = ghostMap.get(c[17].trim());
                if (pGhostInt == null) {
                    System.err.println("Unknown preferred ghost: '" + c[17] + "' for " + name);
                    continue;
                }
                int pGhost = pGhostInt;

                Integer pDateInt = dateMap.get(c[18].trim());
                if (pDateInt == null) {
                    System.err.println("Unknown preferred date: '" + c[18] + "' for " + name);
                    continue;
                }
                int pDate = pDateInt;

                Integer pApproachInt = approachMap.get(c[19].trim());
                if (pApproachInt == null) {
                    System.err.println("Unknown preferred approach: '" + c[19] + "' for " + name);
                    continue;
                }
                int pApproach = pApproachInt;

                Integer pAboutThemInt = personalityMap.get(c[20].trim());
                if (pAboutThemInt == null) {
                    System.err.println("Unknown preferred personality: '" + c[20] + "' for " + name);
                    continue;
                }
                int pAboutThem = pAboutThemInt;

                // Q17: preferred stress
                Integer pStressInt = stressMap.get(c[21].trim());
                if (pStressInt == null) {
                    System.err.println("Unknown preferred stress: '" + c[21] + "' for " + name);
                    continue;
                }
                int pStress = pStressInt;

                // Q18–Q22: already numeric
                int[] answers = new int[]{
                    Integer.parseInt(c[22].trim()),
                    Integer.parseInt(c[23].trim()),
                    Integer.parseInt(c[24].trim()),
                    Integer.parseInt(c[25].trim()),
                    Integer.parseInt(c[26].trim())
                };


                people.add(new Person(
                    name, email, thirdPeriod, contact,
                    String.valueOf(grade),
                    gender, answers, orientation,
                    ownHeight, trait, ghost, freeTime,
                    notice, friendAboutYou, stress,
                    preferredHeight,
                    pTrait, pGhost, pDate,
                    pApproach, pAboutThem, pStress
                ));
                // Was an error test in earlier stages
                //System.out.println(people.get(people.size()-1).toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int[] parsePreferredHeight(String input, Map<String, Integer> map) {
        if (input.equals("No preference")) {
            return new int[]{0,1,2,3,4,5};
        }
    
        String[] parts = input.split(",");
        int[] result = new int[parts.length];
    
        for (int i = 0; i < parts.length; i++) {
            result[i] = map.get(parts[i].trim());
        }
        return result;
    }

    public static ArrayList<Person> getPeople() {
        return people;
    }
}